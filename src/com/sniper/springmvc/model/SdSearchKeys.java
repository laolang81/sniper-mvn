package com.sniper.springmvc.model;

import java.util.Date;

public class SdSearchKeys extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private int totalToday;
	private int totalAll;
	private Date lastDatetime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalToday() {
		return totalToday;
	}

	public void setTotalToday(int totalToday) {
		this.totalToday = totalToday;
	}

	public int getTotalAll() {
		return totalAll;
	}

	public void setTotalAll(int totalAll) {
		this.totalAll = totalAll;
	}

	public Date getLastDatetime() {
		return lastDatetime;
	}

	public void setLastDatetime(Date lastDatetime) {
		this.lastDatetime = lastDatetime;
	}

}
