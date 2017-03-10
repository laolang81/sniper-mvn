package com.sniper.springmvc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TopicUtil {

	/**
	 * 吧json解析成map结构
	 * 
	 * @param value
	 * @return
	 */
	public static Map<String, List<Map<String, Integer>>> valueToMap(
			String value) {

		Map<String, List<Map<String, Integer>>> map = new HashMap<>();

		if (ValidateUtil.isValid(value)) {
			String[] types = value.trim().split("\n");
			if (types != null) {
				for (String type : types) {
					String[] ts = type.split("=");
					if (ts != null && ts.length == 2) {
						List<Map<String, Integer>> vsList = new ArrayList<>();
						String key = ts[0];
						String[] vs = ts[1].split(";");
						for (String v : vs) {
							String[] vvs = v.split(":");
							Map<String, Integer> vvsMap = new HashMap<>();
							vvsMap.put(vvs[0], Integer.valueOf(vvs[1].trim())
									.intValue());
							vsList.add(vvsMap);
						}
						map.put(key, vsList);
					}

				}
			}
		}

		return map;
	}

	/**
	 * 
	 * @param value
	 * @return
	 */
	public static List<Map<String, String>> uncodeJson(String value) {
		
		return null;
	}

	public static void main(String[] args) throws JsonProcessingException {

		List<Map<String, Object>> list = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("a", "a");
		map.put("b", 1);
		map.put("c", "c");
		list.add(map);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("d", "d");
		map2.put("e", 2);
		map2.put("f", "f");
		list.add(map2);

		ObjectMapper mapper = new ObjectMapper();
		String a = mapper.writeValueAsString(list);
		// a = [{"b":1,"c":"c","a":"a"},{"f":"f","d":"d","e":2}]
		System.out.println(a);

	}
}
