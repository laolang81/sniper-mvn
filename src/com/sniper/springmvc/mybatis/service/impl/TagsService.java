package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.Tags;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface TagsService extends BaseService<Tags> {

	public List<Tags> getTagsByName(String name);

	public void viewAdd(String name);

	@DataSource(DataSourceValue.LOCAL)
	public int insertLocale(Tags t);

	@DataSource(DataSourceValue.LOCAL)
	public Tags getLocale(String id);

}
