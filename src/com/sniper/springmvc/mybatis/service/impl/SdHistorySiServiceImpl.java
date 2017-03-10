package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdHistorySi;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("sdHistorySiService")
public class SdHistorySiServiceImpl extends BaseServiceImpl<SdHistorySi>
		implements SdHistorySiService {

	@Resource(name = "sdHistorySiDao")
	@Override
	public void setDao(BaseDao<SdHistorySi> dao) {
		super.setDao(dao);
	}
}
