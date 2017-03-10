package com.sniper.springmvc.junit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerTest {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Test
	public void testOne() {
		logger.warn("0---");
	}
}
