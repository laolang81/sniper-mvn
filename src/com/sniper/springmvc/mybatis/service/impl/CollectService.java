package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.Collect;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface CollectService extends BaseService<Collect> {
	public static String URL_MENU = "url_menu";

	@DataSource(DataSourceValue.LOCAL)
	public List<Collect> getCollect(int uid, String type);

	@DataSource(DataSourceValue.MASTER)
	public void insertValue(String url,String name, String type, AdminUser adminUser);
}
