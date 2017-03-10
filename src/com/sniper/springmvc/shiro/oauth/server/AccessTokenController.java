package com.sniper.springmvc.shiro.oauth.server;

import java.net.URISyntaxException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sniper.springmvc.action.RootController;
import com.sniper.springmvc.mybatis.service.impl.OauthClientService;
import com.sniper.springmvc.mybatis.service.impl.OauthUserService;

@Controller
public class AccessTokenController extends RootController {

	@Resource
	private OauthClientService clientService;

	@Resource
	private OauthUserService oauthUserService;

	/**
	 * 如上代码的作用：
	 * 1、首先通过如http://localhost:8080/chapter17-server/accessToken，POST提交如下数据
	 * ：client_id=1& client_secret=c03c0e9435984f53858f092e1edb5222&grant_type=
	 * authorization_code
	 * &code=828beda907066d058584f37bcfd597b6&redirect_uri=http
	 * ://localhost:9080/chapter17-client/oauth2-login访问 ；
	 * 2、该控制器会验证client_id、client_secret、auth code的正确性，如果错误会返回相应的错误；
	 * 3、如果验证通过会生成并返回相应的访问令牌access token。
	 * 
	 * @param request
	 * @return
	 * @throws URISyntaxException
	 * @throws OAuthSystemException
	 */
	@RequestMapping("/accessToken")
	public ResponseEntity<String> token(HttpServletRequest request)
			throws URISyntaxException, OAuthSystemException {
		try {
			// 构建OAuth请求
			OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

			// 检查提交的客户端id是否正确
			if (!oauthUserService.checkClientId(oauthRequest.getClientId())) {
				OAuthResponse response = OAuthResponse
						.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
						.setError(OAuthError.TokenResponse.INVALID_CLIENT)
						.setErrorDescription(OAuthError.OAUTH_ERROR_DESCRIPTION)
						.buildJSONMessage();
				return new ResponseEntity<String>(response.getBody(),
						HttpStatus.valueOf(response.getResponseStatus()));
			}

			// 检查客户端安全KEY是否正确
			if (!oauthUserService.checkClientSecret(oauthRequest
					.getClientSecret())) {
				OAuthResponse response = OAuthResponse
						.errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
						.setError(OAuthError.TokenResponse.UNAUTHORIZED_CLIENT)
						.setErrorDescription(OAuthError.OAUTH_ERROR_DESCRIPTION)
						.buildJSONMessage();
				return new ResponseEntity<String>(response.getBody(),
						HttpStatus.valueOf(response.getResponseStatus()));
			}

			String authCode = oauthRequest.getParam(OAuth.OAUTH_CODE);
			// 检查验证类型，此处只检查AUTHORIZATION_CODE类型，其他的还有PASSWORD或REFRESH_TOKEN
			if (oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(
					GrantType.AUTHORIZATION_CODE.toString())) {
				if (!oauthUserService.checkAuthCode(authCode)) {
					OAuthResponse response = OAuthResponse
							.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
							.setError(OAuthError.TokenResponse.INVALID_GRANT)
							.setErrorDescription("错误的授权码").buildJSONMessage();
					return new ResponseEntity<String>(response.getBody(),
							HttpStatus.valueOf(response.getResponseStatus()));
				}
			}

			// 生成Access Token
			OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(
					new MD5Generator());
			final String accessToken = oauthIssuerImpl.accessToken();
			oauthUserService.addAccessToken(accessToken,
					oauthUserService.getUsernameByAuthCode(authCode));

			// 生成OAuth响应
			OAuthResponse response = OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(accessToken)
					.setExpiresIn(
							String.valueOf(oauthUserService.getExpireIn()))
					.setScope(OAuth.OAUTH_SCOPE).buildJSONMessage();

			// 根据OAuthResponse生成ResponseEntity
			return new ResponseEntity<String>(response.getBody(),
					HttpStatus.valueOf(response.getResponseStatus()));
		} catch (OAuthProblemException e) {
			// 构建错误响应
			OAuthResponse res = OAuthResponse
					.errorResponse(HttpServletResponse.SC_BAD_REQUEST).error(e)
					.buildJSONMessage();
			return new ResponseEntity<String>(res.getBody(),
					HttpStatus.valueOf(res.getResponseStatus()));
		}
	}
}
