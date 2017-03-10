/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.sniper.springmvc.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

import com.sniper.springmvc.utils.PropertiesUtil;

/**
 * 全局配置类
 * 
 * @author ThinkGem
 * @version 2013-03-23
 */
public class Global {

	/**
	 * 属性文件加载对象
	 */
	// private static PropertiesLoader propertiesLoader = new
	// PropertiesLoader("");
	/**
	 * 保存全局属性值
	 */
	private static Map<String, String> map = new HashMap<>();
	static {
		InputStream in = Global.class.getClassLoader().getResourceAsStream(
				"properties/sniper.properties");
		PropertiesUtil pu = new PropertiesUtil(in);
		map = pu.getValues();
	}

	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		if (map.containsKey(key)) {
			return map.get(key);
		}
		return null;
	}

	// ///////////////////////////////////////////////////////

	/**
	 * 获取管理端根路径
	 */
	public static String getAdminPath() {
		return getConfig("adminPath");
	}

	/**
	 * 获取前端根路径
	 */
	public static String getFrontPath() {
		return getConfig("frontPath");
	}

	/**
	 * 获取URL后缀
	 */
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}

	/**
	 * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
	 */
	public static Boolean isDemoMode() {
		String dm = getConfig("demoMode");
		return "true".equals(dm) || "1".equals(dm);
	}

	/**
	 * 在修改系统用户和角色时是否同步到Activiti
	 */
	public static Boolean isSynActivitiIndetity() {
		String dm = getConfig("activiti.isSynActivitiIndetity");
		return "true".equals(dm) || "1".equals(dm);
	}

	/**
	 * 获取CKFinder上传文件的根目录
	 * 
	 * @return
	 */
	public static String getCkBaseDir() {
		String dir = getConfig("userfiles.basedir");
		Assert.hasText(dir, "配置文件里没有配置userfiles.basedir属性");
		if (!dir.endsWith("/")) {
			dir += "/";
		}
		return dir;
	}
}