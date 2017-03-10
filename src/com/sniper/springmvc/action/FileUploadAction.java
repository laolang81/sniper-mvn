package com.sniper.springmvc.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sniper.springmvc.freemarker.ImageHelpUtil;
import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.mybatis.service.impl.FilesService;
import com.sniper.springmvc.mybatis.service.impl.LogSmbService;
import com.sniper.springmvc.mybatis.service.impl.SdAttachmentsService;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.SmbUtils;
import com.sniper.springmvc.utils.SystemConfigUtil;
import com.sniper.springmvc.utils.UserDetailsUtils;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
public class FileUploadAction extends RootController {

	@Resource
	private FilesService filesService;

	@Resource
	SdAttachmentsService attachmentsService;

	@Resource
	LogSmbService logSmbService;

	/**
	 * 文件上传，返回json数据
	 * 
	 * @param imgFile
	 * @param locale
	 * @param dir
	 * @param response
	 * @param writer
	 * @throws IOException
	 */
	// @RequiresPermissions("admin:upload")
	@RequestMapping(value = "upload")
	public void upload(@RequestParam("imgFile") MultipartFile imgFile, Locale locale,
			@RequestParam(value = "dir", required = false, defaultValue = "image") String dir,
			HttpServletResponse response, PrintWriter writer) throws IOException {
		Map<String, Object> ajaxResult = new HashMap<>();
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		if (!detailsUtils.validRole("ROLE_ADMIN")) {
			long minFileSizeL = SystemConfigUtil.getLong("minFileSize");
			if (imgFile.getSize() < minFileSizeL) {
				ajaxResult = alert(messageSource.getMessage("fileupload.minsize",
						new String[] { String.valueOf(minFileSizeL) }, locale));
			}
		}

		String rootDir = FilesUtil.getRootDir();
		if (imgFile != null) {
			// 上传文件,获取上传后的地址,这个地址不包含目录前缀
			// 原图地址,也可以理解成大图
			String savePath = uploadFile(imgFile, dir);
			// 吧原图复制到另外一个mul
			// 新版不备份大图了,如果you缩略图，酒吧缩略图当做正图处理，原图地址另外储存
			boolean copy = false;
			if (copy && imgFile.getContentType().indexOf("image") > -1) {
				// 更改图片位置
				String copyPath = savePath.replace("kindeditor/image", "bigimages");

				// 备份大图
				FilesUtil.Copy(rootDir + savePath, rootDir + copyPath);
			}

			int thumbWidth = 700;
			int thumbHeight = 10240;

			if (ValidateUtil.isValid(SystemConfigUtil.get("imageThumbWidth"))) {
				thumbWidth = SystemConfigUtil.getInt("imageThumbWidth");
			}
			if (ValidateUtil.isValid(SystemConfigUtil.get("imageThumbHeight"))) {
				thumbHeight = SystemConfigUtil.getInt("imageThumbHeight");
			}
			// 缩略图的储存位置
			String saveThumbUrl = savePath;
			// 对图片的特殊处理,生成缩略图
			if (imgFile.getContentType().startsWith("image")) {
				try {
					BufferedImage bufferedImage = ImageIO.read(new File(rootDir + savePath));
					if (bufferedImage.getWidth() > thumbWidth) {
						// 生成张缩略图
						saveThumbUrl = ImageHelpUtil.thumb(savePath, thumbWidth, thumbHeight);
					} else {
						// 没有缩略图
					}
				} catch (Exception e) {
					LOGGER.error("无法识别图片高度宽度:" + savePath);
					e.printStackTrace();
				}
			}
			// 返回第一章缩略图的路径
			ajaxResult.put("error", 0);
			// 返回编译器使用,返回缩略图，这个值会根据编辑器的设置而改变
			ajaxResult.put("url", FilesUtil.getImagePrefix() + saveThumbUrl);
			// 返回个人使用
			ajaxResult.put("filePath", FilesUtil.getImagePrefix() + saveThumbUrl);
			ajaxResult.put("fileShotPath", saveThumbUrl);
			ajaxResult.put("fileType", imgFile.getContentType());
			String aid = getSaveFilesID(imgFile, savePath, saveThumbUrl);
			ajaxResult.put("id", aid);
			ajaxResult.put("oldName", imgFile.getOriginalFilename());
		} else {
			ajaxResult = alert(messageSource.getMessage("fileupload.nofile", null, locale));

		}

		String header = request.getHeader("User-Agent");
		if (header.contains("MSIE")) {
			response.setContentType("text/html;charset=UTF-8");
		} else {
			response.setContentType("application/json;charset=UTF-8");
		}
		ObjectMapper json = new ObjectMapper();
		String tempfileValue = json.writeValueAsString(ajaxResult);
		JsonNode jsonNode = json.readTree(tempfileValue);
		writer.print(jsonNode);
		return;
	}

	/**
	 * 一直没有被使用，不知道能否能用,用的时候要检测
	 * 
	 * @param imgFile
	 * @param locale
	 * @param dir
	 * @param album
	 * @param tags
	 * @param eventdate
	 * @param response
	 * @param writer
	 * @throws IOException
	 */
	@RequestMapping(value = "uploads")
	public void uploads(@RequestParam("imgFile") MultipartFile[] imgFile, Locale locale,
			@RequestParam(value = "dir", required = false, defaultValue = "image") String dir,
			HttpServletResponse response, PrintWriter writer) throws IOException {
		List<Map<String, Object>> ajaxResult = new ArrayList<>();
		Map<String, Object> result = new HashMap<>();

		// 设置request
		for (int i = 0; i < imgFile.length; i++) {
			MultipartFile file = imgFile[i];

			if (imgFile != null || file.getSize() > 0) {
				// 上传文件,获取上传后的地址
				String savePath = uploadFile(file, dir);
				result.put("error", 0);
				result.put("url", FilesUtil.getWebUrl(getBasePath()) + savePath);
				result.put("filePath", FilesUtil.getWebUrl(getBasePath()) + savePath);
				result.put("fileType", file.getContentType());
				result.put("id", getSaveFilesID(file, savePath, savePath));
				result.put("oldName", file.getOriginalFilename());
				ajaxResult.add(result);
			}
		}

		String header = request.getHeader("User-Agent");
		if (header.contains("MSIE")) {
			response.setContentType("text/html");
		}
		ObjectMapper json = new ObjectMapper();
		String tempfileValue = json.writeValueAsString(ajaxResult);
		JsonNode jsonNode = json.readTree(tempfileValue);
		writer.print(jsonNode);

		return;
	}

	/**
	 * 负责保存附件
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private String uploadFile(MultipartFile imgFile, String dir) throws FileNotFoundException, IOException {

		String rootDir = FilesUtil.getRootDir();

		// 保存的目录，是固定目录，只能更改rootpath目录移动文件
		String saveFileName = getSaveFilename(imgFile).toLowerCase();
		String savePath = FilesUtil.getSaveDir(dir);

		// 用户前台显示
		String saveUrl = savePath;

		// 组装真的url
		savePath = rootDir + savePath;
		// 检查文件夹是否存在
		File fileDir = new File(savePath);
		if (!fileDir.isDirectory()) {
			fileDir.mkdirs();
		}
		savePath += saveFileName;
		saveUrl += saveFileName;

		FileOutputStream out = new FileOutputStream(savePath);
		InputStream in = imgFile.getInputStream();

		byte[] buffer = new byte[1024];
		int len = 0;

		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		out.close();
		in.close();

		return saveUrl;
	}

	/**
	 * 生成文件问名称
	 * 
	 * @return
	 */
	private String getSaveFilename(MultipartFile imgFile) {
		String fileExt = imgFile.getOriginalFilename().substring(imgFile.getOriginalFilename().lastIndexOf(".") + 1)
				.toLowerCase();

		return FilesUtil.getUUIDName(fileExt, false);
	}

	/**
	 * 吧上传的文件保存在数据库
	 * 
	 * @param imgFile
	 * @param savePath
	 * @param saveThumbPath
	 * @param album
	 * @param tags
	 * @return
	 */
	private String getSaveFilesID(MultipartFile imgFile, String savePath, String saveThumbPath) {
		SdAttachments files = new SdAttachments();
		files.setSid(0);
		files.setDateline(0);
		files.setFilename(saveThumbPath);
		// 发送到图片服务器
		String rootDir = FilesUtil.getRootDir();
		SmbUtils smb = SmbUtils.getInstance();
		smb.setLogSmbService(logSmbService);
		if (smb.isEnabled()) {
			// 发送缩略图,缩略图生成规则，
			// 上传的时候要去除public这一级目录
			SmbUtils.Smb smbThread = smb.getSmb(SmbUtils.SMB_PUT_FILE,
					new Object[] { rootDir + saveThumbPath, smb.getDir(saveThumbPath) });
			smbThread.run();
			// 发送大图
			if (!savePath.equals(saveThumbPath)) {
				SmbUtils.Smb smbBigThread = smb.getSmb(SmbUtils.SMB_PUT_FILE,
						new Object[] { rootDir + savePath, smb.getDir(savePath) });
				smbBigThread.run();
			}
		}
		files.setDescription(savePath);
		files.setFilesize(imgFile.getSize());
		// 获取后缀
		String suffix = FilesUtil.getFileExt(savePath);
		files.setFiletype(imgFile.getContentType());
		files.setPrefilename(imgFile.getOriginalFilename().replace("." + suffix, ""));
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		files.setUid(detailsUtils.getAminUser().getId());

		try {
			files.setGuid(DigestUtils.md5Hex(imgFile.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (imgFile.getContentType().indexOf("image") != -1) {
			files.setIsimage(1);
		}
		attachmentsService.insert(files);
		return files.getAid().toString();
	}

	// @RequiresPermissions("admin:htmlmanager")
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "htmlmanager")
	public void htmlmanager(@RequestParam(value = "dir", required = false, defaultValue = "image") String dir,
			@RequestParam(value = "order", required = true) String orderSource,
			@RequestParam(value = "path", required = false) String pathSource, PrintWriter writer) throws IOException {

		String rootDir = FilesUtil.getRootDir();

		// 根目录路径，可以指定绝对路径，比如 /var/www/attached/
		String rootPath = rootDir + "/attachments/";
		// 根目录URL，可以指定绝对路径，比如 http://www.yoursite.com/attached/

		String rootUrl = FilesUtil.getWebUrl(getBasePath()) + "/attachments/";

		// 图片扩展名
		String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp" };

		String dirName = dir;
		if (dirName != null) {
			if (!Arrays.<String>asList(new String[] { "image", "flash", "media", "file" }).contains(dirName)) {

				writer.print("Invalid Directory name.");
			}

			rootPath += dirName + "/";
			rootUrl += dirName + "/";
			File saveDirFile = new File(rootPath);
			if (!saveDirFile.exists()) {
				saveDirFile.mkdirs();
			}
		}
		// 根据path参数，设置各路径和URL
		String path = pathSource != null ? pathSource : "";
		String currentPath = rootPath + path;
		String currentUrl = rootUrl + path;
		String currentDirPath = path;
		String moveupDirPath = "";
		// 请求的路径
		if (!"".equals(path)) {
			String str = currentDirPath.substring(0, currentDirPath.length() - 1);
			moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
		}

		// 排序形式，name or size or type
		String order = orderSource != null ? orderSource.toLowerCase() : "name";

		// 不允许使用..移动到上一级目录
		if (path.indexOf("..") >= 0) {
			writer.print("Access is not allowed.");
			return;
		}

		// 最后一个字符不是/
		if (!"".equals(path) && !path.endsWith("/")) {
			writer.print("Parameter is not valid.");
			return;
		}
		// 目录不存在或不是目录
		File currentPathFile = new File(currentPath);
		if (!currentPathFile.isDirectory()) {
			writer.print("Directory does not exist.");
			return;
		}
		// 遍历目录取的文件信息
		List<Hashtable<String, Object>> fileList = new ArrayList<>();
		if (currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<>();
				String fileName = file.getName();
				if (file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					hash.put("is_photo", false);
					hash.put("filetype", "");
				} else if (file.isFile()) {
					String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", file.length());
					hash.put("is_photo", Arrays.<String>asList(fileTypes).contains(fileExt));
					hash.put("filetype", fileExt);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}

		if ("size".equals(order)) {
			Collections.sort(fileList, new SizeComparator());
		} else if ("type".equals(order)) {
			Collections.sort(fileList, new TypeComparator());
		} else {
			Collections.sort(fileList, new NameComparator());
		}

		Map<String, Object> ajaxResult = new HashMap<>();

		ajaxResult.put("moveup_dir_path", moveupDirPath);
		ajaxResult.put("current_dir_path", currentDirPath);
		ajaxResult.put("current_url", currentUrl);
		ajaxResult.put("total_count", fileList.size());
		ajaxResult.put("file_list", fileList);

		// 输出结果
		ObjectMapper json = new ObjectMapper();
		String tempfileValue = json.writeValueAsString(ajaxResult);
		JsonNode jsonNode = json.readTree(tempfileValue);
		writer.print(jsonNode);
		return;
	}

	/**
	 * 返回错误
	 * 
	 * @param msg
	 * @return
	 */
	private Map<String, Object> alert(String msg) {
		Map<String, Object> ajaxResult = new HashMap<>();

		ajaxResult.put("error", 1);
		ajaxResult.put("message", msg);
		return ajaxResult;
	}

	@SuppressWarnings("rawtypes")
	public class NameComparator implements Comparator {
		@Override
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable) a;
			Hashtable hashB = (Hashtable) b;
			if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
				return 1;
			} else {
				return ((String) hashA.get("filename")).compareTo((String) hashB.get("filename"));
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public class SizeComparator implements Comparator {
		@Override
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable) a;
			Hashtable hashB = (Hashtable) b;
			if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
				return 1;
			} else {
				if (((Long) hashA.get("filesize")) > ((Long) hashB.get("filesize"))) {
					return 1;
				} else if (((Long) hashA.get("filesize")) < ((Long) hashB.get("filesize"))) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}

	/**
	 * 根据类型排序
	 * 
	 * @author sniper
	 * 
	 */
	public class TypeComparator implements Comparator<Object> {
		@Override
		public int compare(Object a, Object b) {
			Hashtable<?, ?> hashA = (Hashtable<?, ?>) a;
			Hashtable<?, ?> hashB = (Hashtable<?, ?>) b;
			if (((Boolean) hashA.get("is_dir")) && !((Boolean) hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean) hashA.get("is_dir")) && ((Boolean) hashB.get("is_dir"))) {
				return 1;
			} else {
				return ((String) hashA.get("filetype")).compareTo((String) hashB.get("filetype"));
			}
		}
	}

}
