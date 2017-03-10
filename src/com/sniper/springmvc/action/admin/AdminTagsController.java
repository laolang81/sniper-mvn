package com.sniper.springmvc.action.admin;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.model.Tags;
import com.sniper.springmvc.mybatis.service.impl.TagsService;
import com.sniper.springmvc.searchUtil.ChannelSearch;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("${adminPath}/admin-tags")
public class AdminTagsController extends AdminBaseController {

	@Resource
	private TagsService tagsService;

	@RequiresPermissions("admin:tags:view")
	@RequestMapping("/")
	public String index(Map<String, Object> map, ChannelSearch channelSearch)
			throws FileNotFoundException {

		map.put("sniperUrl", "/admin-tags/delete");

		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, String> status = new HashMap<>();

		status.put("true", "启用");
		status.put("false", "禁用");
		toHtml.addMapValue("enabled", status);
		Map<String, String> keys = new HashMap<>();
		keys.put("enabled", "审核");
		toHtml.setKeys(keys);

		Map<String, Object> params = new LinkedHashMap<>();

		if (ValidateUtil.isValid(channelSearch.getName())) {
			params.put("name", "%" + channelSearch.getName() + "%");
		}

		if (ValidateUtil.isValid(channelSearch.getStatus())) {
			params.put("enabled", channelSearch.getStatus());
		}

		int count = tagsService.pageCount(params);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "ctime desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<Tags> lists = tagsService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("sniperMenu", toHtml);
		return forward("/admin/admin-tags/index.jsp");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:tags:insert")
	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map) {

		map.put("tags", new Tags());
		return forward("/admin/admin-tags/save-input.jsp");
	}

	@RequiresPermissions("admin:tags:insert")
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(Tags tags, BindingResult result) {

		try {
			if (result.getErrorCount() > 0) {
				return forward("/admin/admin-tags/save-input.jsp");
			} else {
				tags.setId(FilesUtil.getUUIDName("", false));
				tags.setAdminUser(getAdminUser());
				tagsService.insert(tags);
			}
		} catch (Exception e) {

		}
		return redirect("/admin-tags/insert");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:tags:update")
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) String id,
			Map<String, Object> map) {

		Tags tags = null;
		if (ValidateUtil.isValid(id)) {
			tags = tagsService.get(id);
		} else {
			return redirect("/admin-tags/insert");
		}

		map.put("tags", tags);
		return forward("/admin/admin-tags/save-input.jsp");
	}

	@RequiresPermissions("admin:tags:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(Tags tags, BindingResult result) {
		if (null == tags.getId()) {
			return redirect("/admin-tags/insert");
		}
		try {
			if (result.getErrorCount() > 0) {
				return forward("/admin/admin-tags/save-input.jsp");
			} else {

				tagsService.update(tags);
			}
		} catch (Exception e) {

		}
		return redirect("/admin-tags/update?id=" + tags.getId());
	}

	@RequiresPermissions("admin:tags:delete")
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Map<String, Object> delete(@RequestParam("delid") String[] delid,
			@RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) {
		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":
			try {
				tagsService.batchDelete("delete", delid);
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
					Tags tags = tagsService.get(string);
					tags.setEnabled(DataUtil.stringToBoolean(menuValue));
					result += tagsService.update(tags);
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
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}
			break;
		default:
			break;
		}

		return ajaxResult;
	}
}