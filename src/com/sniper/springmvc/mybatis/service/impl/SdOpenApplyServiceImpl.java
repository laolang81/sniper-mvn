package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdOpenApply;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("sdOpenApplyService")
public class SdOpenApplyServiceImpl extends BaseServiceImpl<SdOpenApply>
		implements SdOpenApplyService {

	@Resource(name = "sdOpenApplyDao")
	@Override
	public void setDao(BaseDao<SdOpenApply> dao) {
		super.setDao(dao);
	}
}
