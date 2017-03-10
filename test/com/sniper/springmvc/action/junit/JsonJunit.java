package com.sniper.springmvc.action.junit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonJunit {

	@Test
	public void name() throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<String> a = new ArrayList<>();
		a.add("1");
		a.add("2");
		String c = "";
		String[] d = c.split(",");
		String b = objectMapper.writeValueAsString(d);
		System.out.println(b);
	}
}
