package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.SurveyPage;
import com.sniper.springmvc.mybatis.service.BaseService;
import com.sniper.springmvc.survey.SurveyInsertUtil;

public interface SurveyPageService extends BaseService<SurveyPage> {

	@DataSource(DataSourceValue.LOCAL)
	public SurveyPage getJsonPage(SurveyInsertUtil util);

	@DataSource
	public List<SurveyPage> executeSort(SurveyPage page, String desc);

	public List<SurveyPage> executePlus(SurveyInsertUtil util);
}
