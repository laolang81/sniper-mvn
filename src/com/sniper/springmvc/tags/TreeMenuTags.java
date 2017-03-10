package com.sniper.springmvc.tags;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.sniper.springmvc.mybatis.service.impl.AdminRightService;

/**
 * 左侧导航读取
 * 
 * @author suzhen
 * 
 */
public class TreeMenuTags extends SimpleTagSupport {

	/**
	 * 上级id
	 */
	private int id;

	@Resource
	AdminRightService rightService;
	
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void doTag() throws JspException, IOException {
		super.doTag();
	}
}
