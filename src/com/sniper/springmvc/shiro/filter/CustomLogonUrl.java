package com.sniper.springmvc.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

/**
 * 控制企业没有登录访问页面似的转向 AccessControlFilter
 * 
 * @author suzhen
 * 
 */
public class CustomLogonUrl extends AccessControlFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		// 获取当前网页地址
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		httpServletRequest.getRequestURI();
		Subject subject = getSubject(request, response);
		if (!subject.isAuthenticated() && !subject.isRemembered()) {
			WebUtils.issueRedirect(request, response, getLoginUrl());
			return false;
		}
		return true;
	}
}
