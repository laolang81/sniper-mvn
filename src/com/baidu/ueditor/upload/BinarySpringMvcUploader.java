package com.baidu.ueditor.upload;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.baidu.ueditor.PathFormat;
import com.baidu.ueditor.define.AppInfo;
import com.baidu.ueditor.define.BaseState;
import com.baidu.ueditor.define.FileType;
import com.baidu.ueditor.define.State;
import com.sniper.springmvc.data.DataValues;
import com.sniper.springmvc.freemarker.ImageHelpUtil;
import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.mybatis.service.impl.LogSmbService;
import com.sniper.springmvc.mybatis.service.impl.SdAttachmentsService;
import com.sniper.springmvc.security.SpringContextUtil;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.SmbUtils;
import com.sniper.springmvc.utils.SystemConfigUtil;
import com.sniper.springmvc.utils.UserDetailsUtils;

public class BinarySpringMvcUploader {

	private static SdAttachmentsService attachmentsService;
	static {
		attachmentsService = (SdAttachmentsService) SpringContextUtil.getBean(SdAttachmentsService.class);
	}

	private static LogSmbService logSmbService;
	static {
		logSmbService = (LogSmbService) SpringContextUtil.getBean(LogSmbService.class);
	}

	public static final State save(HttpServletRequest request, Map<String, Object> conf) {

		// 检测是否是form表单提交
		if (!ServletFileUpload.isMultipartContent(request)) {
			return new BaseState(false, AppInfo.NOT_MULTIPART_CONTENT);
		}

		// 下面附件数据的获取和spring mvc配置的数据获取you冲突，如果想一起用的话，这里必须按照spring mvc的格式去写
		try {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
			// 获取上传文件
			MultipartFile multipartFile = multipartHttpServletRequest
					.getFile((String) conf.get("fieldName").toString());

			if (multipartFile.getSize() == 0) {
				return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
			}

			// 检测图片最小的是否规范
			UserDetailsUtils detailsUtils = new UserDetailsUtils();
			if (!detailsUtils.validRole(DataValues.ROLE_ADMIN)) {
				long minFileSizeL = SystemConfigUtil.getLong("minUploadFileSize");
				if (minFileSizeL > 0 && multipartFile.getSize() < minFileSizeL) {
					return new BaseState(false, AppInfo.FILE_TO_SMALL);
				}
			}

			String savePath = (String) conf.get("savePath");
			String originFileName = multipartFile.getOriginalFilename();
			// 文件后缀一律小写
			String suffix = FileType.getSuffixByFilename(originFileName).toLowerCase();

			originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
			savePath = savePath + suffix;

			long maxSize = ((Long) conf.get("maxSize")).longValue();

			if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
				return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
			}

			savePath = PathFormat.parse(savePath, originFileName);

			String rootPath = (String) conf.get("rootPath");
			String physicalPath = rootPath + savePath;
			physicalPath = physicalPath.replace("//", "/");
			InputStream is = multipartFile.getInputStream();
			// 数据的保存
			State storageState = StorageManager.saveFileByInputStream(is, physicalPath, maxSize);
			is.close();

			// 检测图片的大小
			if (storageState.isSuccess()) {
				storageState.putInfo("url", PathFormat.format(savePath));
				storageState.putInfo("type", suffix);
				storageState.putInfo("original", originFileName + suffix);
			}
			// 有关图片缩略图的生成
			int thumbWidth = (SystemConfigUtil.getInt("imageThumbWidth") > 0)
					? SystemConfigUtil.getInt("imageThumbWidth") : 700;
			int thumbHeight = (SystemConfigUtil.getInt("imageThumbWidth") > 0)
					? SystemConfigUtil.getInt("imageThumbWidth") : 10240;

			String saveThumbUrl = savePath;
			// 生成缩略图
			if (multipartFile.getContentType().startsWith("image")) {
				try {
					BufferedImage bufferedImage = ImageIO.read(new File(rootPath + savePath));
					if (bufferedImage.getWidth() > thumbWidth) {
						// 生成张缩略图
						saveThumbUrl = ImageHelpUtil.thumb(savePath, thumbWidth, thumbHeight);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 附件数据组装
			SdAttachments files = new SdAttachments();
			files.setSid(0);
			files.setDateline(0);
			// 可能是原图，也可能是缩略图
			files.setFilename(saveThumbUrl);
			// 发送到图片服务器
			String rootDir = FilesUtil.getRootDir();
			SmbUtils smb = SmbUtils.getInstance();
			smb.setLogSmbService(logSmbService);
			if (smb.isEnabled()) {
				// 发送缩略图,缩略图生成规则，
				SmbUtils.Smb smbThread = smb.getSmb(SmbUtils.SMB_PUT_FILE,
						new Object[] { rootDir + saveThumbUrl, smb.getDir(saveThumbUrl) });
				smbThread.run();
				// 发送大图
				if (!savePath.equals(saveThumbUrl)) {
					SmbUtils.Smb smbBigThread = smb.getSmb(SmbUtils.SMB_PUT_FILE,
							new Object[] { rootDir + savePath, smb.getDir(savePath) });
					smbBigThread.run();
				}
			}
			// 原图
			files.setDescription(savePath);
			files.setFilesize(multipartFile.getSize());
			// 获取后缀
			files.setFiletype(multipartFile.getContentType());
			files.setPrefilename(originFileName);
			files.setUid(detailsUtils.getAminUser().getId());
			try {
				files.setGuid(DigestUtils.md5Hex(multipartFile.getBytes()));
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (multipartFile.getContentType().indexOf("image") != -1) {
				files.setIsimage(1);
			}
			// 插入数据
			attachmentsService.insert(files);
			return storageState;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new BaseState(false, AppInfo.IO_ERROR);
	}

	private static boolean validType(String type, String[] allowTypes) {
		List<String> list = Arrays.asList(allowTypes);
		return list.contains(type);
	}
}
