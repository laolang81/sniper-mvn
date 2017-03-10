package com.sniper.springmvc.mybatis.service.impl;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.OauthClient;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface OauthClientService extends BaseService<OauthClient> {

	@DataSource(DataSourceValue.MASTER)
	public OauthClient createClient(OauthClient client);// 创建客户端

	OauthClient findByClientId(String clientId);// 根据客户端id查找客户端

	OauthClient findByClientSecret(String clientSecret);// 根据客户端安全KEY查找客户端
}
