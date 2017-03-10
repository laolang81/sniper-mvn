package com.sniper.springmvc.model;

/**
 * 记载用户和处室的关系
 * 
 * @author suzhen
 * 
 */
public class AdminUserOffice extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	private String uid;
	private String depid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDepid() {
		return depid;
	}

	public void setDepid(String depid) {
		this.depid = depid;
	}

}
