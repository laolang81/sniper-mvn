package com.sniper.springmvc.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 标签可以绑定其他标的id，作为关联使用,属于多变字段
 * 
 * @author suzhen
 * 
 */
public class Files extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	private String oldName;
	private String newPath;
	private String sourcePath;
	private String hash;
	// 来源
	private String postUrl;
	private Date stime = new Date();
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date eventTimeDate;
	private int download;
	private long size;
	private int width;
	private int height;
	private String fileType;
	private String tags;
	private Boolean isMain;
	private Boolean isThumb;
	private String thumbPrefix;
	private int sort = Long.valueOf(System.currentTimeMillis() / 1000)
			.intValue();
	// 是否属于多媒体
	private Boolean album;
	private String suffix;

	private AdminUser adminUser;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getNewPath() {
		return newPath;
	}

	public void setNewPath(String newPath) {
		this.newPath = newPath;
	}

	public String getSourcePath() {
		return sourcePath;
	}

	public void setSourcePath(String sourcePath) {
		this.sourcePath = sourcePath;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getPostUrl() {
		return postUrl;
	}

	public void setPostUrl(String postUrl) {
		this.postUrl = postUrl;
	}

	public Date getStime() {
		return stime;
	}

	public void setStime(Date stime) {
		this.stime = stime;
	}

	public Date getEventTimeDate() {
		return eventTimeDate;
	}

	public void setEventTimeDate(Date eventTimeDate) {
		this.eventTimeDate = eventTimeDate;
	}

	public int getDownload() {
		return download;
	}

	public void setDownload(int download) {
		this.download = download;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Boolean getIsMain() {
		return isMain;
	}

	public void setIsMain(Boolean isMain) {
		this.isMain = isMain;
	}

	public Boolean getIsThumb() {
		return isThumb;
	}

	public void setIsThumb(Boolean isThumb) {
		this.isThumb = isThumb;
	}

	public String getThumbPrefix() {
		return thumbPrefix;
	}

	public void setThumbPrefix(String thumbPrefix) {
		this.thumbPrefix = thumbPrefix;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

	public Boolean getAlbum() {
		return album;
	}

	public void setAlbum(Boolean album) {
		this.album = album;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

}
