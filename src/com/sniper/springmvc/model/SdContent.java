package com.sniper.springmvc.model;

/**
 * 新闻内容
 * 
 * @author suzhen
 * 
 */
public class SdContent extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer cid;
	private int sid;
	// 新闻内容
	private String content;

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}