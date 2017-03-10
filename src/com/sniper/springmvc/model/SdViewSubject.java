package com.sniper.springmvc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sniper.springmvc.utils.DataUtil;

public class SdViewSubject extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer siteid;
	private Integer itemid;
	private Integer preid;
	private String subject;
	private String subtitle;
	private String authorid;
	private String fromsite;
	private Integer date;
	private Integer todayView;
	private Integer view;
	private Integer lookthroughed;
	private Integer language = 1;
	private String period;
	private String url;
	private Integer swid = 0;
	private Integer bhot = 0;
	private Integer editdate = DataUtil.getTime();
	private String editUser;
	private Integer suggested;
	private Integer displayorder;
	private Integer moftec;
	private Integer viewMobile;
	private String auditUid;
	private Date auditDatetime;
	private String auditIp;
	private Integer temp;
	private Integer icId;
	private Date viewLastTime;
	private Integer tItemid;

	private List<SdAttachments> attachments = new ArrayList<>();

	// 首发处室
	private SdDepartments departments;
	// 手法栏目
	private SdItems items;

	private SdContent content;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public Integer getPreid() {
		return preid;
	}

	public void setPreid(Integer preid) {
		this.preid = preid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public String getAuthorid() {
		return authorid;
	}

	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}

	public String getFromsite() {
		return fromsite;
	}

	public void setFromsite(String fromsite) {
		this.fromsite = fromsite;
	}

	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}

	public Integer getTodayView() {
		return todayView;
	}

	public void setTodayView(Integer todayView) {
		this.todayView = todayView;
	}

	public Integer getView() {
		return view;
	}

	public void setView(Integer view) {
		this.view = view;
	}

	public Integer getLookthroughed() {
		return lookthroughed;
	}

	public void setLookthroughed(Integer lookthroughed) {
		this.lookthroughed = lookthroughed;
	}

	public Integer getLanguage() {
		return language;
	}

	public void setLanguage(Integer language) {
		this.language = language;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getSwid() {
		return swid;
	}

	public void setSwid(Integer swid) {
		this.swid = swid;
	}

	public Integer getBhot() {
		return bhot;
	}

	public void setBhot(Integer bhot) {
		this.bhot = bhot;
	}

	public Integer getEditdate() {
		return editdate;
	}

	public void setEditdate(Integer editdate) {
		this.editdate = editdate;
	}

	public String getEditUser() {
		return editUser;
	}

	public void setEditUser(String editUser) {
		this.editUser = editUser;
	}

	public Integer getSuggested() {
		return suggested;
	}

	public void setSuggested(Integer suggested) {
		this.suggested = suggested;
	}

	public Integer getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	public Integer getMoftec() {
		return moftec;
	}

	public void setMoftec(Integer moftec) {
		this.moftec = moftec;
	}

	public Integer getViewMobile() {
		return viewMobile;
	}

	public void setViewMobile(Integer viewMobile) {
		this.viewMobile = viewMobile;
	}

	public String getAuditUid() {
		return auditUid;
	}

	public void setAuditUid(String auditUid) {
		this.auditUid = auditUid;
	}

	public Date getAuditDatetime() {
		return auditDatetime;
	}

	public void setAuditDatetime(Date auditDatetime) {
		this.auditDatetime = auditDatetime;
	}

	public String getAuditIp() {
		return auditIp;
	}

	public void setAuditIp(String auditIp) {
		this.auditIp = auditIp;
	}

	public Integer getTemp() {
		return temp;
	}

	public void setTemp(Integer temp) {
		this.temp = temp;
	}

	public Integer getIcId() {
		return icId;
	}

	public void setIcId(Integer icId) {
		this.icId = icId;
	}

	public Date getViewLastTime() {
		return viewLastTime;
	}

	public void setViewLastTime(Date viewLastTime) {
		this.viewLastTime = viewLastTime;
	}

	public Integer gettItemid() {
		return tItemid;
	}

	public void settItemid(Integer tItemid) {
		this.tItemid = tItemid;
	}

	public List<SdAttachments> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<SdAttachments> attachments) {
		this.attachments = attachments;
	}

	public SdContent getContent() {
		return content;
	}

	public void setContent(SdContent content) {
		this.content = content;
	}

	public SdDepartments getDepartments() {
		return departments;
	}

	public void setDepartments(SdDepartments departments) {
		this.departments = departments;
	}

	public SdItems getItems() {
		return items;
	}

	public void setItems(SdItems items) {
		this.items = items;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

}