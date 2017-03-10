package com.sniper.springmvc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class FilesUtil {

	/**
	 * 生成指定的缩略图地址
	 * 
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	public static String getThumbPath(String path, int width, int height) {
		// 组装新图片地址
		String suffix = path.substring(path.lastIndexOf("."));
		String thumbPath = path.substring(0, path.lastIndexOf(".")) + "_" + String.valueOf(width) + "_"
				+ String.valueOf(height) + suffix;

		thumbPath = thumbPath.replace("image", "thumb");
		return thumbPath;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean delFileByPath(String path) {

		String rootPath = getRootDir();

		if (rootPath.endsWith("/")) {
			rootPath = rootPath.substring(0, rootPath.lastIndexOf("/"));
		}

		if (path.indexOf(rootPath) == -1) {
			path = rootPath + path;
		}

		System.out.println("file:" + path);
		File file = new File(path);
		if (!file.isFile()) {
			return true;
		}
		// 如果文件存在就删除
		try {
			return file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 删除文件
	 * 
	 * @param path
	 * @return
	 */
	public static boolean delFileByPathThumb(String path, int width, int height) {

		String rootPath = getRootDir();

		path = rootPath + path;

		String suffix = path.substring(path.lastIndexOf("."));
		path = path.substring(0, path.lastIndexOf(".")) + "_" + String.valueOf(width) + "_" + String.valueOf(height)
				+ suffix;

		path = path.replace("image", "thumb");
		File file = new File(path);
		if (!file.isFile()) {
			return true;
		}
		// 如果文件存在就删除
		try {
			return file.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean Move(File srcFile, String destPath) {
		// Destination directory
		File dir = new File(destPath);

		// Move file to new directory
		boolean success = srcFile.renameTo(new File(dir, srcFile.getName()));

		return success;
	}

	public static boolean Move(String srcFile, String destPath) {
		// File (or directory) to be moved
		File file = new File(srcFile);

		// Destination directory
		File dir = new File(destPath);

		// Move file to new directory
		boolean success = file.renameTo(new File(dir, file.getName()));

		return success;
	}

	public static void Copy(String oldPath, String newPath) {
		try {
			// int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					// bytesum += byteread;
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			System.out.println("error  " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void Copy(File oldfile, String newPath) {
		try {
			// int bytesum = 0;
			int byteread = 0;
			// File oldfile = new File(oldPath);
			if (oldfile.exists()) {
				InputStream inStream = new FileInputStream(oldfile);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1024];
				while ((byteread = inStream.read(buffer)) != -1) {
					// Fbytesum += byteread;
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
				fs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getWebUrl(String webUrl) {

		if (webUrl.endsWith("/")) {
			webUrl = webUrl.substring(0, webUrl.length() - 1);
		}
		return webUrl;
		// return "http://www.yummyshandong.com/";
	}

	/**
	 * 不带斜杠结尾 获取域名配置
	 * 
	 * @return
	 */
	public static String getWebUrl() {
		String webUrl = "";

		if (ValidateUtil.isValid(SystemConfigUtil.get("webSite"))) {
			webUrl = SystemConfigUtil.get("webSite");
		}
		if (webUrl.endsWith("/")) {
			webUrl = webUrl.substring(0, webUrl.length() - 1);
		}
		return webUrl;
	}

	/**
	 * 获取图片前缀域名
	 * 
	 * @return
	 */
	public static String getImagePrefix() {
		String webUrl = "";

		if (ValidateUtil.isValid(SystemConfigUtil.get("imagePrefix"))) {
			webUrl = SystemConfigUtil.get("imagePrefix");
		}
		if (webUrl.endsWith("/")) {
			webUrl = webUrl.substring(0, webUrl.length() - 1);
		}
		return webUrl;
	}

	/**
	 * 获取图片保存根目录
	 * 
	 * @return
	 */
	public static String getRootDir() {

		String rootDir = "";
		// 项目根目录
		if (ValidateUtil.isValid(SystemConfigUtil.get("rootDir"))) {
			rootDir = SystemConfigUtil.get("rootDir");
		}

		if (rootDir.equals("")) {
			rootDir = PathUtil.getWebRoot();
		}
		return rootDir;
	}

	/**
	 * 获取图片储存路径
	 * 
	 * @return
	 */
	public static String getSaveDir(String dir) {

		if (!ValidateUtil.isValid(dir)) {
			dir = "image";
		}

		String saveDir = "/public/attachment/three/" + dir + "/";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String ymd = sdf.format(new Date());
		saveDir += ymd + "/";
		return saveDir;
	}

	/**
	 * 生成uuid随机数 ,后缀 是否移除横岗
	 * 
	 * @param suffix
	 * @param removeBar
	 * @return
	 */
	public static String getUUIDName(String suffix, boolean removeBar) {

		StringBuffer name = new StringBuffer();
		UUID uuid = UUID.randomUUID();
		name.append(uuid.toString());

		if (!suffix.equals("")) {
			if (!suffix.startsWith(".")) {
				suffix = "." + suffix;
			}
			name.append(suffix);
		}
		if (removeBar) {
			return name.toString().replaceAll("-", "");
		}
		return name.toString();
	}

	public static String UUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 附件名称获取，获取附件原名称就是去掉_1920x190等字符
	 * 
	 * @param videoPath
	 * @return
	 */
	public static String removeThumbPath(String videoPath) {

		if (videoPath.contains("_")) {
			videoPath = videoPath.substring(0, videoPath.indexOf("_"))
					+ videoPath.substring(videoPath.lastIndexOf("."));
		}

		return videoPath;
	}

	/**
	 * 
	 * @param filepath
	 * @return
	 */
	public static String getFileName(String filepath) {

		int a = filepath.lastIndexOf("/");
		int b = filepath.lastIndexOf(".");
		if (a == -1 || b == -1 || b < a) {
			return filepath;
		}
		String newString = "";
		newString = filepath.substring(a + 1, b);

		return newString;
	}

	/**
	 * 
	 * @param filepath
	 * @return
	 */
	public static String getFileExt(String filepath) {

		int b = filepath.lastIndexOf(".");
		if (b == -1) {
			return filepath;
		}
		String newString = "";
		newString = filepath.substring(b + 1);

		return newString;
	}

	/**
	 * 读取文本内容
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			return "";
		}

		FileInputStream in = new FileInputStream(file);
		StringBuffer result = new StringBuffer();
		byte buffer[] = new byte[in.available()];
		while ((in.read(buffer)) != -1) {
			String a = new String(buffer);
			result.append(a);
		}

		in.close();
		return result.toString();
	}

	public static void main(String[] args) throws IOException {

		System.out.println(FilesUtil
				.readFile("/Users/suzhen/approot/web/java-sdcom/WebRoot/myfiles/Plugin/UEditor/jsp/config.json"));
	}
}
