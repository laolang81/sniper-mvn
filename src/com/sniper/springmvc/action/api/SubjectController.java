package com.sniper.springmvc.action.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.model.SdPageIndex;
import com.sniper.springmvc.model.SdSubjects;
import com.sniper.springmvc.model.SdViewSubject;
import com.sniper.springmvc.mybatis.service.impl.SdPageIndexService;
import com.sniper.springmvc.mybatis.service.impl.SdSubjectsService;
import com.sniper.springmvc.mybatis.service.impl.SdViewSubjectService;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("/apipost")
public class SubjectController extends ApiBaseController {

	@Resource
	SdViewSubjectService viewSubjectService;

	@Resource
	SdSubjectsService subjectsService;

	@Autowired
	HttpServletResponse response;

	@Resource
	SdPageIndexService pageIndexService;

	public void name() {

	}

	/**
	 * 首页幻灯获取
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("slideImages")
	public List<Map<String, String>> slideImages() {
		return getSlideImages(5);
	}

	@ResponseBody
	@RequestMapping("slideImages/{id}")
	public List<Map<String, String>> slideImagesLimit(
			@PathVariable("id") Integer limit) {
		return getSlideImages(limit);
	}

	/**
	 * 获取图片新闻
	 * 
	 * @param response
	 * @param limit
	 * @return
	 */
	private List<Map<String, String>> getSlideImages(Integer limit) {
		response.addHeader("Access-Control-Allow-Origin", "*");

		if (!ValidateUtil.isValid(limit) || limit == 0) {
			limit = 5;
		}
		List<SdViewSubject> subjects = viewSubjectService.getSlides(0, limit);

		List<Map<String, String>> result = new ArrayList<>();
		for (SdViewSubject subject : subjects) {
			Map<String, String> s = new HashMap<>();
			s.put("sid", subject.getId().toString());
			s.put("subject", subject.getSubject());
			s.put("date", subject.getDate().toString());
			List<SdAttachments> attachments = subject.getAttachments();
			if (attachments != null) {
				for (SdAttachments sdAttachments : attachments) {
					s.put("aid", sdAttachments.getAid().toString());
					s.put("filename", sdAttachments.getFilename());
					break;
				}
			}
			result.add(s);
		}
		return result;
	}

	/**
	 * 获取置顶新闻
	 * 
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("newsTop")
	public Map<String, String> newsTop() {

		response.addHeader("Access-Control-Allow-Origin", "*");
		List<SdViewSubject> subjects = viewSubjectService.getTops(1);

		Map<String, String> s = new HashMap<>();
		for (SdViewSubject subject : subjects) {
			s.put("sid", subject.getId().toString());
			s.put("subject", HtmlUtils.htmlEscape(subject.getSubject()));
			s.put("date", subject.getDate().toString());
			s.put("url", subject.getUrl());
			break;
		}
		return s;
	}

	/**
	 * 获取重要新闻
	 * 
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = { "newsTops" })
	public List<Map<String, String>> newsTops() {
		return getNewsTops(5);
	}

	@ResponseBody
	@RequestMapping("newsTops/{id:[0-9\\s]*}")
	public List<Map<String, String>> newsTopsLimit(
			@PathVariable("id") Integer limit) {
		return getNewsTops(limit);
	}

	/**
	 * 获取制定新闻，重要新闻
	 * 
	 * @param response
	 * @param limit
	 * @return
	 */
	private List<Map<String, String>> getNewsTops(int limit) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		List<SdViewSubject> subjects = viewSubjectService.getBhot(limit);
		List<Map<String, String>> result = new ArrayList<>();

		for (SdViewSubject subject : subjects) {
			Map<String, String> s = new HashMap<>();
			s.put("sid", subject.getId().toString());
			s.put("subject", HtmlUtils.htmlEscape(subject.getSubject()));
			s.put("date", subject.getDate().toString());
			s.put("url", subject.getUrl());
			result.add(s);

		}
		return result;
	}

	@ResponseBody
	@RequestMapping("slideImagesValue/{id}")
	public Map<String, Object> slideImagesValue(@PathVariable("id") String id) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> subjects = subjectsService.getSubject(id);
		SdSubjects sdSubjects = (SdSubjects) subjects.get("a");
		Map<String, Object> result = new HashMap<>();
		result.put("view", subjects);
		List<SdAttachments> attachments = sdSubjects.getAttachments();
		List<Map<String, String>> atts = new ArrayList<>();
		if (ValidateUtil.isValid(attachments)) {
			for (SdAttachments attachment : attachments) {
				Map<String, String> a = new HashMap<>();
				a.put("filename", attachment.getFilename());
				a.put("prefilename", attachment.getDescription());
				atts.add(a);
			}
		}
		result.put("attachments", atts);
		return result;
	}

	/**
	 * 根据频道获取新闻,支持分页
	 */
	@ResponseBody
	@RequestMapping("channelPost/{id}")
	public Map<String, Object> channelPost(@PathVariable("id") Integer id) {

		int limit = 30;
		String order = "displayorder";
		return getPost(id, limit, order);
	}

	@ResponseBody
	@RequestMapping("channelPost/{id}/{limit}")
	public Map<String, Object> channelPostLimit(@PathVariable("id") Integer id,
			@PathVariable("limit") Integer limit) {
		if (!ValidateUtil.isValid(limit)) {
			limit = 30;
		}
		String order = "displayorder";
		return getPost(id, limit, order);
	}

	@ResponseBody
	@RequestMapping("channelPost/{id}/{limit}/{order}")
	public Map<String, Object> channelPostLimitOrder(
			@PathVariable("id") Integer id,
			@PathVariable("limit") Integer limit,
			@PathVariable("order") String order) {
		if (!ValidateUtil.isValid(limit)) {
			limit = 30;
		}
		if (!ValidateUtil.isValid(order)) {
			order = "displayorder";
		}

		return getPost(id, limit, order);
	}

	/**
	 * 获取分页新闻
	 * 
	 * @param id
	 * @param limit
	 * @param order
	 */
	private Map<String, Object> getPost(Integer id, int limit, String order) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		List<Integer> look = new ArrayList<>();
		look.add(2);
		params.put("language", 1);
		params.put("lookthroughed", look);
		List<Integer> itemids = new ArrayList<>();
		itemids.add(id);
		params.put("itemid", itemids);

		int count = viewSubjectService.pageCount(params);
		PageUtil page = new PageUtil(count, limit);
		page.setRequest(request);
		String pageHtml = page.show();

		params.put("order", "s." + order + " desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdViewSubject> lists = viewSubjectService.pageList(params);
		Map<String, Map<String, String>> posts = new HashMap<>();
		for (SdViewSubject subject : lists) {
			Map<String, String> p = new HashMap<>();
			p.put("id", subject.getId().toString());
			p.put("subject", subject.getSubject());
			p.put("url", subject.getUrl());
			p.put("date", subject.getDate().toString());
			posts.put(subject.getIcId().toString(), p);
		}

		result.put("post", posts);
		result.put("total", count);
		result.put("page", pageHtml);
		result.put("limit", limit);
		return result;
	}

	/**
	 * 读取聚合栏目
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("channelGroupPost/{id}")
	public Map<String, Object> channelGroupPost(@PathVariable("id") Integer id) {
		int limit = 30;
		String order = "displayorder";
		return getGroupPost(id, limit, order);
	}

	@ResponseBody
	@RequestMapping("channelGroupPost/{id}/{limit}")
	public Map<String, Object> channelGroupPostLimit(
			@PathVariable("id") Integer id, @PathVariable("limit") Integer limit) {
		if (!ValidateUtil.isValid(limit)) {
			limit = 30;
		}

		String order = "displayorder";
		return getGroupPost(id, limit, order);
	}

	@ResponseBody
	@RequestMapping("channelGroupPost/{id}/{limit}/{order}")
	public Map<String, Object> channelGroupPostLimitOrder(
			@PathVariable("id") Integer id,
			@PathVariable("limit") Integer limit,
			@PathVariable("order") String order) {
		if (!ValidateUtil.isValid(limit)) {
			limit = 30;
		}
		if (!ValidateUtil.isValid(order)) {
			order = "displayorder";
		}
		return getGroupPost(id, limit, order);
	}

	/**
	 * 获取新闻列表根据聚合栏目
	 * 
	 * @param id
	 * @param limit
	 * @param order
	 * @return
	 */
	private Map<String, Object> getGroupPost(Integer id, int limit, String order) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		Map<String, Object> result = new HashMap<>();
		Map<String, Object> params = new HashMap<>();
		List<Integer> look = new ArrayList<>();
		look.add(2);
		params.put("language", 1);
		params.put("lookthroughed", look);
		// 获取聚合栏目
		List<String> itemids = new ArrayList<>();
		SdPageIndex index = pageIndexService.get(id.toString());
		if (index != null && ValidateUtil.isValid(index.getItemid())) {
			String[] is = index.getItemid().split(",");
			itemids = Arrays.asList(is);
		} else {
			return null;
		}
		params.put("itemid", itemids);

		int count = viewSubjectService.pageCount(params);
		PageUtil page = new PageUtil(count, limit);
		page.setRequest(request);
		String pageHtml = page.show();

		params.put("order", "s." + order + " desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdViewSubject> lists = viewSubjectService.pageList(params);
		Map<String, Map<String, String>> posts = new HashMap<>();
		for (SdViewSubject subject : lists) {
			Map<String, String> p = new HashMap<>();
			p.put("id", subject.getId().toString());
			p.put("subject", subject.getSubject());
			p.put("url", subject.getUrl());
			p.put("date", subject.getDate().toString());
			posts.put(subject.getIcId().toString(), p);
		}

		result.put("post", posts);
		result.put("total", count);
		result.put("page", pageHtml);
		result.put("limit", limit);
		return result;
	}
}
