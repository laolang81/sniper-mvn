package com.sniper.springmvc.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 开机启动一些计划任务
 * 
 * @author suzhen
 * 
 */
@Component
public class InitStartOnBoot implements
		ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

	}

}
