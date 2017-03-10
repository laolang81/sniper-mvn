package com.sniper.springmvc.model;

import java.util.Date;

public class FullCalendar extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	private String title;
	// 指定是否是全天事件
	private boolean allDay = false;
	private Date startDate;
	private Date endDate;
	// 当用户点击事件的时候，访问一个网址 ，病执行回调函数
	private String url;
	// 一个CSS类（或数组的类），将附加到该事件的元素。
	private String className;
	private boolean editable = false;
	private boolean startEditable = false;
	private boolean durationEditable = false;
	private String redering;
	private String constraints;
	private String source;
	private String color;
	private String backgroundColor;
	private String borderColor;
	private String textColor;

	private AdminUser adminUser;

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

	public boolean isAllDay() {
		return allDay;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public boolean isStartEditable() {
		return startEditable;
	}

	public void setStartEditable(boolean startEditable) {
		this.startEditable = startEditable;
	}

	public boolean isDurationEditable() {
		return durationEditable;
	}

	public void setDurationEditable(boolean durationEditable) {
		this.durationEditable = durationEditable;
	}

	public String getRedering() {
		return redering;
	}

	public void setRedering(String redering) {
		this.redering = redering;
	}

	public String getConstraints() {
		return constraints;
	}

	public void setConstraints(String constraint) {
		this.constraints = constraint;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

}
