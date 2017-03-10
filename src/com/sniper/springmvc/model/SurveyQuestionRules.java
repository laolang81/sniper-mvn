package com.sniper.springmvc.model;


public class SurveyQuestionRules extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	private boolean required = false;
	private boolean email = false;
	private boolean url = false;
	private boolean number = false;
	private boolean size = false;
	private int max = 0;
	private int min = 0;
	private boolean length = false;
	private int minLength = 0;
	private int maxLength = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean require) {
		this.required = require;
	}

	public boolean isEmail() {
		return email;
	}

	public void setEmail(boolean email) {
		this.email = email;
	}

	public boolean isUrl() {
		return url;
	}

	public void setUrl(boolean url) {
		this.url = url;
	}

	public boolean isNumber() {
		return number;
	}

	public void setNumber(boolean number) {
		this.number = number;
	}

	public boolean isSize() {
		return size;
	}

	public void setSize(boolean size) {
		this.size = size;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public boolean isLength() {
		return length;
	}

	public void setLength(boolean length) {
		this.length = length;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

}
