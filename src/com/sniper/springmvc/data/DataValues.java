package com.sniper.springmvc.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本数据保存
 * 
 * @author suzhen
 *
 */
public class DataValues {

	/**
	 * 管理员标签
	 */
	public final static String ROLE_ADMIN = "ROLE_ADMIN";
	/**
	 * 二级审核
	 */
	public final static String ROLE_AUDIT_TZ = "ROLE_AUDIT_TZ";
	/**
	 * 只能写
	 */
	public final static String ROLE_AUTHOR = "ROLE_AUTHOR";
	/**
	 * 普通用户
	 */
	public final static String ROLE_USER = "ROLE_USER";
	/**
	 * 管理员
	 */
	public final static String ROLE_MANGER = "ROLE_MANGER";

	/**
	 * 定义基本数据类型
	 */
	public static final Map<String, String> YES_NO;
	static {
		YES_NO = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;

			{
				put("0", "否");
				put("1", "是");
			}
		};
	}
	public static final Map<String, String> YES_NO_BOOLEAN = new HashMap<>();
	static {
		YES_NO_BOOLEAN.put("false", "否");
		YES_NO_BOOLEAN.put("true", "是");
	}

	public static final Map<String, String> IMAGE_TYPE = new HashMap<>();
	static {
		IMAGE_TYPE.put("0", "普通");
		IMAGE_TYPE.put("1", "图片");
	}

	public static final Map<String, String> PREIDS = new HashMap<>();
	static {
		PREIDS.put("0", "普通");
		PREIDS.put("1", "重要");
		PREIDS.put("2", "很重要");
	}

	public static final Map<String, String> POST_BASE = new HashMap<>();
	static {
		POST_BASE.put("1", "设置");
		POST_BASE.put("0", "取消");
	}

	public static final Map<String, String> IMAGES_MIN = new HashMap<>();
	static {
		IMAGES_MIN.put("1", "重要图片");
	}

	/**
	 * 浮动排序
	 */
	public static List<Integer> TempSort = new ArrayList<>();
	static {
		for (int i = -7; i <= 7; i++) {
			TempSort.add(i);
		}
	}

}
