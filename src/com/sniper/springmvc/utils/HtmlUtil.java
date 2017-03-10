package com.sniper.springmvc.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.web.util.HtmlUtils;

public class HtmlUtil {
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
	private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
	private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	private static final String regEx_space = "\\s*|\t|\r|\n";// 定义空格回车换行符

	/**
	 * @param htmlStr
	 * @return 删除Html标签
	 */
	public static String delHTMLTag(String htmlStr) {
		Pattern p_script = Pattern.compile(regEx_script,
				Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签

		Pattern p_style = Pattern
				.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签

		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签

		Pattern p_space = Pattern
				.compile(regEx_space, Pattern.CASE_INSENSITIVE);
		Matcher m_space = p_space.matcher(htmlStr);
		htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
		return htmlStr.trim(); // 返回文本字符串
	}

	public static String getTextFromHtml(String htmlStr) {
		htmlStr = delHTMLTag(htmlStr);
		htmlStr = htmlStr.replaceAll("&nbsp;", "");
		// htmlStr = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
		// System.out.println(htmlStr);
		return htmlStr;
	}

	/**
	 * 吧一段字符串分解成 map对象
	 * 
	 * @return
	 * @throws IOException
	 */
	public static Map<String, String> selectAsMap(String fileStr)
			throws IOException {
		Map<String, String> selects = new HashMap<>();

		File file = new File(fileStr);
		InputStreamReader in = new InputStreamReader(new FileInputStream(file));
		BufferedReader bufferedReader = new BufferedReader(in);
		StringBuffer result = new StringBuffer();
		String a = "";
		while ((a = bufferedReader.readLine()) != null) {
			result.append(a);
		}
		in.close();
		String p2 = "\"[\\s\\S]*?\">[\\s\\S]*?<";
		Pattern pattern = Pattern.compile(p2);
		Matcher matcher = pattern.matcher(result.toString());
		while (matcher.find()) {
			String group = matcher.group();
			if (group.indexOf("http") == -1) {
				continue;
			}
			group = group.trim();
			// System.out.println(group);
			String g1 = group.substring(0, group.indexOf(">"));
			g1 = g1.replace("\"", "").trim();
			String g2 = group.substring(group.indexOf(">") + 1);
			g2 = g2.replace("<", "").trim();
			selects.put(g1, g2);
		}
		return selects;
	}

	public static void main(String[] args) throws IOException {
		String str = "<div style='text-align:center;'>&nbsp;整治“四风”&nbsp;&nbsp;&nbsp;清弊除垢<br/><span style='font-size:14px;'>&nbsp;</span><span style='font-size:18px;'>公司召开党的群众路线教育实践活动动员大会</span><br/></div>";
		System.out.println(HtmlUtils.htmlEscape(str));
		System.out.println(HtmlUtils.htmlEscapeDecimal(str));
		System.out.println(HtmlUtils.htmlEscapeHex(str));
		System.out.println(HtmlUtils.htmlUnescape(str));
		System.out.println(getTextFromHtml(str));
		// HtmlUtil.selectAsMap("/Users/suzhen/Desktop/各市招商局");
		
	}
}
