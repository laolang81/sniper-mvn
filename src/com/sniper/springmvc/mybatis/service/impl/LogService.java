package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.Log;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface LogService extends BaseService<Log> {

	@DataSource(DataSourceValue.MASTER)
	void createLogTable(String tableName);

	public List<Log> findNearesLogs(int n);

}
