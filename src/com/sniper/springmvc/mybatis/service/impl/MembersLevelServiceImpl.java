package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.MembersLevel;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("membersLevelService")
public class MembersLevelServiceImpl extends BaseServiceImpl<MembersLevel>
		implements MembersLevelService {

	@Resource(name = "membersLevelDao")
	@Override
	public void setDao(BaseDao<MembersLevel> dao) {
		super.setDao(dao);
	}
}
