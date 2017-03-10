package com.sniper.springmvc.survey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.sniper.springmvc.model.Survey;
import com.sniper.springmvc.model.SurveyPage;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.ValidateUtil;

public class SurveyResultExportUtil {

	private Workbook workbook = null;
	private String fileName;
	private String rootPath;
	private String suffix = "xlsx";
	/**
	 * 问卷问题列表
	 */
	private Survey survey;
	/**
	 * 需要显示的结果集
	 */
	private Map<String, Map<String, List<String>>> results = new HashMap<>();

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	public void setResults(Map<String, Map<String, List<String>>> results) {
		this.results = results;
	}

	public SurveyResultExportUtil() {

		rootPath = FilesUtil.getRootDir() + "/export/";
		try {
			if (this.suffix.equals("xlsx")) {
				this.workbook = new XSSFWorkbook();
			} else {
				this.workbook = new HSSFWorkbook();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 定义样式
	 * 
	 * @return
	 */
	protected CellStyle createCellStyle() {

		CellStyle cellStyle = this.workbook.createCellStyle();
		// 设置背景颜色
		cellStyle.setFillBackgroundColor(HSSFColor.LIGHT_YELLOW.index);

		Font font = this.workbook.createFont();
		font.setColor(HSSFColor.BLACK.index);
		cellStyle.setFont(font);
		return cellStyle;
	}

	protected Font createFont() {
		Font font = this.workbook.createFont();
		font.setBold(true);
		return font;
	}

	/**
	 * Creates a cell and aligns it a certain way.
	 * 
	 * @param wb
	 *            the workbook
	 * @param row
	 *            the row to create the cell in
	 * @param column
	 *            the column number to create the cell in
	 * @param halign
	 *            the horizontal alignment for the cell.
	 */

	public void createCell(Row row, int column, String value, short halign,
			short valign, Font font) {
		Cell cell = row.createCell(column);
		if (ValidateUtil.isValid(value)) {
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setWrapText(true);
			// int width = value.length() * 500;
			// Sheet sheet = row.getSheet();
			// sheet.setColumnWidth(column, width);
			cellStyle.setAlignment(halign);
			cellStyle.setVerticalAlignment(valign);
			if (font != null) {
				cellStyle.setFont(font);
			}
			cell.setCellStyle(cellStyle);
			cell.setCellValue(value);
		} else {
			cell.setCellValue("");
		}
	}

	public void createCell(Row row, int column, Integer value, short halign,
			short valign, Font font) {
		Cell cell = row.createCell(column);
		if (ValidateUtil.isValid(value)) {
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setWrapText(true);
			// int width = String.valueOf(value).length() * 250;
			// Sheet sheet = row.getSheet();
			// sheet.setColumnWidth(column, width);
			cellStyle.setAlignment(halign);
			cellStyle.setVerticalAlignment(valign);
			if (font != null) {
				cellStyle.setFont(font);
			}
			cell.setCellStyle(cellStyle);
			cell.setCellValue(value);
		} else {
			cell.setCellValue(0);
		}

	}

	public void createCell(Row row, int column, float value, short halign,
			short valign, Font font) {
		Cell cell = row.createCell(column);
		if (ValidateUtil.isValid(value)) {
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setWrapText(true);
			// int width = String.valueOf(value).length() * 250;
			// Sheet sheet = row.getSheet();
			// sheet.setColumnWidth(column, width);
			cellStyle.setAlignment(halign);
			cellStyle.setVerticalAlignment(valign);
			if (font != null) {
				cellStyle.setFont(font);
			}
			cell.setCellStyle(cellStyle);
			cell.setCellValue(value);
		} else {
			cell.setCellValue(0);
		}

	}

	public void createCell(Row row, int column, double value, short halign,
			short valign, Font font) {
		Cell cell = row.createCell(column);
		if (ValidateUtil.isValid(value)) {
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setWrapText(true);
			// int width = String.valueOf(value).length() * 250;
			// Sheet sheet = row.getSheet();
			// sheet.setColumnWidth(column, width);
			cellStyle.setAlignment(halign);
			cellStyle.setVerticalAlignment(valign);
			if (font != null) {
				cellStyle.setFont(font);
			}
			cell.setCellStyle(cellStyle);
			cell.setCellValue(value);

		} else {
			cell.setCellValue(0);
		}
	}

	public void createCell(Row row, int column, Date value, short halign,
			short valign, Font font) {
		Cell cell = row.createCell(column);
		if (ValidateUtil.isValid(value)) {
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setWrapText(true);
			// int width = String.valueOf(value).length() * 250;
			// Sheet sheet = row.getSheet();
			// sheet.setColumnWidth(column, width);
			// short df = workbook.createDataFormat()
			// .getFormat("YYYY-MM-DD HH:MM");
			// cellStyle.setDataFormat(df);
			cellStyle.setAlignment(halign);
			cellStyle.setVerticalAlignment(valign);
			if (font != null) {
				cellStyle.setFont(font);
			}
			cell.setCellStyle(cellStyle);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			cell.setCellValue(dateFormat.format(value));
		} else {
			cell.setCellValue("");
		}
	}

	/**
	 * 合并cell
	 * 
	 * @param sheet
	 * @param a
	 *            first row
	 * @param b
	 *            last row
	 * @param c
	 *            first column
	 * @param d
	 *            last column
	 */
	protected void mergeCell(Sheet sheet, int a, int b, int c, int d) {
		sheet.addMergedRegion(new CellRangeAddress(a, b, c, d));
	}

	/**
	 * 到处规则第一行是标题，第二行按照前台显示的结果展示答案
	 * 
	 * @return
	 */
	public String run() {
		// 循环标题
		String separator = System.getProperty("line.separator");
		Sheet sheet = workbook.createSheet("答案到处");
		// 输出问题
		int a = 0;
		Row row = sheet.createRow(0);
		for (SurveyPage page : survey.getSurveyPages()) {
			for (SurveyQuestion question : page.getSq()) {
				createCell(row, a, question.getName(), CellStyle.ALIGN_CENTER,
						CellStyle.VERTICAL_CENTER, null);
				a++;
			}
		}
		// 输出答案
		int i = 1;
		for (Map.Entry<String, Map<String, List<String>>> entry : results
				.entrySet()) {
			Row row1 = sheet.createRow(i);
			Map<String, List<String>> secMap = entry.getValue();
			// key是问题id+小题前缀
			// value 是 答案列表
			int j = 0;
			for (SurveyPage page : survey.getSurveyPages()) {
				for (SurveyQuestion question : page.getSq()) {
					String qid = String.valueOf(question.getId());

					switch (question.getType()) {
					// 单选 直接输出答案
					case 1:
					case 2:
					case 3:
					case 4:
					case 5:
					case 6:
					case 7:
					case 8:
						if (secMap.containsKey(qid)) {
							List<String> answers = secMap.get(qid);
							String answer = StringUtils.join(answers, ";"
									+ separator);
							createCell(row1, j, answer, CellStyle.ALIGN_CENTER,
									CellStyle.VERTICAL_CENTER, null);
						}
						break;
					case 11:
						if (secMap.containsKey(qid)) {
							List<String> answers = secMap.get(qid);
							// 循环左侧
							String[] leftTitle = question.getMatrixRowTitles()
									.split(separator);
							StringBuffer buffer = new StringBuffer();
							for (int b = 0; b < leftTitle.length; b++) {
								for (String s : answers) {
									if (s.startsWith(b + "_")) {
										buffer.append(leftTitle[b] + ":"
												+ s.substring(2));
										buffer.append(";");
										buffer.append(separator);
									}
								}
							}
							createCell(row1, j, buffer.toString(),
									CellStyle.ALIGN_CENTER,
									CellStyle.VERTICAL_CENTER, null);
						}

						break;
					case 12:

						if (secMap.containsKey(qid)) {
							List<String> answers = secMap.get(qid);
							// 循环左侧
							String[] leftTitle = question.getMatrixRowTitles()
									.split(separator);
							String[] topTitle = question.getMatrixColTitles()
									.split(separator);
							StringBuffer buffer = new StringBuffer();
							for (int b = 0; b < leftTitle.length; b++) {
								for (int c = 0; c < topTitle.length; c++) {
									for (String s : answers) {
										if (s.equals(b + "_" + topTitle[c])) {
											buffer.append(leftTitle[b] + ":"
													+ s.substring(2));
											buffer.append(";");
											buffer.append(separator);
										}
									}
								}

							}
							createCell(row1, j, buffer.toString(),
									CellStyle.ALIGN_CENTER,
									CellStyle.VERTICAL_CENTER, null);
						}

						break;
					case 13:
						if (secMap.containsKey(qid)) {
							List<String> answers = secMap.get(qid);
							// 循环左侧
							String[] leftTitle = question.getMatrixRowTitles()
									.split(separator);
							String[] topTitle = question.getMatrixColTitles()
									.split(separator);
							StringBuffer buffer = new StringBuffer();
							for (int b = 0; b < leftTitle.length; b++) {
								for (int c = 0; c < topTitle.length; c++) {
									for (String s : answers) {
										if (s.startsWith(b + "_" + c + "_")) {
											buffer.append(leftTitle[b] + ":"
													+ s.substring(4));
											buffer.append(";");
											buffer.append(separator);
										}
									}
								}

							}
							createCell(row1, j, buffer.toString(),
									CellStyle.ALIGN_CENTER,
									CellStyle.VERTICAL_CENTER, null);
						}

						break;
					case 14:
						if (secMap.containsKey(qid)) {
							List<String> answers = secMap.get(qid);
							// 循环左侧
							String[] leftTitle = question.getMatrixRowTitles()
									.split(separator);
							String[] topTitle = question.getMatrixColTitles()
									.split(separator);
							StringBuffer buffer = new StringBuffer();
							for (int b = 0; b < leftTitle.length; b++) {
								for (int c = 0; c < topTitle.length; c++) {
									for (String s : answers) {

										if (s.startsWith(b + "_" + c + "_")) {
											buffer.append(leftTitle[b] + "-"
													+ topTitle[c] + ":"
													+ s.substring(4));
											buffer.append(";");
											buffer.append(separator);
										}
									}
								}

							}
							createCell(row1, j, buffer.toString(),
									CellStyle.ALIGN_CENTER,
									CellStyle.VERTICAL_CENTER, null);
						}

					default:
						break;
					}

					j++;
				}
			}

			i++;

		}
		return "";
	}

	/**
	 * 输出到文件
	 * 
	 * @param filepath
	 * @throws IOException
	 */
	public void write(String filepath) throws IOException {
		File fileDir = new File(rootPath);
		if (!fileDir.isDirectory()) {
			fileDir.mkdirs();
		}
		this.fileName = rootPath + filepath + "." + suffix;
		File file = new File(this.fileName);
		if (file.isFile()) {
			file.delete();
		}
		FileOutputStream out = new FileOutputStream(this.fileName);
		workbook.write(out);
		out.close();
	}

	public String getFileName() {
		return fileName;
	}
}
