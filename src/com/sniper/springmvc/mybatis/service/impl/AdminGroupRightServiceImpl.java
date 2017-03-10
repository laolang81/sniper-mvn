package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.AdminGroupRight;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("adminGroupRightService")
public class AdminGroupRightServiceImpl extends
		BaseServiceImpl<AdminGroupRight> implements AdminGroupRightService {

	@Resource(name = "adminGroupRightDao")
	@Override
	public void setDao(BaseDao<AdminGroupRight> dao) {
		super.setDao(dao);
	}
}
