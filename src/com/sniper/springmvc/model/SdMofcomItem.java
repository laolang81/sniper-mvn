package com.sniper.springmvc.model;

public class SdMofcomItem extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String mname;
	private int mid;
	private int fatherid;
	private int sdid;
	private String sdname;
	private String siteid;
	private String itemids;
	private String beizhu;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getMname() {
		return mname;
	}

	public int getMid() {
		return mid;
	}

	public void setMid(int mid) {
		this.mid = mid;
	}

	public int getFatherid() {
		return fatherid;
	}

	public void setFatherid(int fatherid) {
		this.fatherid = fatherid;
	}

	public int getSdid() {
		return sdid;
	}

	public void setSdid(int sdid) {
		this.sdid = sdid;
	}

	public String getSdname() {
		return sdname;
	}

	public void setSdname(String sdname) {
		this.sdname = sdname;
	}

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public String getItemids() {
		return itemids;
	}

	public void setItemids(String itemids) {
		this.itemids = itemids;
	}

	public String getBeizhu() {
		return beizhu;
	}

	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

}
