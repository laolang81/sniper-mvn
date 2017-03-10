package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.model.Notice;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface NoticeService extends BaseService<Notice> {

	public int getReadNoticeCount(String string, Boolean status);

	public int getSendNoticeCount(String string, Boolean status);

	public List<Notice> getReadNotices(String string, Boolean status);

	public List<Notice> getSendNotices(String string, Boolean status);

}
