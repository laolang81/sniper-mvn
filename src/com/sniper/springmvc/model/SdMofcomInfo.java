package com.sniper.springmvc.model;

/**
 * 商务部推荐新闻
 * 
 * @author suzhen
 * 
 */
public class SdMofcomInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private int mid;
	private int sid;
	private Integer itemid;
	private int type = 0;
	private int subtitle = 1;
	private int source = 1;
	private int puttop = 0;
	private int date;
	private Integer lastdate;
	private int del;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(int subtitle) {
		this.subtitle = subtitle;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getPuttop() {
		return puttop;
	}

	public void setPuttop(int puttop) {
		this.puttop = puttop;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public Integer getLastdate() {
		return lastdate;
	}

	public void setLastdate(Integer lastdate) {
		this.lastdate = lastdate;
	}

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

}
