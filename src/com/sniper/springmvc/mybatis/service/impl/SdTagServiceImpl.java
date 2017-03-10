package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdTag;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("sdTagService")
public class SdTagServiceImpl extends BaseServiceImpl<SdTag> implements
		SdTagService {

	@Resource(name = "sdTagDao")
	@Override
	public void setDao(BaseDao<SdTag> dao) {
		super.setDao(dao);
	}
}
