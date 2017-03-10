package com.sniper.springmvc.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.msgpack.annotation.Message;

/**
 * 通过封装把参数传递给试图
 * https://my.oschina.net/u/1431106/blog/202652
 * @author sniper
 * 
 */
@Message
public class ParamsToHtml implements Serializable {

	private static final long serialVersionUID = 1L;
	// 显示删除按钮
	private int htmlNum = 0;
	private boolean delBotton = true;
	private String key;
	private Map<String, String> keys = new LinkedHashMap<>();
	private Map<String, Map<String, String>> params = new LinkedHashMap<>();
	private List<ParamsToHtmlButton> buttons = new ArrayList<>();

	public int getHtmlNum() {
		return htmlNum;
	}

	public boolean isDelBotton() {
		return delBotton;
	}

	public void setDelBotton(boolean delBotton) {
		this.delBotton = delBotton;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public Map<String, String> getKeys() {
		return keys;
	}

	public void setKeys(Map<String, String> keys) {
		this.keys = keys;
	}

	public String getKeyValue(String key) {
		if (keys.containsKey(key)) {
			return keys.get(key);
		}
		return key;
	}

	public Map<String, Map<String, String>> getParams() {
		return params;
	}

	public void setParams(Map<String, Map<String, String>> params) {
		htmlNum += params.size();
		this.params = params;
	}

	public Map<String, String> getMapValue(String key) {
		return params.get(key);
	}

	public String getMapValueString(String key, String nkey) {
		return params.get(key).get(nkey);
	}

	/**
	 * 为按钮添加赋值
	 * 
	 * @param key
	 * @param value
	 */
	public void addMapValue(String key, Map<String, String> value) {
		htmlNum++;
		params.put(key, value);
	}

	public List<ParamsToHtmlButton> getButtons() {
		return buttons;
	}

	/**
	 * 添加按钮
	 * 
	 * @param buttons
	 */
	public void setButtons(List<ParamsToHtmlButton> buttons) {
		htmlNum++;
		this.buttons = buttons;
	}

	@Override
	public String toString() {
		return "ParamsToHtml [keys=" + keys + ", params=" + params + "]";
	}

}
