package com.sniper.springmvc.searchUtil;

public class AdminGroupSearch {

	private String fid;
	private Boolean isShow;
	private Boolean isMenu;
	private String groupName;
	private String url;
	private String type;
	private String autoload;

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	public Boolean getIsMenu() {
		return isMenu;
	}

	public void setIsMenu(Boolean isMenu) {
		this.isMenu = isMenu;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAutoload() {
		return autoload;
	}

	public void setAutoload(String autoload) {
		this.autoload = autoload;
	}

	@Override
	public String toString() {
		return "AdminGroupSearch [isShow=" + isShow + ", isMenu=" + isMenu
				+ ", groupName=" + groupName + ", url=" + url + "]";
	}

}
