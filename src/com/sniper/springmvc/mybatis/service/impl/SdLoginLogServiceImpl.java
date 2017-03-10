package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdLoginLog;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("sdLoginLogService")
public class SdLoginLogServiceImpl extends BaseServiceImpl<SdLoginLog>
		implements SdLoginLogService {

	@Resource(name = "sdLoginLogDao")
	@Override
	public void setDao(BaseDao<SdLoginLog> dao) {
		super.setDao(dao);
	}
}
