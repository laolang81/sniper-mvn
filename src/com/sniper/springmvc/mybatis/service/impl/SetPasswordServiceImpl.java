package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SetPassword;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("setPasswordService")
public class SetPasswordServiceImpl extends BaseServiceImpl<SetPassword>
		implements SetPasswordService {

	@Resource(name = "setPasswordDao")
	@Override
	public void setDao(BaseDao<SetPassword> dao) {
		super.setDao(dao);
	}

	@Override
	public SetPassword validBySign(String sign) {
		Map<String, Object> params = new HashMap<>();
		params.put("sign", sign);
		return (SetPassword) this.find("select", params);
	}

}
