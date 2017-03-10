package com.sniper.springmvc.scheduler;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sniper.springmvc.model.SdContent;
import com.sniper.springmvc.model.SdStatIndexSum;
import com.sniper.springmvc.model.SdSubjects;
import com.sniper.springmvc.model.SdTabLeaveword;
import com.sniper.springmvc.mybatis.service.impl.SdDepartmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdItemsService;
import com.sniper.springmvc.mybatis.service.impl.SdStatIndexService;
import com.sniper.springmvc.mybatis.service.impl.SdStatIndexSumService;
import com.sniper.springmvc.mybatis.service.impl.SdStatItemService;
import com.sniper.springmvc.mybatis.service.impl.SdSubjectsService;
import com.sniper.springmvc.mybatis.service.impl.SdTabLeavewordService;
import com.sniper.springmvc.utils.SystemConfigUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 每天的访问流量归档统计
 * 
 * @author suzhen
 * 
 */
@Component
public class StatViewTask {

	static final Logger LOGGER = LoggerFactory.getLogger(StatViewTask.class);

	@Resource
	SdStatItemService statItemService;

	@Resource
	SdSubjectsService subjectsService;

	@Resource
	SdDepartmentsService departmentsService;

	@Resource
	SdItemsService itemsService;

	@Resource
	SdStatIndexService statIndexService;

	@Resource
	SdStatIndexSumService indexSumService;

	@Resource
	SdTabLeavewordService leavewordService;

	/**
	 * 每天凌晨一点
	 */
	@Scheduled(cron = "1 1 1 * * ?")
	protected void init() {
		boolean on = false;
		LOGGER.info("计划任务: 每天一点一分一秒:" + new Date().toString());
		// 第一步
		if (on) {
			// 处理每一天的新闻访问量
			this.oneSetSubjectTodayView();
			// 第二部
			this.twoSetSubjectStatItem();
			this.threeSetSubjectViewZero();
			this.fourImportleavewordtonews();
			this.fiveTruncateStatIndex();
		}
	}

	/**
	 * 返回当前时间日期
	 * 
	 * @return
	 */
	public String getDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, -1);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * 把今天的数据结果插入数据库 操作步骤第一步
	 */
	private void oneSetSubjectTodayView() {
		// 查看今天是否已经保存过数据
		Map<String, Object> params = new HashMap<>();
		params.put("date", getDate());
		SdStatIndexSum indexSum = (SdStatIndexSum) indexSumService.find(
				"select", params);
		int total = getAllTotal();
		// 数据已经存在，操作停止
		if (indexSum != null) {
			indexSum.setView(total);
			indexSumService.update(indexSum);
			// 更新
			return;
		}
		// 插入数据
		SdStatIndexSum statIndexSum = new SdStatIndexSum();
		statIndexSum.setDate(new Date());
		statIndexSum.setView(total);
		indexSumService.insert(statIndexSum);
	}

	/**
	 * 获取所有新闻访问统计
	 * 
	 * @return
	 */
	private int getAllTotal() {
		return statIndexService.getAllTotal(getDate());
	}

	/**
	 * 设置栏目统计 第二部要執行的東西
	 */
	private void twoSetSubjectStatItem() {
		statIndexService.saveSubjectView(getDate());
	}

	/**
	 * 设置昨天新闻访问量等于0 第三部要执行的步骤
	 */
	private void threeSetSubjectViewZero() {
		subjectsService.setSubjectViewZero(getDate());
	}

	/**
	 * 留言定时导入,第四部操作
	 */
	public void fourImportleavewordtonews() {

		int[] dayStart = new int[] { 1, 8, 15, 22 };
		// 可执行的天数，数字

		Calendar calendar = Calendar.getInstance();
		// 指定月份的天数，从"28"至"31",获取当前月左后一天的数字
		int dayLast = calendar.getActualMaximum(Calendar.DATE);
		int[] days = new int[] { 8, 15, 22, dayLast };
		// 必须先排序
		Arrays.sort(days);
		// 获取今天是第几天
		int fristDay = calendar.get(Calendar.DATE);
		// 查看是否可以执行操作
		int key = Arrays.binarySearch(days, fristDay);
		if (key == -1) {
			return;
		}

		StringBuffer fristDate = new StringBuffer();
		fristDate.append(calendar.get(Calendar.YEAR));
		fristDate.append("-");
		fristDate.append(calendar.get(Calendar.MONTH) + 1);
		fristDate.append("-");
		fristDate.append(dayStart[key]);

		StringBuffer lastDate = new StringBuffer();
		lastDate.append(calendar.get(Calendar.YEAR));
		lastDate.append("-");
		lastDate.append(calendar.get(Calendar.MONTH) + 1);
		lastDate.append("-");
		lastDate.append(calendar.get(Calendar.DATE));

		Map<String, Object> params = new HashMap<>();
		params.put("bShow", 1);
		params.put("stategt", 1);
		params.put("stime", fristDate.toString());
		params.put("etime", lastDate.toString());
		List<SdTabLeaveword> leavewords = leavewordService.query("select",
				params);

		String template = "<a href=\"http://www.shandongbusiness.gov.cn/leaveword\"><p style=\"background:#ccc;padding: 5px;\"><br>网友\"{user}\"问：{content},咨询时间：{time}</p>回复：{answer},回复时间：{time2}</a><br>";

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		StringBuffer ls = new StringBuffer();
		for (SdTabLeaveword leaveword : leavewords) {
			String t = template
					.replace("{user}", leaveword.getUser())
					.replace("{content}", leaveword.getContent())
					.replace("{time}", dateFormat.format(leaveword.getTime()))
					.replace("{answer}", leaveword.getAnswer())
					.replace("{time2}",
							dateFormat.format(leaveword.getAnswerTime()));
			ls.append(t);
		}

		SdSubjects subjects = new SdSubjects();
		subjects.setLanguage(1);
		subjects.setFromsite(SystemConfigUtil.get("webName"));
		// 获取时间戳
		int time = (int) (calendar.getTimeInMillis() / 1000);
		subjects.setDate(time);
		subjects.setDisplayorder(time);
		SdContent content = new SdContent();
		content.setContent(ls.toString());
		subjects.setContent(content);
		subjects.setSiteid(1);
		subjects.setItemid(1242);
		if (ValidateUtil.isValid(leavewords) && leavewords.size() > 0) {
			subjectsService.insert(subjects);
		}
	}

	/**
	 * 清空统计记录，只保留最后几个月
	 */
	public void fiveTruncateStatIndex() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, -2);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		statIndexService.deleteByDate(dateFormat.format(calendar.getTime()));
	}

	public static void main(String[] args) {
		// 线程安全，单线程效率低
		StringBuffer buffer = new StringBuffer("sssss");
		System.out.println(buffer.toString());
		// 线程不安全，单线程效率高
		StringBuilder builder = new StringBuilder("sssss");
		System.out.println(builder);

	}
}
