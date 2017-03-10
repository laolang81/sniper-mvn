package com.sniper.springmvc.action.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.SdUser;
import com.sniper.springmvc.mybatis.service.impl.AdminGroupService;
import com.sniper.springmvc.mybatis.service.impl.SdDepartmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdUserService;
import com.sniper.springmvc.searchUtil.BaseSearch;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.ValidateUtil;

@RequestMapping("${adminPath}/admin-user")
@Controller
public class AdminUserController extends AdminBaseController {

	@Resource
	private AdminGroupService adminGroupService;

	@Resource
	SdDepartmentsService departmentsService;

	@Resource
	SdUserService userService;

	// 用户组列表
	public List<AdminGroup> getAdminGroups() {
		return adminGroupService.query("select", null);
	}

	@RequiresPermissions("admin:user:view")
	@RequestMapping("/")
	public String index(Map<String, Object> map, BaseSearch search) {

		map.put("sniperUrl", "/admin-user/delete");

		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, String> menu = new HashMap<>();
		menu.put("true", "是");
		menu.put("false", "否");

		toHtml.addMapValue("enabled", menu);

		Map<String, String> ispublic = new HashMap<>();
		ispublic.put("true", "是");
		ispublic.put("false", "否");

		toHtml.addMapValue("locked", ispublic);

		Map<String, String> keys = new HashMap<>();
		keys.put("enabled", "启用");
		keys.put("locked", "锁定");

		toHtml.setKeys(keys);

		Map<String, Object> params = new HashMap<>();

		if (ValidateUtil.isValid(search.getName())) {
			params.put("name", search.getName());
		}
		int count = adminUserService.pageCount(params);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "ctime desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<AdminUser> lists = adminUserService.pageList(params);

		map.put("pageHtml", pageHtml);
		map.put("lists", lists);
		map.put("sniperMenu", toHtml);
		return forward("/admin/admin-user/index.jsp");
	}

	@RequiresPermissions("admin:user:insert")
	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map) {

		map.put("sniperAdminUser", new AdminUser());
		map.put("adminGroups", getAdminGroups());
		Map<String, String> deps2 = departmentsService
				.getMapDep(departmentsService.getDep(
						new int[] { 1, 2, 3, 6, 7 }, 1, 10));
		deps2.remove("0");
		map.put("deps", deps2);
		return forward("/admin/admin-user/save-input.jsp");

	}

	@RequiresPermissions("admin:user:insert")
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(
			Map<String, Object> map,
			AdminUser sniperAdminUser,
			BindingResult result,
			@RequestParam(value = "password_c", required = false) String password_c,
			@RequestParam(value = "sign", required = false) Boolean sign,
			@RequestParam(value = "adminGroups", required = false) String[] adminGroups)
			throws Exception {

		if (result.getErrorCount() > 0) {
			map.put("adminGroups", getAdminGroups());
			map.put("sniperAdminUser", sniperAdminUser);
			Map<String, String> deps2 = departmentsService
					.getMapDep(departmentsService.getDep(new int[] { 1, 2, 3,
							6, 7 }, 1, 10));
			deps2.remove("0");
			map.put("deps", deps2);
			return forward("/admin/admin-user/save-input.jsp");
		} else {
			setAdminGroups(adminGroups, sniperAdminUser);
			if (ValidateUtil.isValid(password_c)) {
				sniperAdminUser.setPassword(DigestUtils.sha1Hex(password_c));
			}

			if (ValidateUtil.isValid(sign) && sign) {
				sniperAdminUser.setSignCode(FilesUtil.getUUIDName("", true));
			}

			try {
				if (ValidateUtil.isValid(sniperAdminUser.getSiteids())) {
					ObjectMapper objectMapper = new ObjectMapper();
					String siteid = objectMapper
							.writeValueAsString(sniperAdminUser.getSiteids());
					sniperAdminUser.setSiteid(siteid);
				}
				sniperAdminUser.setId(FilesUtil.getUUIDName("", false));
				adminUserService.insert(sniperAdminUser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return redirect("/admin-user/insert");

	}

	/**
	 * 去除提交过程中的空值
	 * 
	 * @param adminUser
	 * @return
	 */
	private void setAdminGroups(String[] adminGroups, AdminUser adminUser) {
		List<AdminGroup> adminGroups2 = new ArrayList<>();
		if (adminGroups.length > 0) {
			for (int i = 0; i < adminGroups.length; i++) {
				AdminGroup adminGroup = adminGroupService.get(adminGroups[i]);
				adminGroups2.add(adminGroup);
			}
		}
		adminUser.setAdminGroup(adminGroups2);
	}

	@RequiresPermissions("admin:user:update")
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) String id,
			Map<String, Object> map) throws JsonParseException,
			JsonMappingException, IOException {

		if (ValidateUtil.isValid(id)) {
			AdminUser sniperAdminUser = adminUserService.get(id);
			map.put("deps", getDep());
			map.put("adminGroups", getAdminGroups());
			// 格式化处室
			String siteid = sniperAdminUser.getSiteid();
			if (ValidateUtil.isValid(siteid)) {
				ObjectMapper mapper = new ObjectMapper();
				@SuppressWarnings("unchecked")
				List<String> siteids = mapper.readValue(siteid, List.class);
				sniperAdminUser.setSiteids(siteids);
			}

			map.put("sniperAdminUser", sniperAdminUser);
		}

		return forward("/admin/admin-user/save-input.jsp");
	}

	@RequiresPermissions("admin:user:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(
			Map<String, Object> map,
			AdminUser sniperAdminUser,
			BindingResult result,
			@RequestParam(value = "password_c", required = false) String password_c,
			@RequestParam(value = "sign", required = false) Boolean sign,
			@RequestParam(value = "adminGroups", required = false) String[] adminGroups)
			throws Exception {

		if (result.getErrorCount() == 0) {
			setAdminGroups(adminGroups, sniperAdminUser);

			if (ValidateUtil.isValid(sign) && sign) {
				sniperAdminUser.setSignCode(FilesUtil.getUUIDName("", true));
			}
			// 处理提交过来的用户密码
			if (ValidateUtil.isValid(password_c)) {
				sniperAdminUser.setPassword(DigestUtils.sha1Hex(password_c));
			}
			if (ValidateUtil.isValid(sniperAdminUser.getSiteids())) {
				ObjectMapper objectMapper = new ObjectMapper();
				String siteid = objectMapper.writeValueAsString(sniperAdminUser
						.getSiteids());
				sniperAdminUser.setSiteid(siteid);
			}
			adminUserService.update(sniperAdminUser);
		} else {

			List<FieldError> errors = result.getFieldErrors();
			for (FieldError fieldError : errors) {
				System.out.println(fieldError.getDefaultMessage());
				System.out.println(fieldError.getCode());
			}
		}

		return redirect("/admin-user/update?id=" + sniperAdminUser.getId());

	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:user:delete")
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
				for (String string : delid) {
					// 删除关系组
					adminUserService.delete(string);
				}
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
					AdminUser adminUser = adminUserService.get(string);
					adminUser.setEnabled(DataUtil.stringToBoolean(menuValue));
					adminUserService.update(adminUser);
				}

				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}

			break;
		case "locked":
			try {
				for (String string : delid) {
					AdminUser adminUser = adminUserService.get(string);
					adminUser.setLocked(DataUtil.stringToBoolean(menuValue));
					adminUserService.update(adminUser);
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

	@RequiresPermissions("admin:user:ajaxUserList")
	@ResponseBody
	@RequestMapping("ajaxUserList")
	public List<Map<String, String>> ajaxUserList(
			@RequestParam("term") String term) {

		List<Map<String, String>> ajaxMaps = new ArrayList<>();

		if (ValidateUtil.isValid(term)) {
			List<AdminUser> lists = adminUserService.findListByEmail("%" + term
					+ "%");
			if (ValidateUtil.isValid(lists)) {
				for (AdminUser u : lists) {
					Map<String, String> map = new HashMap<>();
					map.put("id", String.valueOf(u.getId()));
					map.put("label", u.getName());
					map.put("value", u.getEmail());
					ajaxMaps.add(map);
				}
			}
		}
		return ajaxMaps;
	}

	/**
	 * 导入老用户 不推荐使用，因为无法获取用户的密码
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:user:importuser")
	@RequestMapping("importuser")
	public String importUser(Map<String, Object> map) {

		int count = 0;
		List<SdUser> users = userService.query("select", null);
		if (ValidateUtil.isValid(users)) {
			for (SdUser sdUser : users) {
				if (adminUserService.validateByName(sdUser.getUsername()) == null) {
					adminUserService.regUser(sdUser, "666666");
					count++;
				}
			}
		}
		map.put("count", count);
		return forward("/admin/admin-user/importuser.ftl");
	}
}