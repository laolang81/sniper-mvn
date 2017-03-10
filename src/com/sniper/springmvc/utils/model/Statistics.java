package com.sniper.springmvc.utils.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Statistics {
	// 存放输出的处室
	Map<String, String> searchDeps = new HashMap<>();
	// 存放处室统计
	Map<String, Map<String, Object>> siteidCount = new LinkedHashMap<>();
	/**
	 * 存放栏目统计
	 */
	Map<String, Map<String, Object>> itemidCount = new LinkedHashMap<>();
	Map<String, Map<String, Map<String, Object>>> siteidItemidCount = new LinkedHashMap<>();
	// 存放栏目的名称
	Map<String, Map<String, String>> itemsMap = new HashMap<>();

	public Map<String, String> getSearchDeps() {
		return searchDeps;
	}

	public void setSearchDeps(Map<String, String> searchDeps) {
		this.searchDeps = searchDeps;
	}

	public Map<String, Map<String, Object>> getSiteidCount() {
		return siteidCount;
	}

	public void setSiteidCount(Map<String, Map<String, Object>> siteidCount) {
		this.siteidCount = siteidCount;
	}

	public Map<String, Map<String, Object>> getItemidCount() {
		return itemidCount;
	}

	public void setItemidCount(Map<String, Map<String, Object>> itemidCount) {
		this.itemidCount = itemidCount;
	}

	public Map<String, Map<String, String>> getItemsMap() {
		return itemsMap;
	}

	public void setItemsMap(Map<String, Map<String, String>> itemsMap) {
		this.itemsMap = itemsMap;
	}

	public Map<String, Map<String, Map<String, Object>>> getSiteidItemidCount() {
		return siteidItemidCount;
	}

	public void setSiteidItemidCount(
			Map<String, Map<String, Map<String, Object>>> siteidItemidCount) {
		this.siteidItemidCount = siteidItemidCount;
	}

}
