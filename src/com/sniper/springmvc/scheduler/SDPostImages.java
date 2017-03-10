package com.sniper.springmvc.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SDPostImages {

	private String subject;
	private int date;
	private int authorid;
	private int sid;
	private int postTime;
	private String dateTime;
	private List<Map<String, String>> attachment = new ArrayList<>();

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public int getAuthorid() {
		return authorid;
	}

	public void setAuthorid(int authorid) {
		this.authorid = authorid;
	}

	public int getSid() {
		return sid;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public int getPostTime() {
		return postTime;
	}

	public void setPostTime(int postTime) {
		this.postTime = postTime;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public List<Map<String, String>> getAttachment() {
		return attachment;
	}

	public void setAttachment(List<Map<String, String>> attachment) {
		this.attachment = attachment;
	}

}
