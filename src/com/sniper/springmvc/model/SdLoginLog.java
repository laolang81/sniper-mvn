package com.sniper.springmvc.model;

import java.util.Date;

/**
 * 登陆日志
 * 
 * @author suzhen
 * 
 */
public class SdLoginLog extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	private String uname;
	private Date stime;
	private String ip;
	private String pwd;
	private String agent;
	private String message;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public Date getStime() {
		return stime;
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
