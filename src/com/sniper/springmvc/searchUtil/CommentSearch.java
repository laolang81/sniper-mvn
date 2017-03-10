package com.sniper.springmvc.searchUtil;

public class CommentSearch extends BaseSearch {

	private String title;
	private String content;
	private Integer siteid;
	private int flag;

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

	public Integer getSiteid() {
		return siteid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

}
