package com.sniper.springmvc.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.sniper.springmvc.utils.DataUtil;

/**
 * 栏目结构
 * 
 * @author suzhen
 * 
 */
public class SdItems extends BaseEntity {

	private static final long serialVersionUID = 1L;
	// 自增id
	private Integer itemid;
	// 上级id
	private Integer itemup = 0;
	private String type = "subject";
	// 处室id
	private Integer deprtid;
	// 标准名称
	@NotNull
	private String name;
	// 全程，一般不用
	private String fullname;
	private Integer status = 1;
	// 排序
	private Integer displayorder = DataUtil.getTime();
	private Integer subjectnum = 5;
	private Integer todayposts;
	private String lastpost;
	private String description;
	// 栏目中间排序
	private Integer pro1;
	// 右侧排序
	private Integer pro2;
	// 左侧排序
	private Integer pro3;
	// 二级排序
	private Integer pro4;
	private Integer pro5;
	private Integer pro6;
	private Integer blink;
	// 外联
	private String linkUrl;
	// 图片地址
	private String linkImg;
	private Integer style = 0;
	// 是否显示更多
	private boolean hasmore = true;
	// 绑定的处室 。及时加载
	private SdDepartments departments;

	private List<SdTabInfoClass> tabInfoClasses = new ArrayList<>();

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public Integer getItemup() {
		return itemup;
	}

	public void setItemup(Integer itemup) {
		this.itemup = itemup;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public SdDepartments getDepartments() {
		return departments;
	}

	public void setDepartments(SdDepartments departments) {
		this.departments = departments;
	}

	public Integer getDeprtid() {
		return deprtid;
	}

	public void setDeprtid(Integer deprtid) {
		this.deprtid = deprtid;
	}

	public Integer getDisplayorder() {
		return displayorder;
	}

	public void setDisplayorder(Integer displayorder) {
		this.displayorder = displayorder;
	}

	public Integer getSubjectnum() {
		return subjectnum;
	}

	public void setSubjectnum(Integer subjectnum) {
		this.subjectnum = subjectnum;
	}

	public Integer getPro1() {
		return pro1;
	}

	public void setPro1(Integer pro1) {
		this.pro1 = pro1;
	}

	public Integer getPro2() {
		return pro2;
	}

	public void setPro2(Integer pro2) {
		this.pro2 = pro2;
	}

	public Integer getPro3() {
		return pro3;
	}

	public void setPro3(Integer pro3) {
		this.pro3 = pro3;
	}

	public Integer getPro4() {
		return pro4;
	}

	public void setPro4(Integer pro4) {
		this.pro4 = pro4;
	}

	public Integer getPro5() {
		return pro5;
	}

	public void setPro5(Integer pro5) {
		this.pro5 = pro5;
	}

	public Integer getPro6() {
		return pro6;
	}

	public void setPro6(Integer pro6) {
		this.pro6 = pro6;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLinkImg() {
		return linkImg;
	}

	public void setLinkImg(String linkImg) {
		this.linkImg = linkImg;
	}

	public Integer getStyle() {
		return style;
	}

	public void setStyle(Integer style) {
		this.style = style;
	}

	public boolean isHasmore() {
		return hasmore;
	}

	public void setHasmore(boolean hasmore) {
		this.hasmore = hasmore;
	}

	public Integer getTodayposts() {
		return todayposts;
	}

	public void setTodayposts(Integer todayposts) {
		this.todayposts = todayposts;
	}

	public String getLastpost() {
		return lastpost;
	}

	public void setLastpost(String lastpost) {
		this.lastpost = lastpost;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getBlink() {
		return blink;
	}

	public void setBlink(Integer blink) {
		this.blink = blink;
	}

	public List<SdTabInfoClass> getTabInfoClasses() {
		return tabInfoClasses;
	}

	public void setTabInfoClasses(List<SdTabInfoClass> tabInfoClasses) {
		this.tabInfoClasses = tabInfoClasses;
	}

}