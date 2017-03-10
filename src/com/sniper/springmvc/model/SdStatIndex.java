package com.sniper.springmvc.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 需要引入第三方数据库
 * 
 * @author suzhen
 * 
 */
public class SdStatIndex extends BaseEntity {

	
	private static final long serialVersionUID = 1L;
	private Integer id;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	private String url;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	private Date datetime;
	private String stIp;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getStIp() {
		return stIp;
	}

	public void setStIp(String stIp) {
		this.stIp = stIp;
	}

 }
