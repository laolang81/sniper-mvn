package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.mybatis.service.BaseService;
import com.sniper.springmvc.survey.SurveyInsertUtil;

public interface SurveyQuestionServie extends BaseService<SurveyQuestion> {

	public List<SurveyQuestion> executeSort(SurveyQuestion question, String sort);

	public SurveyQuestion executeCopy(SurveyQuestion question);

	public List<SurveyQuestion> executePlus(SurveyInsertUtil util);
}
