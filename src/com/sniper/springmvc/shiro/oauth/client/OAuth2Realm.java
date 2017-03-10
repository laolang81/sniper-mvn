package com.sniper.springmvc.shiro.oauth.client;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * 此Realm首先只支持OAuth2Token类型的Token；然后通过传入的auth code去换取access token；再根据access
 * token去获取用户信息（用户名），然后根据此信息创建AuthenticationInfo；如果需要AuthorizationInfo信息，
 * 可以根据此处获取的用户名再根据自己的业务规则去获取。
 * 
 * @author suzhen
 * 
 */
public class OAuth2Realm extends AuthorizingRealm {

	private String clientId;
	private String clientSecret;
	private String accessTokenUrl;
	private String userInfoUrl;
	private String redirectUrl;

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getAccessTokenUrl() {
		return accessTokenUrl;
	}

	public void setAccessTokenUrl(String accessTokenUrl) {
		this.accessTokenUrl = accessTokenUrl;
	}

	public String getUserInfoUrl() {
		return userInfoUrl;
	}

	public void setUserInfoUrl(String userInfoUrl) {
		this.userInfoUrl = userInfoUrl;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	// 省略setter
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof OAuth2Token; // 表示此Realm只支持OAuth2Token类型
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(
			PrincipalCollection principals) {
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		return authorizationInfo;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		OAuth2Token oAuth2Token = (OAuth2Token) token;
		String code = oAuth2Token.getAuthCode(); // 获取 auth code
		String username = extractUsername(code); // 提取用户名
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				username, code, getName());
		return authenticationInfo;
	}

	private String extractUsername(String code) {
		try {
			OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
			OAuthClientRequest accessTokenRequest = OAuthClientRequest
					.tokenLocation(accessTokenUrl)
					.setGrantType(GrantType.AUTHORIZATION_CODE)
					.setClientId(clientId).setClientSecret(clientSecret)
					.setCode(code).setRedirectURI(redirectUrl)
					.buildQueryMessage();
			// 获取access token
			OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(
					accessTokenRequest, OAuth.HttpMethod.POST);
			String accessToken = oAuthResponse.getAccessToken();
			//有效期
			Long expiresIn = oAuthResponse.getExpiresIn();
			// 获取user info
			OAuthClientRequest userInfoRequest = new OAuthBearerClientRequest(
					userInfoUrl).setAccessToken(accessToken)
					.buildQueryMessage();
			OAuthResourceResponse resourceResponse = oAuthClient.resource(
					userInfoRequest, OAuth.HttpMethod.GET,
					OAuthResourceResponse.class);
			String username = resourceResponse.getBody();
			return username;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
