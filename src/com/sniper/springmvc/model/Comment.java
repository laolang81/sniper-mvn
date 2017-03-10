package com.sniper.springmvc.model;

import java.util.Date;

/**
 * 留言
 * 
 * @author sniper
 * 
 */
public class Comment extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	private Channel category;// 分类编号
	private String contentId; // 归属分类内容的编号（Article.id、Photo.id、Download.id）
	private String title; // 归属分类内容的标题（Article.title、Photo.title、Download.title）
	private String content; // 评论内容
	private String name; // 评论姓名
	private String email;
	private String tel;
	private String ip; // 评论IP
	private Date createDate = new Date();// 评论时间
	private AdminUser auditUser; // 审核人
	private Date auditDate; // 审核时间
	private Integer delFlag = 0; // 删除标记删除标记（0：正常；1：删除；2：正常）
	private Boolean display = true;
	private String replay;
	private int belong;
	// tourism,post
	private String belongType = "none";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Channel getCategory() {
		return category;
	}

	public void setCategory(Channel category) {
		this.category = category;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public AdminUser getAuditUser() {
		return auditUser;
	}

	public void setAuditUser(AdminUser auditUser) {
		this.auditUser = auditUser;
	}

	public Date getAuditDate() {
		return auditDate;
	}

	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

	public String getReplay() {
		return replay;
	}

	public void setReplay(String replay) {
		this.replay = replay;
	}

	public int getBelong() {
		return belong;
	}

	public void setBelong(int belong) {
		this.belong = belong;
	}

	public String getBelongType() {
		return belongType;
	}

	public void setBelongType(String belongType) {
		this.belongType = belongType;
	}

}