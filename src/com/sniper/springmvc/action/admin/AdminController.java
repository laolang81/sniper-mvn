package com.sniper.springmvc.action.admin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.hyperic.sigar.SigarException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.data.DataValues;
import com.sniper.springmvc.enums.UserPostValue;
import com.sniper.springmvc.model.Collect;
import com.sniper.springmvc.model.FullCalendar;
import com.sniper.springmvc.mybatis.service.impl.CollectService;
import com.sniper.springmvc.mybatis.service.impl.FullCalendarService;
import com.sniper.springmvc.mybatis.service.impl.SdTabLeavewordService;
import com.sniper.springmvc.mybatis.service.impl.SdViewSubjectService;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.SystemConfigUtil;
import com.sniper.springmvc.utils.UserDetailsUtils;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * ${adminPath}需要在当前当前类赋值
 * 
 * @author suzhen
 * 
 */
@RequestMapping(value = "${adminPath}")
@Controller
public class AdminController extends AdminBaseController {

	@Resource
	FullCalendarService calendarService;

	@Resource
	SdTabLeavewordService leavewordService;

	@Resource
	SdViewSubjectService viewSubjectService;

	/**
	 * 
	 * @return
	 * @throws SigarException
	 */
	@RequiresPermissions("admin:index:index")
	@RequestMapping("")
	public String index(Map<String, Object> map) {

		// 留言，回复，
		Map<String, Object> leavewordParams = new HashMap<>();
		int commentTotal = leavewordService.pageCount(leavewordParams);

		Map<String, Object> leavewordParams1 = new HashMap<>();
		leavewordParams1.put("state", "1");
		int commentNot = leavewordService.pageCount(leavewordParams1);

		Map<String, Integer> comments = new HashMap<>();
		comments.put("total", commentTotal);
		comments.put("not", commentNot);
		map.put("comments", comments);
		// 事件列表

		HqlUtils hqlUtils5 = new HqlUtils(FullCalendar.class);
		hqlUtils5.getParams().put("uid", getAdminUser().getId());
		int calendarTotal = calendarService.pageCount(hqlUtils5.getParams());
		Map<String, Integer> calendars = new HashMap<>();
		calendars.put("total", calendarTotal);

		DateFormat shortFormat = new SimpleDateFormat("yyyy-MM-dd");

		HqlUtils hqlUtils6 = new HqlUtils(FullCalendar.class);
		hqlUtils6.getParams().put("uid", getAdminUser().getId());

		Calendar calendar = shortFormat.getCalendar();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 8);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		hqlUtils6.getParams().put("start",
				shortFormat.format(calendar.getTime()));
		int calendarTotay = calendarService.pageCount(hqlUtils6.getParams());
		calendars.put("totay", calendarTotay);
		map.put("calendars", calendars);

		// 调用一些常用的数据

		return forward("/admin/admin/index.ftl");
	}

	/**
	 * 获取首页顶部信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("topMenuValue")
	public Map<String, Object> topMemuValue() {

		Map<String, Object> result = new HashMap<>();
		// 获取新闻总数，获取未审核新闻数量
		Map<String, Object> params = new HashMap<>();
		List<Integer> looks = new ArrayList<>();
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		// 确定当前用户拥有的处室
		Map<String, String> deps = getDep();
		List<String> depss = new ArrayList<>();
		for (Entry<String, String> entry : deps.entrySet()) {
			depss.add(entry.getKey());
		}

		if (detailsUtils.validRole(DataValues.ROLE_ADMIN)) {
			// 0本该没有，但是怕
			looks.add(0);
			// 1是用户默认发布状态
			looks.add(1);
		} else {
			// 不是管理员
			looks.add(getUserPostInfo(UserPostValue.READ_POST));
			// 处室限制
			// params.put("authorid", getAdminUser().getId());
			params.put("siteid", depss);
		}
		params.put("lookthroughed", looks);
		int postAduitCount = viewSubjectService.pageCount(params);
		result.put("postAuditCount", postAduitCount);
		// 留言
		params.clear();
		params.put("state", 1);
		params.put("answer", 1);
		params.put("depids", depss);

		int commentCount = leavewordService.pageCount(params);
		result.put("commentCount", commentCount);
		return result;
	}

	/**
	 * 处理用户每次访问的url
	 * 
	 * @param title
	 * @param url
	 * @return
	 */
	@ResponseBody
	@RequestMapping("collect")
	public List<Collect> collect(
			@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "url", required = false) String url) {
		List<Collect> collects = new ArrayList<>();
		if (ValidateUtil.isValid(title) && ValidateUtil.isValid(title)) {
			title = title.replace("\\s", "");
			if (title.indexOf("|") > -1) {
				title = title.substring(0, title.indexOf("|"));
			}
			collectService.insertValue(url, title, CollectService.URL_MENU,
					getAdminUser());
		}

		int historyCollectNum = SystemConfigUtil.getInt("historyCollectNum");
		if (historyCollectNum <= 0) {
			historyCollectNum = 20;
		}

		Map<String, Object> params = new HashMap<>();
		params.put("pageSize", historyCollectNum);
		params.put("uid", getAdminUser().getId());
		params.put("order", "num desc");
		collects = collectService.query("select", params);
		return collects;
	}

}