package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.mybatis.service.BaseService;
import com.sniper.springmvc.survey.SurveyInsertUtil;

public interface SurveyQuestionOptionService extends
		BaseService<SurveyQuestionOption> {

	public List<SurveyQuestionOption> executeSort(SurveyQuestionOption option,
			String sort);

	public SurveyQuestionOption executeCopy(SurveyQuestionOption option);

	public SurveyQuestionOption executeCheck(SurveyQuestionOption option);

	public List<SurveyQuestionOption> executePlus(SurveyInsertUtil utils);
	
	public List<SurveyQuestionOption> getOptions(String qid);
}
