package com.sniper.springmvc.action.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.model.AdminRight;
import com.sniper.springmvc.searchUtil.AdminGroupSearch;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.TreeZTreeUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("${adminPath}/admin-right")
public class AdminRightController extends AdminBaseController {

	private static List<String> targets = new ArrayList<>();
	static {
		targets.add("_self");
		targets.add("_blank");
	}

	/**
	 * 存放整个系统的菜单
	 */
	public String getTreeRightMap() {
		TreeZTreeUtil treeUtil = new TreeZTreeUtil();
		Map<String, Object> params = new HashMap<>();
		params.put("order", "sort asc");
		treeUtil.setAdminRights(adminRightService.query("select", params));
		treeUtil.initAll();
		return treeUtil.getTreeNodesAll();
	}

	@RequiresPermissions("admin:right:view")
	@RequestMapping("/")
	public String index(Map<String, Object> map, AdminGroupSearch search) {

		map.put("groupSearch", search);
		map.put("sniperUrl", "/admin-right/delete");

		ParamsToHtml pth = new ParamsToHtml();
		Map<String, String> menu = new HashMap<>();

		menu.put("true", "是");
		menu.put("false", "否");

		pth.addMapValue("theMenu", menu);

		Map<String, String> show = new HashMap<>();

		show.put("true", "是");
		show.put("false", "否");
		pth.addMapValue("theShow", show);

		pth.addMapValue("type", RIGHTTYPE);

		Map<String, String> keys = new HashMap<>();
		keys.put("theMenu", "是否是菜单");
		keys.put("theShow", "是否是显示");
		keys.put("type", "类型");

		pth.setKeys(keys);
		//
		map.put("sniperMenu", pth);

		Map<String, Object> params = new HashMap<>();
		if (ValidateUtil.isValid(search.getIsShow())) {
			params.put("show", search.getIsShow());
		}

		if (ValidateUtil.isValid(search.getIsMenu())) {
			params.put("menu", search.getIsMenu());
		}

		if (ValidateUtil.isValid(search.getType())) {
			params.put("type", search.getType());
		}

		if (ValidateUtil.isValid(search.getGroupName())) {
			params.put("name", "%" + search.getGroupName() + "%");
		}

		if (ValidateUtil.isValid(search.getUrl())) {
			params.put("url", "%" + search.getUrl() + "%");
		}

		if (ValidateUtil.isValid(search.getFid())) {
			params.put("fid", search.getFid());
		}

		int count = adminRightService.pageCount(params);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();

		params.put("order", "ctime desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());

		List<AdminRight> lists = adminRightService.pageList(params);

		map.put("pageHtml", pageHtml);
		map.put("lists", lists);
		return forward("/admin/admin-right/index.jsp");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:right:insert")
	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map) {
		// 这是id未0,当id未0时,就是保存操作
		map.put("adminRight", new AdminRight());
		map.put("targets", targets);
		map.put("treeRightMap", getTreeRightMap());
		map.put("righttype", RIGHTTYPE);
		return site.getName() + "/admin/admin-right/save-input.jsp";
	}

	/**
	 * 数据插入 数据校验
	 * 
	 * 必须和BindingResult靠着
	 * 
	 * @param adminRight
	 * @param result
	 * @return
	 */
	@RequiresPermissions("admin:right:insert")
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(AdminRight adminRight, BindingResult result,
			Map<String, Object> map) {

		if (result.getErrorCount() > 0) {
			map.put("targets", targets);
			map.put("treeRightMap", getTreeRightMap());
			map.put("righttype", RIGHTTYPE);
			return site.getName() + "/admin/admin-right/save-input.jsp";
		} else {
			adminRight.setId(FilesUtil.getUUIDName("", false));
			adminRightService.insert(adminRight);
		}

		// return "forward:/hello" => 转发到能够匹配 /hello 的 controller 上
		// return "hello" => 实际上还是转发，只不过是框架会找到该逻辑视图名对应的 View 并渲染
		// return "/hello" => 同 return "hello"

		return redirect("/admin-right/insert");
		// return "admin/admin-right/save-input.jsp";
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:right:update")
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) String id,
			Map<String, Object> map, AdminRight adminRight) {

		if (ValidateUtil.isValid(id)) {
			adminRight = adminRightService.get(id);
		}

		if (adminRight == null) {
			return redirect("/admin-right/insert");
		}

		map.put("adminRight", adminRight);
		map.put("targets", targets);
		map.put("treeRightMap", getTreeRightMap());
		map.put("righttype", RIGHTTYPE);
		return site.getName() + "/admin/admin-right/save-input.jsp";
	}

	@RequiresPermissions("admin:right:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(AdminRight adminRight, BindingResult result) {

		adminRightService.update(adminRight);

		return redirect("/admin-right/update?id=" + adminRight.getId());
	}

	@RequiresPermissions("admin:right:delete")
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Map<String, Object> delete(@RequestParam("delid") String[] delid,
			@RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) {
		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":

			try {
				adminRightService.batchDelete("delete", delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败:" + e.getMessage());
			}

			break;
		case "theShow":
			try {
				for (String string : delid) {
					AdminRight adminRight = adminRightService.get(string);
					adminRight.setTheShow(DataUtil.stringToBoolean(menuValue));
					adminRightService.update(adminRight);
				}

				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}

			break;
		case "thePublic":

			try {
				for (String string : delid) {
					AdminRight adminRight = adminRightService.get(string);
					adminRight
							.setThePublic(DataUtil.stringToBoolean(menuValue));
					adminRightService.update(adminRight);
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}

			break;
		case "theMenu":
			try {
				for (String string : delid) {
					AdminRight adminRight = adminRightService.get(string);
					adminRight.setTheMenu(DataUtil.stringToBoolean(menuValue));
					adminRightService.update(adminRight);
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}

			break;

		case "type":
			try {
				for (String string : delid) {
					AdminRight adminRight = adminRightService.get(string);
					adminRight.setType(menuValue);
					adminRightService.update(adminRight);
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
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