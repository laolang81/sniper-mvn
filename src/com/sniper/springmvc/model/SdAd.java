package com.sniper.springmvc.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class SdAd extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:ss:mm")
	private Date ctime = new Date();
	private String path;
	private String uid;
	private String address;
	private Integer width;
	private Integer height;
	private String url;
	private String note;
	private Date stime;
	private Date etime;
	private Integer order;
	private Integer type;
	private Integer enabled;

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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getStime() {
		return stime;
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public Date getEtime() {
		return etime;
	}

	public void setEtime(Date etime) {
		this.etime = etime;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

}
