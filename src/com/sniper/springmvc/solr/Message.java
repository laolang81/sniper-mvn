package com.sniper.springmvc.solr;

import java.io.Serializable;

import org.apache.solr.client.solrj.beans.Field;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;

	@Field("id")
	private String id;
	@Field("postValue")
	private String title;
	@Field("title")
	private String[] content;

	public Message() {
		super();
	}

	public Message(String id, String title, String[] content) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getContent() {
		return content;
	}

	public void setContent(String[] content) {
		this.content = content;
	}

}
