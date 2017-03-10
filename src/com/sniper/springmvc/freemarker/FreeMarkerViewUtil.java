package com.sniper.springmvc.freemarker;

import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.oro.text.regex.MalformedPatternException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.utils.FfmpegUtils;
import com.sniper.springmvc.utils.FileIconUtil;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.JavaShellUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * freemarker一些集成的一些方法
 * 
 * @author suzhen
 * 
 */
public class FreeMarkerViewUtil {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(FreeMarkerUtil.class);

	/**
	 * 截取字符串
	 * 
	 * @param str
	 * @param limit
	 * @return
	 */
	public static String substr(String str, int limit) {
		if (str.length() > limit) {
			return str.substring(0, limit);
		}
		return str;
	}

	/**
	 * 自定义的截取字符串
	 * 
	 * @param str
	 * @param limit
	 * @param suffix
	 * @return
	 */
	public static String substr(String str, int limit, String suffix) {
		if (str.length() > limit) {
			return str.substring(0, limit) + suffix;
		}
		return str + suffix;
	}

	/**
	 * 截取视频的截图
	 * 
	 * @param videoPath
	 * @param widthAndHeight
	 * @return
	 */
	public static String getFace(String videoPath, String widthAndHeight) {

		String defaultPath = "/myfiles/images/default/company-default.png";
		// 获取附件根目录地址
		String rootDir = FilesUtil.getRootDir();

		// 去掉最后的斜杠
		if (rootDir.endsWith("/")) {
			rootDir = rootDir.substring(0, rootDir.length() - 1);
		}

		// 查看文件是否存在
		String filePath = rootDir + videoPath;
		File fileTemp = new File(filePath);
		if (!fileTemp.isFile()) {
			return defaultPath;
		}
		// 开始截图
		FfmpegUtils ffmpegUtils = new FfmpegUtils(rootDir + videoPath);
		if (!widthAndHeight.equals("")) {
			ffmpegUtils.setCutWidthAndHeight(widthAndHeight);
		}
		// 查看截图是否已经存在
		String savePath = ffmpegUtils.getSavePath(".jpg");
		File file = new File(rootDir + savePath);
		if (file.isFile()) {
			return savePath.replaceFirst(rootDir, "");
		}
		try {
			savePath = ffmpegUtils.videoScreenshot();
		} catch (MalformedPatternException e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage(), e);
		}
		return savePath.replace(rootDir, "");
	}

	/**
	 * 截取图片
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	public static String show(String path, int width, int height) {

		String rootWebDir = FilesUtil.getImagePrefix();

		if (!ValidateUtil.isValid(path)) {
			return rootWebDir + path;
		}

		if (width == 0 || height == 0) {
			return rootWebDir + path;
		}

		String rootPath = FilesUtil.getRootDir();

		// 如果地址不包含跟地址,则在头部添加跟地址
		if (path.indexOf(rootPath) == -1) {
			path = rootPath + path;
		}

		// 图片要改成的高度,宽度
		// 组装新图片地址
		String thumbPath = FilesUtil.getThumbPath(path, width, height);
		// 测试缩略图是否已经存在
		File file = new File(thumbPath);
		if (file.exists() && file.isFile()) {
			return rootWebDir + thumbPath.replace(rootPath, "");
		}
		// 测试文件是否已经存在如果文件不存在
		File oldFile = new File(path);
		if (!oldFile.isFile()) {
			return rootWebDir + path.replace(rootPath, "");
		}
		// 检查父级目录是否存在
		String newDir = file.getParent();
		File parentDir = new File(newDir);
		if (!parentDir.isDirectory()) {
			parentDir.mkdirs();
		}
		// 缩略图生成
		try {
			Thumbnails.of(path).size(width, height).toFile(thumbPath);
			return rootWebDir + thumbPath.replace(rootPath, "");
		} catch (Exception e) {

			String widthAndHeight = "";
			if (width > 0) {
				widthAndHeight += String.valueOf(width);
			}
			if (height > 0) {
				widthAndHeight += "x" + String.valueOf(height);
			}
			try {
				JavaShellUtil.executeShell("convert " + path + "  -resize '"
						+ widthAndHeight + "' " + thumbPath);
				return rootWebDir + thumbPath.replace(rootPath, "");
			} catch (IOException e1) {
				e1.printStackTrace();
				LOGGER.error(e.getMessage());
				return rootWebDir + path.replace(rootPath, "");
			}
		}
	}

	/**
	 * 生成缩略图
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	public static String thumb(String path, int width, int height) {

		if (!ValidateUtil.isValid(path)) {
			return path;
		}

		if (width == 0 || height == 0) {
			return path;
		}

		String rootPath = FilesUtil.getRootDir();

		// 处理路径
		if (path.indexOf(rootPath) == -1) {
			path = rootPath + path;
		}

		// 图片要改成的高度,宽度
		// 组装新图片地址

		String thumbPath = FilesUtil.getThumbPath(path, width, height);

		File file = new File(thumbPath);
		if (file.exists() && file.isFile()) {
			return thumbPath.substring(rootPath.length());
		}

		File oldFile = new File(path);
		if (!oldFile.isFile()) {
			return path.substring(rootPath.length());
		}

		String newDir = file.getParent();
		File parentDir = new File(newDir);
		if (!parentDir.isDirectory()) {
			parentDir.mkdirs();
		}

		try {
			Thumbnails.of(path).size(width, height).toFile(thumbPath);
			return thumbPath.substring(rootPath.length());
		} catch (Exception e) {
			String widthAndHeight = "";
			if (width > 0) {
				widthAndHeight += String.valueOf(width);
			}
			if (height > 0) {
				widthAndHeight += "x" + String.valueOf(height);
			}
			try {
				JavaShellUtil.executeShell("convert " + path + "  -resize '"
						+ widthAndHeight + "' " + thumbPath);
				return thumbPath.substring(rootPath.length());
			} catch (IOException e1) {
				e1.printStackTrace();
				LOGGER.error(e.getMessage());
				return path.substring(rootPath.length());
			}
		}

	}

	public static String showProjectFile(Files files) {

		if (files == null) {
			return "";
		}

		if (files.getFileType().contains("image")
				|| files.getFileType().contains("pdf")) {
			return files.getNewPath();
		} else if (files.getFileType().contains("video")) {
			return "/video/" + files.getId();
		} else {
			return "/file-info/download?id=" + files.getId();
		}

	}

	/**
	 * 显示图标
	 * 
	 * @param path
	 * @return
	 */
	public static String fileIcon(String path) {

		String getPath = FileIconUtil.icon(path);
		String rootPath = FilesUtil.getRootDir();
		File file = new File(rootPath + getPath);
		if (file.exists()) {
			return getPath;
		}
		return FileIconUtil.icon(path);
	}

}
