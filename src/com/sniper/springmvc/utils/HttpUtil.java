package com.sniper.springmvc.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtil {

	public final static Logger LOGGER = LoggerFactory
			.getLogger(com.sniper.springmvc.utils.HttpUtil.class);
	private static final String CHARACTER_ENCODING = "UTF-8";
	private static final String METHOD_POST = "POST";
	private static final String CONTENT_TYPE = "Content-Type";

	/**
	 * 判断网站是否真的可用
	 * 
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 */
	public static int isRealWebSite(String url) {
		if (!ValidateUtil.isValid(url)) {
			return 0;
		}
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpGet httpget = new HttpGet(url);
			CloseableHttpResponse response = httpclient.execute(httpget);
			RequestConfig config = RequestConfig.custom()
					.setConnectTimeout(1000).setSocketTimeout(1000).build();
			httpget.setConfig(config);
			int status = response.getStatusLine().getStatusCode();
			response.close();
			return status;
		} catch (IOException e) {
			LOGGER.error("网址：" + url + "" + e.getMessage());
		} finally {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}

	/**
	 * 打印数据
	 * 
	 * @param response
	 * @param data
	 * @throws IOException
	 */
	public static void responseWrite(HttpServletResponse response, byte[] data)
			throws IOException {

		if (data != null) {
			response.setContentLength(data.length);
			DataOutputStream dos = new DataOutputStream(
					response.getOutputStream());
			dos.write(data);
			dos.flush();
			dos.close();
		}
	}

	/**
	 * 从请求中读取字节
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static byte[] requestRead(HttpServletRequest request)
			throws IOException {
		int contentLength = request.getContentLength();
		byte[] data = null;
		if (contentLength > 0) {
			data = new byte[contentLength];
			InputStream inputStream = request.getInputStream();
			DataInputStream dis = new DataInputStream(inputStream);
			dis.readFully(data);
			dis.close();
		}
		return data;
	}

	/**
	 * post 方式指定想指定发送数据包请求,并获取返回的数据包
	 * 
	 * @param urlString
	 * @param requestData
	 * @return
	 */
	public static byte[] postRequest(String urlString, byte[] requestData) {
		Properties properties = new Properties();
		properties.setProperty(CONTENT_TYPE,
				"application/octet-stream;charset=" + CHARACTER_ENCODING);
		return postRequest(urlString, requestData, properties);
	}

	/**
	 * post 方式指定想指定发送数据包请求,并获取返回的数据包
	 * 
	 * @param urlString
	 *            请求地址
	 * @param requestData
	 *            请求数据
	 * @param properties
	 *            请求包体
	 * @return
	 */
	private static byte[] postRequest(String urlString, byte[] requestData,
			Properties properties) {
		byte[] responseData = null;
		HttpURLConnection con = null;
		try {
			URL url = new URL(urlString);
			con = (HttpURLConnection) url.openConnection();
			if ((properties != null) && (properties.size() > 0)) {
				for (Map.Entry<Object, Object> entry : properties.entrySet()) {
					String key = String.valueOf(entry.getKey());
					String value = String.valueOf(entry.getValue());
					con.setRequestProperty(key, value);
				}
			}
			con.setRequestMethod(METHOD_POST);
			con.setDoInput(true);
			con.setDoOutput(true);
			DataOutputStream dos = new DataOutputStream(con.getOutputStream());
			if (requestData != null) {
				dos.write(responseData);
			}
			dos.flush();
			dos.close();
			DataInputStream dis = new DataInputStream(con.getInputStream());
			int length = con.getContentLength();
			if (length > 0) {
				responseData = new byte[length];
				dis.readFully(responseData);
			}
			dis.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.disconnect();
			con = null;
		}
		return responseData;
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection
					.setRequestProperty("Accept",
							"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			connection.setRequestProperty("Accept-Language",
					"zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
			connection
					.setRequestProperty(
							"User-Agent",
							"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:43.0) Gecko/20100101 Firefox/43.0");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			for (String key : map.keySet()) {
				System.out.println(key + "--->" + map.get(key));
			}
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		OutputStreamWriter writer = null;
		BufferedReader in = null;
		HttpURLConnection conn = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			conn = (HttpURLConnection) realUrl.openConnection();
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			// 设置通用的请求属性
			conn.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:31.0) Gecko/20100101 Firefox/31.0");
			conn.connect();
			// 写入post数据
			writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			writer.write(param);
			// flush输出流的缓冲
			writer.flush();

			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}

		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (in != null) {
					in.close();
				}
				conn.disconnect();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	public static void main(String[] args) {
		// 发送 GET 请求

		// String area = new String("省属".getBytes(), Charset.forName("utf-8"));
		// String name = new String("浪潮".getBytes(), Charset.forName("utf-8"));
		// String url =
		// "http://cblw.shandongbusiness.gov.cn/interface/EntList.aspx?type=2&area=&name=浪潮";
		// String url1 =
		// "http://cblw.shandongbusiness.gov.cn/interface/EntList.aspx?type=2&area="
		// + area + "&name=" + name;
		// System.out.println(url1);
		// String s = HttpUtil.sendGet(url, null);
		// System.out.println(s);
		//
		// System.out.println(HttpUtil.isRealWebSite("http://freejudge.net/"));

		// 发送 POST 请求
//		String sr = HttpUtil
//				.sendPost(
//						"http://vote.sdcom.gov.cn/index.php/newvote/vote",
//						"choose[]=1&choose[]=1&choose[]=2&choose[]=3&choose[]=4&choose[]=5&choose[]=6&choose[]=7&choose[]=8&choose[]=9&choose[]=10&sniper=1");
//		System.out.println(sr);

		for (int i = 0; i < 1000; i++) {
			int a = HttpUtil.isRealWebSite("http://192.168.190.106:8080/login");
			System.out.println(a);
		}

	}
}
