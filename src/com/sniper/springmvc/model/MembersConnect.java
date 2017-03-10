package com.sniper.springmvc.model;

/**
 * 用户于外联表的关系
 * 
 * @author sniper
 * 
 */
public class MembersConnect extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	private String provider;
	private String openId;
	private String uid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

}
