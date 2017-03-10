package com.sniper.springmvc.model;

import java.util.Date;

public class SetPassword extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	private String email;
	private String uid;
	private String keyCode;
	private Date sendTime = new Date();
	private Date endTime;
	private String sign;
	private boolean signaTrue = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setSignaTrue(boolean signaTrue) {
		this.signaTrue = signaTrue;
	}

	public boolean isSignaTrue() {
		return signaTrue;
	}

}
