package com.sniper.springmvc.utils;

import java.io.Serializable;

import org.msgpack.annotation.Message;

@Message
public class ParamsToHtmlButton implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static String COLOE_DEFAULE = "default";
	public final static String COLOE_PRIMARY = "primary";
	public final static String COLOE_SUCCESS = "success";
	public final static String COLOE_INFO = "info";
	public final static String COLOE_WARNING = "warning";
	public final static String COLOE_DANGER = "danger";
	private String name;
	private String value;
	private String target;
	private String type;
	private String click;
	private String color;

	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getClick() {
		return click;
	}

	public void setClick(String click) {
		this.click = click;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
