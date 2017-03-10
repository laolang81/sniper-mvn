package com.sniper.springmvc.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 文章操作日志
 * 
 * @author suzhen
 * 
 */
public class SdSubjectLogs extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private int sid;
	private String uid;
	private String logId;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:ss:mm")
	private Date time = new Date();
	private String message;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}