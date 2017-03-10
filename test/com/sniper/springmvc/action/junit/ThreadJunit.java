package com.sniper.springmvc.action.junit;

import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sniper.springmvc.thread.ThreadMxBeanUtils;

public class ThreadJunit {

	@Test
	public void test() {
		ThreadMXBean bean = ThreadMxBeanUtils.getThreadMXBean();
		long[] ids = bean.getAllThreadIds();
		System.out.println("所有进程");
		System.out.println(bean.getAllThreadIds().length);
		System.out.println("守护进程");
		System.out.println(bean.getDaemonThreadCount());
		System.out.println("启动以来高峰值");
		System.out.println(bean.getPeakThreadCount());
		System.out.println("活动进程");
		System.out.println(bean.getThreadCount());
		int count = bean.getThreadCount();
		for (long l : ids) {
			ThreadInfo info = bean.getThreadInfo(l);
			System.out.println(info.getThreadName());
			System.out.println(bean.getThreadCpuTime(l));
			System.out.println(info.getThreadState().name());
			System.out.println(info.getLockName());
			//System.out.println(info.getLockInfo().getClassName());

		}
	}

	public void test2() {
		List<Map<String, String>> maps = com.sniper.springmvc.utils.ThreadUtil
				.listThread();
		for (Map<String, String> map : maps) {
			System.out.println(map.get("name"));
		}
	}
}
