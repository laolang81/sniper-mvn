package com.sniper.springmvc.model;

import java.util.Date;

public class Members extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String uid;
	private String username;
	private String password;
	private String email;
	private Boolean checkEmail;
	private String myid;
	private String myidkey;
	private String regip;
	private Date regdate;
	private String lastloginip;
	private Date lastlogintime;

	// 用户是否启用
	private Boolean enabled;
	// 用户是否锁定
	private Boolean locked;
	// 用户过期时间戳
	private Date usernameExpired;
	// 密码过期时间
	private Date passwordExpired;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getCheckEmail() {
		return checkEmail;
	}

	public void setCheckEmail(Boolean checkEmail) {
		this.checkEmail = checkEmail;
	}

	public String getMyid() {
		return myid;
	}

	public void setMyid(String myid) {
		this.myid = myid;
	}

	public String getMyidkey() {
		return myidkey;
	}

	public void setMyidkey(String myidkey) {
		this.myidkey = myidkey;
	}

	public String getRegip() {
		return regip;
	}

	public void setRegip(String regip) {
		this.regip = regip;
	}

	public Date getRegdate() {
		return regdate;
	}

	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}

	public String getLastloginip() {
		return lastloginip;
	}

	public void setLastloginip(String lastloginip) {
		this.lastloginip = lastloginip;
	}

	public Date getLastlogintime() {
		return lastlogintime;
	}

	public void setLastlogintime(Date lastlogintime) {
		this.lastlogintime = lastlogintime;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
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

}
