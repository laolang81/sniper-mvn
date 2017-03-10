package com.sniper.springmvc.shiro.oauth.client;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.ParameterStyle;
import org.apache.oltu.oauth2.common.utils.OAuthUtils;
import org.apache.oltu.oauth2.rs.request.OAuthAccessResourceRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sniper.springmvc.mybatis.service.impl.OauthClientService;
import com.sniper.springmvc.mybatis.service.impl.OauthUserService;

public class UserInfoController {

	@Resource
	private OauthClientService clientService;

	@Resource
	private OauthUserService oauthUserService;

	/**
	 * 如上代码的作用： 1、首先通过如http://localhost:8080/chapter17-server/userInfo?
	 * access_token=828beda907066d058584f37bcfd597b6进行访问； 2、该控制器会验证access
	 * token的有效性；如果无效了将返回相应的错误，客户端再重新进行授权； 3、如果有效，则返回当前登录用户的用户名。
	 * 
	 * @param request
	 * @return
	 * @throws OAuthSystemException
	 */
	@RequestMapping("/oauthClient")
	public HttpEntity<String> userInfo(HttpServletRequest request)
			throws OAuthSystemException {
		try {
			// 构建OAuth资源请求
			OAuthAccessResourceRequest oauthRequest = new OAuthAccessResourceRequest(
					request, ParameterStyle.QUERY);
			// 获取Access Token
			String accessToken = oauthRequest.getAccessToken();

			// 验证Access Token
			if (!oauthUserService.checkAccessToken(accessToken)) {
				// 如果不存在/过期了，返回未验证错误，需重新验证
				OAuthResponse oauthResponse = OAuthResponse
						.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setError(OAuthError.ResourceResponse.INVALID_TOKEN)
						.setErrorDescription(OAuthError.OAUTH_ERROR_DESCRIPTION)
						.setRealm(OAuth.WWWAuthHeader.REALM)
						.setScope(OAuth.OAUTH_SCOPE).buildHeaderMessage();

				HttpHeaders headers = new HttpHeaders();
				headers.add(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse
						.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
				return new ResponseEntity<String>(headers,
						HttpStatus.UNAUTHORIZED);
			}
			// 返回用户名
			String username = oauthUserService
					.getUsernameByAccessToken(accessToken);
			return new ResponseEntity<String>(username, HttpStatus.OK);
		} catch (OAuthProblemException e) {
			// 检查是否设置了错误码
			String errorCode = e.getError();
			if (OAuthUtils.isEmpty(errorCode)) {
				OAuthResponse oauthResponse = OAuthResponse
						.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setRealm(OAuth.WWWAuthHeader.REALM)
						.buildHeaderMessage();

				HttpHeaders headers = new HttpHeaders();
				headers.add(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse
						.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
				return new ResponseEntity<String>(headers,
						HttpStatus.UNAUTHORIZED);
			}

			OAuthResponse oauthResponse = OAuthResponse
					.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
					.setRealm(OAuth.WWWAuthHeader.REALM).setError(e.getError())
					.setErrorDescription(e.getDescription())
					.setErrorUri(e.getUri()).buildHeaderMessage();

			HttpHeaders headers = new HttpHeaders();
			headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
					oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

}
