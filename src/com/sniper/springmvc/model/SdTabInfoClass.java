package com.sniper.springmvc.model;

import javax.validation.constraints.NotNull;

public class SdTabInfoClass extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	@NotNull
	private String name;
	private Integer itemid;
	private String exField;
	private Integer enabled = 1;
	private Integer sort = 0;
	private SdItems items;

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

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public String getExField() {
		return exField;
	}

	public void setExField(String exField) {
		this.exField = exField;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public SdItems getItems() {
		return items;
	}

	public void setItems(SdItems items) {
		this.items = items;
	}

}
