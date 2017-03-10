package com.sniper.springmvc.model;

import java.util.Date;

public class ErrorLog extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	private String message;
	private String error;
	private String errorClass;
	private Date ctime = new Date();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getErrorClass() {
		return errorClass;
	}

	public void setErrorClass(String errorClass) {
		this.errorClass = errorClass;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

}
