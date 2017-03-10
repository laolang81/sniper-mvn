package com.sniper.springmvc.model;

import java.util.Date;

public class Notice extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	private String acceptUserName;
	private String sendUserName;
	private String title;
	private String content;
	private boolean status;
	private Date datetime = new Date();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAcceptUserName() {
		return acceptUserName;
	}

	public void setAcceptUserName(String acceptUserName) {
		this.acceptUserName = acceptUserName;
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

}
