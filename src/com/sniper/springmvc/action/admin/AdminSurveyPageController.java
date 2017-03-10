package com.sniper.springmvc.action.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.model.SurveyPage;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.model.SurveyQuestionRules;
import com.sniper.springmvc.mybatis.service.impl.SurveyPageService;
import com.sniper.springmvc.mybatis.service.impl.SurveyQuestionOptionService;
import com.sniper.springmvc.mybatis.service.impl.SurveyQuestionRulesService;
import com.sniper.springmvc.mybatis.service.impl.SurveyQuestionServie;
import com.sniper.springmvc.mybatis.service.impl.SurveyService;
import com.sniper.springmvc.survey.SurveyInsertUtil;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 正常表单提交的数据 null的属性会被过滤掉,而这里却被提交了
 * 
 * @author sniper
 * 
 */
@Controller
@RequestMapping("${adminPath}/admin-survey-page")
public class AdminSurveyPageController extends AdminBaseController {

	@Resource
	private SurveyService surveyService;

	@Resource
	private SurveyPageService pageService;

	@Resource
	private SurveyQuestionServie questionServie;

	@Resource
	private SurveyQuestionOptionService optionService;

	@Resource
	private SurveyQuestionRulesService rulesService;

	@RequiresPermissions("admin:survey:page")
	@ResponseBody
	@RequestMapping(value = "/")
	public Map<String, Object> index(SurveyInsertUtil util) {

		Map<String, Object> ajaxResult = new HashMap<>();

		ajaxResult.put("code", 200);
		ajaxResult.put("error", "");

		if (!ValidateUtil.isValid(util.getType())) {
			ajaxResult.put("code", 500);
			ajaxResult.put("error", "参数错误");
			return ajaxResult;
		}
		String type = util.getType();
		String[] types = type.split("_");

		if (types.length != 2) {
			ajaxResult.put("code", 404);
			ajaxResult.put("error", "参数缺失");
			return ajaxResult;
		}

		ajaxResult.put("name", types[0]);
		ajaxResult.put("type", types[1]);

		switch (types[0]) {
		case "page":
			List<SurveyPage> pages = page(util, ajaxResult, types);
			ajaxResult.put("page", pages);
			break;
		case "question":
			List<SurveyQuestion> questions = question(util, ajaxResult, types);
			ajaxResult.put("question", questions);
			break;
		case "option":
			List<SurveyQuestionOption> options = option(util, ajaxResult, types);
			ajaxResult.put("option", options);
			break;

		default:
			break;
		}

		return ajaxResult;
	}

	/**
	 * 页面操作
	 * 
	 * @param util
	 * @param ajaxResult
	 * @param types
	 * @return
	 */
	private List<SurveyPage> page(SurveyInsertUtil util,
			Map<String, Object> ajaxResult, String[] types) {
		List<SurveyPage> pages = new ArrayList<>();
		switch (types[1]) {
		// 批量添加
		case "plus":
			try {
				if (ValidateUtil.isValid(util.getInput())) {
					pages = pageService.executePlus(util);
				}

			} catch (Exception e) {
				e.printStackTrace();
				ajaxResult.put("error", e.getMessage());
				ajaxResult.put("code", 500);
			}
			break;
		case "up":
			// 要获取他上面的页面
			pages = pageService.executeSort(util.getPage(), "<=");
			break;
		case "down":
			pages = pageService.executeSort(util.getPage(), ">=");
			break;
		// 删除
		case "minus":
			pages.add(util.getPage());
			pageService.delete(util.getPage().getId());

			break;
		case "copy":
			String copyPid = util.getPage().getId();
			pages.add(util.getPage());
			SurveyPage page = pageService.get(util.getPage().getId());
			SurveyPage copyPage = (SurveyPage) DataUtil.deeplyCopy(page);
			copyPage.setSort(util.getPage().getSort() + 100);
			pageService.insert(copyPage);
			for (SurveyQuestion question : copyPage.getSq()) {
				SurveyQuestionRules rules = question.getRules();
				rulesService.insert(rules);
				question.setRules(rules);
				questionServie.insert(question);
				for (SurveyQuestionOption option : question.getOptions()) {
					optionService.insert(option);
				}
			}
			// 设置复制的新id
			util.getPage().setId(copyPage.getId());
			SurveyPage page2 = pageService.getJsonPage(util);
			util.getPage().setId(copyPid);
			pages.add(page2);
			break;
		case "check":
			try {
				SurveyPage surveyPage = pageService.get(util.getPage().getId());
				surveyPage.setName(util.getPage().getName().trim());
				surveyPage.setNote(util.getPage().getNote());
				pageService.update(surveyPage);
				ajaxResult.put("page", surveyPage);
			} catch (Exception e) {
				e.printStackTrace();
				ajaxResult.put("error", e.getMessage());
				ajaxResult.put("code", 500);
			}
			break;
		}
		return pages;
	}

	/**
	 * 问题操作
	 * 
	 * @param util
	 * @param ajaxResult
	 * @param types
	 * @return
	 */
	private List<SurveyQuestion> question(SurveyInsertUtil util,
			Map<String, Object> ajaxResult, String[] types) {
		List<SurveyQuestion> questions = new ArrayList<>();
		switch (types[1]) {
		// 批量添加
		case "plus":
			try {
				if (ValidateUtil.isValid(util.getInput())) {
					questions = questionServie.executePlus(util);
				}
			} catch (Exception e) {
				e.printStackTrace();
				ajaxResult.put("error", e.getMessage());
				ajaxResult.put("code", 500);
			}
			break;
		// 删除
		case "minus":
			questions.add(util.getQuestion());
			questionServie.delete(util.getQuestion().getId());
			break;
		case "check":
			SurveyQuestion question2 = questionServie.get(util.getQuestion()
					.getId());
			int sort = question2.getSort();
			BeanUtils.copyProperties(util.getQuestion(), question2);
			question2.setSort(sort);
			questionServie.update(question2);
			questions.add(util.getQuestion());
			break;
		case "copy":
			questions.add(util.getQuestion());
			// 返回复制的数据
			SurveyQuestion question = questionServie.executeCopy(util
					.getQuestion());
			SurveyQuestionRules rules = question.getRules();
			rulesService.insert(rules);
			question.setRules(rules);

			questionServie.insert(question);

			for (SurveyQuestionOption option : question.getOptions()) {
				option.setSort(option.getSort() + 100);
				option.setQuestion(question);
				optionService.insert(option);
			}
			// 重新设置问题
			for (SurveyQuestionOption option : question.getOptions()) {
				option.setQuestion(util.getQuestion());

			}
			question.setPage(util.getQuestion().getPage());
			questions.add(question);
			break;
		case "up":
			questions = questionServie.executeSort(util.getQuestion(), "<=");
			break;
		case "down":
			questions = questionServie.executeSort(util.getQuestion(), ">=");
			break;
		default:

			break;
		}
		return questions;
	}

	/**
	 * 答案操作
	 * 
	 * @param util
	 * @param ajaxResult
	 * @param types
	 * @return
	 */
	private List<SurveyQuestionOption> option(SurveyInsertUtil util,
			Map<String, Object> ajaxResult, String[] types) {
		// 返回的列表
		List<SurveyQuestionOption> options = new ArrayList<>();
		switch (types[1]) {
		// 批量添加
		case "plus":
			try {
				if (ValidateUtil.isValid(util.getInput())) {
					options = optionService.executePlus(util);
				}

			} catch (Exception e) {
				e.printStackTrace();
				ajaxResult.put("error", e.getMessage());
				ajaxResult.put("code", 500);
			}
			break;
		// 删除
		case "minus":
			options.add(util.getOption());
			optionService.delete(util.getOption().getId());
			break;
		case "check":
			// 一般默认只处理修改
			optionService.executeCheck(util.getOption());
			options.add(util.getOption());
			break;
		case "copy":
			options.add(util.getOption());
			SurveyQuestionOption option = optionService.executeCopy(util
					.getOption());
			optionService.insert(option);
			option.setQuestion(util.getOption().getQuestion());
			options.add(option);
			break;
		case "up":
			options = optionService.executeSort(util.getOption(), "<=");
			break;
		case "down":
			options = optionService.executeSort(util.getOption(), ">=");
			break;
		}
		return options;
	}
}
