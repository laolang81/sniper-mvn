package com.sniper.springmvc.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class SdHistorySi extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private int siteid;
	private int item;
	private int view;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date viewTime;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date timeDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getSiteid() {
		return siteid;
	}

	public void setSiteid(int siteid) {
		this.siteid = siteid;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}

	public Date getViewTime() {
		return viewTime;
	}

	public void setViewTime(Date viewTime) {
		this.viewTime = viewTime;
	}

	public Date getTimeDate() {
		return timeDate;
	}

	public void setTimeDate(Date timeDate) {
		this.timeDate = timeDate;
	}

}
