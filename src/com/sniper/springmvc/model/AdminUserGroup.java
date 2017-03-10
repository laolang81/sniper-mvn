package com.sniper.springmvc.model;

public class AdminUserGroup extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String uid;
	private String gid;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

}
