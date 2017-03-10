package com.sniper.springmvc.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.msgpack.annotation.Message;

/**
 * 用户组新版旧版合并
 * 
 * @author suzhen
 * 
 */
@Message
public class AdminGroup extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String value;
	private String note;
	private Date ctime = new Date();
	// 是否拥有审核权限
	private Integer audit;
	// 审核结果
	private Integer lookthrough;
	// 初始新闻状态
	private Integer startlookthrough;
	// 是否可以移动新闻
	private Integer move;
	// 可以审核的新闻状态
	private Integer readLookthrough;

	private List<AdminRight> adminRight = new ArrayList<>();

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public Integer getAudit() {
		return audit;
	}

	public void setAudit(Integer audit) {
		this.audit = audit;
	}

	public Integer getLookthrough() {
		return lookthrough;
	}

	public void setLookthrough(Integer lookthrough) {
		this.lookthrough = lookthrough;
	}

	public Integer getStartlookthrough() {
		return startlookthrough;
	}

	public void setStartlookthrough(Integer startlookthrough) {
		this.startlookthrough = startlookthrough;
	}

	public Integer getMove() {
		return move;
	}

	public void setMove(Integer move) {
		this.move = move;
	}

	public Integer getReadLookthrough() {
		return readLookthrough;
	}

	public void setReadLookthrough(Integer readLookthrough) {
		this.readLookthrough = readLookthrough;
	}

	public List<AdminRight> getAdminRight() {
		return adminRight;
	}

	public void setAdminRight(List<AdminRight> adminRight) {
		this.adminRight = adminRight;
	}

	@Override
	public String toString() {
		return "AdminGroup [id=" + id + ", name=" + name + ", value=" + value
				+ ", note=" + note + "]";
	}

}
