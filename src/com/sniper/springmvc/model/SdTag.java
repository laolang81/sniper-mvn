package com.sniper.springmvc.model;

public class SdTag extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer tagid;
	private String tagname;
	private int subjectnum;
	private String relativetags;
	private String url;

	public Integer getTagid() {
		return tagid;
	}

	public void setTagid(Integer tagid) {
		this.tagid = tagid;
	}

	public String getTagname() {
		return tagname;
	}

	public void setTagname(String tagname) {
		this.tagname = tagname;
	}

	public int getSubjectnum() {
		return subjectnum;
	}

	public void setSubjectnum(int subjectnum) {
		this.subjectnum = subjectnum;
	}

	public String getRelativetags() {
		return relativetags;
	}

	public void setRelativetags(String relativetags) {
		this.relativetags = relativetags;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
