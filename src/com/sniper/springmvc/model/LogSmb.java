package com.sniper.springmvc.model;

import java.util.Date;

public class LogSmb extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	private String localFile;
	private String remoteFile;
	private Date ctime = new Date();
	private Date etime = new Date();
	private Boolean success;
	private String note;
	// 操作处理名称 添加，删除，一般修改等于覆盖，因为文件不存在修改，是覆盖
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLocalFile() {
		return localFile;
	}

	public void setLocalFile(String localFile) {
		this.localFile = localFile;
	}

	public String getRemoteFile() {
		return remoteFile;
	}

	public void setRemoteFile(String remoteFile) {
		this.remoteFile = remoteFile;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Date getEtime() {
		return etime;
	}

	public void setEtime(Date etime) {
		this.etime = etime;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
