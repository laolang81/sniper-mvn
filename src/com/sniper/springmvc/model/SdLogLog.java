package com.sniper.springmvc.model;

import java.util.Date;

/**
 * 校讯通日志，忘记怎么使用了
 * 
 * @author suzhen
 * 
 */
public class SdLogLog extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Date timestamp;
	private String priority;
	private String message;
	private String priorotyName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPriorotyName() {
		return priorotyName;
	}

	public void setPriorotyName(String priorotyName) {
		this.priorotyName = priorotyName;
	}

}
