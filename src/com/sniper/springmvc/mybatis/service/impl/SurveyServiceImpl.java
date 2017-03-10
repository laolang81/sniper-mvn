package com.sniper.springmvc.mybatis.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.Survey;
import com.sniper.springmvc.model.SurveyPage;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.DataUtil;

@Service("surveyService")
public class SurveyServiceImpl extends BaseServiceImpl<Survey> implements
		SurveyService {

	@Resource
	SurveyPageService pageService;

	@Resource
	SurveyQuestionServie questionServie;

	@Resource
	SurveyQuestionOptionService optionService;

	@Resource
	SurveyQuestionRulesService rulesService;

	@Resource(name = "surveyDao")
	@Override
	public void setDao(BaseDao<Survey> dao) {
		super.setDao(dao);
	}

	@Override
	public Survey getSurvey(String id) {
		Survey survey = super.get(id);
		Map<String, Object> params = new HashMap<>();
		params.put("sid", id);
		params.put("order", "sort asc");
		List<SurveyPage> pages = pageService.query("select", params);

		survey.setSurveyPages(pages);
		for (SurveyPage page : pages) {
			Map<String, Object> paramsQ = new HashMap<>();
			paramsQ.put("pid", page.getId());
			paramsQ.put("order", "sort asc");
			List<SurveyQuestion> questions = questionServie.query("select",
					paramsQ);
			List<SurveyQuestion> questions2 = new ArrayList<>();
			for (SurveyQuestion question : questions) {
				SurveyQuestion question2 = questionServie.get(question.getId());
				questions2.add(question2);
			}
			page.setSq(questions2);
		}
		return survey;
	}

	@Override
	public List<Survey> surveyCopy(String[] ids) {
		List<Survey> lists = new ArrayList<>();
		for (String s : ids) {
			Survey survey2 = this.get(s);
			Survey surveyNew = (Survey) DataUtil.deeplyCopy(survey2);

			lists.add(surveyNew);
		}
		return lists;
	}

	@Override
	public int delete(String id) {
		Survey survey = this.get(id);
		if (survey.getSurveyPages().size() > 0) {
			for (SurveyPage page : survey.getSurveyPages()) {
				if (page.getSq().size() > 0) {
					for (SurveyQuestion question : page.getSq()) {
						if (question.getOptions().size() > 0) {
							for (SurveyQuestionOption option : question
									.getOptions()) {
								optionService.delete(option.getId());
							}
						}
						questionServie.delete(question.getId());
					}
				}
				pageService.delete(page.getId());
			}
		}
		return super.delete(id);
	}

	@Override
	public Survey get(String id) {
		Survey survey = super.get(id);
		Map<String, Object> params = new HashMap<>();
		params.put("sid", id);
		params.put("order", "sort asc");
		List<SurveyPage> pages = pageService.query("select", params);
		survey.setSurveyPages(pages);
		return survey;
	}

}
