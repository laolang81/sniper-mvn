package com.sniper.springmvc.shiro;

import javax.servlet.ServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.subject.WebSubject;

public class MyShiroFilterFactoryBean extends ShiroFilterFactoryBean {

	@Override
	public void setLoginUrl(String loginUrl) {
		ServletRequest a = ((WebSubject) SecurityUtils.getSubject())
				.getServletRequest();
		System.out.println(a.getServletContext());

		super.setLoginUrl(loginUrl);
	}

}
