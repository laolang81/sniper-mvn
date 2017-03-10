package com.sniper.springmvc.survey;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

import com.sniper.springmvc.model.Survey;
import com.sniper.springmvc.model.SurveyPage;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionRules;
import com.sniper.springmvc.model.SurveyResult;
import com.sniper.springmvc.model.SurveyResultData;
import com.sniper.springmvc.mybatis.service.impl.SurveyService;
import com.sniper.springmvc.security.SpringContextUtil;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.HttpRequestUtils;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 问卷答案处理 这里不负责保存数据只是返回处理之后的数据
 * 
 * @author sniper
 * 
 */
public class SurveyUtils {

	private SurveyService surveyService;
	/**
	 * 保存的url
	 */
	private HttpServletRequest request;
	/**
	 * 
	 */
	private String surveyId;
	private String split = "_";
	private String uid = "0";
	private Survey survey;
	/**
	 * 要保存的答案结果
	 */
	private List<SurveyResult> results = new ArrayList<>();
	/**
	 * 答案整合
	 */
	private SurveyResultData resultData = new SurveyResultData();
	/**
	 * 格式化之后的答案
	 */

	private Map<String, List<String>> formatAnswers = new HashMap<>();
	/**
	 * 答案
	 * 
	 * @param surveyService
	 */
	private Map<String, String> answer = new HashMap<>();

	private Map<String, List<String>> errors = new HashMap<>();

	public void setSurveyService(SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
		surveyService = (SurveyService) SpringContextUtil
				.getBean(SurveyService.class);
		survey = surveyService.getSurvey(surveyId);

	}

	public Survey getSurvey() {
		return survey;
	}

	public void setSplit(String split) {
		this.split = split;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setParameters(Map<String, String> parameters) {

		this.answer = parameters;
	}

	public Map<String, List<String>> getErrors() {
		return errors;
	}

	public SurveyResultData getResultData() {
		return resultData;
	}

	public List<SurveyResult> getResults() {
		return results;
	}

	private void setErrors(String id, String string) {
		if (errors.containsKey(id)) {
			errors.get(id).add(string);
		} else {
			List<String> list = new ArrayList<>();
			list.add(string);
			errors.put(String.valueOf(id), list);
		}
	}

	private void addFormatAnswer(String qid, String value) {
		if (formatAnswers.containsKey(qid)) {
			formatAnswers.get(qid).add(value);
		} else {
			List<String> aa = new ArrayList<>();
			aa.add(value);
			formatAnswers.put(qid, aa);
		}
	}

	/**
	 * 初始化 用户信息
	 */
	private void initSurveyResultData() {
		// json 格式化工具

		String userAgent = request.getHeader("User-Agent");
		resultData.setAgent(userAgent);
		Locale local = request.getLocale();
		// 语言环境
		resultData.setLocale(local.getLanguage());
		resultData.setSessionid(uid);
		//
		Cookie[] cookies = request.getCookies();
		if (cookies.length > 0) {
			Map<String, String> cookieMap = new HashMap<>();
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie.getValue());
			}
		}

		// 浏览器
		resultData.setNavigator(HttpRequestUtils.getNavigator(userAgent));
		// 系统
		resultData.setOs(HttpRequestUtils.getOS(userAgent));
		resultData.setcTime(new Date());
		resultData.setUid(uid);
		resultData.setId(FilesUtil.getUUIDName("", false));
		resultData.setIp(HttpRequestUtils.getRealIP(request));

	}

	/**
	 * 拆分答案 把问题id，和答案放在一起
	 */
	private void initFormatAnswer() {
		if (!ValidateUtil.isValid(answer)) {
			return;
		}
		for (Map.Entry<String, String> entry : answer.entrySet()) {

			String key = entry.getKey();
			String value = entry.getValue();
			String[] qids = key.split(this.split);
			String qidStr = qids[0];
			if (value.equals("")) {
				continue;
			}
			switch (qids.length) {
			// 单选，填空，多选
			case 1:
				addFormatAnswer(qidStr, value);
				break;
			// 其他，多项填空
			case 2:
				if (qids[1].equals("other")) {
					if (!value.equals("")) {
						value = "other_" + value;
					}
				} else {
					value = qids[1] + "_" + value;
				}
				addFormatAnswer(qidStr, value);
				break;
			// 矩阵
			case 3:
				value = qids[1] + "_" + qids[2] + "_" + value;
				addFormatAnswer(qidStr, value);
				break;

			default:
				break;
			}

		}
	}

	public void run() {
		this.initSurveyResultData();
		this.initFormatAnswer();
		// System.out.println("处理的答案");
		// System.out.println(this.formatAnswers);

		List<SurveyPage> pages = survey.getSurveyPages();

		for (SurveyPage p : pages) {
			List<SurveyQuestion> questions = p.getSq();
			for (SurveyQuestion q : questions) {
				// 数据检测
				SurveyQuestionRules rules = q.getRules();
				if (rules != null) {

					// 先检查是否存在
					checkRequire(q);

					// 检查email
					checkEmail(q);

					// 先检查是url
					checkUrl(q);

					// 先检查是整数
					checkInt(q);
					// 字符串长短
					checkLength(q, rules);
					// 检查数字大小
					checkSize(q, rules);

				}
				// 数据保存到对象中,但是不会保存在数据库中
				String qidStr = String.valueOf(q.getId());
				if (formatAnswers.containsKey(qidStr)) {

					List<String> saa = formatAnswers.get(qidStr);
					SurveyResult result = new SurveyResult();
					// 回答的问题
					result.setQuetion(q.getId());
					// 回答的答案
					result.setUid(uid);
					result.setId(FilesUtil.getUUIDName("", false));
					result.setSurveyId(surveyId);

					switch (q.getType()) {
					case 1:
					case 2:
						// 单选操作,支循环一次
						result.setAnswers(saa.get(0));
						results.add(result);
						break;
					case 3:
					case 4:
						// 多选操作
						for (String as : saa) {
							// 每个答案单独保存
							if (as.indexOf(",") > -1) {
								String[] ass = as.split(",");
								for (int i = 0; i < ass.length; i++) {
									SurveyResult result2 = (SurveyResult) DataUtil
											.deeplyCopy(result);
									result2.setAnswers(ass[i]);
									results.add(result2);
								}
							} else {
								// 保存其他
								result.setAnswers(as);
								results.add(result);
							}
						}
						break;
					// 下拉
					case 5:
						// 单项填空
						result.setAnswers(StringUtils.join(saa, ""));
						results.add(result);
						break;
					case 6:
						// 单项多行填空
					case 7:
						result.setAnswersText(StringUtils.join(saa, ""));
						results.add(result);
						break;
					// 文件提
					case 31:
						result.setAnswers(StringUtils.join(saa, ""));
						results.add(result);
						break;
					// 多项填空
					case 8:
						String[] names = q.getName().split("_");
						StringBuffer namesBuffer = new StringBuffer();
						int b = 0;
						if (q.getName().startsWith("_")) {
							namesBuffer.append(saa.get(b));
							b++;
						}
						for (int a = 0; a < names.length; a++) {
							namesBuffer.append(names[a]);
							// 末尾的_不处理
							if (a < names.length - 1) {
								namesBuffer.append(HtmlUtils.htmlEscape(saa
										.get(b).substring(
												saa.get(b).indexOf("_") + 1)));
							}
							b++;
						}
						if (q.getName().endsWith("_")) {
							namesBuffer.append(saa.get(b));
						}
						result.setAnswers(namesBuffer.toString());
						results.add(result);
						break;
					// 矩阵填空，矩阵单选，多选，下拉
					// 他们都有前缀
					case 11:
					case 12:
					case 13:
					case 14:
						for (String sa : saa) {
							SurveyResult result2 = (SurveyResult) DataUtil
									.deeplyCopy(result);
							result2.setAnswers(HtmlUtils.htmlEscape(sa));
							results.add(result2);
						}
						break;
					case 21:
					case 22:
						break;
					//

					default:
						break;
					}
				}

			}

		}
	}

	private void checkRequire(SurveyQuestion q) {
		if (q.getRules().isRequired()
				&& ValidateUtil.isValid(formatAnswers.get(String.valueOf(q
						.getId())))
				&& !ValidateUtil.isValid(formatAnswers.get(
						String.valueOf(q.getId())).get(0))) {

			setErrors(q.getId(), q.getName() + ": 必须");
		}
	}

	private void checkInt(SurveyQuestion q) {

		if (q.getRules().isNumber()
				&& ValidateUtil.isValid(formatAnswers.get(String.valueOf(q
						.getId())))) {
			if (formatAnswers.get(String.valueOf(q.getId())).size() == 1) {
				if (!ValidateUtil.isInt(formatAnswers.get(
						String.valueOf(q.getId())).get(0))) {
					setErrors(q.getId(), q.getName() + ": 必须是数字");
				}
			}
		}
	}

	private void checkEmail(SurveyQuestion q) {

		if (q.getRules().isEmail()
				&& ValidateUtil.isValid(formatAnswers.get(String.valueOf(q
						.getId())))) {
			if (formatAnswers.get(String.valueOf(q.getId())).size() == 1) {
				if (!ValidateUtil.isEmail(formatAnswers.get(
						String.valueOf(q.getId())).get(0))) {
					setErrors(q.getId(), q.getName() + ": 必须是Email");
				}
			}
		}
	}

	private boolean checkUrl(SurveyQuestion q) {

		if (q.getRules().isUrl()
				&& ValidateUtil.isValid(formatAnswers.get(String.valueOf(q
						.getId())))) {
			if (formatAnswers.get(String.valueOf(q.getId())).size() == 1) {
				if (!ValidateUtil.isUrl(formatAnswers.get(
						String.valueOf(q.getId())).get(0))) {
					setErrors(q.getId(), q.getName() + ": 必须是URL");
				}
			}
		}
		return false;
	}

	private void checkLength(SurveyQuestion q, SurveyQuestionRules rules) {

		if (q.getRules().isLength()
				&& ValidateUtil.isValid(formatAnswers.get(String.valueOf(q
						.getId())))) {
			if (rules.getMinLength() > 0) {
				if (formatAnswers.get(String.valueOf(q.getId())).size() < rules
						.getMinLength()) {
					setErrors(q.getId(),
							q.getName() + ": 答案保持在" + rules.getMinLength()
									+ "之上");
				}
			}

			if (rules.getMaxLength() > 0) {
				if (formatAnswers.get(String.valueOf(q.getId())).size() > rules
						.getMaxLength()) {
					setErrors(q.getId(),
							q.getName() + ": 答案保持在" + rules.getMinLength()
									+ "之下");
				}
			}

		}
	}

	private void checkSize(SurveyQuestion q, SurveyQuestionRules rules) {

		if (q.getRules().isSize()
				&& ValidateUtil.isValid(formatAnswers.get(String.valueOf(q
						.getId())))) {

			if (rules.getMin() > 0) {
				String a = formatAnswers.get(String.valueOf(q.getId())).get(0);
				if (ValidateUtil.isInt(a)) {
					int b = Integer.valueOf(a);
					if (b < rules.getMin()) {
						setErrors(q.getId(),
								q.getName() + ": 答案必须大于" + rules.getMin());
					}
				}
			}

			if (rules.getMax() > 0) {
				String a = formatAnswers.get(String.valueOf(q.getId())).get(0);
				if (ValidateUtil.isInt(a)) {
					int b = Integer.valueOf(a);
					if (b > rules.getMax()) {
						setErrors(q.getId(),
								q.getName() + ": 答案必须小于" + rules.getMax());
					}
				}
			}
		}
	}

	public static void main(String[] args) {
		Map<Integer, String> map = new HashMap<>();

		map.put(0, "a");
		map.put(1, "b");

		System.out.println(map.containsKey(1));
	}

}
