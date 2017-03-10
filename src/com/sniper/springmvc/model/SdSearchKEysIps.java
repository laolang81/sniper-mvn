package com.sniper.springmvc.model;

import java.util.Date;

public class SdSearchKEysIps extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private int kid;
	private String ip;
	private int totalToday;
	private int totalAll;
	private Date lastDatetime;
	private String ipCounty;
	private String ipProvince;
	private String ipCity;
	private String ipType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getKid() {
		return kid;
	}

	public void setKid(int kid) {
		this.kid = kid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getTotalToday() {
		return totalToday;
	}

	public void setTotalToday(int totalToday) {
		this.totalToday = totalToday;
	}

	public int getTotalAll() {
		return totalAll;
	}

	public void setTotalAll(int totalAll) {
		this.totalAll = totalAll;
	}

	public Date getLastDatetime() {
		return lastDatetime;
	}

	public void setLastDatetime(Date lastDatetime) {
		this.lastDatetime = lastDatetime;
	}

	public String getIpCounty() {
		return ipCounty;
	}

	public void setIpCounty(String ipCounty) {
		this.ipCounty = ipCounty;
	}

	public String getIpProvince() {
		return ipProvince;
	}

	public void setIpProvince(String ipProvince) {
		this.ipProvince = ipProvince;
	}

	public String getIpCity() {
		return ipCity;
	}

	public void setIpCity(String ipCity) {
		this.ipCity = ipCity;
	}

	public String getIpType() {
		return ipType;
	}

	public void setIpType(String ipType) {
		this.ipType = ipType;
	}

}
