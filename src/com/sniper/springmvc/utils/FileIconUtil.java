package com.sniper.springmvc.utils;

/**
 * 用户显示文件图标
 * 
 * @author suzhen
 * 
 */
public class FileIconUtil {

	/**
	 * 基础路径
	 */
	private static String BASE_PATH = "/myfiles/images/webicon/";

	public static String icon(String path) {

		String fileName = "error_64.png";
		if (ValidateUtil.isValid(path)) {
			String suffix = FilesUtil.getFileExt(path);
			switch (suffix.toLowerCase()) {
			case "png":
			case "jpg":
			case "jpeg":
			case "gif":
			case "tif":
			case "bmp":
			case "tiff":
				return path;
			case "doc":
			case "docx":
			case "ppt":
			case "pptx":
			case "xls":
			case "xlsx":
				fileName = suffix + "_64.png";
				break;
			case "flv":
			case "swf":
				fileName = "flash_64.png";
				break;
			case "txt":
				fileName = "txt_64.png";
				break;
			case "zip":
				fileName = "zip_64.png";
				break;
			case "tar":
				fileName = "rar_64.png";
				break;
			case "7z":
			case "cab":
				fileName = "file_zip_64.png";
				break;
			case "iso":
				fileName = "iso_64.png";
				break;

			default:
				fileName = "attach_64.png";
				break;
			}
		}
		return BASE_PATH + fileName;
	}

	public static String getDefaultIcon() {
		return BASE_PATH + "error_64.png";
	}
}
