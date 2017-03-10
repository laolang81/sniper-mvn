package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdContent;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("sdContentService")
public class SdContentServiceImpl extends BaseServiceImpl<SdContent> implements
		SdContentService {

	@Resource(name = "sdContentDao")
	@Override
	public void setDao(BaseDao<SdContent> dao) {
		super.setDao(dao);
	}
}
