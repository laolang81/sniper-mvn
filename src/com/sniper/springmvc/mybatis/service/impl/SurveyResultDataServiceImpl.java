package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SurveyResult;
import com.sniper.springmvc.model.SurveyResultData;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("surveyResultDataService")
public class SurveyResultDataServiceImpl extends
		BaseServiceImpl<SurveyResultData> implements SurveyResultDataService {

	@Resource
	SurveyResultService resultService;

	@Resource(name = "surveyResultDataDao")
	@Override
	public void setDao(BaseDao<SurveyResultData> dao) {
		super.setDao(dao);
	}

	@Override
	public List<SurveyResultData> getSurveyResultData(String sid) {
		Map<String, Object> params = new HashMap<>();
		params.put("sid", sid);
		return this.query("select", params);
	}

	@Override
	public SurveyResultData getSurveyResultDataBySession(String sid,
			String sessionid) {
		Map<String, Object> params = new HashMap<>();
		params.put("sid", sid);
		params.put("sessionid", sessionid);
		List<SurveyResultData> datas = this.query("select", params);
		if (datas.size() >= 1) {
			return datas.get(0);
		}
		return null;
	}

	@Override
	public SurveyResultData get(String id) {
		SurveyResultData data = super.get(id);
		Map<String, Object> params = new HashMap<>();
		params.put("data_id", id);
		List<SurveyResult> results = resultService.query("select", params);
		data.setResults(results);
		return data;
	}

}
