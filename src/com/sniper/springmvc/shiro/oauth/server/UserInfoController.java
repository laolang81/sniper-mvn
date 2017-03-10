package com.sniper.springmvc.shiro.oauth.server;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sniper.springmvc.mybatis.service.impl.OauthUserService;
import com.sniper.springmvc.shiro.oauth.Constants;

@Controller
public class UserInfoController {

	@Resource
	private OauthUserService oauthUserService;

	/**
	 * 根据 access_token 获取用户相应的信息
	 * 和客户端的那个是一样的
	 * @param request
	 * @return
	 * @throws OAuthSystemException
	 */
	@RequestMapping("/oauthServer")
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
						.setRealm(Constants.RESOURCE_SERVER_NAME.toString())
						.setError(OAuthError.ResourceResponse.INVALID_TOKEN)
						.buildHeaderMessage();

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
						.setRealm(Constants.RESOURCE_SERVER_NAME.toString())
						.buildHeaderMessage();

				HttpHeaders headers = new HttpHeaders();
				headers.add(OAuth.HeaderType.WWW_AUTHENTICATE, oauthResponse
						.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
				return new ResponseEntity<String>(headers,
						HttpStatus.UNAUTHORIZED);
			}

			OAuthResponse oauthResponse = OAuthResponse
					.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
					.setRealm(Constants.RESOURCE_SERVER_NAME.toString())
					.setError(e.getError())
					.setErrorDescription(e.getDescription())
					.setErrorUri(e.getUri()).buildHeaderMessage();

			HttpHeaders headers = new HttpHeaders();
			headers.add(OAuth.HeaderType.WWW_AUTHENTICATE,
					oauthResponse.getHeader(OAuth.HeaderType.WWW_AUTHENTICATE));
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}
}
