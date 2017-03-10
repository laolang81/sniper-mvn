package com.sniper.springmvc.cas;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SSO {
	private static final Logger LOG = LoggerFactory.getLogger(SSO.class);

	/**
	 * 
	 * http 请求状态码 201
	 */

	private static final int HTTP_STATUS_CODE_201 = 201;

	private static final int HTTP_STATUS_CODE_200 = 200;

	private static final int HTTP_STATUS_CODE_204 = 204;

	private static final int HTTP_STATUS_CODE_400 = 400;

	/**
	 * getServiceTicket
	 * 
	 * @param server
	 *            cas 服务 url
	 * @param username
	 * @param password
	 * @param service
	 *            被验证的服务 url
	 * @return
	 */
	public static String getServiceTicket(final String server,
			final String username, final String password, final String service) {

		notNull(server, "server must not be null");
		notNull(username, "username must not be null");
		notNull(password, "password must not be null");
		notNull(service, "service must not be null");

		String grantingTicket = getGrantingTicket(server, username, password);
		if (grantingTicket == null) {
			return null;
		}

		notNull(grantingTicket, "ticketGrantingTicket must not be null");

		final HttpClient client = new HttpClient();

		final PostMethod post = new PostMethod(server + "/" + grantingTicket);

		post.setRequestBody(new NameValuePair[] { new NameValuePair("service",
				service) });

		try {
			client.executeMethod(post);
			final String response = post.getResponseBodyAsString();
			switch (post.getStatusCode()) {

			case HTTP_STATUS_CODE_200:
				return response;
			case HTTP_STATUS_CODE_400:
				break;
			default:

				LOG.warn("Invalid response code (" + post.getStatusCode()
						+ ") from CAS server!");

				LOG.info("Response (1k): "
						+ response.substring(0,
								Math.min(1024, response.length())));
				break;
			}

		} catch (final IOException e) {
			LOG.warn("getServiceTicket" + e);

		} finally {
			post.releaseConnection();
		}
		return null;
	}

	/**
	 * 
	 * 获取 ticket granting ticket
	 * 
	 * 
	 * 
	 * @param server
	 *            cas 服务 url
	 * 
	 * @param username
	 *            验证的用户名
	 * 
	 * @param password
	 *            验证的用户密码
	 * 
	 * @return ticket granting ticket
	 */

	public static String getGrantingTicket(final String server,
			final String username, final String password) {

		notNull(server, "server must not be null");

		notNull(username, "username must not be null");

		notNull(password, "password must not be null");

		final HttpClient client = new HttpClient();

		final PostMethod post = new PostMethod(server);

		post.setRequestBody(new NameValuePair[] {
				new NameValuePair("username", username),
				new NameValuePair("password", password) });

		try {

			client.executeMethod(post);

			final String response = post.getResponseBodyAsString();
			switch (post.getStatusCode()) {

			case HTTP_STATUS_CODE_201: {

				final Matcher matcher = Pattern.compile(
						".*action=\".*/(.*?)\".*").matcher(response);

				if (matcher.matches()) {
					return matcher.group(1);
				}

				LOG.warn("Successful ticket granting request, but no ticket found!");

				LOG.info("Response (1k): "
						+ response.substring(0,
								Math.min(1024, response.length())));
				break;
			}

			default:
				LOG.warn("Invalid response code (" + post.getStatusCode()
						+ ") from CAS server!");
				LOG.info("Response (1k): "
						+ response.substring(0,
								Math.min(1024, response.length())));
				break;
			}

		} catch (final IOException e) {
			LOG.error("getTicketGrantingTicket", e);

		} finally {
			post.releaseConnection();
		}
		return null;
	}

	/**
	 * 
	 * 删除 cas 服务中制定 ticket granting ticket, 也就是 logout.
	 * 
	 * 
	 * 
	 * @param server
	 *            cas 服务 url
	 * 
	 * @param ticket
	 *            ticket granting ticket
	 */

	public static void deleteGrantingTicket(String server, String grantingTicket) {

		notNull(server, "server must not be null");

		notNull(grantingTicket, "grantingTicket must not be null");

		final HttpClient client = new HttpClient();

		final DeleteMethod delete = new DeleteMethod(server + "/"
				+ grantingTicket);

		try {
			client.executeMethod(delete);
			// final String response = delete.getResponseBodyAsString();
			System.out.println(delete.getStatusCode());
			System.out.println(delete.getStatusText());
			System.out.println(delete.getStatusLine());
			switch (delete.getStatusCode()) {
			case HTTP_STATUS_CODE_200:
				break;
			case HTTP_STATUS_CODE_204:
				// 204 表示服务器执行成功但是没有返回值
				LOG.info("Successful delete ticket granting  ticket.");
				break;
			default:
				LOG.warn("Invalid response code (" + delete.getStatusCode()
						+ ") from CAS server!");

				break;
			}

		} catch (final IOException e) {
			LOG.info("deleteTicket:" + e);

		} finally {
			delete.releaseConnection();
		}

	}

	/**
	 * 
	 * 参数验证方法，保证参数不为 null
	 * 
	 * 
	 * 
	 * @param object
	 *            需要验证的参数
	 * 
	 * @param message
	 *            验证的异常信息
	 */

	private static void notNull(final Object object, final String message) {

		if (object == null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * 验证 ticket
	 * 
	 * @param serverValidate
	 * @param serviceTicket
	 * @param service
	 * @return
	 */
	public static String serviceTicketValidate(String serverValidate,
			String serviceTicket, String service) {
		notNull(serviceTicket, "paramter 'serviceTicket' is not null");
		notNull(service, "paramter 'service' is not null");

		final HttpClient client = new HttpClient();
		GetMethod post = null;

		try {
			post = new GetMethod(serverValidate + "?" + "ticket="
					+ serviceTicket + "&service="
					+ URLEncoder.encode(service, "UTF-8"));
			client.executeMethod(post);
			final String response = post.getResponseBodyAsString();
			switch (post.getStatusCode()) {
			case HTTP_STATUS_CODE_200: {
				return response;
			}
			default:
			}

		} catch (Exception e) {
			LOG.error("ticket验证", e);
		} finally {
			// 释放资源
			post.releaseConnection();
		}
		return "";
	}

	public static void main(final String[] args) throws Exception {
		final String server = "http://java.laolang.com:8080/cas-server/v1/tickets";
		final String username = "admin";
		final String password = "admin";
		final String service = "http://java.laolang.com:8080/dmt";
		final String proxyValidate = "http://java.laolang.com:8080/cas-server/proxyValidate";
		// 下面是登录过程
		// 其实到了这一步基本就算登录了，我测试过只有用户名和密码都正确这里才有返回值，否则就是错误或者空
		String serviceTicket = getServiceTicket(server, username, password,
				service);
		System.out.println("serviceTicket:" + serviceTicket);
		if (serviceTicket == null) {
			System.out.println("登录失败");
		} else {
			String result = serviceTicketValidate(proxyValidate, serviceTicket,
					service);
			String validateString = "<cas:user>" + username + "</cas:user>";
			if (result.indexOf(validateString) > -1) {
				System.out.println("登录成功");
			} else {
				System.out.println("登录失败");
			}
		}
		// 下面是登出过程
		String grantingTicket = getGrantingTicket(server, username, password);
		System.out.println(grantingTicket);
		deleteGrantingTicket(server, grantingTicket);
	}
}