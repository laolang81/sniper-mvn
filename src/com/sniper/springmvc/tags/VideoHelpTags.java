package com.sniper.springmvc.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.sniper.springmvc.freemarker.FreeMarkerViewUtil;

public class VideoHelpTags extends SimpleTagSupport {

	private String path;
	private String widthAndHeight;

	public void setPath(String path) {
		this.path = path;
	}

	public void setWidthAndHeight(String widthAndHeight) {
		this.widthAndHeight = widthAndHeight;
	}

	@Override
	public void doTag() throws JspException, IOException {

		this.getJspContext().getOut()
				.write(FreeMarkerViewUtil.getFace(path, widthAndHeight));
	}
}
