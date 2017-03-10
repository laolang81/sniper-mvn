package com.sniper.springmvc.model;

import com.sniper.springmvc.utils.DataUtil;

/**
 * 多个表之间的关系链接
 * 
 * @author suzhen
 * 
 */
public class TablesRelation extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	// 被绑定这id
	private String source;
	// 帮顶这id
	private String bind;
	private int ordersort = DataUtil.getTime();
	// 专家expert,企业enterprise,项目project,文件file
	// project-expert,project-enterprise,expert-file,expert-enterprise,enterprise-file
	// 新加项目和视频的关系project-video,专家和频道的关系其实也可以放到这expert-channel
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getBind() {
		return bind;
	}

	public void setBind(String bind) {
		this.bind = bind;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getOrdersort() {
		return ordersort;
	}

	public void setOrdersort(int ordersort) {
		this.ordersort = ordersort;
	}

}
