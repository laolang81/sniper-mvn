package com.sniper.springmvc.freemarker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sniper.springmvc.model.SdPageIndex;
import com.sniper.springmvc.model.SdSubjects;
import com.sniper.springmvc.model.SdViewSubject;
import com.sniper.springmvc.mybatis.service.impl.SdDepartmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdItemsService;
import com.sniper.springmvc.mybatis.service.impl.SdPageIndexService;
import com.sniper.springmvc.mybatis.service.impl.SdSubjectsService;
import com.sniper.springmvc.mybatis.service.impl.SdViewSubjectService;
import com.sniper.springmvc.security.SpringContextUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 专题模板数据解析
 * 
 * @author suzhen
 * 
 */
public class TopicResolver {

	static SdViewSubjectService viewSubjectService;
	static {
		viewSubjectService = (SdViewSubjectService) SpringContextUtil
				.getBean(SdViewSubjectService.class);
	}

	static SdSubjectsService subjectService;
	static {
		subjectService = (SdSubjectsService) SpringContextUtil
				.getBean(SdSubjectsService.class);
	}

	static SdDepartmentsService departmentsService;
	static {
		departmentsService = (SdDepartmentsService) SpringContextUtil
				.getBean(SdDepartmentsService.class);
	}

	static SdItemsService itemsService;
	static {
		itemsService = (SdItemsService) SpringContextUtil
				.getBean(SdItemsService.class);
	}

	static SdPageIndexService pageIndexService;
	static {
		pageIndexService = (SdPageIndexService) SpringContextUtil
				.getBean(SdPageIndexService.class);
	}

	/**
	 * 获取图片新闻
	 * 
	 * @param dep
	 *            处室id
	 * @param item
	 *            栏目id
	 * @param limit
	 *            数量
	 * @return
	 */
	public static List<SdViewSubject> getSlices(Integer dep, Integer item,
			int limit) {

		Map<String, Object> params = new HashMap<>();
		if (ValidateUtil.isValid(dep)) {
			List<Integer> deps = new ArrayList<>();
			deps.add(dep);
			params.put("siteid", deps);
		}

		if (ValidateUtil.isValid(item)) {
			List<Integer> sitems = new ArrayList<>();
			sitems.add(item);
			params.put("itemid", sitems);
		}
		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("lookthroughed", lookthroughed);
		params.put("pageSize", limit);
		params.put(SdViewSubjectService.FILED_NAME_IS_IMG, 1);
		params.put("order", "s.displayorder desc");
		return viewSubjectService.query("select", params);
	}

	/**
	 * 获取指定处室，栏目，的新闻
	 * 
	 * @param dep
	 *            处室id
	 * @param item
	 *            栏目id
	 * @param limit
	 *            数量
	 * @return
	 */
	public List<SdViewSubject> getSubjects(Integer dep, Integer item, int limit) {

		Map<String, Object> params = new HashMap<>();
		if (ValidateUtil.isValid(dep)) {
			List<Integer> deps = new ArrayList<>();
			deps.add(dep);
			params.put("siteid", deps);
		}

		if (ValidateUtil.isValid(item)) {
			List<Integer> sitems = new ArrayList<>();
			sitems.add(item);
			params.put("itemid", sitems);
		}
		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("lookthroughed", lookthroughed);
		params.put("pageSize", limit);
		params.put("order", "s.displayorder desc");
		return viewSubjectService.query("select", params);
	}

	/**
	 * 获取新闻根据聚合栏目id
	 * 
	 * @param pid
	 * @param limit
	 * @return
	 */
	public List<SdViewSubject> getSubjectsByPage(String pid, int limit) {
		Map<String, Object> params = new HashMap<>();

		SdPageIndex pageIndex = pageIndexService.get(pid);
		if (pageIndex == null) {
			return null;
		}
		String items = pageIndex.getItemid();
		if (ValidateUtil.isValid(items)) {
			String[] itemss = items.split(",");
			List<Integer> sitems = new ArrayList<>();
			for (int i = 0; i < itemss.length; i++) {
				sitems.add(Integer.valueOf(itemss[i]));
			}
			params.put("itemid", sitems);
		}
		List<Integer> lookthroughed = new ArrayList<>();
		lookthroughed.add(2);
		params.put("lookthroughed", lookthroughed);
		params.put("pageSize", limit);
		params.put("order", "s.displayorder desc");
		return viewSubjectService.query("select", params);
	}

	/**
	 * 吧时间戳转成时间字符串
	 * 
	 * @param dateInt
	 * @return
	 */
	public static String timeMillisToString(int dateInt) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (dateInt == 0) {
			return dateFormat.format(new Date());
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateInt * 1000l);
		return dateFormat.format(calendar.getTime());
	}

	/**
	 * 获取新闻地址
	 * 
	 * @param subject
	 * @return
	 */
	public String subjectUrl(SdViewSubject subject) {

		if (subject == null) {
			return "/";
		}
		String html = "/public/html/news/201701/388648.html";
		String timeStr = timeMillisToString(subject.getDate());
		timeStr = timeStr.substring(0, 7).replace("_", "");
		return html.replace("201701", timeStr).replace("388648",
				subject.getId() + "");
	}

	/**
	 * 读取新闻
	 * 
	 * @param sid
	 * @return
	 */
	public SdSubjects getSubject(String sid) {
		return subjectService.get(sid);
	}
}
