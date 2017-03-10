package com.sniper.springmvc.utils;


/**
 * ImageMagick 命令版
 * 
 * @author sniper
 * 
 */
public class JMagickCMDUtils {

	private final static String CONVERT = "convert";

	public static JMagickCMDUtils getInstance() {
		return new JMagickCMDUtils();
	}

	public static JMagickCMDUtils of(String file) {
		return JMagickCMDUtils.getInstance();
	}

	public static JMagickCMDUtils resize(int width, int height) {
		
		return null;

	}
	
	
	private void exec() {
		
	}

	public static void to(String file) {

	}
}
