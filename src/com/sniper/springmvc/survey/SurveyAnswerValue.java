package com.sniper.springmvc.survey;

import java.util.Map;

public class SurveyAnswerValue {
	/**
	 * 单选，文本
	 */
	private Map<String, String> answer;
	private String password;

	public Map<String, String> getAnswer() {
		return answer;
	}

	public void setAnswer(Map<String, String> answer) {
		this.answer = answer;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

}
