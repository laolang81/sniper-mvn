package com.sniper.springmvc.model;


public class UserRunAs extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	private String fromUserId;// 授予身份帐号
	private String toUserId;// 被授予身份帐号

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
	}

}
