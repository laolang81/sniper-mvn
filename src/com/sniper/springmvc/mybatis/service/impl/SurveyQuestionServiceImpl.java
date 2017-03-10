package com.sniper.springmvc.mybatis.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SurveyPage;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.model.SurveyQuestionRules;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.survey.SurveyInsertUtil;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.FilesUtil;

@Service("surveyQuestionServie")
public class SurveyQuestionServiceImpl extends BaseServiceImpl<SurveyQuestion>
		implements SurveyQuestionServie {

	@Resource
	SurveyQuestionOptionService optionService;

	@Resource
	SurveyPageService pageService;

	@Resource
	SurveyQuestionRulesService rulesService;

	@Resource(name = "surveyQuestionDao")
	@Override
	public void setDao(BaseDao<SurveyQuestion> dao) {
		super.setDao(dao);
	}

	@Override
	public List<SurveyQuestion> executeSort(SurveyQuestion question, String sort) {
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

		SurveyQuestion question2 = this.get(question.getId());

		Map<String, Object> params = new HashMap<>();
		params.put("sort", "sort" + sort + question2.getSort());
		params.put("pid", question.getPage().getId());
		params.put("order", "sort " + order);
		params.put("pageOffset", 0);
		params.put("pageSize", 2);

		List<SurveyQuestion> optionsBack = new ArrayList<>();
		List<SurveyQuestion> options = this.pageList(params);
		if (options.size() == 2) {

			int sort0 = options.get(0).getSort();
			int sort1 = options.get(1).getSort();

			SurveyQuestion option4 = options.get(1);

			option4.setSort(sort0);
			super.update(option4);
			option4.setPage(question.getPage());
			option4.setOptions(new ArrayList<SurveyQuestionOption>());
			optionsBack.add(option4);

			SurveyQuestion option3 = options.get(0);
			option3.setSort(sort1);
			this.update(option3);

			option3.setPage(question.getPage());
			option3.setOptions(new ArrayList<SurveyQuestionOption>());
			optionsBack.add(option3);

		}

		return optionsBack;
	}

	@Override
	public SurveyQuestion executeCopy(SurveyQuestion question) {
		SurveyQuestion questionCopy = this.get(question.getId());
		// 增加排序
		SurveyQuestion optionCopy2 = (SurveyQuestion) DataUtil
				.deeplyCopy(questionCopy);
		optionCopy2.setSort(questionCopy.getSort() + 10);
		return optionCopy2;
	}

	@Override
	public List<SurveyQuestion> executePlus(SurveyInsertUtil util) {

		String input = util.getInput();
		// 换行符
		String[] inputs = input.split(System.getProperty("line.separator"));

		// 获取当前问题下面有多少问题
		SurveyPage page2 = pageService
				.get(util.getQuestion().getPage().getId());
		List<SurveyQuestion> questions = new ArrayList<>();
		int questionSize = 1;
		int size = page2.getSq().size();
		if (size > 0) {
			questionSize = page2.getSq().get(size - 1).getSort();
		}

		SurveyPage page = new SurveyPage();
		page.setId(util.getQuestion().getPage().getId());

		for (int i = 0; i < inputs.length; i++) {
			String string = inputs[i].trim();
			SurveyQuestion question = new SurveyQuestion();
			question.setId(FilesUtil.UUID());
			question.setName(string);
			SurveyQuestionRules rules = new SurveyQuestionRules();
			rules.setId(FilesUtil.getUUIDName("", false));
			rulesService.insert(rules);
			question.setRules(rules);
			question.setPage(page);
			question.setSort(questionSize + ((i + 1) * 1000));
			this.insert(question);
			questions.add(question);
		}

		return questions;
	}

	@Override
	public SurveyQuestion get(String id) {

		Map<String, Object> params2 = new HashMap<>();
		params2.put("id", id);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) super.find("selectMap",
				params2);

		String ruleId = (String) map.get("rule_id");

		SurveyQuestionRules rules = rulesService.get(ruleId);
		SurveyQuestion question = super.get(id);
		question.setRules(rules);
		Map<String, Object> params = new HashMap<>();
		params.put("qid", id);
		params.put("order", "sort asc");
		List<SurveyQuestionOption> options = optionService.query("select",
				params);
		question.setOptions(options);
		return question;
	}

	@Override
	public int insert(SurveyQuestion t) {
		List<SurveyQuestionOption> options = t.getOptions();
		if (options.size() > 0) {
			for (SurveyQuestionOption option : options) {
				option.setQuestion(t);
				optionService.insert(option);
			}
		}
		return super.insert(t);
	}

	@Override
	public int update(SurveyQuestion t) {
		if (t.getRules() != null) {
			rulesService.update(t.getRules());
		}
		return super.update(t);
	}

	@Override
	public int delete(String id) {
		SurveyQuestion question = this.get(id);
		optionService.delete("deleteByQuestion", id);
		int r = super.delete(id);
		rulesService.delete(question.getRules().getId());
		return r;
	}
}
