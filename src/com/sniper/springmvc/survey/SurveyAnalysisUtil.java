package com.sniper.springmvc.survey;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.utils.ValidateUtil;

public class SurveyAnalysisUtil {
	/**
	 * 答案前缀
	 */
	public final static String answerPrefix = "answer";
	private final static DecimalFormat df = new DecimalFormat("#.##");

	public static String answer(SurveyQuestion question,
			Map<String, Integer> answers) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<div class=\"q_title\">");
		buffer.append(question.getName());
		if (question.getRules().isRequired()) {
			buffer.append("<span class=\"text-danger\">&nbsp;&nbsp;*</span>");
		}
		buffer.append("</div>");
		buffer.append("<div class=\"q_body\">");

		List<SurveyQuestionOption> options = question.getOptions();

		switch (question.getType()) {
		// 横向单选
		case 1:
			buffer.append("<ul class=\"list-inline\">");
			for (SurveyQuestionOption option : options) {
				buffer.append("<li>");
				HorizontalRadio(question, option, buffer, answers);
				buffer.append("</li>");
			}
			buffer.append("</ul>");
			break;
		// 竖向单选
		case 2:
			buffer.append("<ul>");
			for (SurveyQuestionOption option : options) {
				buffer.append("<li>");
				VerticalRadio(question, option, buffer, answers);
				buffer.append("</li>");
			}
			buffer.append("</ul>");
			break;
		// 横向多选
		case 3:
			buffer.append("<ul class=\"list-inline\">");
			for (SurveyQuestionOption option : options) {
				buffer.append("<li>");
				HorizontalMulti(question, option, buffer, answers);
				buffer.append("</li>");
			}
			buffer.append("</ul>");
			break;
		// 竖向多选
		case 4:
			buffer.append("<ul>");
			for (SurveyQuestionOption option : options) {
				buffer.append("<li>");
				VerticalMulti(question, option, buffer, answers);
				buffer.append("</li>");
			}
			buffer.append("</ul>");
			break;
		// 下滑菜单
		case 5:
			buffer.append("<ul>");
			for (SurveyQuestionOption option : options) {
				SelectOption(question, option, buffer, answers);
			}
			buffer.append("</ul>");
			break;
		// 单项填空
		case 6:
			SingleInput(question, buffer, answers);
			break;
		// 单项多行填空
		case 7:
			MulitInput(question, buffer, answers);
			break;
		// 多选填空
		case 8:
			MulitSingleInput(question, buffer, answers);
			break;
		// 矩阵填空
		case 11:
			MatrixText(question, buffer, answers);
			break;
		// 矩阵单选
		case 12:
			MatrixRadio(question, buffer, answers);
			break;
		// 矩阵多选
		case 13:
			MatrixCheckBox(question, buffer, answers);
			break;
		// 矩阵下拉
		case 14:
			MatrixSelect(question, buffer, answers);
			break;
		// 投票单选
		case 21:

			break;
		// 投票多选
		case 22:
			break;
		// 文件题
		case 31:
			// 文件提就是填空
			FileInput(question, buffer, answers);
			break;

		default:
			break;
		}

		buffer.append("</div>");
		return buffer.toString();
	}

	private static void SetID(SurveyQuestion question,
			SurveyQuestionOption option, StringBuffer buffer) {
		buffer.append(" id=\"o_");
		buffer.append(question.getId());
		if (option != null) {
			buffer.append("_");
			buffer.append(option.getId());
		}
		buffer.append("\" ");
	}

	private static void SetName(SurveyQuestion question, StringBuffer buffer,
			String suffix) {

		buffer.append("name=\"");
		buffer.append(answerPrefix);
		buffer.append("[");
		buffer.append(question.getId());
		if (ValidateUtil.isValid(suffix)) {
			buffer.append("_");
			buffer.append(suffix);
		}
		buffer.append("]");
		buffer.append("\" ");
	}

	private static void SetValue(SurveyQuestionOption option,
			StringBuffer buffer) {
		buffer.append("value=\"");
		buffer.append(option.getName());
		buffer.append("\" ");
	}

	/**
	 * 答案会先
	 * 
	 * @param option
	 * @param buffer
	 * @param answer
	 */
	private static void SetChecked(SurveyQuestionOption option,
			StringBuffer buffer, Map<String, Integer> answers) {
	}

	/**
	 * 获取组装的class
	 * 
	 * @param question
	 * @param buffer
	 */
	private static void SetClass(SurveyQuestion question, StringBuffer buffer) {

		buffer.append("class=\"");
		if (question.getType() == 6 || question.getType() == 7) {
			buffer.append("form-control ");
		}
		buffer.append("\" ");
	}

	/**
	 * 横向单选
	 * 
	 * @param question
	 * @param rules
	 * @return
	 */
	private static void HorizontalRadio(SurveyQuestion question,
			SurveyQuestionOption option, StringBuffer buffer,
			Map<String, Integer> answers) {

		buffer.append("<label class=\"radio-inline\">");
		buffer.append("<input type=\"radio\" ");
		SetID(question, option, buffer);
		SetName(question, buffer, null);
		SetValue(option, buffer);
		SetClass(question, buffer);
		if (option.getName().equals(answers.get(0))) {
			buffer.append(" checked ");
		}
		SetChecked(option, buffer, answers);
		buffer.append("> ");
		buffer.append(option.getName());
		buffer.append("&nbsp;<span class=\"label label-success\">");
		String colName = question.getId() + "_" + option.getId();
		int col = answers.get(colName);
		buffer.append(answers.get(colName));
		String totalKey = question.getId();
		int total = answers.get(totalKey);
		buffer.append("/");
		buffer.append(total);
		if (total > 0) {
			buffer.append("(");
			float c = (Float.valueOf(col) / Float.valueOf(total));
			buffer.append(df.format(c * 100));
			buffer.append("%");
			buffer.append(")");
		}
		buffer.append("</span>");
		buffer.append("</label>");
	}

	/**
	 * 竖向单选
	 * 
	 * @param question
	 * @param option
	 * @return
	 */
	private static void VerticalRadio(SurveyQuestion question,
			SurveyQuestionOption option, StringBuffer buffer,
			Map<String, Integer> answers) {

		buffer.append("<div class=\"radio\">");
		buffer.append("<label>");
		buffer.append("<input type=\"radio\" ");
		SetID(question, option, buffer);
		SetName(question, buffer, null);
		SetValue(option, buffer);
		SetClass(question, buffer);
		if (option.getName().equals(answers.get(0))) {
			buffer.append(" checked ");
		}
		buffer.append(">");
		buffer.append(option.getName());
		buffer.append("&nbsp;<span class=\"label label-success\">");
		String colName = question.getId() + "_" + option.getId();
		int col = answers.get(colName);
		buffer.append(answers.get(colName));
		String totalKey = question.getId();
		int total = answers.get(totalKey);
		buffer.append("/");
		buffer.append(total);
		if (total > 0) {
			buffer.append("(");
			float c = (Float.valueOf(col) / Float.valueOf(total));
			buffer.append(df.format(c * 100));
			buffer.append("%");
			buffer.append(")");
		}
		buffer.append("</span>");
		buffer.append("</label>");
		buffer.append("</div>");
	}

	/**
	 * 横向多选
	 * 
	 * @param question
	 * @param option
	 * @return
	 */
	private static void HorizontalMulti(SurveyQuestion question,
			SurveyQuestionOption option, StringBuffer buffer,
			Map<String, Integer> answers) {

		buffer.append("<label class=\"checkbox-inline\">");
		buffer.append("<input type=\"checkbox\" ");
		SetID(question, option, buffer);
		SetClass(question, buffer);
		SetValue(option, buffer);
		SetName(question, buffer, null);
		SetChecked(option, buffer, answers);
		buffer.append(">");
		buffer.append(option.getName());
		buffer.append("&nbsp;<span class=\"label label-success\">");
		String colName = question.getId() + "_" + option.getId();
		int col = answers.get(colName);
		buffer.append(answers.get(colName));
		String totalKey = question.getId();
		int total = answers.get(totalKey);
		buffer.append("/");
		buffer.append(total);
		if (total > 0) {
			buffer.append("(");
			float c = (Float.valueOf(col) / Float.valueOf(total));
			buffer.append(df.format(c * 100));
			buffer.append("%");
			buffer.append(")");
		}
		buffer.append("</span>");
		buffer.append("</label>");
	}

	/**
	 * 竖向
	 * 
	 * @param question
	 * @param option
	 * @param buffer
	 */
	private static void VerticalMulti(SurveyQuestion question,
			SurveyQuestionOption option, StringBuffer buffer,
			Map<String, Integer> answers) {

		buffer.append("<div class=\"checkbox\">");
		buffer.append("<label>");
		buffer.append("<input type=\"checkbox\" ");
		SetID(question, option, buffer);
		SetClass(question, buffer);
		SetValue(option, buffer);
		SetName(question, buffer, null);
		buffer.append(">");
		buffer.append(option.getName());
		buffer.append("&nbsp;<span class=\"label label-success\">");
		String colName = question.getId() + "_" + option.getId();
		int col = answers.get(colName);
		buffer.append(answers.get(colName));
		String totalKey = question.getId();
		int total = answers.get(totalKey);
		buffer.append("/");
		buffer.append(total);
		if (total > 0) {
			buffer.append("(");
			float c = (Float.valueOf(col) / Float.valueOf(total));
			buffer.append(df.format(c * 100));
			buffer.append("%");
			buffer.append(")");
		}
		buffer.append("</span>");
		buffer.append("</label>");
		buffer.append("</div>");

	}

	private static void SelectOption(SurveyQuestion question,
			SurveyQuestionOption option, StringBuffer buffer,
			Map<String, Integer> answers) {

		buffer.append("<li>");
		buffer.append(option.getName());
		buffer.append("&nbsp;<span class=\"label label-success\">");
		String colKey = question.getId() + "_" + option.getId();
		int col = answers.get(colKey);
		buffer.append(col);
		String totalKey = question.getId();
		int total = answers.get(totalKey);
		buffer.append("/");
		buffer.append(total);
		if (total > 0) {
			buffer.append("(");
			float c = (Float.valueOf(col) / Float.valueOf(total));
			buffer.append(df.format(c * 100));
			buffer.append("%");
			buffer.append(")");
		}
		buffer.append("</span>");
		buffer.append("</li>");

	}

	/**
	 * 单行文本输入
	 * 
	 * @param option
	 * @return
	 */
	private static void SingleInput(SurveyQuestion question,
			StringBuffer buffer, Map<String, Integer> answers) {

		buffer.append("<input type=\"text\" ");
		SetID(question, null, buffer);
		SetName(question, buffer, null);
		SetClass(question, buffer);
		buffer.append("value = \"\"");
		buffer.append(">");

	}

	/**
	 * 多行行文本输入
	 * 
	 * @param option
	 * @return
	 */
	private static void MulitInput(SurveyQuestion question,
			StringBuffer buffer, Map<String, Integer> answers) {

		buffer.append("<textarea rows=\"3\" ");
		SetID(question, null, buffer);
		SetName(question, buffer, null);
		SetClass(question, buffer);
		buffer.append(">");
		buffer.append("</textarea>");
	}

	/**
	 * 单行文本输入
	 * 
	 * @param option
	 * @return
	 */
	private static void MulitSingleInput(SurveyQuestion question,
			StringBuffer buffer, Map<String, Integer> answers) {

		if (ValidateUtil.isValid(answers)) {
			return;
		}
		String name = question.getName();

		buffer.append("<div class=\"input-group\" id=\"mulitsingleinput_\">");

		String[] ss = question.getName().split("(_)+");

		int j = 0;
		if (name.startsWith("_")) {
			buffer.append("<input type=\"text\" name=\"" + answerPrefix + "["
					+ question.getId() + "_" + j
					+ "]\" class=\"form-control mulitsingleinput_"
					+ question.getId() + "\" >");
			j++;
		}

		for (int i = 0; i < ss.length; i++) {
			buffer.append("<div class=\"input-group-addon\">");
			buffer.append(ss[i]);
			buffer.append("</div>");
			// 末尾的_不处理
			if (i < ss.length - 1) {
				buffer.append("<input type=\"text\" name=\"");
				buffer.append(answerPrefix);
				buffer.append("[");
				buffer.append(question.getId());
				buffer.append("_");
				buffer.append(j);
				buffer.append("]\" class=\"form-control mulitsingleinput_");
				buffer.append(question.getId());
				buffer.append("\" aria-describedby=\"");
				buffer.append(question.getName());
				buffer.append("\" >");
			}
			j++;
		}

		if (name.endsWith("_")) {
			buffer.append("<input type=\"text\" name=\"" + answerPrefix + "["
					+ question.getId() + "_" + j
					+ "]\" class=\"form-control mulitsingleinput_"
					+ question.getId() + "\" >");
		}

		buffer.append("</div>");
	}

	/**
	 * 矩阵填空
	 * 
	 * @param question
	 * @param buffer
	 */
	private static void MatrixText(SurveyQuestion question,
			StringBuffer buffer, Map<String, Integer> answers) {
		buffer.append("<table class=\"table table-bordered\">");
		// 循环标题
		String separator = System.getProperty("line.separator");

		buffer.append("<tbody>");
		// 循环左侧
		String[] rowTitle = question.getMatrixRowTitles().split(separator);

		for (int row = 0; row < rowTitle.length; row++) {
			buffer.append("<tr>");
			buffer.append("<th scope=\"row\">" + rowTitle[row] + "</th>");
			buffer.append("<td><input type=\"text\" class=\"form-control\" ");
			SetName(question, buffer, String.valueOf(row));
			buffer.append(">");
			buffer.append("</td>");
		}

		buffer.append("</tbody>");
		buffer.append(" </table>");
	}

	/**
	 * 矩阵单选
	 * 
	 * @param question
	 * @param buffer
	 */
	private static void MatrixRadio(SurveyQuestion question,
			StringBuffer buffer, Map<String, Integer> answers) {

		buffer.append("<table class=\"table table-bordered\">");
		buffer.append("<thead>");
		buffer.append("<tr>");
		buffer.append("<th></th>");
		// 循环标题
		String separator = System.getProperty("line.separator");
		String[] topTitle = question.getMatrixColTitles().split(separator);
		for (int top = 0; top < topTitle.length; top++) {
			buffer.append("<th>" + topTitle[top] + "</th>");
		}
		buffer.append("</tr>");
		buffer.append("</thead>");
		buffer.append("<tbody>");
		// 循环左侧
		String[] rowTitle = question.getMatrixRowTitles().split(separator);

		for (int row = 0; row < rowTitle.length; row++) {
			buffer.append("<tr>");
			buffer.append("<th scope=\"row\">" + rowTitle[row] + "</th>");
			for (int top2 = 0; top2 < topTitle.length; top2++) {
				buffer.append("<td><input type=\"radio\" ");
				SetName(question, buffer, "" + row);
				buffer.append("value=\"" + topTitle[top2] + "\"");
				buffer.append(">");
				buffer.append("&nbsp;<span class=\"label label-success\">");
				String colName = question.getId() + "_" + row + "_" + top2;
				int single = answers.get(colName);
				buffer.append(answers.get(colName));
				String totalName = question.getId() + "_" + row;
				int total = answers.get(totalName);
				if (total > 0) {
					buffer.append("/");

					float c = (Float.valueOf(single) / Float
							.valueOf(total + ""));
					buffer.append(df.format(c * 100));
					buffer.append("%");

				}
				buffer.append("/");
				buffer.append(total);
				buffer.append("</span>");
				buffer.append("</td>");
			}
		}

		buffer.append("</tbody>");
		buffer.append(" </table>");
	}

	/**
	 * 矩阵多选
	 * 
	 * @param question
	 * @param buffer
	 */
	private static void MatrixCheckBox(SurveyQuestion question,
			StringBuffer buffer, Map<String, Integer> answers) {
		buffer.append("<table class=\"table table-bordered\">");
		buffer.append("<thead>");
		buffer.append("<tr>");
		buffer.append("<th></th>");
		// 循环标题
		String separator = System.getProperty("line.separator");
		String[] topTitle = question.getMatrixColTitles().split(separator);
		for (int top = 0; top < topTitle.length; top++) {
			buffer.append("<th>" + topTitle[top] + "</th>");
		}
		buffer.append("</tr>");
		buffer.append("</thead>");
		buffer.append("<tbody>");
		// 循环左侧
		String[] rowTitle = question.getMatrixRowTitles().split(separator);

		for (int row = 0; row < rowTitle.length; row++) {
			buffer.append("<tr>");
			buffer.append("<th scope=\"row\">" + rowTitle[row] + "</th>");
			for (int top2 = 0; top2 < topTitle.length; top2++) {
				buffer.append("<td><input type=\"checkbox\" ");
				SetName(question, buffer, row + "_" + top2);
				buffer.append("value=\"" + topTitle[top2] + "\"");
				buffer.append(">");
				buffer.append("&nbsp;<span class=\"label label-success\">");
				String colName = question.getId() + "_" + row + "_" + top2;
				int single = answers.get(colName);
				buffer.append(answers.get(colName));
				String totalName = question.getId() + "_" + row;
				int total = answers.get(totalName);
				buffer.append("/");
				buffer.append(total);
				if (total > 0) {
					buffer.append("(");
					float c = (Float.valueOf(single) / Float
							.valueOf(total + ""));
					buffer.append(df.format(c * 100));
					buffer.append("%");
					buffer.append(")");
				}
				buffer.append("</span>");
				buffer.append("</td>");
			}
		}

		buffer.append("</tbody>");
		buffer.append(" </table>");
	}

	private static void MatrixSelect(SurveyQuestion question,
			StringBuffer buffer, Map<String, Integer> answers) {
		buffer.append("<table class=\"table table-bordered\">");
		buffer.append("<thead>");
		buffer.append("<tr>");
		buffer.append("<th></th>");
		// 循环标题
		String separator = System.getProperty("line.separator");
		String[] topTitle = question.getMatrixColTitles().split(separator);

		for (int top = 0; top < topTitle.length; top++) {
			buffer.append("<th>" + topTitle[top] + "</th>");
		}
		buffer.append("</tr>");
		buffer.append("</thead>");
		buffer.append("<tbody>");
		// 循环左侧
		String[] rowTitle = question.getMatrixRowTitles().split(separator);

		String[] selectTitle = question.getMatrixSelectOptions().split(
				separator);

		for (int row = 0; row < rowTitle.length; row++) {
			buffer.append("<tr>");
			buffer.append("<th scope=\"row\">" + rowTitle[row] + "</th>");
			for (int top2 = 0; top2 < topTitle.length; top2++) {
				buffer.append("<td>");
				buffer.append("<ul class=\"list-unstyled\">");
				for (int select = 0; select < selectTitle.length; select++) {
					buffer.append("<li>");
					buffer.append(selectTitle[select]);
					buffer.append("&nbsp;<span class=\"label label-success\">");
					int single = answers.get(question.getId() + "_" + row + "_"
							+ top2 + "_" + select);
					buffer.append(single);
					String singleName = question.getId() + "_" + row + "_"
							+ top2;
					int total = answers.get(singleName);
					buffer.append("/");
					buffer.append(total);
					if (total > 0) {
						buffer.append("(");
						float c = (Float.valueOf(single) / Float.valueOf(total));
						buffer.append(df.format(c * 100));
						buffer.append("%");
						buffer.append(")");
					}
					buffer.append("</span>");
					buffer.append("</li>");
				}
				buffer.append("</ul>");
				buffer.append("</td>");

			}
		}

		buffer.append("</tbody>");
		buffer.append(" </table>");
	}

	/**
	 * 单行文本输入
	 * 
	 * @param option
	 * @return
	 */
	private static void FileInput(SurveyQuestion question, StringBuffer buffer,
			Map<String, Integer> answers) {
		if (ValidateUtil.isValid(answers.get(0))) {
			buffer.append("<a href=\"" + answers.get(0)
					+ "\" target=\"_blank\">下载</a>");
			return;
		}
		buffer.append("<input type=\"text\" class=\"fileUpload form-control\" ");
		SetID(question, null, buffer);
		SetName(question, buffer, null);
		SetClass(question, buffer);
		buffer.append(">");

	}

}
