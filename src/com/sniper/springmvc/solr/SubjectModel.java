package com.sniper.springmvc.solr;

import java.io.Serializable;
import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 商务网数据插入模型
 * 
 * @author suzhen
 * 
 */
public class SubjectModel implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String CORE_NAME = "subject";
	// id
	@Field("id")
	private String id;
	// 标题
	@Field("subject")
	private String subject;
	// 来源
	@Field("source_s")
	private String source;
	// 日期
	// 因为*.dt是动态字段
	@Field("date_dt")
	private Date time;
	// 处室id
	@Field("depId_i")
	private int depId;
	// 处室名称
	@Field("depName_s")
	private String depName;
	// 栏目id
	@Field("itemid_i")
	private int itemid;
	// 栏目名称
	@Field("itemidName_s")
	private String itemidName;
	// 新闻地址
	@Field("url")
	private String links;
	// 内容
	@Field("postValue")
	private String content;
	// 是否可用
	@Field("enabled_b")
	private boolean enabled;
	// 附件列表
	@Field("attachment")
	public String attachment;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public int getDepId() {
		return depId;
	}

	public void setDepId(int depId) {
		this.depId = depId;
	}

	public String getDepName() {
		return depName;
	}

	public void setDepName(String depName) {
		this.depName = depName;
	}

	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	public String getItemidName() {
		return itemidName;
	}

	public void setItemidName(String itemidName) {
		this.itemidName = itemidName;
	}

	public String getLinks() {
		return links;
	}

	public void setLinks(String links) {
		this.links = links;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	@Override
	public String toString() {
		return "SubjectModel [id=" + id + ", subject=" + subject + ", source="
				+ source + ", time=" + time + ", depId=" + depId + ", depName="
				+ depName + ", itemid=" + itemid + ", itemidName=" + itemidName
				+ ", links=" + links + ", content=" + content + ", enabled="
				+ enabled + "]";
	}

}
