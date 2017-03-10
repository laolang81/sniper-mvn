package com.sniper.springmvc.model;

import java.util.Date;

/**
 * solr搜索关键词
 * 
 * @author suzhen
 * 
 */
public class SolrSearchKey extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	private String key;
	private Date lastDate;
	private int count;
	private int todayCount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTodayCount() {
		return todayCount;
	}

	public void setTodayCount(int todayCount) {
		this.todayCount = todayCount;
	}

}
