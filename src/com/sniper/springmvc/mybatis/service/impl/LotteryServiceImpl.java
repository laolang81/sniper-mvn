package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.Lottery;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("lotteryService")
public class LotteryServiceImpl extends BaseServiceImpl<Lottery> implements
		LotteryService {

	@Resource(name = "lotteryDao")
	@Override
	public void setDao(BaseDao<Lottery> dao) {
		super.setDao(dao);
	}
}
