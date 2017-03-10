package com.sniper.springmvc.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CreateLogTableTaskAnn {


	@Scheduled(cron = "0 0 0 15 * ?")
	protected void init() {
		
	}

}
