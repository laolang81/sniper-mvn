package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SurveyQuestionRules;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("surveyQuestionRulesService")
public class SurveyQuestionRulesServiceImpl extends
		BaseServiceImpl<SurveyQuestionRules> implements
		SurveyQuestionRulesService {

	@Override
	@Resource(name = "surveyQuestionRulesDao")
	public void setDao(BaseDao<SurveyQuestionRules> dao) {
		super.setDao(dao);
	}

}
