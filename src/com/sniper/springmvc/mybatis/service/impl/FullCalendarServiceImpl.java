package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.FullCalendar;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("fullCalendarService")
public class FullCalendarServiceImpl extends BaseServiceImpl<FullCalendar>
		implements FullCalendarService {

	@Resource(name = "fullCalendarDao")
	@Override
	public void setDao(BaseDao<FullCalendar> dao) {
		super.setDao(dao);
	}
}
