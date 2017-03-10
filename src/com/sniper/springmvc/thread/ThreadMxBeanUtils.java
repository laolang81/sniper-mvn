package com.sniper.springmvc.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 * 线程管理 http://www.apihome.cn/api/java/ThreadMXBean.html
 * 
 * @author suzhen
 * 
 */
public class ThreadMxBeanUtils {
	private static final ThreadMXBean threadMXBean = ManagementFactory
			.getThreadMXBean();

	public static ThreadMXBean getThreadMXBean() {
		return threadMXBean;
	}

	/**
	 * 获取当前线程cpu使用时间
	 * 
	 * @return
	 */
	public static long getCurrentThreadCpuTime() {
		return threadMXBean.getCurrentThreadCpuTime();
	}

	/**
	 * 获取制定线程cpu使用时间
	 * 
	 * @param id
	 * @return
	 */
	public static long getThreadCpuTime(long id) {
		return threadMXBean.getThreadCpuTime(id);
	}

	/**
	 * 获取相关线程信息
	 * 
	 * @param id
	 * @return
	 */
	public static ThreadInfo getThreadInfo(long id) {

		ThreadInfo info = threadMXBean.getThreadInfo(id);
		info.getThreadName();
		info.getThreadState();
		info.getBlockedCount();
		info.getLockInfo().getClassName();

		return info;
	}

	/**
	 * 获取所有线程信息
	 * 
	 * @return
	 */
	public static List<ThreadModel> listThreads() {
		List<ThreadModel> models = new ArrayList<>();
		long[] ids = threadMXBean.getAllThreadIds();
		for (long l : ids) {
			ThreadInfo info = threadMXBean.getThreadInfo(l);
			ThreadModel model = new ThreadModel();
			model.setId(l);
			model.setBlockedCount(info.getBlockedCount());
			model.setBlockedTime(info.getBlockedTime());
			model.setCpuTime(threadMXBean.getThreadCpuTime(l));
			model.setUserTime(threadMXBean.getThreadUserTime(l));
			model.setWaitedCount(info.getWaitedCount());
			model.setWaitedTime(info.getWaitedTime());
			model.setName(info.getThreadName());
			model.setState(info.getThreadState());
			if (info.getLockInfo() != null) {
				model.setLockInfo(info.getLockInfo());
			}
			models.add(model);
		}
		return models;
	}

}
