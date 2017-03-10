package com.sniper.springmvc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 调查页面多个页面组成一个完整调查
 * 
 * @author laolang
 * 
 */
public class SurveyPage extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private Date ctime = new Date();
	private Integer sort;
	private String note;
	// optional 导致我 的数据在service里面不用flush不能保存数据
	private Survey survey;

	// 问题列表
	private List<SurveyQuestion> sq = new ArrayList<>();

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

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public List<SurveyQuestion> getSq() {
		return sq;
	}

	public void setSq(List<SurveyQuestion> sq) {
		this.sq = sq;
	}

}
