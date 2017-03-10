package com.sniper.springmvc.listener;

import org.quartz.SchedulerException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;

import com.sniper.springmvc.shiro.scheduler.QuartzManagerUtil;

/**
 * 挡系统关闭的时候做一些事
 * 
 * @author suzhen
 * 
 */
public class InitStopOnBoot implements ApplicationListener<ContextStoppedEvent> {

	@Override
	public void onApplicationEvent(ContextStoppedEvent event) {
		try {
			QuartzManagerUtil.stop();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

}
