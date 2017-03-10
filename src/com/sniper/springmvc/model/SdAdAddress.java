package com.sniper.springmvc.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class SdAdAddress extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	@NotNull
	private String name;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:ss:mm")
	private Date ctime = new Date();
	private String uid;
	private String note;
	private String key;

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

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
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

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
