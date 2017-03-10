package com.sniper.springmvc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sniper.springmvc.freemarker.ViewHomeUtils;
import com.sniper.springmvc.freemarker.ViewProjectImport;
import com.sniper.springmvc.model.SdViewSubject;
import com.sniper.springmvc.mybatis.service.impl.SdViewSubjectService;
import com.sniper.springmvc.solr.SubjectViewModel;
import com.sniper.springmvc.utils.model.Statistics;

/**
 * 数据导出
 * 
 * @author suzhen
 * 
 */
public class ExeclStaticExportUtils {
	private final static Logger LOGGER = LoggerFactory
			.getLogger(ExeclStaticExportUtils.class);
	private Workbook workbook = null;
	/**
	 * 文件根目录
	 */
	private String rootDir;
	private SdViewSubjectService computerService;
	private Statistics statistics;
	/**
	 * 导入的数据行数
	 */
	private int importCount = 0;
	/**
	 * 储存循环读取数据的字段
	 */
	private Map<String, String> headerFields = new LinkedHashMap<>();
	/**
	 * 包含顶部显示文字和每一行的数据读取字段名称
	 */
	private List<Map<String, String>> headFieldsAll = new ArrayList<>();
	/**
	 * 数据输出起始行
	 */
	private int headStart = 0;

	public ExeclStaticExportUtils() {
	}

	public void setHeaderFields(Map<String, String> headerFields) {
		this.headerFields = headerFields;
	}

	public void setComputerService(SdViewSubjectService computerService) {
		this.computerService = computerService;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}

	public int getImportCount() {
		return importCount;
	}

	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}

	public void setHeadFieldsAll(List<Map<String, String>> headFieldsAll) {
		this.headFieldsAll = headFieldsAll;
	}

	public double getResult(float a) {
		BigDecimal bigDecimal = new BigDecimal(a);
		BigDecimal b = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		return b.floatValue() * 100;
	}

	public double getResult(double a) {
		BigDecimal bigDecimal = new BigDecimal(a);
		BigDecimal b = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		return b.doubleValue() * 100;
	}

	public void importData(String fileImportPath) throws IOException,
			ParseException {
		// 读取文件保存前缀
		fileImportPath = rootDir + fileImportPath;
		File file2 = new File(fileImportPath);
		if (!file2.exists()) {
			LOGGER.error("文件导入文件地址不存在" + fileImportPath);
			throw new FileNotFoundException(fileImportPath + "文件不存在");
		}
		InputStream in = new FileInputStream(file2);
		if (fileImportPath.endsWith("xlsx")) {
			this.workbook = new XSSFWorkbook(in);
		} else {
			this.workbook = new HSSFWorkbook(in);
		}

		int sheetsCount = workbook.getNumberOfSheets();
		for (int i = 0; i < sheetsCount; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			// 如果sheetName不包含明细不读取
			Iterator<Row> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				// 一个row就是一个项目
				Row row = rows.next();
				// 过滤掉第一行第二行
				if (row.getRowNum() > 2) {
					Iterator<Cell> cells = row.cellIterator();
					readCells(cells);
				}
			}
		}
	}

	/**
	 * 读取cell的内容 读取一行的内容
	 * 
	 * @param cells
	 * @param sheetYearName
	 * @throws ParseException
	 */
	private void readCells(Iterator<Cell> cells) throws ParseException {

		SdViewSubject computer = new SdViewSubject();
		while (cells.hasNext()) {
			Cell cell = (Cell) cells.next();
			// TODO 设置内容开始行数
			cellWriteToObject(cell, computer);
		}

		if (ValidateUtil.isValid(computer.getSubject())) {
			computerService.insert(computer);
			importCount++;
		}
	}

	/**
	 * 表达式查找字符串是否存在
	 * 
	 * @param str
	 * @param reg
	 * @return
	 */
	public boolean findString(String str, String reg) {
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}

	/**
	 * 设置 给对象赋值
	 * 
	 * @param cell
	 * @param project
	 * @throws ParseException
	 */
	private void cellWriteToObject(Cell cell, SdViewSubject computer)
			throws ParseException {

		// 设置名称
		switch (cell.getColumnIndex()) {
		case 0:
			if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING) {
				if (ValidateUtil.isValid(cell.getStringCellValue())) {
				}
			}
			break;

		default:
			break;
		}

	}

	/**
	 * 处室栏目统计导出 数据导出 倒数数据统计
	 * 
	 * @throws IOException
	 */
	public void exportPostCount(String fileExportPath) throws IOException {

		if (fileExportPath.endsWith("xlsx")) {
			this.workbook = new XSSFWorkbook();
		} else {
			this.workbook = new HSSFWorkbook();
		}
		Sheet sheet = workbook.createSheet("数据导出");
		// 设置导出的头部
		setSheetHeader(sheet);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 导出的内容
		if (statistics.getSearchDeps() != null
				&& statistics.getSearchDeps().size() > 0) {
			int j = headStart;
			for (Entry<String, String> entry : statistics.getSearchDeps()
					.entrySet()) {
				int i = 0;
				Row row = sheet.createRow(j);
				// 循环处室
				// 处室名称
				createCell(row, i, entry.getValue(), CellStyle.ALIGN_CENTER,
						CellStyle.VERTICAL_CENTER, null);

				i++;
				// 新闻访问量
				createCell(
						row,
						i,
						getMapDate(statistics.getSiteidCount()
								.get(entry.getKey()).get("view")),
						CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, null);

				i++;
				// 新闻发布量
				createCell(
						row,
						i,
						getMapDate(statistics.getSiteidCount()
								.get(entry.getKey()).get("sid")),
						CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, null);

				i++;
				// 执行2次因为这里date为空
				i++;
				// 留言量/回复量
				createCell(
						row,
						i,
						getMapDate(statistics.getSiteidCount()
								.get(entry.getKey()).get("worldCount"))
								+ "/"
								+ getMapDate(statistics.getSiteidCount()
										.get(entry.getKey())
										.get("worldAnswerCount")),
						CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, null);

				// 行数+1
				j++;
				i = 0;
				// 然后循环处室结果
				if (statistics.getItemsMap().containsKey(entry.getKey())) {
					Map<String, Map<String, Object>> itemCount = statistics
							.getSiteidItemidCount().get(entry.getKey());
					for (Entry<String, String> entry2 : statistics
							.getItemsMap().get(entry.getKey()).entrySet()) {
						Row row2 = sheet.createRow(j);
						i = 0;
						// 检测栏目是否可用
						if (itemCount.get(entry2.getKey()) == null) {
							// 处室名称
							createCell(row2, i, entry2.getValue(),
									CellStyle.ALIGN_CENTER,
									CellStyle.VERTICAL_CENTER, null);
						} else {
							// 处室名称
							createCell(row2, i, entry2.getValue(),
									CellStyle.ALIGN_CENTER,
									CellStyle.VERTICAL_CENTER, null);

							i++;
							// 新闻访问量
							createCell(row2, i,
									getMapDate(itemCount.get(entry2.getKey())
											.get("view")),
									CellStyle.ALIGN_CENTER,
									CellStyle.VERTICAL_CENTER, null);

							i++;
							// 新闻发布量
							createCell(row2, i,
									getMapDate(itemCount.get(entry2.getKey())
											.get("sid")),
									CellStyle.ALIGN_CENTER,
									CellStyle.VERTICAL_CENTER, null);

							i++;
							// 最后新闻发布日期
							if (ValidateUtil.isValid(itemCount.get(
									entry2.getKey()).get("date"))) {
								Date date2 = (Date) itemCount.get(
										entry2.getKey()).get("date");
								createCell(row2, i, dateFormat.format(date2),
										CellStyle.ALIGN_CENTER,
										CellStyle.VERTICAL_CENTER, null);
							}
						}
						j++;
					}
				}
				j++;
			}
		}

		fileExportPath = rootDir + fileExportPath;
		File file = new File(fileExportPath);
		File parent = file.getParentFile();
		if (!parent.isDirectory()) {
			parent.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(fileExportPath);
		workbook.write(out);
		workbook.close();
		out.close();
	}

	/**
	 * 数据导出 倒数数据统计
	 * 
	 * @throws IOException
	 */
	public void exportChannelPostCount(String fileExportPath)
			throws IOException {

		if (fileExportPath.endsWith("xlsx")) {
			this.workbook = new XSSFWorkbook();
		} else {
			this.workbook = new HSSFWorkbook();
		}
		Sheet sheet = workbook.createSheet("数据导出");
		// 设置导出的头部
		setSheetHeader(sheet);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 导出的内容
		if (statistics.getSearchDeps() != null
				&& statistics.getSearchDeps().size() > 0) {
			int j = headStart;
			for (Entry<String, String> entry : statistics.getSearchDeps()
					.entrySet()) {
				int i = 0;
				Row row = sheet.createRow(j);
				// 循环处室
				// 处室名称
				createCell(row, i, entry.getValue(), CellStyle.ALIGN_CENTER,
						CellStyle.VERTICAL_CENTER, null);

				i++;
				// 新闻访问量
				createCell(
						row,
						i,
						getMapDate(statistics.getSiteidCount()
								.get(entry.getKey()).get("view")),
						CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, null);

				i++;
				// 新闻发布量
				createCell(
						row,
						i,
						getMapDate(statistics.getSiteidCount()
								.get(entry.getKey()).get("sid")),
						CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, null);

				i++;
				// 最后新闻发布日期
				if (ValidateUtil.isValid(statistics.getSiteidCount()
						.get(entry.getKey()).get("date"))) {
					Date date = (Date) statistics.getSiteidCount()
							.get(entry.getKey()).get("date");
					createCell(row, i, dateFormat.format(date),
							CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER,
							null);
				}

				i++;
				// 留言量/回复量
				createCell(
						row,
						i,
						getMapDate(statistics.getSiteidCount()
								.get(entry.getKey()).get("worldCount"))
								+ "/"
								+ getMapDate(statistics.getSiteidCount()
										.get(entry.getKey())
										.get("worldAnswerCount")),
						CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, null);

				// 行数+1
				j++;
				i = 0;
				// 然后循环处室结果

				if (statistics.getItemsMap().containsKey(entry.getKey())) {
					for (Entry<String, String> entry2 : statistics
							.getItemsMap().get(entry.getKey()).entrySet()) {
						Row row2 = sheet.createRow(j);
						i = 0;
						// 处室名称
						createCell(row2, i, entry2.getValue(),
								CellStyle.ALIGN_CENTER,
								CellStyle.VERTICAL_CENTER, null);

						i++;
						// 新闻访问量
						createCell(
								row2,
								i,
								getMapDate(statistics.getItemidCount()
										.get(entry2.getKey()).get("view")),
								CellStyle.ALIGN_CENTER,
								CellStyle.VERTICAL_CENTER, null);

						i++;
						// 新闻发布量
						createCell(
								row2,
								i,
								getMapDate(statistics.getItemidCount()
										.get(entry2.getKey()).get("sid")),
								CellStyle.ALIGN_CENTER,
								CellStyle.VERTICAL_CENTER, null);

						i++;
						// 最后新闻发布日期
						if (ValidateUtil.isValid(statistics.getItemidCount()
								.get(entry2.getKey()).get("date"))) {
							Date date2 = (Date) statistics.getItemidCount()
									.get(entry2.getKey()).get("date");
							createCell(row2, i, dateFormat.format(date2),
									CellStyle.ALIGN_CENTER,
									CellStyle.VERTICAL_CENTER, null);
						}

						i++;
						// 留言量/回复量
						createCell(
								row2,
								i,
								getMapDate(statistics.getItemidCount()
										.get(entry2.getKey()).get("worldCount"))
										+ "/"
										+ getMapDate(statistics
												.getItemidCount()
												.get(entry2.getKey())
												.get("worldAnswerCount")),
								CellStyle.ALIGN_CENTER,
								CellStyle.VERTICAL_CENTER, null);
						j++;
					}
				}
				j++;
			}
		}

		fileExportPath = rootDir + fileExportPath;
		File file = new File(fileExportPath);
		File parent = file.getParentFile();
		if (!parent.isDirectory()) {
			parent.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(fileExportPath);
		workbook.write(out);
		workbook.close();
		out.close();
	}

	/**
	 * 月份统计 数据导出 倒数数据统计
	 * 
	 * @throws IOException
	 */
	public void exportMapKeyValue(String path,
			List<Map<String, Object>> postMonthCount) throws IOException {

		if (path.endsWith("xlsx")) {
			this.workbook = new XSSFWorkbook();
		} else {
			this.workbook = new HSSFWorkbook();
		}
		Sheet sheet = workbook.createSheet("数据导出");
		// 设置导出的头部
		setSheetHeader(sheet);
		// 数据导出
		if (ValidateUtil.isValid(postMonthCount)) {
			for (Map<String, Object> map : postMonthCount) {
				int j = headStart;
				int line = 1;
				Row row = sheet.createRow(j);
				int i = 0;
				for (Entry<String, String> entry : headerFields.entrySet()) {
					switch (entry.getKey()) {
					case "id":
						createCell(row, 0, line, CellStyle.ALIGN_CENTER,
								CellStyle.VERTICAL_CENTER, null);
						break;
					default:
						createCell(row, i, map.get(entry.getKey()),
								CellStyle.ALIGN_CENTER,
								CellStyle.VERTICAL_CENTER, null);
						break;
					}
					i++;
				}
				line++;
				j++;
			}
		}

		path = rootDir + path;
		File file = new File(path);
		File parent = file.getParentFile();
		if (!parent.isDirectory()) {
			parent.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(path);
		workbook.write(out);
		workbook.close();
		out.close();
	}

	/**
	 * 数据导出 导出新闻列表，审核专用
	 * 
	 * @throws IOException
	 */
	public void exportGroupSubject(String fileExportPath,
			List<SdViewSubject> subjects) throws IOException {

		if (fileExportPath.endsWith("xlsx")) {
			this.workbook = new XSSFWorkbook();
		} else {
			this.workbook = new HSSFWorkbook();
		}
		Sheet sheet = workbook.createSheet("数据导出");
		// 设置导出的头部
		setSheetHeader(sheet);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 导出的内容
		if (subjects != null && subjects.size() > 0) {
			int j = headStart;
			// 序号
			int number = 1;
			for (SdViewSubject subject : subjects) {
				int i = 0;
				Row row = sheet.createRow(j);
				for (Entry<String, String> entry : headerFields.entrySet()) {
					switch (entry.getKey()) {
					// 序号设置
					case "id":
						createCell(row, i, number, CellStyle.ALIGN_CENTER,
								CellStyle.VERTICAL_CENTER, null);
						break;
					// 特殊处理的时间
					case "date":
						String dS = ViewProjectImport.getFiledValue(subject,
								"date");
						int dd = Integer.valueOf(dS).intValue();
						if (ValidateUtil.isValid(dS) && dS.length() == 11) {
							Date date = ViewHomeUtils.intToDate(dd);
							createCell(row, i, dateFormat.format(date),
									CellStyle.ALIGN_CENTER,
									CellStyle.VERTICAL_CENTER, null);
						}
						break;
					default:
						try {
							createCell(row, i, ViewProjectImport.getFiledValue(
									subject, entry.getKey()),
									CellStyle.ALIGN_CENTER,
									CellStyle.VERTICAL_CENTER, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;

					}
					i++;
				}
				number++;
				j++;
				// 添加底部
				Row row2 = sheet.createRow(j);
				createCell(row2, 1, "信息审核人", CellStyle.ALIGN_CENTER,
						CellStyle.VERTICAL_CENTER, null);

				createCell(row2, 2, "", CellStyle.ALIGN_CENTER,
						CellStyle.VERTICAL_CENTER, null);

				createCell(row2, 3, "办公室负责人", CellStyle.ALIGN_CENTER,
						CellStyle.VERTICAL_CENTER, null);
				createCell(row2, 4, "", CellStyle.ALIGN_CENTER,
						CellStyle.VERTICAL_CENTER, null);
			}
		}

		fileExportPath = rootDir + fileExportPath;
		File file = new File(fileExportPath);
		File parent = file.getParentFile();
		if (!parent.isDirectory()) {
			parent.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(fileExportPath);
		workbook.write(out);
		workbook.close();
		out.close();
	}

	/**
	 * 数据导出
	 * 
	 * @param fileExportPath
	 * @param subjects
	 * @throws IOException
	 */
	public void exportSolrSubject(String fileExportPath,
			List<SubjectViewModel> subjects) throws IOException {

		if (fileExportPath.endsWith("xlsx")) {
			this.workbook = new XSSFWorkbook();
		} else {
			this.workbook = new HSSFWorkbook();
		}
		Sheet sheet = workbook.createSheet("数据导出");
		// 设置导出的头部
		setSheetHeader(sheet);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		// 导出的内容
		if (subjects != null && subjects.size() > 0) {
			int j = headStart;
			// 序号
			int number = 1;
			for (SubjectViewModel subject : subjects) {
				int i = 0;
				Row row = sheet.createRow(j);
				for (Entry<String, String> entry : headerFields.entrySet()) {
					switch (entry.getKey()) {
					// 序号设置
					case "id":
						createCell(row, i, number, CellStyle.ALIGN_CENTER,
								CellStyle.VERTICAL_CENTER, null);
						break;
					// 特殊处理的时间
					case "date":
						createCell(row, i,
								dateFormat.format(subject.getDate()),
								CellStyle.ALIGN_CENTER,
								CellStyle.VERTICAL_CENTER, null);
						break;
					default:
						try {
							createCell(row, i, ViewProjectImport.getFiledValue(
									subject, entry.getKey()),
									CellStyle.ALIGN_CENTER,
									CellStyle.VERTICAL_CENTER, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
						break;
					}
					i++;
				}
				number++;
				j++;
			}
		}

		fileExportPath = rootDir + fileExportPath;
		System.out.println(fileExportPath);
		File file = new File(fileExportPath);
		File parent = file.getParentFile();
		if (!parent.isDirectory()) {
			parent.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(fileExportPath);
		workbook.write(out);
		workbook.close();
		out.close();
	}

	private int getMapDate(Object a) {
		int result = 0;
		if (!ValidateUtil.isValid(a)) {
			return result;
		}
		if (a.getClass() == BigDecimal.class) {
			BigDecimal bigDecimal = (BigDecimal) a;
			result = bigDecimal.intValue();
		} else if (a.getClass() == Long.class) {
			long view = (long) a;
			result = Long.valueOf(view).intValue();
		} else {
			int view = (int) a;
			result = Integer.valueOf(view).intValue();
		}
		return result;
	}

	/**
	 * 设置导出的头部
	 * 
	 * @param sheet
	 */
	public void setSheetHeader(Sheet sheet) {

		this.headStart = headFieldsAll.size();
		// 循环所有的
		int r = 0;
		for (Map<String, String> map : headFieldsAll) {
			int c = 0;
			Row row = sheet.createRow(r);
			// 循环标题
			for (Entry<String, String> entry : map.entrySet()) {
				if (entry.getKey().startsWith(":")) {
					String key = entry.getKey().substring(1);
					String[] keys = key.split("-");
					if (keys.length == 2) {
						// 合并，处理左右合并。没有处理上下
						mergeCell(sheet, r, r, Integer.valueOf(keys[0])
								.intValue(), Integer.valueOf(keys[1])
								.intValue());
						createCell(row, Integer.valueOf(keys[0]).intValue(),
								entry.getValue(), CellStyle.ALIGN_CENTER,
								CellStyle.VERTICAL_CENTER, null);
					} else if (keys.length == 4) {
						// 合并，这里处室上下左右合并,但是这里没写
						mergeCell(sheet, r, r, Integer.valueOf(keys[0])
								.intValue(), Integer.valueOf(keys[1])
								.intValue());
						createCell(row, Integer.valueOf(keys[0]).intValue(),
								entry.getValue(), CellStyle.ALIGN_CENTER,
								CellStyle.VERTICAL_CENTER, null);
					}

				} else {
					createCell(row, c, entry.getValue(),
							CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER,
							null);
				}
				c++;
			}
			r++;
		}
	}

	/**
	 * 自定义设置莫一行的
	 * 
	 * @param sheet
	 * @param headers
	 * @param rowCount
	 */
	public void setSheetHeaderCustom(Sheet sheet, Map<String, String> headers,
			int rowCount) {
		short i = 0;
		Row row = sheet.createRow(rowCount);
		for (Entry<String, String> entry : headerFields.entrySet()) {
			createCell(row, i, entry.getValue(), CellStyle.ALIGN_CENTER,
					CellStyle.VERTICAL_CENTER, null);
			i++;
		}
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

	public void createCell(Row row, int column, Object value, short halign,
			short valign, Font font) {
		if (value.getClass() == Integer.class) {
			int val = (int) value;
			createCell(row, column, val, halign, valign, font);
		} else if (value.getClass() == String.class) {
			String val = (String) value;
			createCell(row, column, val, halign, valign, font);
		} else if (value.getClass() == Long.class) {
			long val = (long) value;
			createCell(row, column, val, halign, valign, font);
		} else if (value.getClass() == Float.class) {
			float val = (float) value;
			createCell(row, column, val, halign, valign, font);
		} else if (value.getClass() == Double.class) {
			double val = (double) value;
			createCell(row, column, val, halign, valign, font);
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

	public void createCell(Row row, int column, long value, short halign,
			short valign, Font font) {
		Cell cell = row.createCell(column);
		if (ValidateUtil.isValid(value)) {
			CellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setWrapText(true);
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
	private void mergeCell(Sheet sheet, int a, int b, int c, int d) {
		sheet.addMergedRegion(new CellRangeAddress(a, b, c, d));
	}

}
