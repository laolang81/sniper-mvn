package com.sniper.springmvc.model;

/**
 * 表是mc_link_type
 * 
 * @author suzhen
 * 
 */
public class SdLinkGroup extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private Integer order;
	private String uid;
	private String note;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

}
