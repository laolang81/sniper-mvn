package com.sniper.springmvc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sniper.springmvc.utils.DataUtil;

public class Channel extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String fid = "";
	private Integer sort = DataUtil.getTime();
	private Date stime = new Date();
	private Date letime = new Date();
	private String uid = "";
	private Boolean status;
	private String url;
	private String target; // 目标（ _blank、_self、_parent、_top）
	private String description; // 描述，填写有助于搜索引擎优化
	private String keywords; // 关键字，填写有助于搜索引擎优化
	// 页面显示方式,频道类型
	private Integer showType;
	// 是否前台显示
	private Boolean showHome;
	private String attachement;
	private String note;
	// 这个栏目不会再前台显示，他是专门用于文章小栏目选择的，属于特殊栏目
	private boolean special;

	private List<ChannelGroup> groups = new ArrayList<>();

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

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Date getStime() {
		return stime;
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public Date getLetime() {
		return letime;
	}

	public void setLetime(Date letime) {
		this.letime = letime;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getShowType() {
		return showType;
	}

	public void setShowType(Integer showType) {
		this.showType = showType;
	}

	public Boolean getShowHome() {
		return showHome;
	}

	public void setShowHome(Boolean showHome) {
		this.showHome = showHome;
	}

	public String getAttachement() {
		return attachement;
	}

	public void setAttachement(String attachement) {
		this.attachement = attachement;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Channel(String id) {
		super();
		this.id = id;
	}

	public Channel() {
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public List<ChannelGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<ChannelGroup> groups) {
		this.groups = groups;
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}

}