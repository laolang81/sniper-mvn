package com.sniper.springmvc.java;

import java.util.Date;

public class OsValues {
	// 堆内存使用情况
	private long heapMemoryUsageInit;
	private long heapMemoryUsageUsed;
	private long heapMemoryUsageMax;
	// 虚拟机使用情况
	private long javaMemoryFree;
	private long javaMemoryMax;
	// 已使用内存+free
	private long javaMemoryTotal;
	private String javaVersion;

	private String osName;
	private String osVersion;
	private String osArch;
	private int osCpuNum;
	private double systemLoadAverage;
	private long osDiskTotal;
	private long osDiskFree;
	private long osDiskUsed;

	private int threadCount;
	private int peakThreadCount;
	private long currentTheardCpuTime;
	private long currentTheardUserTime;
	private int daemonThreadCount;

	private String vmName;
	private String vmVersion;
	private Date vmStartTime;
	private long vmUpTime;

	private long memoryTotal;
	private long memoryFree;
	private long memoryUsed;

	public long getHeapMemoryUsageInit() {
		return heapMemoryUsageInit;
	}

	public void setHeapMemoryUsageInit(long heapMemoryUsageInit) {
		this.heapMemoryUsageInit = heapMemoryUsageInit;
	}

	public long getHeapMemoryUsageUsed() {
		return heapMemoryUsageUsed;
	}

	public void setHeapMemoryUsageUsed(long heapMemoryUsageUsed) {
		this.heapMemoryUsageUsed = heapMemoryUsageUsed;
	}

	public long getHeapMemoryUsageMax() {
		return heapMemoryUsageMax;
	}

	public void setHeapMemoryUsageMax(long heapMemoryUsageMax) {
		this.heapMemoryUsageMax = heapMemoryUsageMax;
	}

	public long getJavaMemoryFree() {
		return javaMemoryFree;
	}

	public void setJavaMemoryFree(long javaMemoryFree) {
		this.javaMemoryFree = javaMemoryFree;
	}

	public long getJavaMemoryMax() {
		return javaMemoryMax;
	}

	public void setJavaMemoryMax(long javaMemoryMax) {
		this.javaMemoryMax = javaMemoryMax;
	}

	public long getJavaMemoryTotal() {
		return javaMemoryTotal;
	}

	public void setJavaMemoryTotal(long javaMemoryTotal) {
		this.javaMemoryTotal = javaMemoryTotal;
	}

	public String getJavaVersion() {
		return javaVersion;
	}

	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}

	public String getOsName() {
		return osName;
	}

	public void setOsName(String osName) {
		this.osName = osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getOsArch() {
		return osArch;
	}

	public void setOsArch(String osArch) {
		this.osArch = osArch;
	}

	public int getOsCpuNum() {
		return osCpuNum;
	}

	public void setOsCpuNum(int osCpuNum) {
		this.osCpuNum = osCpuNum;
	}

	public double getSystemLoadAverage() {
		return systemLoadAverage;
	}

	public void setSystemLoadAverage(double systemLoadAverage) {
		this.systemLoadAverage = systemLoadAverage;
	}

	public long getOsDiskTotal() {
		return osDiskTotal;
	}

	public void setOsDiskTotal(long osDiskTotal) {
		this.osDiskTotal = osDiskTotal;
	}

	public long getOsDiskFree() {
		return osDiskFree;
	}

	public void setOsDiskFree(long osDiskFree) {
		this.osDiskFree = osDiskFree;
	}

	public long getOsDiskUsed() {
		return osDiskUsed;
	}

	public void setOsDiskUsed(long osDiskUsed) {
		this.osDiskUsed = osDiskUsed;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

	public int getPeakThreadCount() {
		return peakThreadCount;
	}

	public void setPeakThreadCount(int peakThreadCount) {
		this.peakThreadCount = peakThreadCount;
	}

	public long getCurrentTheardCpuTime() {
		return currentTheardCpuTime;
	}

	public void setCurrentTheardCpuTime(long currentTheardCpuTime) {
		this.currentTheardCpuTime = currentTheardCpuTime;
	}

	public long getCurrentTheardUserTime() {
		return currentTheardUserTime;
	}

	public void setCurrentTheardUserTime(long currentTheardUserTime) {
		this.currentTheardUserTime = currentTheardUserTime;
	}

	public int getDaemonThreadCount() {
		return daemonThreadCount;
	}

	public void setDaemonThreadCount(int daemonThreadCount) {
		this.daemonThreadCount = daemonThreadCount;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getVmVersion() {
		return vmVersion;
	}

	public void setVmVersion(String vmVersion) {
		this.vmVersion = vmVersion;
	}

	public Date getVmStartTime() {
		return vmStartTime;
	}

	public void setVmStartTime(Date vmStartTime) {
		this.vmStartTime = vmStartTime;
	}

	public long getVmUpTime() {
		return vmUpTime;
	}

	public void setVmUpTime(long vmUpTime) {
		this.vmUpTime = vmUpTime;
	}

	public long getMemoryTotal() {
		return memoryTotal;
	}

	public void setMemoryTotal(long memoryTotal) {
		this.memoryTotal = memoryTotal;
	}

	public long getMemoryFree() {
		return memoryFree;
	}

	public void setMemoryFree(long memoryFree) {
		this.memoryFree = memoryFree;
	}

	public long getMemoryUsed() {
		return memoryUsed;
	}

	public void setMemoryUsed(long memoryUsed) {
		this.memoryUsed = memoryUsed;
	}

}
