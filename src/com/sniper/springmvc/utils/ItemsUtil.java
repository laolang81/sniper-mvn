package com.sniper.springmvc.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sniper.springmvc.model.SdItems;

public class ItemsUtil {

	/**
	 * 根据栏目的id进项倒叙排序
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, SdItems> sortItemidDesc(Map<String, SdItems> map) {
		List<Entry<String, SdItems>> listMaps = new ArrayList<>(map.entrySet());
		Collections.sort(listMaps, new ItemidDesc());
		Map<String, SdItems> map2 = new LinkedHashMap<>();
		for (Entry<String, SdItems> entry : listMaps) {
			map2.put(entry.getKey(), entry.getValue());
		}

		return map2;
	}

	/**
	 * 根据栏目的id进项倒叙排序
	 * 
	 * @param map
	 * @return
	 */
	public static Map<String, SdItems> sortItemidAsc(Map<String, SdItems> map) {
		List<Entry<String, SdItems>> listMaps = new ArrayList<>(map.entrySet());
		Collections.sort(listMaps, new ItemidAsc());
		Map<String, SdItems> map2 = new LinkedHashMap<>();
		for (Entry<String, SdItems> entry : listMaps) {
			map2.put(entry.getKey(), entry.getValue());
		}
		return map2;
	}

	/**
	 * 根据itemid倒叙排列
	 * 
	 * @author suzhen
	 * 
	 */
	public static class ItemidDesc implements
			Comparator<Entry<String, SdItems>> {

		@Override
		public int compare(Entry<String, SdItems> o1, Entry<String, SdItems> o2) {
			if (o1.getValue().getItemid().intValue() < o2.getValue()
					.getItemid().intValue()) {
				return 1;
			} else if (o1.getValue().getItemid().intValue() > o2.getValue()
					.getItemid().intValue()) {
				return -1;
			}
			return 0;
		}

	}

	/**
	 * 根据itemid倒叙排列
	 * 
	 * @author suzhen
	 * 
	 */
	public static class ItemidAsc implements Comparator<Entry<String, SdItems>> {

		@Override
		public int compare(Entry<String, SdItems> o1, Entry<String, SdItems> o2) {
			if (o1.getValue().getItemid().intValue() < o2.getValue()
					.getItemid().intValue()) {
				return -1;
			} else if (o1.getValue().getItemid().intValue() > o2.getValue()
					.getItemid().intValue()) {
				return 1;
			}
			return 0;
		}

	}

	public static class ItemupComparator implements Comparator<SdItems> {

		@Override
		public int compare(SdItems o1, SdItems o2) {
			if (o1.getItemup() > o2.getItemup()) {
				return 1;
			} else if (o1.getItemup() < o2.getItemup()) {
				return -1;
			}
			return 0;
		}

	}

	/**
	 * 对栏目进行排序,并添加上下级名字链接
	 * 
	 * @param items
	 * @return
	 */
	public static Map<String, SdItems> sortNamed(List<SdItems> items) {
		Collections.sort(items, new ItemupComparator());
		Map<String, SdItems> map = new LinkedHashMap<>();
		for (SdItems sdItems : items) {
			map.put(sdItems.getItemid().toString(), sdItems);
		}

		Map<String, SdItems> treeMap = new LinkedHashMap<>();
		for (Entry<String, SdItems> entry : map.entrySet()) {
			// 检查父级是否存在，如果不存在就取消当前数据操作
			if (entry.getValue().getItemup() > 0) {
				if (map.containsKey(entry.getValue().getItemup() + "") == false) {
					continue;
				}
			}
			String name = entry.getValue().getName();
			if (map.containsKey(String.valueOf(entry.getValue().getItemup()))) {
				name = map.get(String.valueOf(entry.getValue().getItemup()))
						.getName() + ":" + name;
			}
			entry.getValue().setName(name);
			treeMap.put(entry.getKey(), entry.getValue());
		}

		// 自然排序，如果不自然排序的话，相同的栏目就无法靠在一起，但是
		// Map<String, SdItems> map2 = new TreeMap<>(treeMap);
		return treeMap;
	}
}
