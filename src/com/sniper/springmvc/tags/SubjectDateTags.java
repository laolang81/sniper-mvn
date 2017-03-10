package com.sniper.springmvc.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.sniper.springmvc.freemarker.ViewHomeUtils;

public class SubjectDateTags extends SimpleTagSupport {

	private int date;

	public void setDate(int date) {
		this.date = date;
	}

	@Override
	public void doTag() throws JspException, IOException {
		
		getJspContext().getOut().print(ViewHomeUtils.intToDateString(date));
	}
}
