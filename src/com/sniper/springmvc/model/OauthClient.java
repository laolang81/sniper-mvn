package com.sniper.springmvc.model;

import java.util.Date;

public class OauthClient extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	// 客户端名称
	private String clientName;
	// 客户端描述
	private String description;
	// 客户端地址
	private String clientUrl;
	// 应用所有者
	private String uid;
	private String tags;
	// 图标16*16
	private String icoOne;
	// 80* 80
	private String icoTwo;
	// 120* 120
	private String icoThree;
	// 图标描述
	private String icoDescription;
	// 是一个客户点 App Key , 可以用id 代替
	private String clientId;
	// 客户端安全key-uuid App Secret
	private String clientSecret;

	private boolean enabled;

	private Date ctime = new Date();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getClientUrl() {
		return clientUrl;
	}

	public void setClientUrl(String clientUrl) {
		this.clientUrl = clientUrl;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getIcoOne() {
		return icoOne;
	}

	public void setIcoOne(String icoOne) {
		this.icoOne = icoOne;
	}

	public String getIcoTwo() {
		return icoTwo;
	}

	public void setIcoTwo(String icoTwo) {
		this.icoTwo = icoTwo;
	}

	public String getIcoThree() {
		return icoThree;
	}

	public void setIcoThree(String icoThree) {
		this.icoThree = icoThree;
	}

	public String getIcoDescription() {
		return icoDescription;
	}

	public void setIcoDescription(String icoDescription) {
		this.icoDescription = icoDescription;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

}
