package com.sniper.springmvc.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.mybatis.service.impl.SdAttachmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdSubjectsService;
import com.sniper.springmvc.mybatis.service.impl.TablesRelationService;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.SystemConfigUtil;
import com.sniper.springmvc.utils.ValidateUtil;
import com.sniper.springmvc.utils.ZipUtil;

/**
 * 文件信息获取
 * 
 * @author sniper
 * 
 */
@Controller
@RequestMapping("/file-info")
public class FileInfoAction extends RootController {

	@Resource
	SdAttachmentsService attachmentsService;

	@Resource
	private SdSubjectsService postService;

	@Resource
	TablesRelationService relationService;

	@ResponseBody
	@RequestMapping("deleteFileByID")
	public Map<String, Object> deleteFileByID(
			@RequestParam(value = "id", defaultValue = "") String id) {
		Map<String, Object> ajaxResult = new HashMap<>();
		ajaxResult.put("code", 500);
		if (ValidateUtil.isValid(id)) {
			try {
				attachmentsService.delete(id);
				ajaxResult.put("code", 200);
				ajaxResult.put("file", null);
				ajaxResult.put("msg", "操作成功");
			} catch (Exception e) {
				ajaxResult.put("code", 500);
				ajaxResult.put("file", null);
				ajaxResult.put("msg", "操作失败");
			}
		}
		return ajaxResult;
	}

	/**
	 * 设置是否是图片新闻
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("setprimeimage")
	public Map<String, Object> setPrimeImage(
			@RequestParam(value = "id", defaultValue = "") String id) {
		Map<String, Object> ajaxResult = new HashMap<>();
		ajaxResult.put("code", 500);
		if (ValidateUtil.isValid(id)) {
			try {
				SdAttachments attachments = attachmentsService.get(id);
				if (attachments.getIsprimeimage() == 1) {
					attachments.setIsprimeimage(0);
				} else {
					attachments.setIsprimeimage(1);
				}
				attachmentsService.update(attachments);
				ajaxResult.put("code", 200);
				ajaxResult.put("file", null);
				ajaxResult.put("msg", "操作成功");
			} catch (Exception e) {
				ajaxResult.put("code", 500);
				ajaxResult.put("file", null);
				ajaxResult.put("msg", "操作失败");
			}

		}
		return ajaxResult;
	}

	/**
	 * 设置是否是图片新闻
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("setmainsite")
	public Map<String, Object> setMainsite(
			@RequestParam(value = "id", defaultValue = "") String id) {
		Map<String, Object> ajaxResult = new HashMap<>();
		ajaxResult.put("code", 500);
		if (ValidateUtil.isValid(id)) {
			try {
				SdAttachments attachments = attachmentsService.get(id);
				if (attachments.getMainsite() == 1) {
					attachments.setMainsite(0);
				} else {
					attachments.setMainsite(1);
				}
				attachmentsService.update(attachments);
				ajaxResult.put("code", 200);
				ajaxResult.put("msg", "操作成功");
				ajaxResult.put("file", null);
			} catch (Exception e) {
				ajaxResult.put("code", 500);
				ajaxResult.put("file", null);
				ajaxResult.put("msg", "操作失败");
			}

		}
		return ajaxResult;
	}

	/**
	 * 根据文章id获取附件列表
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getFilesByPostID")
	public Map<String, Object> getFilesByPostID(@RequestParam("id") String id) {
		Map<String, Object> ajaxResult = new HashMap<>();
		ajaxResult.put("error", 1);
		if (ValidateUtil.isValid(id)) {
			List<SdAttachments> attachments = attachmentsService.getFiles(id,
					null, null);
			if (attachments != null && attachments.size() > 0) {
				List<Map<String, Object>> tempPost = new ArrayList<>();
				for (SdAttachments files2 : attachments) {
					if (files2 != null) {
						tempPost.add(getFileInfo(files2));
					}
				}
				ajaxResult.put("error", 0);
				ajaxResult.put("file", tempPost);
			}
		}
		return ajaxResult;
	}

	/**
	 * 根据附件id获取附件
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getFileByID")
	public Map<String, Object> getFileByID(@RequestParam("id") String id) {
		Map<String, Object> ajaxResult = new HashMap<>();
		ajaxResult.put("error", 1);
		if (ValidateUtil.isValid(id)) {
			SdAttachments files = attachmentsService.get(id);
			if (files != null) {
				ajaxResult.put("files", getFileInfo(files));
			}
		}
		return ajaxResult;
	}

	/**
	 * 根据文件地址获取附件
	 * 
	 * @param url
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getFileByUrl")
	public Map<String, Object> getFileByUrl(@RequestParam("url") String url) {
		Map<String, Object> ajaxResult = new HashMap<>();
		// 去除域名部分
		if (url.startsWith("http")) {
			url = url.substring(url.indexOf("//") + 2);
			url = url.substring(url.indexOf("/"));
		}
		// System.out.println(url);
		// 删除本地测试的url前缀
		// url = url.replace("/java-sdcom", "");
		ajaxResult.put("error", 1);
		if (ValidateUtil.isValid(url)) {
			SdAttachments files = attachmentsService.getFileByUrl(url);
			if (files != null) {
				ajaxResult.put("error", 0);
				ajaxResult.put("file", getFileInfo(files));
			}
		}
		return ajaxResult;
	}

	/**
	 * 组装返回数据结构
	 * 
	 * @param file
	 * @return
	 */
	private Map<String, Object> getFileInfo(SdAttachments file) {
		Map<String, Object> result = new HashMap<>();
		result.put("error", 0);
		result.put("url", FilesUtil.getImagePrefix() + file.getFilename());
		result.put("filePath", file.getFilename());
		result.put("fileType", file.getFiletype());
		result.put("id", file.getAid());
		result.put("checked", file.getIsprimeimage());
		result.put("oldName", file.getPrefilename());

		return result;
	}

	private String getFileName(String filePath)
			throws UnsupportedEncodingException {

		if (filePath.indexOf("/") != -1) {
			filePath = filePath.substring(filePath.lastIndexOf("/") + 1);
		}

		String Agent = request.getHeader("User-Agent");
		if (null != Agent) {
			Agent = Agent.toLowerCase();
			if (Agent.indexOf("firefox") != -1) {
				filePath = new String(filePath.getBytes(), "ISO8859-1");
			} else if (Agent.indexOf("msie") != -1) {
				filePath = URLEncoder.encode(filePath, "UTF-8");
			} else {
				filePath = URLEncoder.encode(filePath, "UTF-8");
			}
		}
		return filePath;
	}

	/**
	 * 需要通过 redirect:download 传递文件路径或者文件类型
	 * 
	 * @param path
	 * @param type
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "download")
	public ResponseEntity<byte[]> download(
			@RequestParam(value = "path", required = false) String path,
			@RequestParam(value = "id", required = false) String id,
			@RequestParam(value = "fileName", required = false) String fileName,
			@RequestParam(value = "type", required = false, defaultValue = "application/vnd.ms-excel") String type)
			throws IOException {
		// 确定各个成员变量的值

		if (ValidateUtil.isValid(id)) {
			SdAttachments files = attachmentsService.get(id);
			path = files.getFilename();
			fileName = files.getPrefilename();
			String suffix = FilesUtil.getFileExt(path);
			if (fileName.indexOf(suffix) == -1) {
				fileName = fileName + "." + suffix;
			}
			type = files.getFiletype();
		}

		String rootDir = FilesUtil.getRootDir();

		path = rootDir + path;
		// 如何文件名称不存在
		if (!ValidateUtil.isValid(fileName)) {
			fileName = path.substring(path.lastIndexOf("."));
		}

		fileName = getFileName(fileName);

		HttpHeaders headers = new HttpHeaders();
		byte[] body = "文件不存在".getBytes();
		HttpStatus httpState = HttpStatus.NOT_FOUND;
		File file = new File(path);
		if (file.exists() && file.isFile()) {

			InputStream is = new FileInputStream(file);
			body = new byte[is.available()];
			is.read(body);
			is.close();
			headers.add("Content-Type", type);
			headers.add("Content-Length", "" + body.length);
			// 加双引号解决火狐空格断点的问题
			headers.add("Content-Disposition", "attachment;filename=\""
					+ fileName + "\"");
			httpState = HttpStatus.OK;

		}

		ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers,
				httpState);

		return entity;
	}

	/**
	 * type 在这时没有使用,设置只是为了和前台url项符合
	 * 
	 * @param id
	 * @param type
	 * @param filetype
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "downloads")
	public Map<String, Object> downloads(
			@RequestParam(value = "delid", required = false) String[] id,
			@RequestParam(value = "type", required = false) String type,
			@RequestParam(value = "filetype", required = false, defaultValue = "application/vnd.ms-excel") String filetype)
			throws IOException {
		// 确定各个成员变量的值
		Map<String, Object> ajaxResult = new HashMap<>();
		String rootDir = "";

		if (ValidateUtil.isValid(SystemConfigUtil.get("imagePath"))) {
			rootDir = SystemConfigUtil.get("imagePath");
		} else {
			rootDir = request.getServletContext().getRealPath("/");
		}

		ZipUtil util = new ZipUtil();
		util.setRootDir(rootDir);
		for (int i = 0; i < id.length; i++) {
			SdAttachments files = attachmentsService.get(id[i]);
			util.addFile(files);
		}
		String fileName = "批量下载" + ".zip";
		String zipFilePath = util.zipData(fileName);

		String webUrl = FilesUtil.getWebUrl("");

		zipFilePath = zipFilePath.replace(rootDir, webUrl);

		ajaxResult.put("url", zipFilePath);
		ajaxResult.put("code", 200);
		ajaxResult.put("msg", "开始下载");

		return ajaxResult;
	}

}