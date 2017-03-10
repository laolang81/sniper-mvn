package com.sniper.springmvc.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.msgpack.annotation.Message;

@Message
public class AdminRight extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String url;
	private String note;
	private Date ctime = new Date();

	// 公共资源所有问都可以访问
	private Boolean thePublic = false;
	// 显示为menu
	private Boolean theMenu = false;
	private Boolean theShow = false;
	private Integer sort = 0;
	private String fid = "0";
	private String target = "_self";
	private String permission;
	private String icon;
	private String type;

	/* mappedBy写在那边那边不维护 */
	private Set<AdminGroup> adminGroup = new HashSet<>();

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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public Boolean getThePublic() {
		return thePublic;
	}

	public void setThePublic(Boolean thePublic) {
		this.thePublic = thePublic;
	}

	public Boolean getTheMenu() {
		return theMenu;
	}

	public void setTheMenu(Boolean theMenu) {
		this.theMenu = theMenu;
	}

	public Boolean getTheShow() {
		return theShow;
	}

	public void setTheShow(Boolean theShow) {
		this.theShow = theShow;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<AdminGroup> getAdminGroup() {
		return adminGroup;
	}

	public void setAdminGroup(Set<AdminGroup> adminGroup) {
		this.adminGroup = adminGroup;
	}

	@Override
	public String toString() {
		return "AdminRight [id=" + id + ", name=" + name + ", url=" + url
				+ ", note=" + note + ", thePublic=" + thePublic + ", theMenu="
				+ theMenu + ", theShow=" + theShow + ", sort=" + sort
				+ ", fid=" + fid + ", target=" + target + "]";
	}

}
