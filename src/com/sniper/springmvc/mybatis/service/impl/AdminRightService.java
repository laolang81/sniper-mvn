package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.model.AdminRight;
import com.sniper.springmvc.mybatis.service.BaseService;

/**
 * 权限right
 * 
 * @author sniper
 * 
 */

public interface AdminRightService extends BaseService<AdminRight> {

	@DataSource
	public void appendRightByURL(String url);

	@DataSource
	public List<AdminRight> springRight();

	@DataSource
	public AdminRight getCRightByUrl(String url);

}
