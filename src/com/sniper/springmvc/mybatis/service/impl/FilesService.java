package com.sniper.springmvc.mybatis.service.impl;

import java.sql.Date;
import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface FilesService extends BaseService<Files> {

	@DataSource(DataSourceValue.SLAVE)
	public Files getFileByUrl(String url);

	@DataSource(DataSourceValue.SLAVE)
	public Files getFileBySourcePath(String sourcePath);

	@DataSource(DataSourceValue.MASTER)
	public boolean deleteByPath(String path);

	@DataSource(DataSourceValue.SLAVE)
	public List<Files> lastLists(int limit, Date date);

	@DataSource(DataSourceValue.MASTER)
	public void updateTags(String id, String tag);

}
