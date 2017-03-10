package com.sniper.springmvc.mybatis.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.survey.SurveyInsertUtil;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.FilesUtil;

@Service("surveyQuestionOptionService")
public class SurveyQuestionOptionServiceImpl extends
		BaseServiceImpl<SurveyQuestionOption> implements
		SurveyQuestionOptionService {

	@Resource
	SurveyQuestionServie questionServie;

	@Override
	@Resource(name = "surveyQuestionOptionDao")
	public void setDao(BaseDao<SurveyQuestionOption> dao) {
		super.setDao(dao);
	}

	@Override
	public List<SurveyQuestionOption> executeSort(SurveyQuestionOption option,
			String sort) {
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

		SurveyQuestionOption option2 = this.get(option.getId());

		Map<String, Object> params = new HashMap<>();
		params.put("sort", "sort" + sort + option2.getSort());
		params.put("qid", option.getQuestion().getId());
		params.put("order", "sort " + order);
		params.put("pageOffset", 0);
		params.put("pageSize", 2);
		List<SurveyQuestionOption> optionsBack = new ArrayList<>();
		List<SurveyQuestionOption> options = this.pageList(params);
		if (options.size() == 2) {

			int sort0 = options.get(0).getSort();
			int sort1 = options.get(1).getSort();

			SurveyQuestionOption option4 = options.get(1);
			option4.setSort(sort0);
			this.update(option4);
			option4.setQuestion(option.getQuestion());
			optionsBack.add(option4);

			SurveyQuestionOption option3 = options.get(0);
			option3.setSort(sort1);
			this.update(option3);
			option3.setQuestion(option.getQuestion());
			optionsBack.add(option3);

		}

		return optionsBack;
	}

	@Override
	public SurveyQuestionOption executeCopy(SurveyQuestionOption option) {
		SurveyQuestionOption optionCopy = this.get(option.getId());
		// 增加排序
		SurveyQuestionOption optionCopy2 = (SurveyQuestionOption) DataUtil
				.deeplyCopy(optionCopy);
		optionCopy2.setSort(optionCopy.getSort() + 10);
		return optionCopy2;
	}

	@Override
	public SurveyQuestionOption executeCheck(SurveyQuestionOption option) {

		SurveyQuestionOption option2 = this.get(option.getId());
		option2.setChecked(option.isChecked());
		option2.setWrited(option.isWrited());
		option2.setName(option.getName().trim());
		this.update(option2);
		return option;
	}

	@Override
	public List<SurveyQuestionOption> executePlus(SurveyInsertUtil utils) {

		SurveyQuestion question = questionServie.get(utils.getOption()
				.getQuestion().getId());
		List<SurveyQuestionOption> options = new ArrayList<>();
		try {

			String input = utils.getInput();
			// 换行符
			String[] inputs = input.split(System.getProperty("line.separator"));
			// 获取当前问题下面有多少答案

			int optionSize = 1;
			int size = question.getOptions().size();
			if (size > 0) {
				optionSize = question.getOptions().get(size - 1).getSort();
			}

			// 创建
			for (int i = 0; i < inputs.length; i++) {
				String string = inputs[i].trim();
				SurveyQuestionOption option = new SurveyQuestionOption();
				option.setName(string.toString());
				option.setSort(optionSize + ((i + 1) * 1000));
				try {
					option.setId(FilesUtil.getUUIDName("", false));
					option.setQuestion(question);
					this.insert(option);
					options.add(option);
				} catch (Exception e) {
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return options;
	}

	@Override
	public List<SurveyQuestionOption> getOptions(String qid) {
		Map<String, Object> params = new HashMap<>();
		params.put("qid", qid);
		return this.query("select", params);
	}

}