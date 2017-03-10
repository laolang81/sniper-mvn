package com.sniper.springmvc.model;


/**
 * 此页面是保存每个问题及其答案
 * 
 * @author sniper
 * 
 */
public class SurveyResult extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String id;
	// 被投票id或者人
	private String surveyId = "0";
	// 投票人可为空
	private String uid = "0";
	// 得票人数
	private int num = 0;
	// 问题id,因为问题的标题都是保存在数据库中的所以直接使用id即可
	private String quetion = "0";
	// 答案就不一样了,因为答案可以自己填写所以不能使用int
	private String answers;
	private String answersText;

	private SurveyResultData resultData;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSurveyId() {
		return surveyId;
	}

	public void setSurveyId(String surveyId) {
		this.surveyId = surveyId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getQuetion() {
		return quetion;
	}

	public void setQuetion(String quetion) {
		this.quetion = quetion;
	}

	public void setAnswers(String answers) {
		this.answers = answers;
	}

	public String getAnswers() {
		return answers;
	}

	public String getAnswersText() {
		return answersText;
	}

	public void setAnswersText(String answersText) {
		this.answersText = answersText;
	}

	public SurveyResultData getResultData() {
		return resultData;
	}

	public void setResultData(SurveyResultData resultData) {
		this.resultData = resultData;
	}

}
