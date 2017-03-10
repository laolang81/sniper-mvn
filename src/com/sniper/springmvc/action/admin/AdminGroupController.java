package com.sniper.springmvc.action.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.data.DataValues;
import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.model.AdminRight;
import com.sniper.springmvc.model.AdminUserGroup;
import com.sniper.springmvc.mybatis.service.impl.AdminGroupService;
import com.sniper.springmvc.mybatis.service.impl.AdminUserGroupService;
import com.sniper.springmvc.mybatis.service.impl.SdSubjectsServiceImpl;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.TreeZTreeUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("${adminPath}/admin-group")
public class AdminGroupController extends AdminBaseController {

	@Resource
	private AdminGroupService adminGroupService;

	@Resource
	AdminUserGroupService adminUserGroupService;

	public String getTreeRightMap() {
		TreeZTreeUtil treeUtil = new TreeZTreeUtil();
		Map<String, Object> params = new HashMap<>();
		params.put("order", "sort asc");
		List<AdminRight> adminRights = adminRightService
				.query("select", params);

		treeUtil.setAdminRights(adminRights);
		treeUtil.initAll();
		return treeUtil.getTreeNodesAll();
	}

	@Override
	@ModelAttribute
	public void init(Map<String, Object> map) {

		ParamsToHtml html = new ParamsToHtml();
		html.setKey(getTreeRightMap());
		map.put("sniperMenu", html);
		super.init(map);
	}

	@RequiresPermissions("admin:group:view")
	@RequestMapping("/")
	public String index(Map<String, Object> map) {

		Map<String, Object> params = new HashMap<>();
		int count = adminGroupService.pageCount(params);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();

		params.put("order", "ctime desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());

		List<AdminGroup> lists = adminGroupService.pageList(params);

		map.put("pageHtml", pageHtml);
		map.put("lists", lists);
		map.put("sniperUrl", "/admin-group/delete");
		return forward("/admin/admin-group/index.ftl");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:group:insert")
	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map) {

		map.put("adminGroup", new AdminGroup());
		map.put("yes_no", DataValues.YES_NO);
		map.put("lookthrouth", SdSubjectsServiceImpl.LOOK_THROUTH);
		return site.getName() + "/admin/admin-group/save-input.jsp";
	}

	@RequiresPermissions("admin:group:insert")
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(Map<String, Object> map, AdminGroup adminGroup,
			BindingResult result,
			@RequestParam("fromRight") List<AdminRight> fromRight) {

		try {
			if (result.getFieldErrorCount() > 0) {
				Map<String, String> errors = new HashMap<>();
				for (FieldError error : result.getFieldErrors()) {
					errors.put(error.getField(), error.getDefaultMessage());
				}
				map.put("errors", errors);
				map.put("lookthrouth", SdSubjectsServiceImpl.LOOK_THROUTH);
				return forward("/admin/admin-group/save-input.jsp");
			} else {
				adminGroup.setId(FilesUtil.getUUIDName("", false));
				adminGroup.setAdminRight(fromRight);
				adminGroupService.insert(adminGroup);
			}
		} catch (Exception e) {

		}

		return redirect("/admin-group/insert");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:group:update")
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) String id,
			Map<String, Object> map, AdminGroup adminGroup) {

		if (ValidateUtil.isValid(id)) {
			adminGroup = adminGroupService.get(id);

		} else {
			return redirect("/admin-group/insert");
		}

		map.put("yes_no", DataValues.YES_NO);
		map.put("lookthrouth", SdSubjectsServiceImpl.LOOK_THROUTH);
		map.put("adminGroup", adminGroup);
		return forward("/admin/admin-group/save-input.jsp");
	}

	@RequiresPermissions("admin:group:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(AdminGroup adminGroup, BindingResult result,
			@RequestParam("fromRight") List<AdminRight> fromRight,
			Map<String, Object> map) {

		try {
			if (result.getFieldErrorCount() == 0) {
				adminGroup.setAdminRight(fromRight);
				adminGroupService.update(adminGroup);
			} else {
				Map<String, String> errors = new HashMap<>();
				for (FieldError error : result.getFieldErrors()) {
					errors.put(error.getField(), error.getDefaultMessage());
				}
				map.put("errors", errors);
				map.put("lookthrouth", SdSubjectsServiceImpl.LOOK_THROUTH);
				return forward("/admin/admin-group/save-input.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirect("/admin-group/update?id=" + adminGroup.getId());
	}

	@RequiresPermissions("admin:group:delete")
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Map<String, Object> delete(@RequestParam("delid") String[] delid,
			@RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) {
		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":
			try {
				for (String string : delid) {

					List<AdminUserGroup> adminUserGroups = adminUserGroupService
							.getAUG(string);
					if (adminUserGroups.size() == 0) {
						adminGroupService.delete(string);
					} else {
						ajaxResult.put("code", -1);
						ajaxResult.put("msg", "旗下用户不为空，不得删除。");
						break;
					}
				}
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