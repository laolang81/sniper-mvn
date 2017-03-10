package com.sniper.springmvc.java;

import org.hyperic.sigar.CpuPerc;

public class OsCPU {

	// 用户使用率
	private String user;
	// 系统
	private String sys;
	// 等待
	private String wait;
	// 错误
	private String nice;
	// 空闲
	private String idle;
	// 总的使用率
	private String combined;

	private CpuPerc cpuPerc;

	public OsCPU(CpuPerc cpuPerc) {
		this.cpuPerc = cpuPerc;
		this.init();
	}

	public OsCPU() {
	}

	private void init() {
		// this.user = String.format("%.2f %%", cpuPerc.getUser());
		
		this.user = String.format("%.2f", cpuPerc.getUser());
		this.sys = String.format("%.2f", cpuPerc.getSys());
		this.wait = String.format("%.2f", cpuPerc.getWait());
		this.nice = String.format("%.2f", cpuPerc.getNice());
		this.idle = String.format("%.2f", cpuPerc.getIdle());
		this.combined = String.format("%.2f", cpuPerc.getCombined());
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getSys() {
		return sys;
	}

	public void setSys(String sys) {
		this.sys = sys;
	}

	public String getWait() {
		return wait;
	}

	public void setWait(String wait) {
		this.wait = wait;
	}

	public String getNice() {
		return nice;
	}

	public void setNice(String nice) {
		this.nice = nice;
	}

	public String getIdle() {
		return idle;
	}

	public void setIdle(String idle) {
		this.idle = idle;
	}

	public String getCombined() {
		return combined;
	}

	public void setCombined(String combined) {
		this.combined = combined;
	}

}
