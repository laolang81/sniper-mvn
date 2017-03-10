package com.sniper.springmvc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 本表存放的是用户所有数据的集合
 * 
 * @author sniper
 * 
 */
public class SurveyResultData extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	// 答题时间
	private Date cTime = new Date();
	// ip
	private String ip;
	// cookie
	private String cookie;
	// 用户浏览器信息
	private String agent;
	private String accept;
	private String locale;
	private String navigator;
	private String os;
	// 是否是登录用户
	private String uid;
	private String sessionid;

	private List<SurveyResult> results = new ArrayList<>();

	private Survey survey;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getAgent() {
		return agent;
	}

	public String getAccept() {
		return accept;
	}

	public void setAccept(String accept) {
		this.accept = accept;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getNavigator() {
		return navigator;
	}

	public void setNavigator(String navigator) {
		this.navigator = navigator;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public List<SurveyResult> getResults() {
		return results;
	}

	public void setResults(List<SurveyResult> results) {
		this.results = results;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

}
