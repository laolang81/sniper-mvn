package com.sniper.springmvc.mybatis.service.impl;

import java.util.Map;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.SystemConfig;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SystemConfigService extends BaseService<SystemConfig> {

	@DataSource(DataSourceValue.SLAVE)
	public Map<String, String> getAdminConfig(boolean autoload);
}
