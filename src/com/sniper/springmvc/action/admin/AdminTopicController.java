package com.sniper.springmvc.action.admin;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.data.DataValues;
import com.sniper.springmvc.model.Topic;
import com.sniper.springmvc.mybatis.service.impl.TopicService;
import com.sniper.springmvc.searchUtil.ChannelSearch;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("${adminPath}/admin-topic")
public class AdminTopicController extends AdminBaseController {

	@Resource
	TopicService topicService;

	@RequiresPermissions("admin:topic:view")
	@RequestMapping("/")
	public String index(Map<String, Object> map, ChannelSearch channelSearch)
			throws FileNotFoundException {

		map.put("sniperUrl", "/admin-topic/delete");

		ParamsToHtml toHtml = new ParamsToHtml();

		toHtml.addMapValue("enabled", DataValues.YES_NO_BOOLEAN);
		Map<String, String> keys = new HashMap<>();
		keys.put("enabled", "启用");
		toHtml.setKeys(keys);

		Map<String, Object> params = new LinkedHashMap<>();

		if (ValidateUtil.isValid(channelSearch.getName())) {
			params.put("name", "%" + channelSearch.getName() + "%");
		}

		if (ValidateUtil.isValid(channelSearch.getStatus())) {
			params.put("enabled", channelSearch.getStatus());
		}

		int count = topicService.pageCount(params);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "stime desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<Topic> lists = topicService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("sniperMenu", toHtml);
		return forward("/admin/admin-topic/index.jsp");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:topic:insert")
	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map) {

		map.put("topic", new Topic());
		return forward("/admin/admin-topic/save-input.jsp");
	}

	@RequiresPermissions("admin:topic:insert")
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(Topic topic, BindingResult result) {
		List<String> errors = new ArrayList<>();
		try {
			if (result.getErrorCount() > 0) {
				// 设置选中的栏目
				List<FieldError> fieldErrors = result.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					errors.add(fieldError.getDefaultMessage());
				}
				errors.add("添加失败");
				return forward("/admin/admin-topic/save-input.jsp");
			} else {
				topic.setId(FilesUtil.getUUIDName("", false));
				topicService.insert(topic);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirect("/admin-topic/insert");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:topic:update")
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) String id,
			Map<String, Object> map) {

		Topic topic = null;
		if (ValidateUtil.isValid(id)) {
			topic = topicService.get(id);
		} else {
			return redirect("/admin-topic/insert");
		}

		map.put("topic", topic);
		return forward("/admin/admin-topic/save-input.jsp");
	}

	@RequiresPermissions("admin:topic:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(Topic topic, BindingResult result) {
		if (null == topic.getId()) {
			return redirect("/admin-topic/insert");
		}
		List<String> errors = new ArrayList<>();
		try {
			if (result.getErrorCount() > 0) {
				// 设置选中的栏目
				List<FieldError> fieldErrors = result.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					errors.add(fieldError.getDefaultMessage());
				}
				errors.add("添加失败");
				return forward("/admin/admin-topic/save-input.jsp");
			} else {
				topicService.update(topic);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirect("/admin-topic/update?id=" + topic.getId());
	}

	@RequiresPermissions("admin:topic:delete")
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Map<String, Object> delete(@RequestParam("delid") String[] delid,
			@RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) {
		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":
			try {
				topicService.batchDelete("delete", delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;
		case "enabled":
			try {
				int result = 0;
				for (String string : delid) {
					Topic topic = topicService.get(string);
					topic.setEnabled(DataUtil.stringToBoolean(menuValue));
					result += topicService.update(topic);
					Map<String, Object> fieldHtml = new HashMap<>();
					fieldHtml.put(string,
							DataValues.YES_NO_BOOLEAN.get(menuValue));
					ajaxResult.put(menuType, fieldHtml);
				}
				if (result > 0) {
					ajaxResult.put("code", 1);
					ajaxResult.put("msg", messageSource.getMessage(
							"action.success", null, locale));
				} else {
					ajaxResult.put("code", 1);
					ajaxResult.put("msg", "Failed");
				}

			} catch (Exception e) {
				e.printStackTrace();
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "Failed:" + e.getMessage());
			}
			break;
		default:
			break;
		}

		return ajaxResult;
	}
}
