package com.sniper.springmvc.mybatis.service.impl;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.ErrorLog;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface ErrorLogService extends BaseService<ErrorLog> {

	@DataSource(DataSourceValue.MASTER)
	public void writeLog(Exception e);
}
