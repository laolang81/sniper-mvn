package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.Notice;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("noticeServie")
public class NoticeServiceImpl extends BaseServiceImpl<Notice> implements
		NoticeService {

	@Resource(name = "noticeDao")
	@Override
	public void setDao(BaseDao<Notice> dao) {
		super.setDao(dao);
	}

	@Override
	public int getReadNoticeCount(String string, Boolean status) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSendNoticeCount(String string, Boolean status) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Notice> getReadNotices(String string, Boolean status) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Notice> getSendNotices(String string, Boolean status) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
