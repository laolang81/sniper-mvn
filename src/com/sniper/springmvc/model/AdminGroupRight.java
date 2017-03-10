package com.sniper.springmvc.model;

public class AdminGroupRight extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String group_id;
	private String right_id;

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getRight_id() {
		return right_id;
	}

	public void setRight_id(String right_id) {
		this.right_id = right_id;
	}

}
