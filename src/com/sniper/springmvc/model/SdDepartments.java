package com.sniper.springmvc.model;

import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * 处室
 * 
 * @author suzhen
 * 
 */
public class SdDepartments extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	// 标准名字
	@NotNull
	private String name;
	// 短名字
	private String deShortName;
	// 类型 ，有 内设机构，派驻机构，驻外代表处
	private Integer type = 1;
	private String banner;
	// 是否显示首页新闻
	private Integer bshowimgnews;
	// 外联
	private String exUrl;
	// 是否启用
	private Date ctime = new Date();
	// 是否启用
	private Integer deTrue = 1;
	// 是否在首页显示
	private Integer deHome = 0;
	// 排序
	private Integer deOrder = 1;

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeShortName() {
		return deShortName;
	}

	public void setDeShortName(String deShortName) {
		this.deShortName = deShortName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBanner() {
		return banner;
	}

	public void setBanner(String banner) {
		this.banner = banner;
	}

	public Integer getBshowimgnews() {
		return bshowimgnews;
	}

	public void setBshowimgnews(Integer bshowimgnews) {
		this.bshowimgnews = bshowimgnews;
	}

	public String getExUrl() {
		return exUrl;
	}

	public void setExUrl(String exUrl) {
		this.exUrl = exUrl;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Integer getDeTrue() {
		return deTrue;
	}

	public void setDeTrue(Integer deTrue) {
		this.deTrue = deTrue;
	}

	public Integer getDeHome() {
		return deHome;
	}

	public void setDeHome(Integer deHome) {
		this.deHome = deHome;
	}

	public Integer getDeOrder() {
		return deOrder;
	}

	public void setDeOrder(Integer deOrder) {
		this.deOrder = deOrder;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
