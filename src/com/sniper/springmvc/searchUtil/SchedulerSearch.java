package com.sniper.springmvc.searchUtil;

public class SchedulerSearch extends BaseSearch {

	private int id;
	private String jobName;
	private String submit;
	private String path;
	private String cron;
	// 每个线程做的新闻数量
	private int greaterThenId;
	private int pageSize = 5000;
	private int pageOffset = 0;
	// 处理的最大记录数
	private int maxCount = 0;
	private int maxThreadCount = 100;

	private int count;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getSubmit() {
		return submit;
	}

	public void setSubmit(String submit) {
		this.submit = submit;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public int getMaxThreadCount() {
		return maxThreadCount;
	}

	public void setMaxThreadCount(int maxThreadCount) {
		this.maxThreadCount = maxThreadCount;
	}

	public int getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(int pageOffset) {
		this.pageOffset = pageOffset;
	}

	public int getGreaterThenId() {
		return greaterThenId;
	}

	public void setGreaterThenId(int greaterThenId) {
		this.greaterThenId = greaterThenId;
	}

	@Override
	public String toString() {
		return "SchedulerSearch [jobName=" + jobName + ", submit=" + submit
				+ ", path=" + path + ", cron=" + cron + ", count=" + count
				+ "]";
	}

}
