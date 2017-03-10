package com.sniper.springmvc.model;

import java.util.Date;

public class SdTabLeaveword extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String user;
	private String email;
	private String content;
	private Date time = new Date();
	private String ip;
	private Integer state;
	private Integer depId;
	private String answer;
	private Date answerTime;
	private int answerDiff;
	private String answerUser;
	private Integer type;
	private String tel;
	// 是否显示
	private Integer bShow;
	// 是否显示联系方式
	private Integer bopen;
	private Integer sid;
	private String phpServerObject;
	private Integer bmob;
	private Date officeMoveTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getDepId() {
		return depId;
	}

	public void setDepId(Integer depId) {
		this.depId = depId;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Date getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(Date answerTime) {
		this.answerTime = answerTime;
	}

	public String getAnswerUser() {
		return answerUser;
	}

	public void setAnswerUser(String answerUser) {
		this.answerUser = answerUser;
	}

	public int getAnswerDiff() {
		return answerDiff;
	}

	public void setAnswerDiff(int answerDiff) {
		this.answerDiff = answerDiff;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Integer getbShow() {
		return bShow;
	}

	public void setbShow(Integer bShow) {
		this.bShow = bShow;
	}

	public Integer getBopen() {
		return bopen;
	}

	public void setBopen(Integer bopen) {
		this.bopen = bopen;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public String getPhpServerObject() {
		return phpServerObject;
	}

	public void setPhpServerObject(String phpServerObject) {
		this.phpServerObject = phpServerObject;
	}

	public Integer getBmob() {
		return bmob;
	}

	public void setBmob(Integer bmob) {
		this.bmob = bmob;
	}

	public Date getOfficeMoveTime() {
		return officeMoveTime;
	}

	public void setOfficeMoveTime(Date officeMoveTime) {
		this.officeMoveTime = officeMoveTime;
	}

}
