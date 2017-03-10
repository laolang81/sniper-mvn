package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.model.Survey;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SurveyService extends BaseService<Survey> {

	@DataSource
	public Survey getSurvey(String id);

	@DataSource
	public List<Survey> surveyCopy(String[] ids);
}
