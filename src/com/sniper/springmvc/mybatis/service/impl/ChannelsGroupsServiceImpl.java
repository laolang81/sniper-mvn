package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.ChannelsGroups;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("channelsGroupsService")
public class ChannelsGroupsServiceImpl extends BaseServiceImpl<ChannelsGroups>
		implements ChannelsGroupsService {

	@Resource(name = "channelsGroupsDao")
	@Override
	public void setDao(BaseDao<ChannelsGroups> dao) {
		super.setDao(dao);
	}
}
