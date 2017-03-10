package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;
import java.util.Map;

import com.sniper.springmvc.model.Survey;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyResult;
import com.sniper.springmvc.model.SurveyResultData;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SurveyResultService extends BaseService<SurveyResult> {

	public Map<String, Integer> getSurveyResult(Survey survey);

	public Map<String, List<String>> getAnswer(String sid, String rdid);

	public Map<String, Map<String, List<String>>> exportResult(
			List<SurveyResultData> datas, String sid);

	public int searchQuestionOption(String qid, String optionName, boolean b);

	public int searchQuestionOption(SurveyQuestion question);

}
