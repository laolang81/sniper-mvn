package com.sniper.springmvc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 调查属性
 * 
 * @author laolang
 * 
 */
public class Survey extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	private Date cTime = new Date();
	// 当前
	private int peopleNum;
	// 规定最大参与人数
	private int peopleMaxNum;
	private Boolean locked = true;
	// 设置密码
	private String password;
	private Boolean page = false;
	private int template = 0;
	private String note;
	private String submitName = "完成问卷";
	private String listStyle = "";
	// 设置问卷到用户的关系
	private AdminUser adminUser;
	// 计划开始时间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date entDate;

	private Boolean multi = false;
	private int multiNum = 0;
	private Boolean verifyCode;
	private Integer verifyIpNum;
	private Integer verifyPhone;
	private String url;
	private Boolean openResult;

	private List<SurveyPage> surveyPages = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public int getPeopleNum() {
		return peopleNum;
	}

	public void setPeopleNum(int peopleNum) {
		this.peopleNum = peopleNum;
	}

	public int getPeopleMaxNum() {
		return peopleMaxNum;
	}

	public void setPeopleMaxNum(int peopleMaxNum) {
		this.peopleMaxNum = peopleMaxNum;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getPage() {
		return page;
	}

	public void setPage(Boolean page) {
		this.page = page;
	}

	public int getTemplate() {
		return template;
	}

	public void setTemplate(int template) {
		this.template = template;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSubmitName() {
		return submitName;
	}

	public void setSubmitName(String submitName) {
		this.submitName = submitName;
	}

	public String getListStyle() {
		return listStyle;
	}

	public void setListStyle(String listStyle) {
		this.listStyle = listStyle;
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEntDate() {
		return entDate;
	}

	public void setEntDate(Date entDate) {
		this.entDate = entDate;
	}

	public Boolean getMulti() {
		return multi;
	}

	public void setMulti(Boolean multi) {
		this.multi = multi;
	}

	public int getMultiNum() {
		return multiNum;
	}

	public void setMultiNum(int multiNum) {
		this.multiNum = multiNum;
	}

	public Boolean getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(Boolean verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Integer getVerifyIpNum() {
		return verifyIpNum;
	}

	public void setVerifyIpNum(Integer verifyIpNum) {
		this.verifyIpNum = verifyIpNum;
	}

	public Integer getVerifyPhone() {
		return verifyPhone;
	}

	public void setVerifyPhone(Integer verifyPhone) {
		this.verifyPhone = verifyPhone;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getOpenResult() {
		return openResult;
	}

	public void setOpenResult(Boolean openResult) {
		this.openResult = openResult;
	}

	public List<SurveyPage> getSurveyPages() {
		return surveyPages;
	}

	public void setSurveyPages(List<SurveyPage> surveyPages) {
		this.surveyPages = surveyPages;
	}

}
