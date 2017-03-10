package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdStatIndexSum;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("sdStatIndexSumService")
public class SdStatIndexSumServiceImpl extends BaseServiceImpl<SdStatIndexSum>
		implements SdStatIndexSumService {

	@Resource(name = "sdStatIndexSumDao")
	@Override
	public void setDao(BaseDao<SdStatIndexSum> dao) {
		super.setDao(dao);
	}

	@Override
	public List<SdStatIndexSum> statIndexDay(int limit) {
		Map<String, Object> params = new HashMap<>();
		params.put("pageSize", limit);
		params.put("order", "id desc");
		return this.query("select", params);
	}

	@Override
	public List<Map<String, Object>> statIndexMonth(int limit) {
		Map<String, Object> params = new HashMap<>();
		params.put("pageSize", limit);
		List<Map<String, Object>> lists = this.queryMap("statMonth", params);
		return lists;
	}

	@Override
	public List<Map<String, Object>> statIndexYear(int limit) {
		Map<String, Object> params = new HashMap<>();
		params.put("pageSize", limit);
		List<Map<String, Object>> lists = this.queryMap("statYear", params);
		return lists;
	}
}
