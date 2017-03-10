package com.sniper.springmvc.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.sniper.springmvc.utils.FileIconUtil;

public class FileIconTags extends SimpleTagSupport {

	private String path;

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public void doTag() throws JspException, IOException {
		getJspContext().getOut().print(FileIconUtil.icon(path));
	}
}
