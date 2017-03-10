package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.MailFiles;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("mailFilesService")
public class MailFilesServiceImpl extends BaseServiceImpl<MailFiles> implements
		MailFilesService {

	@Resource(name = "mailFilesDao")
	@Override
	public void setDao(BaseDao<MailFiles> dao) {
		super.setDao(dao);
	}
}
