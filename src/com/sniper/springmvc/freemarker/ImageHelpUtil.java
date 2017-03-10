package com.sniper.springmvc.freemarker;

import java.io.File;
import java.io.IOException;

import net.coobird.thumbnailator.Thumbnails;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.JavaShellUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 用户前台图片大小的展示
 * 
 * @author sniper
 * 
 */
public class ImageHelpUtil {

	private final static Logger LOGGER = LoggerFactory
			.getLogger(ImageHelpUtil.class);

	public static String show(String path, int width, int height) {

		String rootWebDir = FilesUtil.getImagePrefix();

		if (!ValidateUtil.isValid(path)) {
			return rootWebDir + path;
		}

		if (width == 0 || height == 0) {
			return rootWebDir + path;
		}

		String rootPath = FilesUtil.getRootDir();

		if (path.indexOf(rootPath) == -1) {
			path = rootPath + path;
		}

		// 图片要改成的高度,宽度
		// 组装新图片地址
		String thumbPath = getThumbPath(path, width, height);

		File file = new File(thumbPath);
		if (file.exists() && file.isFile()) {
			return rootWebDir + thumbPath.replace(rootPath, "");
		}

		File oldFile = new File(path);
		if (!oldFile.isFile()) {
			return rootWebDir + path.replace(rootPath, "");
		}

		String newDir = file.getParent();
		File parentDir = new File(newDir);
		if (!parentDir.isDirectory()) {
			parentDir.mkdirs();
		}

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

		String thumbPath = getThumbPath(path, width, height);

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

	/**
	 * 获取缩略图的位置
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	public static String getThumbPath(String path, int width, int height) {
		// 组装新图片地址
		String suffix = path.substring(path.lastIndexOf("."));
		String thumbPath = path.substring(0, path.lastIndexOf(".")) + "_"
				+ String.valueOf(width) + "_" + String.valueOf(height) + suffix;

		thumbPath = thumbPath.replace("image", "thumb");
		return thumbPath;
	}

}
