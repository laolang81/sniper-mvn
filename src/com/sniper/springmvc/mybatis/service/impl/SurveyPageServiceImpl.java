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
import com.sniper.springmvc.survey.SurveyInsertUtil;
import com.sniper.springmvc.utils.FilesUtil;

@Service("surveyPageService")
public class SurveyPageServiceImpl extends BaseServiceImpl<SurveyPage>
		implements SurveyPageService {

	@Resource
	SurveyQuestionServie questionServie;

	@Resource
	SurveyService surveyService;

	@Resource
	SurveyQuestionOptionService optionService;

	@Resource
	SurveyQuestionRulesService rulesService;

	@Resource(name = "surveyPageDao")
	@Override
	public void setDao(BaseDao<SurveyPage> dao) {
		super.setDao(dao);
	}

	@Override
	public SurveyPage getJsonPage(SurveyInsertUtil util) {
		SurveyPage page = this.get(util.getPage().getId());

		for (SurveyQuestion question : page.getSq()) {
			SurveyQuestion question2 = new SurveyQuestion();
			question2.setId(question.getId());
			for (SurveyQuestionOption option : question.getOptions()) {
				option.setQuestion(question2);
			}
			question.setPage(util.getPage());
		}
		page.setSurvey(util.getSurvey());
		return page;
	}

	@Override
	public List<SurveyPage> executeSort(SurveyPage page, String sort) {
		List<SurveyPage> questionsBack = new ArrayList<>();

		String order = "asc";
		switch (sort) {
		case "<=":
			order = "desc";

			break;
		case ">=":
			order = "asc";
			break;

		default:
			break;
		}

		SurveyPage question2 = this.get(page.getId());

		Map<String, Object> params = new HashMap<>();
		params.put("sort", "sort" + sort + question2.getSort());
		params.put("sid", page.getSurvey().getId());
		params.put("order", "sort " + order);
		params.put("pageOffset", 0);
		params.put("pageSize", 2);
		List<SurveyPage> questions = this.pageList(params);

		if (questions.size() == 2) {

			int sort0 = questions.get(0).getSort();
			int sort1 = questions.get(1).getSort();

			SurveyPage option4 = questions.get(1);
			option4.setSort(sort0);
			this.update(option4);
			option4.setSurvey(page.getSurvey());
			option4.setSq(new ArrayList<SurveyQuestion>());
			questionsBack.add(option4);

			SurveyPage option3 = questions.get(0);
			option3.setSort(sort1);
			this.update(option3);
			option3.setSurvey(page.getSurvey());
			option3.setSq(new ArrayList<SurveyQuestion>());
			questionsBack.add(option3);

		}

		return questionsBack;
	}

	@Override
	public List<SurveyPage> executePlus(SurveyInsertUtil util) {

		List<SurveyPage> pages = new ArrayList<>();
		String input = util.getInput();
		// 换行符
		String[] inputs = input.split(System.getProperty("line.separator"));

		Survey survey2 = surveyService.get(util.getPage().getSurvey().getId());
		// 获取初始排序
		int size = survey2.getSurveyPages().size();
		int sort = 0;
		if (size > 0) {
			sort = survey2.getSurveyPages().get(size - 1).getSort();
		}

		for (int i = 0; i < inputs.length; i++) {
			String string = inputs[i].trim();
			SurveyPage surveyPage = new SurveyPage();
			surveyPage.setId(FilesUtil.UUID());
			surveyPage.setName(string);
			surveyPage.setSurvey(survey2);
			surveyPage.setSort(sort + ((i + 1) * 1000));
			this.insert(surveyPage);
			pages.add(surveyPage);
		}
		return pages;
	}

	@Override
	public SurveyPage get(String id) {
		SurveyPage page = super.get(id);
		Map<String, Object> params = new HashMap<>();
		params.put("pid", id);
		params.put("order", "sort asc");
		List<SurveyQuestion> questions = questionServie.query("select", params);
		page.setSq(questions);
		return page;
	}

	@Override
	public int delete(String id) {
		SurveyPage page = this.get(id);
		if (page.getSq().size() > 0) {
			for (SurveyQuestion question : page.getSq()) {
				if (question.getRules() != null) {
					rulesService.delete(question.getRules().getId());
					for (SurveyQuestionOption option : question.getOptions()) {
						optionService.delete(option.getId());
					}
				}
				questionServie.delete(question.getId());
			}
		}
		return super.delete(id);
	}
}
