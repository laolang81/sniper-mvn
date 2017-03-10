package com.sniper.springmvc.model;

import java.util.Date;

public class SdUser extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String username;
	private String fullname;
	private String password;
	private String group;
	private boolean enbaled;
	private Date lastTime;
	private String lastIp;
	private String rand;
	private String siteid;
	private int audit;
	private int lookthrough;
	private int move;
	private int readLookthrough;
	private boolean onSign;
	private String sign;
	private String mobile;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public boolean isEnbaled() {
		return enbaled;
	}

	public void setEnbaled(boolean enbaled) {
		this.enbaled = enbaled;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public String getLastIp() {
		return lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	public String getRand() {
		return rand;
	}

	public void setRand(String rand) {
		this.rand = rand;
	}

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public int getAudit() {
		return audit;
	}

	public void setAudit(int audit) {
		this.audit = audit;
	}

	public int getLookthrough() {
		return lookthrough;
	}

	public void setLookthrough(int lookthrough) {
		this.lookthrough = lookthrough;
	}

	public int getMove() {
		return move;
	}

	public void setMove(int move) {
		this.move = move;
	}

	public boolean isOnSign() {
		return onSign;
	}

	public void setOnSign(boolean onSign) {
		this.onSign = onSign;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int getReadLookthrough() {
		return readLookthrough;
	}

	public void setReadLookthrough(int readLookthrough) {
		this.readLookthrough = readLookthrough;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
