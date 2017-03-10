package com.sniper.springmvc.model;

import java.util.Date;

/**
 * 附件结构
 * 
 * @author suzhen
 * 
 */
public class SdAttachments extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer aid;
	// 关联的新闻id
	private Integer sid;
	// 关联的用户id
	private String uid;
	private Integer dateline;
	private String prefilename;
	// 图片地址
	private String filename;
	private String description;
	private String filetype;
	private Long filesize;
	// 是否是图片
	private Integer isimage;
	// 是否是推荐新闻图片
	private Integer isprimeimage;
	private Integer mainsite;
	private String guid;
	private Date atTime = new Date();
	// 添加一个临时数据
	private SdSubjects subjects;

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Integer getDateline() {
		return dateline;
	}

	public void setDateline(Integer dateline) {
		this.dateline = dateline;
	}

	public String getPrefilename() {
		return prefilename;
	}

	public void setPrefilename(String prefilename) {
		this.prefilename = prefilename;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFiletype() {
		return filetype;
	}

	public void setFiletype(String filetype) {
		this.filetype = filetype;
	}

	public Long getFilesize() {
		return filesize;
	}

	public void setFilesize(Long filesize) {
		this.filesize = filesize;
	}

	public Integer getIsimage() {
		return isimage;
	}

	public void setIsimage(Integer isimage) {
		this.isimage = isimage;
	}

	public Integer getIsprimeimage() {
		return isprimeimage;
	}

	public void setIsprimeimage(Integer isprimeimage) {
		this.isprimeimage = isprimeimage;
	}

	public Integer getMainsite() {
		return mainsite;
	}

	public void setMainsite(Integer mainsite) {
		this.mainsite = mainsite;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Date getAtTime() {
		return atTime;
	}

	public void setAtTime(Date atTime) {
		this.atTime = atTime;
	}

	public SdSubjects getSubjects() {
		return subjects;
	}

	public void setSubjects(SdSubjects subjects) {
		this.subjects = subjects;
	}

}
