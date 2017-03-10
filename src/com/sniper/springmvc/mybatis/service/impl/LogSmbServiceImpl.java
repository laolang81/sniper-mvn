package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.LogSmb;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("logSmbService")
public class LogSmbServiceImpl extends BaseServiceImpl<LogSmb> implements
		LogSmbService {

	@Resource(name = "logSmbDao")
	@Override
	public void setDao(BaseDao<LogSmb> dao) {
		super.setDao(dao);
	}
}
