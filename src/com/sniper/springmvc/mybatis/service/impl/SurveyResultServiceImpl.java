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
import com.sniper.springmvc.model.SurveyResult;
import com.sniper.springmvc.model.SurveyResultData;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("surveyResultService")
public class SurveyResultServiceImpl extends BaseServiceImpl<SurveyResult>
		implements SurveyResultService {

	@Resource(name = "surveyResultDao")
	@Override
	public void setDao(BaseDao<SurveyResult> dao) {
		super.setDao(dao);
	}

	/**
	 * 数据分析 查询每一个答案的答题数 ，查询每一个问题的答题数
	 */
	@Override
	public Map<String, Integer> getSurveyResult(Survey survey) {
		Map<String, Integer> answerCount = new HashMap<>();
		if (ValidateUtil.isValid(survey)) {
			// key对的组成部分 ，问题id，加上分题小id,_隔开
			String separator = System.getProperty("line.separator");

			List<SurveyPage> pages = survey.getSurveyPages();

			for (SurveyPage surveyPage : pages) {
				for (SurveyQuestion question : surveyPage.getSq()) {
					switch (question.getType()) {
					// 单选,多选，下拉
					case 1:
					case 2:
					case 3:
					case 4:
						// 查询这道题的答案占比
						answerCount.put(String.valueOf(question.getId()),
								this.searchQuestionOption(question));
						// 查询每个答案
						for (SurveyQuestionOption option : question
								.getOptions()) {
							if (option.isWrited()) {
								int i = this.searchQuestionOption(
										question.getId(), "other_", true);
								answerCount
										.put(question.getId() + "_"
												+ option.getId(), i);
							} else {
								int i = this.searchQuestionOption(
										question.getId(), option.getName(),
										false);
								answerCount
										.put(question.getId() + "_"
												+ option.getId(), i);
							}
						}
						break;
					case 5:
						// 查询这道题的答案占比
						answerCount.put(String.valueOf(question.getId()),
								this.searchQuestionOption(question));
						// 查询每个答案
						for (SurveyQuestionOption option : question
								.getOptions()) {
							int i = this.searchQuestionOption(question.getId(),
									option.getName(), false);
							answerCount.put(
									question.getId() + "_" + option.getId(), i);
						}
						break;
					case 6:
					case 7:
					case 8:
						break;
					// 矩阵填空
					case 11:
						break;
					// 矩阵单选
					case 12:
						String[] leftTitles = question.getMatrixRowTitles()
								.split(separator);
						String[] topTitles = question.getMatrixColTitles()
								.split(separator);

						for (int i = 0; i < leftTitles.length; i++) {
							String firstName = i + "_";
							// 查询横向的答题总数 ，及查询数据库中已制定字符串开头的就是
							answerCount.put(question.getId() + "_" + i, this
									.searchQuestionOption(question.getId(),
											firstName, false));
							for (int j = 0; j < topTitles.length; j++) {
								String name = i + "_" + topTitles[j];
								answerCount.put(
										question.getId() + "_" + i + "_" + j,
										this.searchQuestionOption(
												question.getId(), name, false));
							}
						}
						break;
					// 矩阵多选
					case 13:
						String[] leftTitles1 = question.getMatrixRowTitles()
								.split(separator);
						String[] topTitles1 = question.getMatrixColTitles()
								.split(separator);

						for (int i = 0; i < leftTitles1.length; i++) {

							String firstName = i + "_";
							// 查询横向的答题总数 ，及查询数据库中已制定字符串开头的就是
							answerCount.put(question.getId() + "_" + i, this
									.searchQuestionOption(question.getId(),
											firstName, false));

							for (int j = 0; j < topTitles1.length; j++) {
								String name = i + "_" + j + "_" + topTitles1[j];
								answerCount.put(
										question.getId() + "_" + i + "_" + j,
										this.searchQuestionOption(
												question.getId(), name, false));
							}
						}
						break;
					// 矩阵下拉
					case 14:
						String[] leftTitles2 = question.getMatrixRowTitles()
								.split(separator);
						String[] topTitles2 = question.getMatrixColTitles()
								.split(separator);
						String[] selectTitles2 = question
								.getMatrixSelectOptions().split(separator);

						for (int i = 0; i < leftTitles2.length; i++) {
							for (int j = 0; j < topTitles2.length; j++) {
								String selectName = i + "_" + j + "_";
								// 查询横向的答题总数 ，及查询数据库中已制定字符串开头的就是
								answerCount.put(question.getId() + "_" + i
										+ "_" + j, this.searchQuestionOption(
										question.getId(), selectName, false));

								for (int g = 0; g < selectTitles2.length; g++) {
									String name = i + "_" + j + "_"
											+ selectTitles2[g];
									answerCount.put(question.getId() + "_" + i
											+ "_" + j + "_" + g, this
											.searchQuestionOption(
													question.getId(), name,
													false));
								}
							}
						}
						break;
					case 21:
					case 22:
						break;
					case 31:
						break;
					default:
						break;
					}
				}
			}
		}
		return answerCount;
	}

	/**
	 * 查询问题和答案的答题数
	 * 
	 * @param id
	 *            问题id
	 * @param name
	 *            答案名称
	 * @param b
	 *            精确匹配还是模糊匹配
	 * @return
	 */
	@Override
	public int searchQuestionOption(String qid, String optionName, boolean b) {
		Map<String, Object> params = new HashMap<>();
		params.put("quetion", qid);
		if (b) {
			optionName += "%";
		}
		params.put("answers", optionName);
		return this.pageCount(params);
	}

	/**
	 * 查询问题的答题数
	 * 
	 * @param question
	 * @return
	 */
	@Override
	public int searchQuestionOption(SurveyQuestion question) {
		Map<String, Object> params = new HashMap<>();
		params.put("quetion", question.getId());
		return this.pageCount(params);
	}

	/**
	 * 读取所有答题结果
	 */
	@Override
	public Map<String, List<String>> getAnswer(String sid, String rdid) {
		Map<String, Object> params = new HashMap<>();
		params.put("surveyId", sid);
		params.put("data_id", rdid);
		List<SurveyResult> results = this.query("select", params);
		Map<String, List<String>> answers = new HashMap<>();
		for (SurveyResult surveyResult : results) {
			String qid = String.valueOf(surveyResult.getQuetion());
			if (answers.containsKey(qid)) {
				if (surveyResult.getAnswersText() != null) {
					answers.get(qid).add(surveyResult.getAnswersText());
				}
				if (surveyResult.getAnswers() != null) {
					answers.get(qid).add(surveyResult.getAnswers());
				}

			} else {
				List<String> ss = new ArrayList<>();
				if (surveyResult.getAnswersText() != null) {
					ss.add(surveyResult.getAnswersText());
				}
				if (surveyResult.getAnswers() != null) {
					ss.add(surveyResult.getAnswers());
				}

				answers.put(qid, ss);
			}
		}
		return answers;
	}

	/**
	 * 导出数据
	 */
	@Override
	public Map<String, Map<String, List<String>>> exportResult(
			List<SurveyResultData> datas, String sid) {
		Map<String, Map<String, List<String>>> results = new HashMap<>();
		for (SurveyResultData surveyResultData : datas) {
			results.put(surveyResultData.getId(),
					this.getAnswer(sid, surveyResultData.getId()));
		}
		return results;
	}
}