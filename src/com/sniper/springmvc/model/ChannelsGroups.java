package com.sniper.springmvc.model;

public class ChannelsGroups extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String group_id;
	private String channel_id;

	public String getGroup_id() {
		return group_id;
	}

	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

}