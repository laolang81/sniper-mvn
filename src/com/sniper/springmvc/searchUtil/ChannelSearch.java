package com.sniper.springmvc.searchUtil;

public class ChannelSearch extends BaseSearch {

	private String type;
	private String fid;
	private String depid;
	private String itemUp;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getDepid() {
		return depid;
	}

	public void setDepid(String depid) {
		this.depid = depid;
	}

	public void setItemUp(String itemUp) {
		this.itemUp = itemUp;
	}

	public String getItemUp() {
		return itemUp;
	}

}
