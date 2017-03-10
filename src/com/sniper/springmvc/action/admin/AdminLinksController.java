package com.sniper.springmvc.action.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.data.DataValues;
import com.sniper.springmvc.model.SdLink;
import com.sniper.springmvc.model.SdLinkGroup;
import com.sniper.springmvc.mybatis.service.impl.SdLinkGroupService;
import com.sniper.springmvc.mybatis.service.impl.SdLinksService;
import com.sniper.springmvc.searchUtil.ChannelSearch;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.HtmlUtil;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("${adminPath}/admin-links")
public class AdminLinksController extends AdminBaseController {

	@Resource
	SdLinksService linkService;

	@Resource
	SdLinkGroupService linkGroupService;
	static Map<String, String> selectMap = new HashMap<>();
	static {
		selectMap.put("7", "/approot/www/ROOT/国务院");
		selectMap.put("8", "/approot/www/ROOT/省政府");
		selectMap.put("9", "/approot/www/ROOT/市主管部门");
		selectMap.put("10", "/approot/www/ROOT/各市商务局");
		selectMap.put("11", "/approot/www/ROOT/各市招商局");
	}

	/**
	 * 获取友情链接组
	 * 
	 * @return
	 */
	public List<SdLinkGroup> getChannels() {
		List<SdLinkGroup> channels = new ArrayList<>();
		if (channels.size() == 0) {
			channels = linkGroupService.query("select", null);
		}
		return channels;
	}

	@ModelAttribute
	@Override
	public void init(Map<String, Object> map) {
		super.init(map);
		map.put("channels", getChannels());
	}

	@RequiresPermissions("admin:links:view")
	@RequestMapping("/")
	public String index(Map<String, Object> map, ChannelSearch search) {

		map.put("sniperUrl", "/admin-links/delete");
		ParamsToHtml toHtml = new ParamsToHtml();
		Map<String, String> enabled = new HashMap<>();
		enabled.put("true", "是");
		enabled.put("false", "否");
		toHtml.addMapValue("enabled", enabled);

		Map<String, String> channelsMap = linkGroupService
				.mapFormat(getChannels());

		toHtml.addMapValue("channels", channelsMap);
		toHtml.getKeys().put("enabled", "状态");
		toHtml.getKeys().put("channels", "频道");

		Map<String, Object> params = new HashMap<>();
		if (ValidateUtil.isValid(search.getType())) {
			params.put("upid", search.getType());
		}

		if (ValidateUtil.isValid(search.getStatus())) {
			params.put("enabled", search.getStatus());
		}

		if (ValidateUtil.isValid(search.getName())) {
			params.put("name", "%" + search.getName() + "%");
		}
		int count = linkService.pageCount(params);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "l.linkid desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdLink> lists = linkService.pageList(params);

		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("sniperMenu", toHtml);
		map.put("search", search);

		return forward("/admin/admin-links/index.jsp");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 * @throws IOException
	 */
	@RequiresPermissions("admin:links:insert")
	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map) {

		map.put("links", new SdLink());
		Map<String, String> groups = linkGroupService.mapFormat(getChannels());
		map.put("groups", groups);
		map.put("yes_no", DataValues.YES_NO);
		return forward("/admin/admin-links/save-input.jsp");
	}

	public void importSelect() throws IOException {
		for (Entry<String, String> entry : selectMap.entrySet()) {
			Map<String, String> selects = HtmlUtil
					.selectAsMap(entry.getValue());
			SdLinkGroup group = linkGroupService.get(entry.getKey());
			for (Entry<String, String> select : selects.entrySet()) {
				SdLink link = new SdLink();
				link.setLinkGroup(group);
				link.setUrl(select.getKey());
				link.setName(select.getValue());
				link.setDescription(select.getValue());
				link.setDisplayorder(10);
				link.setViewnum(0);
				link.setLogo("");
				try {
					if (linkService.getLink(select.getValue(), select.getKey()) == null) {
						linkService.insert(link);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	@RequiresPermissions("admin:links:insert")
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(SdLink links, BindingResult result,
			Map<String, Object> map) {

		try {
			if (result.getFieldErrorCount() > 0) {
				Map<String, String> groups = linkGroupService
						.mapFormat(getChannels());
				map.put("groups", groups);
				map.put("yes_no", DataValues.YES_NO);
				return forward("/admin/admin-links/save-input.jsp");
			} else {
				linkService.insert(links);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return redirect("/admin-links/insert");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:links:update")
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) String id,
			SdLink links, Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {

			links = linkService.get(id);
		} else {
			return redirect("/admin-links/insert");
		}

		map.put("yes_no", DataValues.YES_NO);
		map.put("links", links);
		Map<String, String> groups = linkGroupService.mapFormat(getChannels());
		map.put("groups", groups);
		return forward("/admin/admin-links/save-input.jsp");
	}

	@RequiresPermissions("admin:links:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(SdLink links, BindingResult result,
			Map<String, Object> map) {

		try {
			if (result.getFieldErrorCount() > 0) {
				Map<String, String> groups = linkGroupService
						.mapFormat(getChannels());
				map.put("groups", groups);
				map.put("yes_no", DataValues.YES_NO);
				return forward("/admin/admin-links/save-input.jsp");
			} else {
				linkService.update(links);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirect("/admin-links/update?id=" + links.getLinkid());
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:links:delete")
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Map<String, Object> delete(@RequestParam("delid") String[] delid,
			@RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) {

		// code 小于1表示有错误,大于0表示ok,==0表示未操作
		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":

			try {
				linkService.batchDelete("delete", delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;
		case "enabled":
			try {
				for (String string : delid) {
					SdLink links = linkService.get(string);
					links.setViewnum(DataUtil.stringToInteger(menuValue));
					linkService.update(links);
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}

			break;
		case "channels":
			Map<String, String> result = new HashMap<>();
			Map<String, String> groups = linkGroupService
					.mapFormat(getChannels());
			for (String string : delid) {
				SdLink ad = linkService.get(string);
				ad.setLinkid(Integer.valueOf(menuValue));
				linkService.update(ad);
				result.put(menuType, groups.get(menuValue));
				ajaxResult.put(string, result);
			}
			ajaxResult.put("code", 1);
			ajaxResult.put("msg", "success");
			break;

		default:
			break;
		}
		return ajaxResult;
	}

	@RequiresPermissions("admin:linkgroup:view")
	@RequestMapping("linkgroup")
	public String linkgroup(Map<String, Object> map, ChannelSearch search) {

		map.put("sniperUrl", "/admin-links/groupdelete");

		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, Object> params = new HashMap<>();

		if (ValidateUtil.isValid(search.getName())) {
			params.put("name", "%" + search.getName() + "%");
		}
		int count = linkGroupService.pageCount(params);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "lt_id desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdLinkGroup> lists = linkGroupService.pageList(params);

		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("sniperMenu", toHtml);
		map.put("search", search);

		return forward("/admin/admin-links/group.jsp");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:linkgroup:insert")
	@RequestMapping(value = "groupinsert", method = RequestMethod.GET)
	public String groupInsert(Map<String, Object> map) {

		map.put("links", new SdLinkGroup());
		return forward("/admin/admin-links/group-input.jsp");
	}

	@RequiresPermissions("admin:linkgroup:insert")
	@RequestMapping(value = "groupinsert", method = RequestMethod.POST)
	public String groupInsert(SdLinkGroup links, BindingResult result) {

		try {
			if (result.getErrorCount() > 0) {
				return forward("/admin/admin-links/group-input.jsp");
			} else {
				linkGroupService.insert(links);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return redirect("/admin-links/groupinsert");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:linkgroup:update")
	@RequestMapping(value = "groupupdate", method = RequestMethod.GET)
	public String groupupdate(
			@RequestParam(value = "id", required = false) String id,
			SdLinkGroup links, Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {
			links = linkGroupService.get(id);
		} else {
			return redirect("/admin-links/groupinsert");
		}

		map.put("links", links);
		return forward("/admin/admin-links/group-input.jsp");
	}

	@RequiresPermissions("admin:linkgroup:update")
	@RequestMapping(value = "groupupdate", method = RequestMethod.POST)
	public String groupupdate(SdLinkGroup links, BindingResult result) {

		try {
			if (result.getErrorCount() > 0) {
				return forward("/admin/admin-links/group-input.jsp");
			} else {
				linkGroupService.update(links);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return redirect("/admin-links/groupupdate?id=" + links.getId());
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:linkgroup:delete")
	@ResponseBody
	@RequestMapping(value = "groupdelete", method = RequestMethod.POST)
	public Map<String, Object> groupdelete(
			@RequestParam("delid") String[] delid,
			@RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) {

		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":

			try {
				linkGroupService.batchDelete("delete", delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;

		default:
			break;
		}
		return ajaxResult;
	}

}