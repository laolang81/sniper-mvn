package com.sniper.springmvc.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.sniper.springmvc.mybatis.service.impl.AdminUserService;
import com.sniper.springmvc.shiro.oauth.Constants;

public class SysUserFilter extends PathMatchingFilter {
	@Autowired
	private AdminUserService userService;

	@Override
	protected boolean onPreHandle(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		Subject subject = SecurityUtils.getSubject();
		String username = (String) subject.getPrincipal();
		if (username != null) {
			if (request.getAttribute(Constants.CURRENT_USER.toString()) == null) {
				request.setAttribute(Constants.CURRENT_USER.toString(),
						userService.validateByName(username));
			}
		}

		return true;
	}
}
