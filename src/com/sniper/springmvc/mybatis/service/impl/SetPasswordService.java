package com.sniper.springmvc.mybatis.service.impl;

import com.sniper.springmvc.model.SetPassword;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SetPasswordService extends BaseService<SetPassword> {

	public SetPassword validBySign(String sign) ;
}
