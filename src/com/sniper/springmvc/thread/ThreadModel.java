package com.sniper.springmvc.thread;

import java.lang.Thread.State;
import java.lang.management.LockInfo;

public class ThreadModel {

	private long id;
	private long cpuTime;
	private long userTime;
	private String name;
	private State state;
	private LockInfo lockInfo;
	private Long blockedCount;
	private Long blockedTime;
	private Long waitedCount;
	private Long waitedTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCpuTime() {
		return cpuTime;
	}

	public void setCpuTime(long cpuTime) {
		this.cpuTime = cpuTime;
	}

	public long getUserTime() {
		return userTime;
	}

	public void setUserTime(long userTime) {
		this.userTime = userTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public LockInfo getLockInfo() {
		return lockInfo;
	}

	public void setLockInfo(LockInfo lockInfo) {
		this.lockInfo = lockInfo;
	}

	public Long getBlockedCount() {
		return blockedCount;
	}

	public void setBlockedCount(Long blockedCount) {
		this.blockedCount = blockedCount;
	}

	public Long getBlockedTime() {
		return blockedTime;
	}

	public void setBlockedTime(Long blockedTime) {
		this.blockedTime = blockedTime;
	}

	public Long getWaitedCount() {
		return waitedCount;
	}

	public void setWaitedCount(Long waitedCount) {
		this.waitedCount = waitedCount;
	}

	public Long getWaitedTime() {
		return waitedTime;
	}

	public void setWaitedTime(Long waitedTime) {
		this.waitedTime = waitedTime;
	}

}
