package com.sniper.springmvc.model;

public class SdLink extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer linkid;
	// upid
	private SdLinkGroup linkGroup;
	private String name;
	private String description;
	private String logo;
	private String url;
	private int displayorder = 0;
	// 表示连接状态
	private int viewnum;

	public Integer getLinkid() {
		return linkid;
	}

	public void setLinkid(Integer linkid) {
		this.linkid = linkid;
	}

	public SdLinkGroup getLinkGroup() {
		return linkGroup;
	}

	public void setLinkGroup(SdLinkGroup linkGroup) {
		this.linkGroup = linkGroup;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(int displayorder) {
		this.displayorder = displayorder;
	}

	public int getViewnum() {
		return viewnum;
	}

	public void setViewnum(int viewnum) {
		this.viewnum = viewnum;
	}

}
