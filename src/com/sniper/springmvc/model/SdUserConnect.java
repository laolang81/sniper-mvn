package com.sniper.springmvc.model;

import java.util.Date;

/**
 * 用户于外部链接关系表
 * 
 * @author suzhen
 * 
 */
public class SdUserConnect extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String uid;
	private Date ctime = new Date();
	private String uname;
	private String dname;
	private String sourceid;
	private String depid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getDname() {
		return dname;
	}

	public void setDname(String dname) {
		this.dname = dname;
	}

	public String getSourceid() {
		return sourceid;
	}

	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}

	public String getDepid() {
		return depid;
	}

	public void setDepid(String depid) {
		this.depid = depid;
	}

}
