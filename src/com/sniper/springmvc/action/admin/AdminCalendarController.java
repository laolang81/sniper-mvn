package com.sniper.springmvc.action.admin;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.model.FullCalendar;
import com.sniper.springmvc.mybatis.service.impl.FullCalendarService;
import com.sniper.springmvc.utils.BeanUtils;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("${adminPath}/admin-calendar")
public class AdminCalendarController extends AdminBaseController {

	@Resource
	FullCalendarService calendarService;

	@RequiresPermissions("admin:calendar:view")
	@RequestMapping("")
	public String calendar() {

		return forward("/admin/admin-calendar/calendar.ftl");
	}

	// @RequiresPermissions("admin:calendar:events")
	@ResponseBody
	@RequestMapping("events")
	public Map<String, Object> events(
			@RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "end", required = false) String end)
			throws ParseException {

		DateFormat longFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat shortFormat = new SimpleDateFormat("yyyy-MM-dd");

		Map<String, Object> params = new HashMap<>();
		if (start.length() > 10) {
			longFormat.parse(start);
			params.put("start", start);
		} else {
			shortFormat.parse(start);
			params.put("start", start);
		}

		if (start.length() > 10) {
			longFormat.parse(end);
			params.put("end", end);
		} else {
			shortFormat.parse(end);
			params.put("end", end);
		}

		params.put("uid", getAdminUser().getId());

		List<FullCalendar> calendars = calendarService.query("select", params);
		Map<String, Object> resultMap = new HashMap<>();
		List<Map<String, Object>> result = new ArrayList<>();
		Calendar calendar = Calendar.getInstance();

		if (calendars.size() == 0) {
			return resultMap;
		}

		for (FullCalendar fullCalendar : calendars) {
			Map<String, Object> map1 = new HashMap<>();
			map1.put("id", fullCalendar.getId());
			map1.put("title", fullCalendar.getTitle());
			// 判断日式是否含有小时
			calendar.setTime(fullCalendar.getStartDate());
			if (calendar.get(Calendar.HOUR_OF_DAY) > 0) {
				map1.put("start",
						longFormat.format(fullCalendar.getStartDate()));
			} else {
				map1.put("start",
						shortFormat.format(fullCalendar.getStartDate()));
				map1.put("allDay", false);
			}

			if (fullCalendar.getEndDate() != fullCalendar.getStartDate()) {
				calendar.setTime(fullCalendar.getEndDate());
				if (calendar.get(Calendar.HOUR_OF_DAY) > 0) {
					map1.put("end",
							longFormat.format(fullCalendar.getEndDate()));
				} else {
					map1.put("end",
							shortFormat.format(fullCalendar.getEndDate()));
				}
			}
			if (ValidateUtil.isValid(fullCalendar.getClassName())) {
				map1.put("className", fullCalendar.getClassName());
			}
			result.add(map1);
		}
		resultMap.put("code", 200);
		resultMap.put("result", result);
		return resultMap;
	}

	@ResponseBody
	@RequiresPermissions("admin:calendar:eventGet")
	@RequestMapping(value = "eventGet", method = RequestMethod.POST)
	public FullCalendar eventGet(FullCalendar fullCalendar) {
		FullCalendar calendar = calendarService.get(fullCalendar.getId());
		if (calendar == null) {
			return null;
		}
		return calendar;
	}

	@RequiresPermissions("admin:calendar:eventInsert")
	@ResponseBody
	@RequestMapping(value = "eventInsert", method = RequestMethod.POST)
	public FullCalendar eventInsert(FullCalendar calendar) {

		calendar.setAdminUser(getAdminUser());
		Calendar calendar2 = Calendar.getInstance();
		if (!ValidateUtil.isValid(calendar.getStartDate())) {
			calendar2.setTime(new Date());
			calendar2.set(Calendar.HOUR_OF_DAY, 8);
			calendar2.set(Calendar.MINUTE, 0);
			calendar2.set(Calendar.SECOND, 0);
			calendar.setStartDate(calendar2.getTime());
		}

		if (!ValidateUtil.isValid(calendar.getEndDate())) {
			calendar2.setTime(new Date());
			calendar2.set(Calendar.DAY_OF_MONTH,
					calendar2.get(Calendar.DAY_OF_MONTH) + 1);
			calendar2.set(Calendar.HOUR_OF_DAY, 8);
			calendar2.set(Calendar.MINUTE, 0);
			calendar2.set(Calendar.SECOND, 0);
			calendar.setEndDate(calendar2.getTime());
		}
		calendar.setId(FilesUtil.getUUIDName("", false));
		calendarService.insert(calendar);
		calendar.setAdminUser(null);
		return calendar;
	}

	@RequiresPermissions("admin:calendar:eventUpdate")
	@ResponseBody
	@RequestMapping(value = "eventUpdate", method = RequestMethod.POST)
	public Map<String, Object> eventUpdate(FullCalendar calendar) {

		Map<String, Object> map = new HashMap<>();
		map.put("code", 0);
		if (calendar.getId() != null) {
			try {
				map.put("code", 200);
				FullCalendar fullCalendar = calendarService.get(calendar
						.getId());
				BeanUtils.copyProperties(calendar, fullCalendar);
				calendarService.update(fullCalendar);
			} catch (Exception e) {
				LOGGER.error("事件修改失败：" + e.getMessage());
			}

		}
		return map;
	}

	@RequiresPermissions("admin:calendar:eventDelete")
	@ResponseBody
	@RequestMapping(value = "eventDelete", method = RequestMethod.POST)
	public Map<String, Object> eventDelete(FullCalendar calendar) {

		Map<String, Object> map = new HashMap<>();
		map.put("code", 0);
		if (calendar.getId() != null) {
			try {
				map.put("code", 200);
				calendarService.delete(calendar.getId());
			} catch (Exception e) {
				LOGGER.error("事件删除失败：" + e.getMessage());
			}
		}
		return map;
	}
}