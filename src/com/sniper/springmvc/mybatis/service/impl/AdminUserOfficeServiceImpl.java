package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.AdminUserOffice;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("adminUserOfficeService")
public class AdminUserOfficeServiceImpl extends
		BaseServiceImpl<AdminUserOffice> implements AdminUserOfficeService {

	@Resource(name = "adminUserOfficeDao")
	@Override
	public void setDao(BaseDao<AdminUserOffice> dao) {
		super.setDao(dao);
	}

}
