package com.sniper.springmvc.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 申请公开
 * 
 * @author suzhen
 * 
 */
public class SdOpenApply extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String username;
	private String workunit;
	private String idname;
	private String idnum;
	private String address;
	private String tel;
	private String email;
	private String frname;
	private String frcode;
	private String frzhizhao;
	private String frdaibiao;
	private String frtel;
	private String fremail;
	private String frmaster;
	private String frqianming;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date frtime;
	private String content;
	private String xtnum;
	private String xtuse;
	private String xtmoney;
	private String xttype;
	private String xtinfo;
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:ss:mm")
	private Date stime = new Date();
	private String ip;
	private String xtothertype;
	private String postcode;
	private Integer enabled;
	private Integer office;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getWorkunit() {
		return workunit;
	}

	public void setWorkunit(String workunit) {
		this.workunit = workunit;
	}

	public String getIdname() {
		return idname;
	}

	public void setIdname(String idname) {
		this.idname = idname;
	}

	public String getIdnum() {
		return idnum;
	}

	public void setIdnum(String idnum) {
		this.idnum = idnum;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFrname() {
		return frname;
	}

	public void setFrname(String frname) {
		this.frname = frname;
	}

	public String getFrcode() {
		return frcode;
	}

	public void setFrcode(String frcode) {
		this.frcode = frcode;
	}

	public String getFrzhizhao() {
		return frzhizhao;
	}

	public void setFrzhizhao(String frzhizhao) {
		this.frzhizhao = frzhizhao;
	}

	public String getFrdaibiao() {
		return frdaibiao;
	}

	public void setFrdaibiao(String frdaibiao) {
		this.frdaibiao = frdaibiao;
	}

	public String getFrtel() {
		return frtel;
	}

	public void setFrtel(String frtel) {
		this.frtel = frtel;
	}

	public String getFremail() {
		return fremail;
	}

	public void setFremail(String fremail) {
		this.fremail = fremail;
	}

	public String getFrmaster() {
		return frmaster;
	}

	public void setFrmaster(String frmaster) {
		this.frmaster = frmaster;
	}

	public String getFrqianming() {
		return frqianming;
	}

	public void setFrqianming(String frqianming) {
		this.frqianming = frqianming;
	}

	public Date getFrtime() {
		return frtime;
	}

	public void setFrtime(Date frtime) {
		this.frtime = frtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getXtnum() {
		return xtnum;
	}

	public void setXtnum(String xtnum) {
		this.xtnum = xtnum;
	}

	public String getXtuse() {
		return xtuse;
	}

	public void setXtuse(String xtuse) {
		this.xtuse = xtuse;
	}

	public String getXtmoney() {
		return xtmoney;
	}

	public void setXtmoney(String xtmoney) {
		this.xtmoney = xtmoney;
	}

	public String getXttype() {
		return xttype;
	}

	public void setXttype(String xttype) {
		this.xttype = xttype;
	}

	public String getXtinfo() {
		return xtinfo;
	}

	public void setXtinfo(String xtinfo) {
		this.xtinfo = xtinfo;
	}

	public Date getStime() {
		return stime;
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getXtothertype() {
		return xtothertype;
	}

	public void setXtothertype(String xtothertype) {
		this.xtothertype = xtothertype;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Integer getOffice() {
		return office;
	}

	public void setOffice(Integer office) {
		this.office = office;
	}

}
