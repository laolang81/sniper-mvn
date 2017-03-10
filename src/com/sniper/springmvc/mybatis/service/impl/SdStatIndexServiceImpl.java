package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdStatIndex;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("sdStatIndexService")
public class SdStatIndexServiceImpl extends BaseServiceImpl<SdStatIndex>
		implements SdStatIndexService {

	@Resource(name = "sdStatIndexDao")
	@Override
	public void setDao(BaseDao<SdStatIndex> dao) {
		super.setDao(dao);
	}

	@Override
	public int getAllTotal(String date) {
		Map<String, Object> params = new HashMap<>();
		params.put("date", date);
		int a = this.pageCount(params);
		return a;
	}

	@Override
	public int saveSubjectView(String date) {
		Map<String, Object> params = new HashMap<>();
		params.put("date", date);
		return this.dao.insert("saveSubjectView", params);
	}

	@Override
	public int deleteByDate(String date) {
		return this.delete("deleteByDate", date);
	}

}
