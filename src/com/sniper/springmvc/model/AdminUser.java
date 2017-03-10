package com.sniper.springmvc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.msgpack.annotation.Message;
import org.springframework.format.annotation.DateTimeFormat;

@Message
public class AdminUser extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String password;
	private String nickName;
	private String mobile;
	private String email;
	// 用户是否启用
	private Boolean enabled;
	// 用户过期时间戳
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date usernameExpired;
	// 密码过期时间
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date passwordExpired;
	// 用户是否锁定
	private Boolean locked;
	// 用户加密密钥结果
	private String signCode;
	// 创建时间
	private Date ctime = new Date();
	// 用户包含的处室
	private String siteid;
	private List<String> siteids = new ArrayList<>();
	// 原来的uid
	private String sourceUid;

	// 对应用户组
	private List<AdminGroup> adminGroup = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Date getUsernameExpired() {
		return usernameExpired;
	}

	public void setUsernameExpired(Date usernameExpired) {
		this.usernameExpired = usernameExpired;
	}

	public Date getPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(Date passwordExpired) {
		this.passwordExpired = passwordExpired;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public String getSignCode() {
		return signCode;
	}

	public void setSignCode(String signCode) {
		this.signCode = signCode;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public List<String> getSiteids() {
		return siteids;
	}

	public void setSiteids(List<String> siteids) {
		this.siteids = siteids;
	}

	public String getSourceUid() {
		return sourceUid;
	}

	public void setSourceUid(String sourceUid) {
		this.sourceUid = sourceUid;
	}

	public List<AdminGroup> getAdminGroup() {
		return adminGroup;
	}

	public void setAdminGroup(List<AdminGroup> adminGroup) {
		this.adminGroup = adminGroup;
	}
}
