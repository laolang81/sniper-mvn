package com.sniper.springmvc.java;

import java.util.HashMap;
import java.util.Map;

public class OsKeys {
	private static final Map<String, String> keys = new HashMap<>();
	static {
		keys.put("heapMemoryUsageInit", "堆内存初始化");
		keys.put("heapMemoryUsageUsed", "堆内存已使用");
		keys.put("heapMemoryUsageMax", "堆内存最大");

		keys.put("javaMemoryFree", "虚拟机空余内存");
		keys.put("javaMemoryMax", "虚拟机最大内存");
		keys.put("javaMemoryTotal", "虚拟机总内存");

		keys.put("osName", "系统名称");
		keys.put("osVersion", "系统版本");
		keys.put("osArch", "系统架构");
		keys.put("osCpuNum", "CPU个数");
		keys.put("osDiskTotal", "系统空间");
		keys.put("osDiskFree", "系统可用空间");
		keys.put("osDiskUsed", "系统已使用空间");
		keys.put("threadCount", "线程数量");
		keys.put("peakThreadCount", "线程高峰数量");
		keys.put("currentTheardCpuTime", "当前线程CPU时间");
		keys.put("currentTheardUserTime", "当前线程用户时间");
		keys.put("daemonThreadCount", "守护线程数量");
		keys.put("sysName", "系统名称");
		keys.put("sysVersion", "系统版本");
		keys.put("sysArch", "系统架构");
		keys.put("sysDescription", "系统描述");
		keys.put("cpuNum", "CPU数量");
		keys.put("ip", "IP");
		keys.put("username", "用户名");
		keys.put("pcName", "电脑名称");
		keys.put("pcUserdomain", "用户域");
		keys.put("javaVersion", "JAVA版本");
		keys.put("vmName", "VM名称");
		keys.put("vmVendor", "VM供应商");
		keys.put("vmVersion", "VM版本");
		keys.put("vmStartTime", "VM启动时间");
		keys.put("vmUpTime", "VM已启动多少秒");
		keys.put("time", "时间");

	}

	public static Map<String, String> getKeys() {
		return keys;
	}

	public static void main(String[] args) {
		System.out.println(OsKeys.keys);
	}
}
