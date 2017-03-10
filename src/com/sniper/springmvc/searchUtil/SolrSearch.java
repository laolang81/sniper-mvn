package com.sniper.springmvc.searchUtil;

public class SolrSearch extends BaseSearch {

	private int pageSize = 50;
	// 默认和页面数据等同，因为默认是第一页
	private int pageOffset = 0;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(int pageOffset) {
		this.pageOffset = pageOffset;
	}

}
