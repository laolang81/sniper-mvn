package com.sniper.springmvc.utils;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class HttpRequestUtils {

	public static String getAccept(String accept) {
		return accept;

	}

	public static String getLocale(Locale locale) {

		if (Locale.SIMPLIFIED_CHINESE.equals(locale)) {
			return "简体中文";
		} else if (Locale.TRADITIONAL_CHINESE.equals(locale)) {
			return "繁体中文";

		} else if (Locale.ENGLISH.equals(locale)) {
			return "英文";
		} else if (Locale.JAPANESE.equals(locale)) {
			return "日本";
		}
		return locale.getLanguage();

	}

	/**
	 * 获取浏览器及其版本
	 * 
	 * @param userAgent
	 * @return
	 */
	public static String getNavigator(String userAgent) {
		if (userAgent.toLowerCase().indexOf("msie") > 0) {
			return getIE(userAgent);
		} else if (userAgent.toLowerCase().indexOf("chrome") > 0) {
			return getChrome(userAgent);
		} else if (userAgent.toLowerCase().indexOf("maxthon") > 0) {
			return getMaxthon(userAgent);
		} else if (userAgent.toLowerCase().indexOf("firefox") > 0) {
			return getFirefox(userAgent);
		} else {
			return "其他浏览器";
		}
	}

	public static String getIE(String userAgent) {
		Pattern pattern = Pattern.compile("MSIE [0-9]*\\.[0-9]*",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgent);
		String group = "";
		while (matcher.find()) {
			group = matcher.group();
		}
		return group;
	}

	public static boolean isFirefox(String userAgent) {
		Pattern pattern = Pattern.compile("Firefox/[0-9]*\\.[0-9]*",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgent);
		while (matcher.find()) {
			return true;
		}
		return false;
	}

	public static String getFirefox(String userAgent) {
		Pattern pattern = Pattern.compile("Firefox/[0-9]*\\.[0-9]*",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgent);
		String group = "";
		while (matcher.find()) {
			group = matcher.group();
		}
		return group;
	}

	public static String getChrome(String userAgent) {
		Pattern pattern = Pattern.compile("Chrome/[0-9]*\\.[0-9]*",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgent);
		String group = "";
		while (matcher.find()) {
			group = matcher.group();
		}
		return group;
	}

	public static String getMaxthon(String userAgent) {
		Pattern pattern = Pattern.compile("Maxthon/.* ",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgent);
		String group = "";
		while (matcher.find()) {
			group = matcher.group();
		}
		return group;
	}

	public static String getOS(String userAgent) {

		if (userAgent.indexOf("MSIE") > 0) {
			return getWindow(userAgent);
		} else if (userAgent.indexOf("Linux") > 0) {
			return "Linux";
		} else if (userAgent.indexOf("Mac") > 0) {
			return getMac(userAgent);
		} else {
			return getMobile(userAgent);
		}
	}

	public static String getWindow(String userAgent) {
		Pattern pattern = Pattern.compile("Windows [a-zA-Z]* [0-9]*\\.[0-9]*",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgent);
		String group = "";
		while (matcher.find()) {
			group = matcher.group().trim();
		}
		return group;
	}

	public static String getMac(String userAgent) {
		Pattern pattern = Pattern.compile("Mac OS X [0-9]*\\.[0-9]*",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(userAgent);
		String group = "";
		while (matcher.find()) {
			group = matcher.group();
		}
		return group;
	}

	public static String getMobile(String userAgent) {
		String[] mobileAgents = { "iphone", "android", "phone", "mobile",
				"wap", "netfront", "java", "opera mobi", "opera mini", "ucweb",
				"windows ce", "symbian", "series", "webos", "sony",
				"blackberry", "dopod", "nokia", "samsung", "palmsource", "xda",
				"pieplus", "meizu", "midp", "cldc", "motorola", "foma",
				"docomo", "up.browser", "up.link", "blazer", "helio", "hosin",
				"huawei", "novarra", "coolpad", "webos", "techfaith",
				"palmsource", "alcatel", "amoi", "ktouch", "nexian",
				"ericsson", "philips", "sagem", "wellcom", "bunjalloo", "maui",
				"smartphone", "iemobile", "spice", "bird", "zte-", "longcos",
				"pantech", "gionee", "portalmmm", "jig browser", "hiptop",
				"benq", "haier", "^lct", "320x320", "240x320", "176x220",
				"w3c ", "acs-", "alav", "alca", "amoi", "audi", "avan", "benq",
				"bird", "blac", "blaz", "brew", "cell", "cldc", "cmd-", "dang",
				"doco", "eric", "hipt", "inno", "ipaq", "java", "jigs", "kddi",
				"keji", "leno", "lg-c", "lg-d", "lg-g", "lge-", "maui", "maxo",
				"midp", "mits", "mmef", "mobi", "mot-", "moto", "mwbp", "nec-",
				"newt", "noki", "oper", "palm", "pana", "pant", "phil", "play",
				"port", "prox", "qwap", "sage", "sams", "sany", "sch-", "sec-",
				"send", "seri", "sgh-", "shar", "sie-", "siem", "smal", "smar",
				"sony", "sph-", "symb", "t-mo", "teli", "tim-", "tosh", "tsm-",
				"upg1", "upsi", "vk-v", "voda", "wap-", "wapa", "wapi", "wapp",
				"wapr", "webc", "winw", "winw", "xda", "xda-",
				"Googlebot-Mobile" };

		for (String mobileAgent : mobileAgents) {
			if (userAgent.toLowerCase().indexOf(mobileAgent) >= 0) {
				return mobileAgent;
			}
		}
		return "其他";
	}

	public static String getRealIP(HttpServletRequest request) {
		// nginx转发
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-real-ip");
		}
		// 代理客户端
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		// 多级代理
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		//
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取cookie的值
	 * 
	 * @param name
	 * @param cookies
	 * @return
	 */
	public static String getCookieValue(String name, Cookie[] cookies) {
		String result = "";
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					result = cookie.getValue();
				}
			}
		}

		return result;
	}

	/*
	 * 验证IP是否属于某个IP段
	 * 
	 * 
	 * 
	 * ipSection IP段（以'-'分隔）
	 * 
	 * ip 所验证的IP号码
	 */

	public static boolean ipExistsInRange(String ip, String ipSection) {

		ipSection = ipSection.trim();
		ip = ip.trim();
		int idx = ipSection.indexOf('-');
		String beginIP = ipSection.substring(0, idx);
		String endIP = ipSection.substring(idx + 1);
		return getIp2long(beginIP) <= getIp2long(ip)
				&& getIp2long(ip) <= getIp2long(endIP);

	}

	/**
	 * 转8进制
	 * 
	 * @param ip
	 * @return
	 */
	public static long getIp2long(String ip) {

		ip = ip.trim();
		String[] ips = ip.split("\\.");
		long ip2long = 0L;
		for (int i = 0; i < 4; ++i) {
			ip2long = ip2long << 8 | Integer.parseInt(ips[i]);
		}
		return ip2long;
	}

	public static long getIp2long2(String ip) {
		ip = ip.trim();
		String[] ips = ip.split("\\.");
		long ip1 = Integer.parseInt(ips[0]);
		long ip2 = Integer.parseInt(ips[1]);
		long ip3 = Integer.parseInt(ips[2]);
		long ip4 = Integer.parseInt(ips[3]);
		long ip2long = 1L * ip1 * 256 * 256 * 256 + ip2 * 256 * 256 + ip3 * 256
				+ ip4;
		return ip2long;
	}

	public static void main(String[] args) {
		// 10.10.10.116 是否属于固定格式的IP段10.10.1.00-10.10.255.255

		String ip = "10.10.10.116";

		String ipSection = "10.10.1.00-10.10.255.255";

		boolean exists = ipExistsInRange(ip, ipSection);
		System.out.println(exists);
		System.out.println(getIp2long(ip));
		System.out.println(getIp2long2(ip));
	}
}
