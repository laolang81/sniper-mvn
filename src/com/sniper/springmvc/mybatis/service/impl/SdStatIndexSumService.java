package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;
import java.util.Map;

import com.sniper.springmvc.model.SdStatIndexSum;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdStatIndexSumService extends BaseService<SdStatIndexSum> {

	public List<SdStatIndexSum> statIndexDay(int limit);
	
	public List<Map<String, Object>> statIndexMonth(int limit);
	
	public List<Map<String, Object>> statIndexYear(int limit);
}
