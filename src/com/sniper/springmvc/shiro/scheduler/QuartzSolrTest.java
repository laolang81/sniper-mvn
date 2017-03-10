package com.sniper.springmvc.shiro.scheduler;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;

public class QuartzSolrTest implements Job {

	public static String jobName = "Solr初始化";

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("------");

	}

	public static void main(String[] args) throws SchedulerException {
		QuartzManagerUtil.start();
		// 添加重复任务，设置属性 1秒间隔，执行一次，立即开始
		QuartzManagerUtil.addSimpleJob(jobName, QuartzSolrTest.class, new Date());
	}

}
