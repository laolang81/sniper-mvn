package com.sniper.springmvc.searchUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class PostSearch extends BaseSearch {
	// 默认false
	private Boolean solr = false;
	private String delid;
	private String menuType;
	private String menuValue;
	private String group;
	private Integer type;
	// 栏目发布新闻开始
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date itemDateStart;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date postDateStart;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date postDateEnd;
	private Integer siteid;
	private Integer itemid;
	private Integer siteidMove;
	private Integer itemidMove;
	private Integer bhot;
	private Integer icid;
	// 推荐到商务部
	private Integer mofcom;
	// 要问管理
	private Integer preid;
	// 推荐新闻
	private Integer suggested;
	// 是否是图片
	private Integer isimage;
	// 是否是图片新闻
	private Integer isprimeimage;
	// 是否是主站新闻
	private Integer mainsite;
	private List<Integer> lookthroughed = new ArrayList<>();
	private Integer imageType = 1;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public Date getPostDateStart() {
		return postDateStart;
	}

	public void setPostDateStart(Date postDateStart) {
		this.postDateStart = postDateStart;
	}

	public Date getPostDateEnd() {
		return postDateEnd;
	}

	public void setPostDateEnd(Date postDateEnd) {
		this.postDateEnd = postDateEnd;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSiteid() {
		return siteid;
	}

	public void setSiteid(Integer siteid) {
		this.siteid = siteid;
	}

	public Integer getItemid() {
		return itemid;
	}

	public void setItemid(Integer itemid) {
		this.itemid = itemid;
	}

	public Integer getSiteidMove() {
		return siteidMove;
	}

	public void setSiteidMove(Integer siteidMove) {
		this.siteidMove = siteidMove;
	}

	public Integer getItemidMove() {
		return itemidMove;
	}

	public void setItemidMove(Integer itemidMove) {
		this.itemidMove = itemidMove;
	}

	public Integer getMofcom() {
		return mofcom;
	}

	public void setMofcom(Integer mofcom) {
		this.mofcom = mofcom;
	}

	public Integer getPreid() {
		return preid;
	}

	public void setPreid(Integer preid) {
		this.preid = preid;
	}

	public Integer getSuggested() {
		return suggested;
	}

	public void setSuggested(Integer suggested) {
		this.suggested = suggested;
	}

	public Integer getIsimage() {
		return isimage;
	}

	public void setIsimage(Integer isimage) {
		this.isimage = isimage;
	}

	public Integer getIsprimeimage() {
		return isprimeimage;
	}

	public void setIsprimeimage(Integer isprimeimage) {
		this.isprimeimage = isprimeimage;
	}

	public Integer getMainsite() {
		return mainsite;
	}

	public void setMainsite(Integer mainsite) {
		this.mainsite = mainsite;
	}

	public List<Integer> getLookthroughed() {
		return lookthroughed;
	}

	public void setLookthroughed(List<Integer> lookthroughed) {
		this.lookthroughed = lookthroughed;
	}

	public Integer getBhot() {
		return bhot;
	}

	public void setBhot(Integer bhot) {
		this.bhot = bhot;
	}

	public String getDelid() {
		return delid;
	}

	public void setDelid(String delid) {
		this.delid = delid;
	}

	public String getMenuType() {
		return menuType;
	}

	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}

	public String getMenuValue() {
		return menuValue;
	}

	public void setMenuValue(String menuValue) {
		this.menuValue = menuValue;
	}

	public Integer getImageType() {
		return imageType;
	}

	public void setImageType(Integer imageType) {
		this.imageType = imageType;
	}

	public Integer getIcid() {
		return icid;
	}

	public void setIcid(Integer icid) {
		this.icid = icid;
	}

	public Date getItemDateStart() {
		return itemDateStart;
	}

	public void setItemDateStart(Date itemDateStart) {
		this.itemDateStart = itemDateStart;
	}

	public Boolean getSolr() {
		return solr;
	}

	public void setSolr(Boolean solr) {
		this.solr = solr;
	}

	@Override
	public String toString() {
		return "PostSearch [delid=" + delid + ", menuType=" + menuType
				+ ", menuValue=" + menuValue + ", group=" + group + ", type="
				+ type + ", postDateStart=" + postDateStart + ", postDateEnd="
				+ postDateEnd + ", siteid=" + siteid + ", itemid=" + itemid
				+ ", siteidMove=" + siteidMove + ", itemidMove=" + itemidMove
				+ ", bhot=" + bhot + ", mofcom=" + mofcom + ", preid=" + preid
				+ ", suggested=" + suggested + ", isimage=" + isimage
				+ ", isprimeimage=" + isprimeimage + ", mainsite=" + mainsite
				+ ", lookthroughed=" + lookthroughed + ", imageType="
				+ imageType + "]";
	}

}
