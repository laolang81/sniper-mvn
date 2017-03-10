package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.MembersConnect;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("membersConnectService")
public class MembersConnectServiceImpl extends BaseServiceImpl<MembersConnect>
		implements MembersConnectService {

	@Resource(name = "membersConnectDao")
	@Override
	public void setDao(BaseDao<MembersConnect> dao) {
		super.setDao(dao);
	}
}
