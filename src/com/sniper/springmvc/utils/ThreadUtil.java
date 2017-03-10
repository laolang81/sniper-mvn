package com.sniper.springmvc.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sniper.springmvc.freemarker.ViewHomeUtils;

/**
 * 线程获取
 * 
 * @author suzhen
 * 
 */
public class ThreadUtil {

	/**
	 * 获取所有线程
	 * 
	 * @return
	 */
	public static List<Map<String, String>> listThread() {
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		ThreadGroup topGroup = group;
		// 遍历线程组树，获取根线程组
		while (group != null) {
			topGroup = group;
			group = group.getParent();
		}
		// 激活的线程数加倍
		int estimatedSize = topGroup.activeCount() * 2;
		// 这个表示获取线程
		Thread[] slackList = new Thread[estimatedSize];
		// 获取根线程组的所有线程
		int actualSize = topGroup.enumerate(slackList);
		// copy into a list that is the exact size
		Thread[] list = new Thread[actualSize];
		System.arraycopy(slackList, 0, list, 0, actualSize);
		List<Map<String, String>> lists = new ArrayList<>();

		for (Thread thread : list) {
			Map<String, String> m = new HashMap<>();
			m.put("id", String.valueOf(thread.getId()));
			m.put("name", ViewHomeUtils.substr(thread.getName(), 50));
			m.put("longName", thread.getName());
			m.put("priority", String.valueOf(thread.getPriority()));
			m.put("alive", new Boolean(thread.isAlive()).toString());
			m.put("threadGroupName", thread.getThreadGroup().getName());
			m.put("daemon", new Boolean(thread.isDaemon()).toString());
			m.put("interrupted", new Boolean(thread.isInterrupted()).toString());
			lists.add(m);
		}
		return lists;
	}

	/**
	 * 获取一个线程信息
	 * 
	 * @param id
	 * @return
	 */
	public static Thread getThread(Long id) {
		ThreadGroup group = Thread.currentThread().getThreadGroup();
		ThreadGroup topGroup = group;
		// 遍历线程组树，获取根线程组
		while (group != null) {
			topGroup = group;
			group = group.getParent();
		}
		// 激活的线程数加倍
		int estimatedSize = topGroup.activeCount() * 2;
		// 这个表示获取线程
		Thread[] slackList = new Thread[estimatedSize];
		// 获取根线程组的所有线程
		int actualSize = topGroup.enumerate(slackList);
		// copy into a list that is the exact size
		Thread[] list = new Thread[actualSize];
		System.arraycopy(slackList, 0, list, 0, actualSize);

		for (Thread thread : list) {
			if (id == thread.getId()) {
				return thread;
			}
		}
		return null;
	}

	/**
	 * 终止线程
	 * 
	 * @param id
	 */
	public void stopThread(long id) {
		Thread thread = ThreadUtil.getThread(id);
		while (!thread.isInterrupted()) {
			thread.interrupt();
		}
	}

	public static void main(String[] args) {
		System.out.println("--");
	}
}
