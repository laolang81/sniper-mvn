package com.sniper.springmvc.model;

import java.util.ArrayList;
import java.util.List;

public class SurveyQuestion extends BaseEntity {

	private static final long serialVersionUID = 1L;
	private String id;
	// 名称
	private String name;
	// 填写提示
	private String title;
	private Integer type = 1;
	private int sort = 0;
	// 当选择那个题的时候显示此题
	private String relation;
	// 其他样式, 文本框,;下拉框
	private Integer otherStyle;
	// 矩阵集合行标题
	private String matrixRowTitles;
	// 矩阵右侧集合航标题
	private String matrixRowRightTitles;

	// 矩阵列标题
	private String matrixColTitles;
	private String matrixColValues;

	// 矩阵下拉选集
	private String matrixSelectOptions;
	// optional 表示是否必须
	private SurveyPage page;

	private List<SurveyQuestionOption> options = new ArrayList<>();

	private SurveyQuestionRules rules;

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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public SurveyPage getPage() {
		return page;
	}

	public void setPage(SurveyPage page) {
		this.page = page;
	}

	public List<SurveyQuestionOption> getOptions() {
		return options;
	}

	public void setOptions(List<SurveyQuestionOption> options) {
		this.options = options;
	}

	public SurveyQuestionRules getRules() {
		return rules;
	}

	public void setRules(SurveyQuestionRules rules) {
		this.rules = rules;
	}

	public Integer getOtherStyle() {
		return otherStyle;
	}

	public void setOtherStyle(Integer otherStyle) {
		this.otherStyle = otherStyle;
	}

	public String getMatrixRowTitles() {
		return matrixRowTitles;
	}

	public void setMatrixRowTitles(String matrixRowTitles) {
		this.matrixRowTitles = matrixRowTitles;
	}

	public String getMatrixColTitles() {
		return matrixColTitles;
	}

	public void setMatrixColTitles(String matrixColTitles) {
		this.matrixColTitles = matrixColTitles;
	}

	public String getMatrixSelectOptions() {
		return matrixSelectOptions;
	}

	public void setMatrixSelectOptions(String matrixSelectOptions) {
		this.matrixSelectOptions = matrixSelectOptions;
	}

	public String getMatrixRowRightTitles() {
		return matrixRowRightTitles;
	}

	public void setMatrixRowRightTitles(String matrixRowRightTitles) {
		this.matrixRowRightTitles = matrixRowRightTitles;
	}

	public String getMatrixColValues() {
		return matrixColValues;
	}

	public void setMatrixColValues(String matrixColValues) {
		this.matrixColValues = matrixColValues;
	}

}
