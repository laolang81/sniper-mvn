package com.sniper.springmvc.survey;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.model.SurveyQuestionRules;
import com.sniper.springmvc.utils.ValidateUtil;

public class SurveyTemplateUtil {
	/**
	 * 答案前缀
	 */
	public final static String answerPrefix = "answer";
	/**
	 * 复数
	 */
	public final static String answersPrefix = "answers";
	/**
	 * 答案分隔符
	 */
	public final static String answerSplit = "::";

	public static String answer(SurveyQuestion question, List<String> answers) {

		StringBuffer buffer = new StringBuffer();
		// if (question.getType() != 8) {
		buffer.append("<div class=\"q_title\">");
		buffer.append(question.getName());
		if (question.getRules().isRequired()) {
			buffer.append("<span class=\"text-danger\">&nbsp;&nbsp;*</span>");
		}
		buffer.append("</div>");
		// }

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
			buffer.append("<select ");
			SetID(question, null, buffer);
			SetName(question, buffer, null);
			SetClass(question, buffer);
			buffer.append(">");
			for (SurveyQuestionOption option : options) {
				DownMenu(option, buffer, answers);
			}
			buffer.append("</select>");
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
	 */
	public static void SetAnswerText(SurveyQuestionOption option,
			StringBuffer buffer, String answer) {

	}

	/**
	 * 答案会先
	 * 
	 * @param option
	 * @param buffer
	 * @param answer
	 */
	private static void SetChecked(SurveyQuestionOption option,
			StringBuffer buffer, List<String> answers) {
		if (ValidateUtil.isValid(answers)) {
			for (String answer : answers) {
				if (option.getName().equals(answer)) {
					buffer.append(" checked ");
				}
			}
		}
	}

	/**
	 * 答案会先
	 * 
	 * @param option
	 * @param buffer
	 * @param answer
	 */
	private static void SetSelect(SurveyQuestionOption option,
			StringBuffer buffer, List<String> answers) {

		if (ValidateUtil.isValid(answers)) {
			for (String answer : answers) {
				if (option.getName().equals(answer)) {
					buffer.append(" selected ");
				}
			}
		}
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
		SurveyQuestionRules rules = question.getRules();
		if (rules == null) {
			buffer.append("\" ");
			return;
		}

		List<String> notCustoms = new ArrayList<>();

		if (rules.isRequired()) {
			notCustoms.add("required");
		}
		if (rules.isSize()) {
			if (rules.getMin() > 0) {
				notCustoms.add("min[" + rules.getMin() + "]");
			}
			if (rules.getMax() > 0) {
				notCustoms.add("max[" + rules.getMax() + "]");
			}

		}
		if (rules.isLength()) {
			if (rules.getMinLength() > 0) {
				notCustoms.add("minSize[" + rules.getMinLength() + "]");
			}
			if (rules.getMaxLength() > 0) {
				notCustoms.add("maxSize[" + rules.getMaxLength() + "]");
			}
		}

		List<String> customs = new ArrayList<>();
		if (rules.isEmail()) {
			customs.add("email");
		}
		if (rules.isNumber()) {
			customs.add("integer");
		}
		if (rules.isUrl()) {
			customs.add("url");
		}
		if (customs.size() > 0 || notCustoms.size() > 0) {
			buffer.append("validate[");

			if (notCustoms.size() > 0) {
				buffer.append(StringUtils.join(notCustoms, ","));
			}
			if (customs.size() > 0) {
				buffer.append(",");
				buffer.append("custom[");
				buffer.append(StringUtils.join(customs, ","));
				buffer.append("]");
			}

			buffer.append("]");
		}

		buffer.append("\" ");

	}

	private static void SetOther(SurveyQuestion question,
			SurveyQuestionOption option, StringBuffer buffer, List<String> value) {
		String answer = "";
		if (ValidateUtil.isValid(value)) {
			for (String a : value) {
				if (a.startsWith("other_")) {
					answer = a;
				}
			}
		}

		if (option.isWrited()) {
			buffer.append("<div class=\"input-group\">");
			buffer.append("<div class=\"input-group-addon\">");
			buffer.append("<input ");
			SetName(question, buffer, null);
			if (question.getType() == 1 || question.getType() == 2) {
				buffer.append("type=\"radio\"");
			} else if (question.getType() == 3 || question.getType() == 4) {
				buffer.append("type=\"checkbox\"");
			}

			if (!answer.equals("")) {
				buffer.append(" checked ");
			}

			buffer.append(" aria-label=\"" + option.getName() + "\">");
			buffer.append(option.getName() + "</div>");
			buffer.append("<input type=\"text\" ");
			SetID(question, option, buffer);
			SetName(question, buffer, "other");
			if (!answer.equals("")) {
				buffer.append("value=\"" + answer.substring(6) + "\"");
			}
			buffer.append("class=\"form-control\" ");
			buffer.append(">");
			buffer.append("</div>");
		}
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
			List<String> answers) {

		buffer.append("<label class=\"radio-inline\">");
		if (option.isWrited()) {
			SetOther(question, option, buffer, answers);
		} else {
			buffer.append("<input type=\"radio\" ");
			SetID(question, option, buffer);
			SetName(question, buffer, null);
			SetValue(option, buffer);
			SetClass(question, buffer);
			SetChecked(option, buffer, answers);
			buffer.append("> ");
			buffer.append(option.getName());
		}

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
			List<String> answers) {

		buffer.append("<div class=\"radio\">");
		if (!option.isWrited()) {
			buffer.append("<label>");
			buffer.append("<input type=\"radio\" ");
			SetID(question, option, buffer);
			SetName(question, buffer, null);
			SetValue(option, buffer);
			SetClass(question, buffer);
			SetChecked(option, buffer, answers);
			buffer.append(">");
			buffer.append(option.getName());
			buffer.append("</label>");

		} else {
			SetOther(question, option, buffer, answers);
		}

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
			List<String> answers) {

		buffer.append("<label class=\"checkbox-inline\">");
		if (!option.isWrited()) {
			buffer.append("<input type=\"checkbox\" ");
			SetID(question, option, buffer);
			SetClass(question, buffer);
			SetValue(option, buffer);
			SetName(question, buffer, null);
			SetChecked(option, buffer, answers);
			buffer.append(">");
			buffer.append(option.getName());

		} else {
			SetOther(question, option, buffer, answers);
		}
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
			List<String> answers) {

		buffer.append("<div class=\"checkbox\">");
		if (option.isWrited()) {

			SetOther(question, option, buffer, answers);
		} else {
			buffer.append("<label>");
			buffer.append("<input type=\"checkbox\" ");
			SetID(question, option, buffer);
			SetClass(question, buffer);
			SetValue(option, buffer);
			SetName(question, buffer, null);
			SetChecked(option, buffer, answers);
			buffer.append(">");
			buffer.append(option.getName());
			buffer.append("</label>");

		}
		buffer.append("</div>");

	}

	private static void DownMenu(SurveyQuestionOption option,
			StringBuffer buffer, List<String> answers) {

		buffer.append("<option value=\"");
		buffer.append(option.getName());
		buffer.append("\"");
		SetSelect(option, buffer, answers);
		buffer.append(">");
		buffer.append(option.getName());
		buffer.append("</option>");

	}

	/**
	 * 单行文本输入
	 * 
	 * @param option
	 * @return
	 */
	private static void SingleInput(SurveyQuestion question,
			StringBuffer buffer, List<String> answers) {

		buffer.append("<input type=\"text\" ");
		SetID(question, null, buffer);
		SetName(question, buffer, null);
		SetClass(question, buffer);
		if (ValidateUtil.isValid(answers)) {
			buffer.append("value = \"" + answers.get(0) + "\"");
		}

		buffer.append(">");

	}

	/**
	 * 多行行文本输入
	 * 
	 * @param option
	 * @return
	 */
	private static void MulitInput(SurveyQuestion question,
			StringBuffer buffer, List<String> answers) {

		buffer.append("<textarea rows=\"3\" ");
		SetID(question, null, buffer);
		SetName(question, buffer, null);
		SetClass(question, buffer);
		buffer.append(">");
		if (ValidateUtil.isValid(answers)) {
			buffer.append(answers.get(0));
		}

		buffer.append("</textarea>");
	}

	/**
	 * 单行文本输入
	 * 
	 * @param option
	 * @return
	 */
	private static void MulitSingleInput(SurveyQuestion question,
			StringBuffer buffer, List<String> answers) {

		if (ValidateUtil.isValid(answers)) {
			buffer.append(answers.get(0));
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
			StringBuffer buffer, List<String> answers) {
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
			SetName(question, buffer, row + "");
			if (ValidateUtil.isValid(answers)) {
				for (String s : answers) {
					if (s.startsWith(row + "_")) {
						buffer.append("value= \"" + s.substring(2) + "\"");
					}
				}
			}

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
			StringBuffer buffer, List<String> answers) {

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
				if (ValidateUtil.isValid(answers)) {
					for (String s : answers) {
						if ((row + "_" + topTitle[top2]).equals(s)) {
							buffer.append(" checked ");
						}
					}
				}

				buffer.append(">");
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
			StringBuffer buffer, List<String> answers) {
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
				if (ValidateUtil.isValid(answers)) {
					for (String s : answers) {
						if (s.startsWith(row + "_" + top2 + "_")) {
							buffer.append(" checked ");
						}
					}
				}

				buffer.append(">");
				buffer.append("</td>");
			}
		}

		buffer.append("</tbody>");
		buffer.append(" </table>");
	}

	private static void MatrixSelect(SurveyQuestion question,
			StringBuffer buffer, List<String> answers) {
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
				buffer.append("<select ");
				SetName(question, buffer, row + "_" + top2);
				buffer.append(">");
				for (int select = 0; select < selectTitle.length; select++) {
					buffer.append("<option value=\"");
					buffer.append(selectTitle[select]);
					buffer.append("\"");
					if (ValidateUtil.isValid(answers)) {
						for (String s : answers) {
							if ((row + "_" + top2 + "_" + selectTitle[select])
									.equals(s)) {
								buffer.append(" selected ");
							}
						}
					}

					buffer.append(">" + selectTitle[select] + "</option>");
				}

				buffer.append("</select>");
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
			List<String> answers) {
		if (ValidateUtil.isValid(answers)) {
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
