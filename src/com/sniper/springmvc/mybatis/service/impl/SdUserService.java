package com.sniper.springmvc.mybatis.service.impl;

import com.sniper.springmvc.model.SdUser;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdUserService extends BaseService<SdUser> {

	public SdUser login(String username, String password);

	public SdUser getUserByName(String name);
}
