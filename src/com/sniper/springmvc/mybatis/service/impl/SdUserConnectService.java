package com.sniper.springmvc.mybatis.service.impl;

import com.sniper.springmvc.model.SdUserConnect;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdUserConnectService extends BaseService<SdUserConnect> {

	/**
	 * 需要接受的参数，登录名称，处室名称，
	 * @param uname
	 * @param sourceid
	 * @param dname
	 * @return
	 */
	public boolean login(String uname, String did, String dname);
}
