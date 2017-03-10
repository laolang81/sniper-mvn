package com.sniper.springmvc.solr;

import java.io.Serializable;
import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

/**
 * Solr 吧新闻试图存放进来 这样整个网站前台速度速度超快
 * 
 * @author suzhen
 * 
 */

public class SubjectViewModel implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String CORE_NAME = "subjectView";

	// 和新闻id一样
	@Field("id")
	private String id;
	// 新闻id
	@Field("sid")
	private Integer sid;
	@Field("siteid_i")
	private Integer siteid;
	@Field("siteidName_s")
	private String siteidName;
	@Field("itemid_i")
	private Integer itemid;
	@Field("itemidName_s")
	private String itemidName;
	@Field("preid_i")
	private Integer preid;
	@Field("subject")
	private String subject;
	@Field("subtitle_s")
	private String subtitle;
	@Field("fromsite_s")
	private String fromsite;
	@Field("date_dt")
	private Date date;
	@Field("todayView_i")
	private Integer todayView;
	@Field("view_i")
	private Integer view;
	@Field("lookthroughed_i")
	private Integer lookthroughed;
	@Field("language_i")
	private Integer language;
	@Field("period_s")
	// 阅读期刊
	private String period;
	@Field("url_s")
	private String url;
	@Field("webUrl_s")
	private String webUrl;
	@Field("bhot_i")
	private Integer bhot;
	@Field("suggested_i")
	private Integer suggested;
	@Field("displayorder_i")
	private Integer displayorder;
	@Field("moftec_i")
	private Integer moftec;
	@Field("viewMobile_i")
	private Integer viewMobile;
	@Field("icId_i")
	private Integer icId;
	@Field("viewLastTime_dt")
	private Date viewLastTime;
	@Field("tItemid_is")
	private Integer[] tItemid;

	@Field("attachments_ss")
	private String[] attachments;

	@Field("subjectContent")
	private String content;
	// 添加编辑日期和编辑人，审核人，审核时间，审核ip
	@Field("editdate_dt")
	private Date editdate;
	@Field("editUser_s")
	private String editUser;
	@Field("auditUid_s")
	private String auditUid;
	@Field("auditDatetime_dt")
	private Date auditDatetime;
	@Field("auditIp_s")
	private String auditIp;
	@Field("authorid_s")
	private String authorid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

	public String getSiteidName() {
		return siteidName;
	}

	public void setSiteidName(String siteidName) {
		this.siteidName = siteidName;
	}

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public String getItemidName() {
		return itemidName;
	}

	public void setItemidName(String itemidName) {
		this.itemidName = itemidName;
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

	public String getFromsite() {
		return fromsite;
	}

	public void setFromsite(String fromsite) {
		this.fromsite = fromsite;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
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

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public Integer getBhot() {
		return bhot;
	}

	public void setBhot(Integer bhot) {
		this.bhot = bhot;
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

	public Integer[] gettItemid() {
		return tItemid;
	}

	public void settItemid(Integer[] tItemid) {
		this.tItemid = tItemid;
	}

	public String[] getAttachments() {
		return attachments;
	}

	public void setAttachments(String[] attachments) {
		this.attachments = attachments;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getEditdate() {
		return editdate;
	}

	public void setEditdate(Date editdate) {
		this.editdate = editdate;
	}

	public String getEditUser() {
		return editUser;
	}

	public void setEditUser(String editUser) {
		this.editUser = editUser;
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

	public String getAuthorid() {
		return authorid;
	}

	public void setAuthorid(String authorid) {
		this.authorid = authorid;
	}

}