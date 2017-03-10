package com.sniper.springmvc.shiro.oauth.server;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ResponseType;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sniper.springmvc.action.RootController;
import com.sniper.springmvc.mybatis.service.impl.OauthClientService;
import com.sniper.springmvc.mybatis.service.impl.OauthUserService;

@Controller
public class OauthController extends RootController {
	@Resource
	private OauthClientService clientService;

	@Resource
	private OauthUserService oauthUserService;

	/**
	 * 如上代码的作用： 1、首先通过如http://java.laolang.com:8080/shiro/authorize ?client_id
	 * =1&response_type=code&redirect_uri
	 * =http://localhost:9080/chapter17-client/oauth2-login访问授权页面；
	 * 2、该控制器首先检查clientId是否正确；如果错误将返回相应的错误信息； 3、然后判断用户是否登录了，如果没有登录首先到登录页面登录；
	 * 4、登录成功后生成相应的auth
	 * code即授权码，然后重定向到客户端地址，如http://localhost:9080/chapter17-client
	 * /oauth2-login?
	 * code=52b1832f5dff68122f4f00ae995da0ed；在重定向到的地址中会带上code参数（授权码
	 * ），接着客户端可以根据授权码去换取access token。
	 * 
	 * @param map
	 * @param request
	 * @return
	 * @throws OAuthSystemException
	 * @throws URISyntaxException
	 */
	@RequestMapping("/authorize")
	public Object authorize(Map<String, Object> map, HttpServletRequest request)
			throws OAuthSystemException, URISyntaxException {

		try {
			// 构建OAuth 授权请求
			OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);

			// 检查传入的客户端id是否正确
			if (!oauthUserService.checkClientId(oauthRequest.getClientId())) {
				OAuthResponse response = OAuthResponse
						.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription(OAuthError.OAUTH_ERROR_DESCRIPTION)
						.buildJSONMessage();
				return new ResponseEntity<String>(response.getBody(),
						HttpStatus.valueOf(response.getResponseStatus()));

			}

			Subject subject = SecurityUtils.getSubject();
			// 如果用户没有登录，跳转到登陆页面
			if (!subject.isAuthenticated()) {
				if (!login(subject, request)) {
					// 登录失败时跳转到登陆页面
					map.put("client", clientService.findByClientId(oauthRequest
							.getClientId()));
					return "admin/login/oauth2login.jsp";
				}
			}

			String username = (String) subject.getPrincipal();
			// 生成授权码
			String authorizationCode = null;
			// responseType目前仅支持CODE，另外还有TOKEN
			String responseType = oauthRequest
					.getParam(OAuth.OAUTH_RESPONSE_TYPE);
			if (responseType.equals(ResponseType.CODE.toString())) {
				OAuthIssuerImpl oauthIssuerImpl = new OAuthIssuerImpl(
						new MD5Generator());
				authorizationCode = oauthIssuerImpl.authorizationCode();
				oauthUserService.addAuthCode(authorizationCode, username);
			}
			// 进行OAuth响应构建
			OAuthASResponse.OAuthAuthorizationResponseBuilder builder = OAuthASResponse
					.authorizationResponse(request,
							HttpServletResponse.SC_FOUND);
			// 设置授权码
			builder.setCode(authorizationCode);
			// 得到到客户端重定向地址
			String redirectURI = oauthRequest
					.getParam(OAuth.OAUTH_REDIRECT_URI);

			// 构建响应
			final OAuthResponse response = builder.location(redirectURI)
					.buildQueryMessage();

			// 根据OAuthResponse返回ResponseEntity响应
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(new URI(response.getLocationUri()));
			return new ResponseEntity<Object>(headers,
					HttpStatus.valueOf(response.getResponseStatus()));

		} catch (OAuthProblemException e) {
			// 出错处理
			String redirectUri = e.getRedirectUri();
			if (OAuthUtils.isEmpty(redirectUri)) {
				// 告诉客户端没有传入redirectUri直接报错
				return new ResponseEntity<String>(
						"OAuth callback url needs to be provided by client!!!",
						HttpStatus.NOT_FOUND);
			}
			// 返回错误消息（如?error=）
			final OAuthResponse response = OAuthResponse
					.errorResponse(HttpServletResponse.SC_FOUND).error(e)
					.location(redirectUri).buildQueryMessage();
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(new URI(response.getLocationUri()));
			return new ResponseEntity<Object>(headers,
					HttpStatus.valueOf(response.getResponseStatus()));

		}

	}

	private boolean login(Subject subject, HttpServletRequest request) {
		if ("get".equalsIgnoreCase(request.getMethod())) {
			return false;
		}
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		password = DigestUtils.sha1Hex(password);

		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			return false;
		}

		UsernamePasswordToken token = new UsernamePasswordToken(username,
				password);
		try {

			subject.login(token);
			return subject.isAuthenticated();
		} catch (Exception e) {
			request.setAttribute("error",
					"登录失败:" + e.getClass().getName() + e.getMessage());
			return false;
		}
	}

	@RequestMapping("callback")
	public String callback(@RequestParam("code") String code) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(
				"http://java.laolang.com:8080/shiro/accessToken");

		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("client_id", "1"));
		params.add(new BasicNameValuePair("client_secret",
				"c03c0e9435984f53858f092e1edb5222"));
		params.add(new BasicNameValuePair("grant_type", "authorization_code"));
		params.add(new BasicNameValuePair("code", code));
		params.add(new BasicNameValuePair("redirect_uri",
				"http://java.laolang.com:8080/shiro/callback2"));

		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			CloseableHttpResponse httpResponse = httpclient.execute(httpPost);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpEntity = httpResponse.getEntity();

				ObjectMapper mapper = new ObjectMapper();

				String result = EntityUtils.toString(httpEntity);// 取出应答字符串
				// 去除两边的双引号
				result = result.substring(1, result.length() - 1);
				if (result.indexOf("\\") > -1) {
					result = result.replace("\\", "");
				}

				@SuppressWarnings("unchecked")
				Map<String, Object> cackValue = mapper.readValue(result,
						Map.class);
				String accessToken = (String) cackValue.get("access_token");
				if (accessToken != null && !accessToken.equals("")) {
					String url = "http://java.laolang.com:8080/shiro/userInfo?access_token="
							+ accessToken;
					System.out.println(url);
					return "redirect:" + url;
				} else {

					return "redirect:/run";
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";

	}

	@RequestMapping("callback2")
	public String callback2(@RequestParam("access_token") String accessToken) {
		System.out.println(accessToken);
		String url = "http://java.laolang.com:8080/shiro/userInfo?access_token="
				+ accessToken;
		System.out.println(url);
		return "redirect:" + url;
	}

	@RequestMapping("run")
	public String run() {
		String url = "http://java.laolang.com:8080/shiro/authorize?client_id=1&response_type=code&redirect_uri=http://java.laolang.com:8080/shiro/callback";
		System.out.println(url);
		return "redirect:" + url;
	}

}