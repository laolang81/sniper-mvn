package com.sniper.springmvc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.sniper.springmvc.utils.DataUtil;

public class SdSubjects extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer sid;
	private Integer itemid;
	private Integer siteid;
	private Integer preid;
	@NotNull
	private String subject;
	private String subtitle;
	// 用户id
	private String authorid;
	private String authorname;
	private String fromsite;
	private String img;
	private String download;
	private Integer date = DataUtil.getTime();
	private Integer lastdate;
	private Integer todayView;
	private Integer view;
	private Integer lookthroughed;
	private Integer language;
	private String period;
	private String url;
	private String tags;
	private Integer swid;
	// 设置默认值
	private Integer icId;
	private Integer bhot;
	private Integer editDate;
	private String editUser;
	private Integer suggested;
	private Integer displayorder = DataUtil.getTime();
	private Integer moftec;
	private Integer viewMobile;
	// 临时字段不操作数据库
	private Integer temp;
	private Integer temp2;
	//
	private String sjIp;
	private String sjLastIp;
	private String auditUid;
	private Date auditDatetime;
	private String auditIp;
	private Date viewLastTime;
	private SdContent content;
	private List<SdItems> items = new ArrayList<>();
	private List<SdAttachments> attachments = new ArrayList<>();

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
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

	public String getAuthorname() {
		return authorname;
	}

	public void setAuthorname(String authorname) {
		this.authorname = authorname;
	}

	public String getFromsite() {
		return fromsite;
	}

	public void setFromsite(String fromsite) {
		this.fromsite = fromsite;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}

	public Integer getLastdate() {
		return lastdate;
	}

	public void setLastdate(Integer lastdate) {
		this.lastdate = lastdate;
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

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Integer getSwid() {
		return swid;
	}

	public void setSwid(Integer swid) {
		this.swid = swid;
	}

	public Integer getIcId() {
		return icId;
	}

	public void setIcId(Integer icId) {
		this.icId = icId;
	}

	public Integer getBhot() {
		return bhot;
	}

	public void setBhot(Integer bhot) {
		this.bhot = bhot;
	}

	public Integer getEditDate() {
		return editDate;
	}

	public void setEditDate(Integer editDate) {
		this.editDate = editDate;
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

	public String getSjIp() {
		return sjIp;
	}

	public void setSjIp(String sjIp) {
		this.sjIp = sjIp;
	}

	public String getSjLastIp() {
		return sjLastIp;
	}

	public void setSjLastIp(String sjLastIp) {
		this.sjLastIp = sjLastIp;
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

	public Date getViewLastTime() {
		return viewLastTime;
	}

	public void setViewLastTime(Date viewLastTime) {
		this.viewLastTime = viewLastTime;
	}

	public SdContent getContent() {
		return content;
	}

	public void setContent(SdContent content) {
		this.content = content;
	}

	public List<SdItems> getItems() {
		return items;
	}

	public void setItems(List<SdItems> items) {
		this.items = items;
	}

	public List<SdAttachments> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<SdAttachments> attachments) {
		this.attachments = attachments;
	}

	public Integer getTemp() {
		return temp;
	}

	public void setTemp(Integer temp) {
		this.temp = temp;
	}

	public Integer getTemp2() {
		return temp2;
	}

	public void setTemp2(Integer temp2) {
		this.temp2 = temp2;
	}

}