package com.sniper.springmvc.model;

public class SystemConfig extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	private String keyName;
	private String keyValue;
	private String keyInfo;
	private boolean autoload = true;
	private String input;
	private String placeholder;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getKeyInfo() {
		return keyInfo;
	}

	public void setKeyInfo(String keyInfo) {
		this.keyInfo = keyInfo;
	}

	public boolean getAutoload() {
		return autoload;
	}

	public void setAutoload(boolean autoload) {
		this.autoload = autoload;
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}

	public SystemConfig() {
		super();
	}

	public SystemConfig(String keyName, String keyValue) {
		super();
		this.keyName = keyName;
		this.keyValue = keyValue;
	}

}
