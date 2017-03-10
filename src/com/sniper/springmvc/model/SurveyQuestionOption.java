package com.sniper.springmvc.model;


public class SurveyQuestionOption extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String title;
	// 可填写
	private boolean writed;
	private int sort = 0;
	private boolean checked;
	// 填写之后跳到那个提
	private Integer goid;
	// 默认值
	private String defaultvalue;
	// 答案
	private String answer;

	private SurveyQuestion question;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isWrited() {
		return writed;
	}

	public void setWrited(boolean writed) {
		this.writed = writed;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Integer getGoid() {
		return goid;
	}

	public void setGoid(Integer goid) {
		this.goid = goid;
	}

	public String getDefaultvalue() {
		return defaultvalue;
	}

	public void setDefaultvalue(String defaultvalue) {
		this.defaultvalue = defaultvalue;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public SurveyQuestion getQuestion() {
		return question;
	}

	public void setQuestion(SurveyQuestion question) {
		this.question = question;
	}

}
