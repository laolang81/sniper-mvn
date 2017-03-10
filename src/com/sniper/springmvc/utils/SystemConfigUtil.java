package com.sniper.springmvc.utils;

import java.util.HashMap;
import java.util.Map;

import com.sniper.springmvc.mybatis.service.impl.SystemConfigService;
import com.sniper.springmvc.security.SpringContextUtil;

/**
 * 负责保存系统基本数据,
 * 
 * @author sniper
 * 
 */
public class SystemConfigUtil {

	private static SystemConfigService configService;
	static {
		configService = (SystemConfigService) SpringContextUtil
				.getBean(SystemConfigService.class);
	}

	public static SystemConfigService getConfigService() {
		return configService;
	}

	private static Map<String, String> systemConfig = new HashMap<>();
	static {
		init();
	}

	public static Map<String, String> getSystemConfig() {
		return systemConfig;
	}

	public static String get(String key) {
		if (systemConfig.containsKey(key)) {
			return systemConfig.get(key);
		}
		return "";
	}

	public static int getInt(String key) {
		if (systemConfig.containsKey(key)) {
			return Integer.valueOf(systemConfig.get(key)).intValue();
		}
		return 0;
	}

	public static long getLong(String key) {
		if (systemConfig.containsKey(key)) {
			return Long.valueOf(systemConfig.get(key)).longValue();
		}
		return 0;
	}

	public static void init() {
		if (getConfigService() != null) {
			systemConfig = getConfigService().getAdminConfig(true);
		} else {
			systemConfig = new HashMap<>();
		}
	}

	public static void setSystemConfig(Map<String, String> systemConfig) {
		SystemConfigUtil.systemConfig = systemConfig;
	}

	public static void clear() {
		systemConfig.clear();
	}
}
