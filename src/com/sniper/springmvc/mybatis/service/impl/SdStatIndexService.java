package com.sniper.springmvc.mybatis.service.impl;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.SdStatIndex;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdStatIndexService extends BaseService<SdStatIndex> {

	@DataSource(DataSourceValue.STAT)
	public int deleteByDate(String date);

	@DataSource(DataSourceValue.STAT)
	public int getAllTotal(String date);

	@DataSource(DataSourceValue.MASTER)
	public int saveSubjectView(String date);
}
