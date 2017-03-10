package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.OauthClient;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.FilesUtil;

@Service("oauthClientService")
public class OauthClientServiceImpl extends BaseServiceImpl<OauthClient>
		implements OauthClientService {

	@Resource(name = "oauthClientDao")
	@Override
	public void setDao(BaseDao<OauthClient> dao) {
		super.setDao(dao);

	}

	@Override
	public OauthClient createClient(OauthClient client) {
		client.setClientId(FilesUtil.getUUIDName("", true));
		client.setClientSecret(FilesUtil.getUUIDName("", true));
		this.insert(client);
		return client;
	}

	@Override
	public OauthClient findByClientId(String clientId) {
		return this.get(clientId);
	}

	@Override
	public OauthClient findByClientSecret(String clientSecret) {
		Map<String, Object> params = new HashMap<>();
		params.put("clientSecret", clientSecret);
		return (OauthClient) this.find("find", params);
	}

}
