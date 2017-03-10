package com.sniper.springmvc.model;

import java.util.Date;

public class SdStatItem extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer statid;
	private Integer siteid;
	private Integer itemid;
	private Date date;
	private Date dateline = new Date();
	private Integer post;
	private Integer view;

	public Integer getStatid() {
		return statid;
	}

	public void setStatid(Integer statid) {
		this.statid = statid;
	}

	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDateline() {
		return dateline;
	}

	public void setDateline(Date dateline) {
		this.dateline = dateline;
	}

	public Integer getPost() {
		return post;
	}

	public void setPost(Integer post) {
		this.post = post;
	}

	public Integer getView() {
		return view;
	}

	public void setView(Integer view) {
		this.view = view;
	}

}
