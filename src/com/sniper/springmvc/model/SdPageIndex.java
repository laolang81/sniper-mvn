package com.sniper.springmvc.model;

import javax.validation.constraints.NotNull;

public class SdPageIndex extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	@NotNull
	private String name;
	private String itemid;
	private Integer language;
	private Integer exSiteId;
	private Integer order;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItemid() {
		return itemid;
	}

	public void setItemid(String itemid) {
		this.itemid = itemid;
	}

	public Integer getLanguage() {
		return language;
	}

	public void setLanguage(Integer language) {
		this.language = language;
	}

	public Integer getExSiteId() {
		return exSiteId;
	}

	public void setExSiteId(Integer exSiteId) {
		this.exSiteId = exSiteId;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

}
