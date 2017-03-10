package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdStatItem;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("sdStatItemService")
public class SdStatItemServiceImpl extends BaseServiceImpl<SdStatItem>
		implements SdStatItemService {

	@Resource(name = "sdStatItemDao")
	@Override
	public void setDao(BaseDao<SdStatItem> dao) {
		super.setDao(dao);
	}
}
