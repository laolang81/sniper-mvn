package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.model.SurveyResultData;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SurveyResultDataService extends BaseService<SurveyResultData> {

	public List<SurveyResultData> getSurveyResultData(String sid);

	public SurveyResultData getSurveyResultDataBySession(String sid,
			String sessionid);

}
