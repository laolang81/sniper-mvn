package com.sniper.springmvc.action.survey;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sniper.springmvc.action.RootController;
import com.sniper.springmvc.freemarker.FreeMarkerUtil;
import com.sniper.springmvc.model.Survey;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.model.SurveyResult;
import com.sniper.springmvc.model.SurveyResultData;
import com.sniper.springmvc.mybatis.service.impl.SurveyQuestionOptionService;
import com.sniper.springmvc.mybatis.service.impl.SurveyQuestionServie;
import com.sniper.springmvc.mybatis.service.impl.SurveyResultDataService;
import com.sniper.springmvc.mybatis.service.impl.SurveyResultService;
import com.sniper.springmvc.mybatis.service.impl.SurveyService;
import com.sniper.springmvc.survey.SurveyAnalysisUtil;
import com.sniper.springmvc.survey.SurveyAnswerValue;
import com.sniper.springmvc.survey.SurveyTemplateUtil;
import com.sniper.springmvc.survey.SurveyUtils;
import com.sniper.springmvc.utils.ValidateUtil;

import freemarker.template.TemplateHashModel;

@Controller
@RequestMapping("/survey")
public class SurveyController extends RootController {

	private static TemplateHashModel analysisModel = FreeMarkerUtil
			.getFreeMarkerStaticModel(SurveyAnalysisUtil.class);

	private static TemplateHashModel surveyModel = FreeMarkerUtil
			.getFreeMarkerStaticModel(SurveyTemplateUtil.class);
	private final static String surveyCookieKey = "setSurvey";
	@Resource
	private SurveyService surveyService;

	@Resource
	SurveyQuestionServie questionServie;

	@Resource
	SurveyQuestionOptionService optionService;

	@Resource
	private SurveyResultDataService resultDataService;

	@Resource
	private SurveyResultService resultService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Map<String, Object> map,
			@RequestParam(value = "id", required = false) String id,
			HttpServletResponse response) {
		// 是否允许填写问卷
		boolean allow = true;
		if (id == null) {
			return "survey/index/index.ftl";
		}

		// 所有问卷的内容
		Survey survey = surveyService.getSurvey(id);

		if (ValidateUtil.isValid(survey.getUrl())) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX
					+ survey.getUrl();
		}

		String sessionid = "";
		try {
			sessionid = request.getSession(false).getId();
		} catch (Exception e) {
			e.printStackTrace();
			sessionid = request.getSession().getId();
		}
		// 设置cookie
		String seid = checkCookieGetSessionID(id, sessionid, response, false);

		if (!seid.equals("")) {
			allow = false;
		}

		if (survey.getMulti()) {
			allow = true;
		}

		Calendar calendar = Calendar.getInstance();
		if (survey.getStartDate() != null) {
			// before 判断 1 是否在2的前边
			if (!survey.getStartDate().before(calendar.getTime())) {
				allow = false;
			}
		}

		if (survey.getEntDate() != null) {
			if (survey.getEntDate().before(calendar.getTime())) {
				allow = false;
			}
		}
		if (survey.getLocked()) {
			allow = false;
		}
		// 查看当前用户提交的数据
		map.put("survey", survey);
		map.put("surveyModel", surveyModel);

		if (!survey.getMulti()) {
			SurveyResultData data = resultDataService
					.getSurveyResultDataBySession(id, sessionid);
			// 数据回显
			if (ValidateUtil.isValid(data)) {
				Map<String, List<String>> answers = resultService.getAnswer(id,
						data.getId());
				map.put("answers", answers);
				return "survey/index/answerback.ftl";
			}
		}
		map.put("allow", allow);
		return "survey/index/answer.ftl";
	}

	/**
	 * 提交问卷
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String index(Map<String, Object> map,
			@RequestParam(value = "id") String id, SurveyAnswerValue answer,
			HttpServletResponse response) {

		Map<String, List<String>> resultErrors = new HashMap<>();
		Survey survey = surveyService.getSurvey(id);
		map.put("survey", survey);
		map.put("surveyModel", surveyModel);
		map.put("allow", true);

		if (!survey.getPassword().equals("")) {
			if (!answer.getPassword().equals(survey.getPassword())) {
				setResultError(resultErrors, "密码错误");
				map.put("resultErrors", resultErrors);
				return "survey/index/answer.ftl";
			}
		}

		if (survey.getPeopleMaxNum() > 0
				&& survey.getPeopleNum() > survey.getPeopleMaxNum()) {

			setResultError(resultErrors, "人数已达上限");
			map.put("resultErrors", resultErrors);
			return "survey/index/answer.ftl";
		}
		// 所有问卷的内容
		SurveyUtils surveyUtils = new SurveyUtils();
		surveyUtils.setRequest(request);
		surveyUtils.setSurveyId(id);
		surveyUtils.setParameters(answer.getAnswer());
		surveyUtils.run();
		List<SurveyResult> results = surveyUtils.getResults();
		resultErrors = surveyUtils.getErrors();
		// 数据保存
		SurveyResultData resultData = surveyUtils.getResultData();

		String sessionid = "";
		try {
			sessionid = request.getSession(false).getId();
		} catch (Exception e) {
			sessionid = request.getSession().getId();
		}

		if (resultErrors.size() == 0) {
			// 设置cookie
			String seid = checkCookieGetSessionID(id, sessionid, response, true);
			// 判断用户是否已经提交
			if (seid.equals("") || survey.getMulti()) {
				resultData.setSurvey(survey);
				resultDataService.insert(resultData);
				for (SurveyResult surveyResult : results) {
					Subject subject = SecurityUtils.getSubject();
					// 插入用户账号信息
					if (subject != null && subject.isAuthenticated()) {
						resultData.setUid(subject.getPrincipal().toString());
					}
					surveyResult.setResultData(resultData);
					resultService.insert(surveyResult);
				}
				survey.setPeopleNum(survey.getPeopleNum() + 1);
				surveyService.update(survey);
			}

			if (survey.getMulti()) {
				return "survey/index/answer.ftl";
			}

			if (ValidateUtil.isValid(seid)) {
				sessionid = seid;
			}
			SurveyResultData data = resultDataService
					.getSurveyResultDataBySession(id, sessionid);

			setResultError(resultErrors, "参加问卷成功");
			map.put("resultErrors", resultErrors);

			if (ValidateUtil.isValid(data)) {
				Map<String, List<String>> answers = resultService.getAnswer(id,
						data.getId());
				map.put("answers", answers);
			}

			return "survey/index/answerback.ftl";

		}
		map.put("resultErrors", resultErrors);
		return "survey/index/answer.ftl";
	}

	private void setResultError(Map<String, List<String>> resultErrors,
			String error) {
		if (resultErrors.containsKey("0")) {
			resultErrors.get("0").add(error);
		} else {
			List<String> errors = new ArrayList<>();
			errors.add(error);
			resultErrors.put("0", errors);
		}
	}

	@RequestMapping("analysis")
	public String analysis(Map<String, Object> map,
			@RequestParam(value = "id") String id) {
		// 所有问卷的内容
		Survey survey = surveyService.getSurvey(id);
		if (!ValidateUtil.isValid(survey) || survey.getOpenResult() == false) {
			return "survey/index/index.ftl";
		}
		Map<String, Integer> answersCount = resultService
				.getSurveyResult(survey);

		map.put("answersCount", answersCount);
		map.put("survey", survey);
		map.put("analysisModel", analysisModel);
		return "survey/index/analysis.ftl";
	}

	/**
	 * 如果问卷一填写返回当时问卷的sessionid 保存key 问卷id=》sessionid
	 * 
	 * @param id
	 * @param sessionid
	 * @param response
	 * @param put
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String checkCookieGetSessionID(String id, String sessionid,
			HttpServletResponse response, boolean put) {
		// 问卷id
		String ssid = String.valueOf(id);
		Cookie[] cookies = request.getCookies();
		if (!ValidateUtil.isValid(cookies)) {
			return "";
		}

		ObjectMapper mapper = new ObjectMapper();
		for (Cookie c : cookies) {
			if (c.getName().equals(surveyCookieKey)) {
				Map<String, String> map = new HashMap<>();
				try {
					map = mapper.readValue(c.getValue(), Map.class);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				if (map.containsKey(ssid)) {
					return map.get(ssid);
				} else {
					if (put) {
						map.put(ssid, sessionid);
						try {
							Cookie cookie = new Cookie(surveyCookieKey,
									mapper.writeValueAsString(map));
							cookie.setMaxAge(3600);
							response.addCookie(cookie);
						} catch (JsonProcessingException e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				if (put) {
					Map<String, String> list = new HashMap<>();
					list.put(ssid, sessionid);
					try {
						Cookie cookie = new Cookie(surveyCookieKey,
								mapper.writeValueAsString(list));
						cookie.setMaxAge(3600);
						response.addCookie(cookie);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return "";
	}

	/**
	 * 获取当前提的答案，总投票数，每道题的投票数
	 */
	@ResponseBody
	@RequestMapping("optionAnswer")
	public Map<String, Object> optionAnswer(@RequestParam("qid") String id) {

		Map<String, Object> answers = new HashMap<>();

		// 获取当前提的所有答案
		SurveyQuestion question = questionServie.get(id);
		Map<String, Integer> answerCount = new HashMap<>();
		switch (question.getType()) {
		case 1:
		case 2:
		case 3:
		case 4:
			// 查询每个答案
			for (SurveyQuestionOption option : question.getOptions()) {
				int i = 0;
				if (option.isWrited()) {
					i = resultService.searchQuestionOption(question.getId(),
							"other_", true);

				} else {
					i = resultService.searchQuestionOption(question.getId(),
							option.getName(), false);
				}
				answerCount.put(option.getId(), i);
			}
			break;

		default:
			break;
		}
		// 获取所有答案
		List<SurveyQuestionOption> options = optionService.getOptions(id);
		// 查询没打答案的投票数,下面不能处理矩阵结果
		for (SurveyQuestionOption option : options) {
			resultService.searchQuestionOption(id, option.getName(), false);
		}

		return answers;
	}
}