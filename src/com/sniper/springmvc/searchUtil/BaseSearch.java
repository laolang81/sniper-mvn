package com.sniper.springmvc.searchUtil;

import java.util.ArrayList;
import java.util.List;

public class BaseSearch {

	private String submit;
	private String sid;
	private Boolean status;
	private String channel;
	private String name;
	private String startDate;
	private String endDate;
	private String intStatus;
	private String order;
	private Integer siteid;
	private Integer itemid;
	private int limit = 50;
	private List<Integer> limits = new ArrayList<>();

	public String getSubmit() {
		return submit;
	}

	public void setSubmit(String submit) {
		this.submit = submit;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getIntStatus() {
		return intStatus;
	}

	public void setIntStatus(String intStatus) {
		this.intStatus = intStatus;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrder() {
		return order;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public void setLimits(List<Integer> limits) {
		this.limits = limits;
	}

	public List<Integer> getLimits() {
		limits.add(25);
		limits.add(50);
		limits.add(100);
		limits.add(200);
		limits.add(500);
		limits.add(1000);
		return limits;
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

}
