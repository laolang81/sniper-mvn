package com.sniper.springmvc.java;

import org.hyperic.sigar.FileSystemUsage;

public class OsHardDisk {
	// 总空间
	private String total;
	// 剩余空间
	private String free;
	// 可用大小
	private String avail;
	// 已用大小
	private String used;
	// 资源利用率
	private String percent;
	private FileSystemUsage usage;
	private long diskReads;
	private long diskWrites;

	private double size = 1024l;

	public OsHardDisk() {
		// TODO Auto-generated constructor stub
	}

	public OsHardDisk(FileSystemUsage usage) {
		this.usage = usage;
		this.init();

	}

	public OsHardDisk(FileSystemUsage usage, int size) {

		this.usage = usage;
		this.size = size;
		this.init();
	}

	private void init() {
		System.out.println(usage.getTotal());
		this.total = String.format("%.2f GB", usage.getTotal() / size / size);
		this.avail = String.format("%.2f GB", usage.getAvail() / size / size);
		this.free = String.format("%.2f GB", usage.getFree() / size / size);
		this.used = String.format("%.2f GB", usage.getUsed() / size / size);
		this.percent = String.format("%.0f %%", usage.getUsePercent() * 100D);

		this.diskReads = usage.getDiskReadBytes();
		this.diskWrites = usage.getDiskWriteBytes();

	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getFree() {
		return free;
	}

	public void setFree(String free) {
		this.free = free;
	}

	public String getAvail() {
		return avail;
	}

	public void setAvail(String avail) {
		this.avail = avail;
	}

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public void setDiskReads(long diskReads) {
		this.diskReads = diskReads;
	}

	public long getDiskReads() {
		return diskReads;
	}

	public void setDiskWrites(long diskWrites) {
		this.diskWrites = diskWrites;
	}

	public long getDiskWrites() {
		return diskWrites;
	}
}
