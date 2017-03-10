package com.sniper.springmvc.junit;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonAndItemsJunit {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	public void name() {

		String a = "[\"35\",\"12\",\"22\",\"23\",\"21\"]";

		ObjectMapper mapper = new ObjectMapper();
		try {
			@SuppressWarnings("unchecked")
			List<String> b = mapper.readValue(a, List.class);
			System.out.println(b);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void url() {
		String url = "http://shandongbusiness.gov.cn/ssasdas";
		url = url.substring(url.indexOf("//") + 2);
		System.out.println(url);
		url = url.substring(url.indexOf("/"));
		System.out.println(url);
		logger.debug("------");
	}
}
