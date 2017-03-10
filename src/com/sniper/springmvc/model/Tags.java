package com.sniper.springmvc.model;

import java.util.Date;

public class Tags extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private Date ctime;
	private AdminUser adminUser;
	private Integer sort;
	private String plug;
	private Boolean enabled;
	private int searchNum;
	private String description;

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

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getPlug() {
		return plug;
	}

	public void setPlug(String plug) {
		this.plug = plug;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public int getSearchNum() {
		return searchNum;
	}

	public void setSearchNum(int searchNum) {
		this.searchNum = searchNum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}