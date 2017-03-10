package com.sniper.springmvc.model;

import java.util.Date;

/**
 * 收藏
 * 
 * @author suzhen
 * 
 */
public class Collect extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String value;
	private AdminUser adminUser;
	private Date stime;
	private int num;
	private String type;
	private Date etime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	public Date getStime() {
		return stime;
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getEtime() {
		return etime;
	}

	public void setEtime(Date etime) {
		this.etime = etime;
	}
}
