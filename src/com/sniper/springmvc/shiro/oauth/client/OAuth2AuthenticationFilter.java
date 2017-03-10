package com.sniper.springmvc.shiro.oauth.client;

import java.io.IOException;

import javax.naming.AuthenticationException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * 该拦截器的作用： 1、首先判断有没有服务端返回的error参数，如果有则直接重定向到失败页面； 2、接着如果用户还没有身份验证，判断是否有auth
 * code参数（即是不是服务端授权之后返回的），如果没有则重定向到服务端进行授权； 3、否则调用executeLogin进行登录，通过auth
 * code创建OAuth2Token提交给Subject进行登录； 4、登录成功将回调onLoginSuccess方法重定向到成功页面；
 * 5、登录失败则回调onLoginFailure重定向到失败页面。
 * 
 * @author suzhen
 * 
 */
public class OAuth2AuthenticationFilter extends AuthenticatingFilter {
	// oauth2 authc code参数名
	private String authcCodeParam = "code";
	// 客户端id
	private String clientId;
	// 服务器端登录成功/失败后重定向到的客户端地址
	private String redirectUrl;
	// oauth2服务器响应类型
	private String responseType = "code";
	private String failureUrl;

	public String getAuthcCodeParam() {
		return authcCodeParam;
	}

	public void setAuthcCodeParam(String authcCodeParam) {
		this.authcCodeParam = authcCodeParam;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getFailureUrl() {
		return failureUrl;
	}

	public void setFailureUrl(String failureUrl) {
		this.failureUrl = failureUrl;
	}

	@Override
	protected AuthenticationToken createToken(ServletRequest request,
			ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String code = httpRequest.getParameter(authcCodeParam);
		return new OAuth2Token(code);
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		String error = request.getParameter("error");
		String errorDescription = request.getParameter("error_description");
		if (!StringUtils.isEmpty(error)) {// 如果服务端返回了错误
			WebUtils.issueRedirect(request, response, failureUrl + "?error="
					+ error + "error_description=" + errorDescription);
			return false;
		}
		Subject subject = getSubject(request, response);
		if (!subject.isAuthenticated()) {
			if (StringUtils.isEmpty(request.getParameter(authcCodeParam))) {
				// 如果用户没有身份验证，且没有auth code，则重定向到服务端授权
				saveRequestAndRedirectToLogin(request, response);
				return false;
			}
		}
		// 执行父类里的登录逻辑，调用Subject.login登录
		return executeLogin(request, response);
	}

	// 登录成功后的回调方法 重定向到成功页面
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token,
			Subject subject, ServletRequest request, ServletResponse response)
			throws Exception {
		issueSuccessRedirect(request, response);
		return false;
	}

	// 登录失败后的回调
	protected boolean onLoginFailure(AuthenticationToken token,
			AuthenticationException ae, ServletRequest request,
			ServletResponse response) {
		Subject subject = getSubject(request, response);
		if (subject.isAuthenticated() || subject.isRemembered()) {
			try { // 如果身份验证成功了 则也重定向到成功页面
				issueSuccessRedirect(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try { // 登录失败时重定向到失败页面
				WebUtils.issueRedirect(request, response, failureUrl);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
