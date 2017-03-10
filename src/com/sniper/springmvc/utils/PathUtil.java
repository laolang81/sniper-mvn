package com.sniper.springmvc.utils;

import org.junit.Test;

/**
 * java类获取web应用的根目录
 * 
 * @author suzhen
 * 
 */
public class PathUtil {

	@Test
	public void test() {
		System.out.println(PathUtil.getWebClassesPath());
		System.out.println(PathUtil.getWebRoot());
		System.out.println(PathUtil.getWebInfPath());
	}

	/**
	 * 获取当前类数据
	 * 
	 * @return
	 */
	public static String getWebClassesPath() {
		return PathUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	}

	public static String getWebInfPath() {
		String path = getWebClassesPath();
		if (path.indexOf("WEB-INF") > 0) {
			path = path.substring(0, path.indexOf("WEB-INF") + 8);
		}
		return path;
	}

	public static String getWebRoot() {
		String path = getWebClassesPath();
		if (path.indexOf("WEB-INF") > 0) {
			path = path.substring(0, path.indexOf("WEB-INF/classes"));
		}
		return path;
	}
}