package com.sniper.springmvc.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.msgpack.annotation.Message;

/**
 * 用于保存数据一遍Redis缓存处理
 * 
 * @author suzhen
 * 
 */
@Message
public class RedisDepItemData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Map<String, Map<String, String>> depItems = new HashMap<>();

	public Map<String, Map<String, String>> getDepItems() {
		return depItems;
	}

	public void setDepItems(Map<String, Map<String, String>> depItems) {
		this.depItems = depItems;
	}

}
