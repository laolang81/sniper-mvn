package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdUser;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("sdUserService")
public class SdUserServiceImpl extends BaseServiceImpl<SdUser> implements
		SdUserService {

	@Resource(name = "sdUserDao")
	@Override
	public void setDao(BaseDao<SdUser> dao) {
		super.setDao(dao);
	}

	@Override
	public SdUser login(String username, String password) {

		Map<String, Object> params = new HashMap<>();
		params.put("username", username);
		params.put("password", password);
		SdUser sdUser = (SdUser) this.find("find", params);
		return sdUser;
	}

	@Override
	public SdUser getUserByName(String name) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		SdUser sdUser = (SdUser) this.find("find", params);
		return sdUser;
	}
}
