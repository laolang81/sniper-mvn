package com.sniper.springmvc.shiro.oauth.client;

import org.apache.shiro.authc.AuthenticationToken;

public class OAuth2Token implements AuthenticationToken {

	private static final long serialVersionUID = 1L;
	private String authCode;
	private String credential;
	private String principal;

	public OAuth2Token() {
	}

	public OAuth2Token(String authCode) {
		super();
		this.authCode = authCode;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

	@Override
	public Object getCredentials() {
		return this.credential;
	}

	public String getAuthCode() {
		return this.authCode;
	}

}
