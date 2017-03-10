package com.sniper.springmvc.action.admin;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.data.DataValues;
import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.model.SdItems;
import com.sniper.springmvc.model.SdPageIndex;
import com.sniper.springmvc.model.SdTabInfoClass;
import com.sniper.springmvc.model.SdViewSubject;
import com.sniper.springmvc.mybatis.service.impl.SdDepartmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdDepartmentsServiceImpl;
import com.sniper.springmvc.mybatis.service.impl.SdItemsService;
import com.sniper.springmvc.mybatis.service.impl.SdItemsServiceImpl;
import com.sniper.springmvc.mybatis.service.impl.SdPageIndexService;
import com.sniper.springmvc.mybatis.service.impl.SdTabInfoClassService;
import com.sniper.springmvc.searchUtil.BaseSearch;
import com.sniper.springmvc.searchUtil.ChannelSearch;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.ItemsUtil;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.UserDetailsUtils;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("${adminPath}/admin-channel")
public class AdminChannelController extends AdminBaseController {

	@Resource
	private SdDepartmentsService departmentsService;

	@Resource
	private SdItemsService itemsService;

	@Resource
	private SdTabInfoClassService infoClassService;

	@Resource
	private SdPageIndexService pageIndexService;

	/**
	 * 获取栏目 ajax获取栏目
	 * 
	 * @param deprtid
	 *            处室id
	 * @param enabled
	 *            状态
	 * @return
	 */
	@ResponseBody
	// @RequiresPermissions("admin:ajax:items")
	@RequestMapping("ajaxitems")
	public List<Map<String, String>> ajaxItems(@RequestParam(value = "id", required = false) String deprtid,
			@RequestParam(value = "status", required = false, defaultValue = "1") Integer enabled) {
		List<Map<String, String>> result = new ArrayList<>();

		if (!ValidateUtil.isValid(deprtid) || deprtid.equals("0")) {
			return result;
		}
		List<SdItems> items = itemsService.getItems(Integer.valueOf(deprtid).intValue(),
				Integer.valueOf(enabled).intValue());
		if (items != null && items.size() > 0) {
			Map<String, SdItems> map = ItemsUtil.sortNamed(items);
			map = ItemsUtil.sortItemidAsc(map);
			for (Entry<String, SdItems> entry : map.entrySet()) {
				Map<String, String> temp2 = new HashMap<>();
				temp2.put("id", entry.getKey());
				temp2.put("name", entry.getValue().getName());
				result.add(temp2);
			}
		}
		return result;
	}

	@ResponseBody
	// @RequiresPermissions("admin:ajax:class")
	@RequestMapping("ajaxclass")
	public List<Map<String, String>> ajaxClassAction(@RequestParam(value = "id", defaultValue = "0") Integer id,
			@RequestParam(value = "status", defaultValue = "1", required = false) Integer status) {
		if (!ValidateUtil.isValid(id) && id == 0) {
			return null;
		}
		List<Map<String, String>> result = new ArrayList<>();
		List<SdTabInfoClass> classes = infoClassService.getTabInfo(id, status);
		if (classes != null && classes.size() > 0) {
			for (SdTabInfoClass sdTabInfoClass : classes) {
				Map<String, String> temp2 = new HashMap<>();
				temp2.put("id", sdTabInfoClass.getId().toString());
				temp2.put("name", sdTabInfoClass.getName());
				result.add(temp2);
			}
		}

		return result;
	}

	/**
	 * 处室管理
	 * 
	 * @param map
	 * @param search
	 * @return
	 * @throws FileNotFoundException
	 */
	@RequiresPermissions("admin:office:view")
	@RequestMapping("office")
	public String office(Map<String, Object> map, ChannelSearch search) throws FileNotFoundException {

		map.put("sniperUrl", "/admin-channel/officedelete");
		ParamsToHtml toHtml = new ParamsToHtml();
		toHtml.addMapValue("status", DataValues.YES_NO);
		toHtml.addMapValue("type", SdDepartmentsServiceImpl.TYPES);

		Map<String, String> keys = new HashMap<>();
		keys.put("status", "变更状态");
		keys.put("type", "更改类型");

		toHtml.setKeys(keys);

		Map<String, Object> params = new HashMap<>();
		if (ValidateUtil.isValid(search.getType())) {
			List<Integer> types = new ArrayList<>();
			types.add(Integer.valueOf(search.getType()));
			params.put("type", types);
		}

		if (ValidateUtil.isValid(search.getStatus())) {
			params.put("enabled", search.getStatus());
		}

		if (ValidateUtil.isValid(search.getName())) {
			params.put("name", "%" + search.getName() + "%");
		}

		int count = departmentsService.pageCount(params);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();

		params.put("order", "id desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdDepartments> lists = departmentsService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("search", search);
		map.put("yes_no", DataValues.YES_NO);
		map.put("sniperMenu", toHtml);
		return forward("/admin/admin-channel/office.jsp");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:office:insert")
	@RequestMapping(value = "officeinsert", method = RequestMethod.GET)
	public String officeInsert(Map<String, Object> map) {

		map.put("office", new SdDepartments());
		map.put("yes_no", DataValues.YES_NO);
		map.put("types", SdDepartmentsServiceImpl.TYPES);
		return forward("/admin/admin-channel/office-save-input.jsp");
	}

	@RequiresPermissions("admin:office:insert")
	@RequestMapping(value = "officeinsert", method = RequestMethod.POST)
	public String officeInsert(SdDepartments office, BindingResult result, Map<String, Object> map) {

		try {
			if (result.getFieldErrorCount() > 0) {
				Map<String, String> errors = new HashMap<>();
				for (FieldError error : result.getFieldErrors()) {
					errors.put(error.getField(), error.getDefaultMessage());
				}
				map.put("errors", errors);
				map.put("yes_no", DataValues.YES_NO);
				map.put("types", SdDepartmentsServiceImpl.TYPES);
				return forward("/admin/admin-channel/office-save-input.jsp");
			} else {
				if (!ValidateUtil.isValid(office.getDeShortName())) {
					office.setDeShortName(office.getName());
				}
				departmentsService.insert(office);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirect("/admin-channel/officeinsert");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:office:update")
	@RequestMapping(value = "officeupdate", method = RequestMethod.GET)
	public String officeUpdate(@RequestParam(value = "id", required = false) String id, SdDepartments office,
			Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {
			office = departmentsService.get("get", id);
		} else {
			return redirect("/admin-channel/officeinsert");
		}

		map.put("office", office);
		map.put("yes_no", DataValues.YES_NO);
		map.put("types", SdDepartmentsServiceImpl.TYPES);
		return forward("/admin/admin-channel/office-save-input.jsp");
	}

	@RequiresPermissions("admin:office:update")
	@RequestMapping(value = "officeupdate", method = RequestMethod.POST)
	public String officeUpdate(SdDepartments office, BindingResult result, Map<String, Object> map) {
		if (null == office.getId()) {
			return redirect("/admin-channel/officeinsert");
		}
		try {
			if (result.getFieldErrorCount() == 0) {
				departmentsService.update("update", office);
			} else {
				Map<String, String> errors = new HashMap<>();
				for (FieldError error : result.getFieldErrors()) {
					errors.put(error.getField(), error.getDefaultMessage());
				}
				map.put("errors", errors);
				map.put("office", office);
				map.put("types", SdDepartmentsServiceImpl.TYPES);
				map.put("yes_no", DataValues.YES_NO);
				return forward("/admin/admin-channel/office-save-input.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirect("/admin-channel/officeupdate?id=" + office.getId());
	}

	@RequiresPermissions("admin:office:delete")
	@ResponseBody
	@RequestMapping(value = "officedelete", method = RequestMethod.POST)
	public Map<String, Object> officeDelete(@RequestParam("delid") String[] delid,
			@RequestParam("menuType") String menuType, @RequestParam("menuValue") String menuValue) {

		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":
			try {
				for (String string : delid) {
					// 检查是否是空处室，非空不得删除
					List<SdItems> items = itemsService.getItemsByDep(string, 1);
					if (items.size() == 0) {
						departmentsService.delete(string);
					} else {
						ajaxResult.put("code", -1);
						ajaxResult.put("msg", "非空处室不得删除");
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
		case "status":
			try {
				for (String string : delid) {
					SdDepartments channel = departmentsService.get(string);
					channel.setDeTrue(Integer.valueOf(menuValue));
					departmentsService.update(channel);
					Map<String, String> result = new HashMap<>();
					result.put(menuType, DataValues.YES_NO.get(menuValue));
					ajaxResult.put(string, result);
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
				for (String id : delid) {
					SdDepartments channel = departmentsService.get(id);
					channel.setType(DataUtil.stringToInteger(menuValue));
					departmentsService.update(channel);
					Map<String, String> result = new HashMap<>();
					result.put(menuType, DataValues.YES_NO.get(menuValue));
					ajaxResult.put(id, result);
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

	/**
	 * 栏目管理
	 * 
	 * @param map
	 * @param search
	 * @return
	 * @throws FileNotFoundException
	 */

	@RequiresPermissions("admin:items:view")
	@RequestMapping("items")
	public String items(Map<String, Object> map, ChannelSearch search) throws FileNotFoundException {

		map.put("sniperUrl", "/admin-channel/itemsdelete");

		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, String> depsMap = getDep();
		toHtml.addMapValue("status", DataValues.YES_NO);
		toHtml.addMapValue("dep", depsMap);

		Map<String, String> keys = new HashMap<>();
		keys.put("status", "变更状态");
		keys.put("dep", "变更处室");

		toHtml.setKeys(keys);

		Map<String, Object> params = new HashMap<>();
		if (ValidateUtil.isValid(search.getType())) {
			params.put("style", search.getType());
		}

		if (ValidateUtil.isValid(search.getStatus())) {
			params.put("enabled", search.getStatus());
		}

		if (ValidateUtil.isValid(search.getName())) {
			params.put("name", "%" + search.getName() + "%");
		}

		List<Integer> deps = new ArrayList<>();
		if (ValidateUtil.isValid(search.getDepid())) {
			int depritid = Integer.valueOf(search.getDepid());
			if (depritid > 0) {
				deps.add(Integer.valueOf(search.getDepid()));
			}
		} else {
			UserDetailsUtils detailsUtils = new UserDetailsUtils();
			if (!detailsUtils.hasRole(DataValues.ROLE_ADMIN)) {
				for (Entry<String, String> entry : depsMap.entrySet()) {
					deps.add(Integer.valueOf(entry.getKey()));
				}
			}
		}

		params.put("deps", deps);

		if (ValidateUtil.isValid(search.getItemUp())) {
			int itemup = Integer.valueOf(search.getItemUp());
			if (itemup > 0) {
				params.put("itemup", search.getItemUp());
			}
		}

		int count = itemsService.pageCount(params);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();

		params.put("order", "t.itemid desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdItems> lists = itemsService.pageList(params);

		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("search", search);
		map.put("sniperMenu", toHtml);
		map.put("yes_no", DataValues.YES_NO);
		map.put("deps", depsMap);
		map.put("items", getItemsByRedis(depsMap));
		map.put("styles", SdItemsServiceImpl.STYLES);
		return forward("/admin/admin-channel/items.jsp");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:items:insert")
	@RequestMapping(value = "itemsinsert", method = RequestMethod.GET)
	public String itemsInsert(Map<String, Object> map) {

		map.put("items", new SdItems());
		map.put("yes_no", DataValues.YES_NO);
		map.put("styles", SdItemsServiceImpl.STYLES);
		map.put("deps", getDep());
		return forward("/admin/admin-channel/items-save-input.jsp");
	}

	@RequiresPermissions("admin:items:insert")
	@RequestMapping(value = "itemsinsert", method = RequestMethod.POST)
	public String itemsInsert(SdItems items, BindingResult result, Map<String, Object> map) {
		try {
			if (result.getFieldErrorCount() > 0) {
				Map<String, String> errors = new HashMap<>();
				for (FieldError error : result.getFieldErrors()) {
					errors.put(error.getField(), error.getDefaultMessage());
				}
				map.put("errors", errors);
				map.put("yes_no", DataValues.YES_NO);
				map.put("styles", SdItemsServiceImpl.STYLES);
				map.put("items", items);
				map.put("deps", getDep());
				return forward("/admin/admin-channel/items-save-input.jsp");
			} else {
				itemsService.insert("insert", items);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirect("/admin-channel/itemsinsert");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:items:update")
	@RequestMapping(value = "itemsupdate", method = RequestMethod.GET)
	public String itemsUpdate(@RequestParam(value = "id", required = false) String id, SdItems items,
			Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {
			items = itemsService.get(id);
		} else {
			return redirect("/admin-channel/itemsinsert");
		}

		map.put("items", items);
		map.put("yes_no", DataValues.YES_NO);
		map.put("styles", SdItemsServiceImpl.STYLES);
		map.put("deps", getDep());
		return forward("/admin/admin-channel/items-save-input.jsp");
	}

	@RequiresPermissions("admin:items:update")
	@RequestMapping(value = "itemsupdate", method = RequestMethod.POST)
	public String itemsUpdate(SdItems items, BindingResult result, Map<String, Object> map) {

		try {
			if (result.getFieldErrorCount() == 0) {
				itemsService.update(items);
			} else {
				Map<String, String> errors = new HashMap<>();
				for (FieldError error : result.getFieldErrors()) {
					errors.put(error.getField(), error.getDefaultMessage());
				}
				map.put("errors", errors);
				map.put("items", items);
				map.put("yes_no", DataValues.YES_NO);
				map.put("styles", SdItemsServiceImpl.STYLES);
				map.put("deps", getDep());
				return forward("/admin/admin-channel/items-save-input.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirect("/admin-channel/itemsupdate?id=" + items.getItemid());
	}

	@RequiresPermissions("admin:items:delete")
	@ResponseBody
	@RequestMapping(value = "itemsdelete", method = RequestMethod.POST)
	public Map<String, Object> itemsDelete(@RequestParam("delid") String[] delid,
			@RequestParam("menuType") String menuType, @RequestParam("menuValue") String menuValue) {

		Map<String, Object> ajaxResult = new HashMap<>();
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		switch (menuType) {
		case "delete":
			try {
				for (String string : delid) {
					SdItems items = itemsService.get(string);
					// 检查是否拥有下级栏目
					List<SdItems> itemsUp = itemsService.getItems(items.getItemup(), 1);
					if (itemsUp != null && itemsUp.size() > 0) {
						// itemsService.delete(string);
						ajaxResult.put("code", -1);
						ajaxResult.put("msg", "删除失败,拥有下级栏目");
						break;
					}
					// 检查是否有新闻
					List<SdViewSubject> subjects = viewSubjectService.getSubjectByItem(items.getItemid(), 2);
					if (subjects != null && subjects.size() > 0) {
						ajaxResult.put("code", -1);
						ajaxResult.put("msg", "删除失败,拥有新闻");
						break;
					}
					// 检查栏目是否在允许的范围内
					if (!detailsUtils.hasRole(DataValues.ROLE_ADMIN)) {
						Map<String, String> deps = getDep();
						if (!deps.containsKey(String.valueOf(items.getDeprtid()))) {
							ajaxResult.put("code", -1);
							ajaxResult.put("msg", "删除失败,拥有新闻");
							break;
						}
					}
					// 删除栏目
					itemsService.delete(string);
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;
		case "status":
			try {
				Map<String, String> result = new HashMap<>();
				result.put("status", DataValues.YES_NO.get(menuValue));
				for (String string : delid) {
					SdItems channel = itemsService.get(string);
					channel.setStatus(Integer.valueOf(menuValue));
					itemsService.update(channel);
					ajaxResult.put(string, result);
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}
			break;

		case "dep":
			Map<String, String> depsMap = getDep();
			try {
				Map<String, String> result = new HashMap<>();
				result.put("dep", depsMap.get(menuValue));
				for (String string : delid) {
					SdItems channel = itemsService.get(string);
					channel.setDeprtid(Integer.valueOf(menuValue));
					itemsService.update(channel);
					ajaxResult.put(string, result);
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}
			break;
		case "style":
			try {
				for (String string : delid) {
					SdItems channel = itemsService.get(string);
					channel.setStyle(DataUtil.stringToInteger(menuValue));
					itemsService.update(channel);
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

	/**
	 * 栏目管理
	 * 
	 * @param map
	 * @param search
	 * @return
	 * @throws FileNotFoundException
	 */

	@RequiresPermissions("admin:infoclass:view")
	@RequestMapping("infoclass")
	public String infoclass(Map<String, Object> map, ChannelSearch search) throws FileNotFoundException {

		map.put("sniperUrl", "/admin-channel/infoclassdelete");

		ParamsToHtml toHtml = new ParamsToHtml();

		toHtml.addMapValue("status", DataValues.YES_NO);

		Map<String, String> keys = new HashMap<>();
		keys.put("status", "变更状态");

		toHtml.setKeys(keys);

		Map<String, Object> params = new HashMap<>();

		if (ValidateUtil.isValid(search.getStatus())) {
			params.put("enabled", search.getStatus());
		}

		if (ValidateUtil.isValid(search.getName())) {
			params.put("name", "%" + search.getName() + "%");
		}

		if (ValidateUtil.isValid(search.getItemUp())) {
			int itemup = Integer.valueOf(search.getItemUp());
			if (itemup > 0) {
				params.put("itemid", search.getItemUp());
			}
		}

		int count = infoClassService.pageCount(params);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();

		params.put("order", "ic_id desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdTabInfoClass> lists = infoClassService.pageList(params);
		// 查找看么，处室
		Map<String, SdItems> parents = new HashMap<>();
		for (SdTabInfoClass infoClass : lists) {
			if (parents.containsKey(infoClass.getItemid())) {
				continue;
			}
			if (infoClass.getItemid() > 0) {
				SdItems parentItems = itemsService.getItemInfo(infoClass.getItemid() + "");
				parents.put(infoClass.getItemid() + "", parentItems);
			}
		}
		map.put("parents", parents);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("search", search);
		map.put("sniperMenu", toHtml);
		map.put("departments", getDep());
		return forward("/admin/admin-channel/info.jsp");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:infoclass:insert")
	@RequestMapping(value = "infoclassinsert", method = RequestMethod.GET)
	public String infoclassInsert(Map<String, Object> map) {

		map.put("infoclass", new SdTabInfoClass());
		map.put("yes_no", DataValues.YES_NO);
		map.put("departments", getDep());
		return forward("/admin/admin-channel/info-save-input.jsp");
	}

	@RequiresPermissions("admin:infoclass:insert")
	@RequestMapping(value = "infoclassinsert", method = RequestMethod.POST)
	public String infoclassInsert(SdTabInfoClass infoclass, BindingResult result, Map<String, Object> map) {

		try {
			if (result.getFieldErrorCount() > 0) {
				Map<String, String> errors = new HashMap<>();
				for (FieldError error : result.getFieldErrors()) {
					errors.put(error.getField(), error.getDefaultMessage());
				}
				map.put("errors", errors);
				map.put("yes_no", DataValues.YES_NO);
				map.put("infoclass", infoclass);
				map.put("departments", getDep());
				return forward("/admin/admin-channel/info-save-input.jsp");
			} else {
				infoClassService.insert(infoclass);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirect("/admin-channel/infoclassinsert");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:infoclass:update")
	@RequestMapping(value = "infoclassupdate", method = RequestMethod.GET)
	public String infoclassUpdate(@RequestParam(value = "id", required = false) String id, SdTabInfoClass infoclass,
			Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {
			infoclass = infoClassService.get("get", id);
		} else {
			return redirect("/admin-channel/infoclassinsert");
		}
		SdItems parentItems = new SdItems();
		if (ValidateUtil.isValid(infoclass.getItemid())) {
			parentItems = itemsService.getItemInfo(String.valueOf(infoclass.getItemid()));

		}
		map.put("parentItems", parentItems);
		map.put("infoclass", infoclass);
		map.put("yes_no", DataValues.YES_NO);
		map.put("departments", getDep());
		return forward("/admin/admin-channel/info-save-input.jsp");
	}

	@RequiresPermissions("admin:infoclass:update")
	@RequestMapping(value = "infoclassupdate", method = RequestMethod.POST)
	public String infoclassUpdate(SdTabInfoClass infoclass, BindingResult result, Map<String, Object> map) {

		try {
			if (result.getFieldErrorCount() == 0) {
				infoClassService.update(infoclass);
			} else {
				Map<String, String> errors = new HashMap<>();
				for (FieldError error : result.getFieldErrors()) {
					errors.put(error.getField(), error.getDefaultMessage());
				}
				map.put("errors", errors);
				map.put("infoclass", infoclass);
				map.put("yes_no", DataValues.YES_NO);
				map.put("styles", SdItemsServiceImpl.STYLES);
				map.put("departments", getDep());
				return forward("/admin/admin-channel/info-save-input.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirect("/admin-channel/infoclassupdate?id=" + infoclass.getItemid());
	}

	@RequiresPermissions("admin:infoclass:delete")
	@ResponseBody
	@RequestMapping(value = "infoclassdelete", method = RequestMethod.POST)
	public Map<String, Object> infoclassDelete(@RequestParam("delid") String[] delid,
			@RequestParam("menuType") String menuType, @RequestParam("menuValue") String menuValue) {

		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":
			try {
				for (String string : delid) {
					infoClassService.delete(string);
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;
		case "status":
			try {
				Map<String, String> result = new HashMap<>();
				result.put("status", DataValues.YES_NO.get(menuValue));
				for (String string : delid) {
					SdTabInfoClass channel = infoClassService.get(string);
					channel.setEnabled(Integer.valueOf(menuValue));
					infoClassService.update(channel);
					ajaxResult.put(string, result);
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

	public String ZTreeOnLine(List<SdItems> channels, Map<String, Object> map, String attr) {

		List<String> nodes = new ArrayList<>();
		Integer[] fids = new Integer[channels.size()];
		int i = 0;
		for (SdItems channel : channels) {
			if (channel.getItemup() != 0) {
				fids[i] = channel.getItemup();
				i++;
			}
		}
		System.out.println(Arrays.asList(fids));
		// Arrays.sort(fids);
		for (SdItems c : channels) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("{id:");
			buffer.append("'" + c.getItemid() + "'");
			buffer.append(",pId:");
			buffer.append("'" + c.getItemid() + "'");
			buffer.append(",name:");
			buffer.append("\"");
			buffer.append(c.getName());
			buffer.append("\"");
			buffer.append(",title:\"");
			buffer.append(c.getName().trim());
			buffer.append("\"");
			buffer.append(",target:\"_blank\"");
			buffer.append(",url:\"" + adminPath + "/admin-channel/update?id=" + c.getItemid() + "\"");
			buffer.append(",type:\"" + c.getType() + "\"");

			if (Arrays.binarySearch(fids, c.getItemid()) > -1) {
				buffer.append(attr);
			}

			buffer.append("}");
			nodes.add(buffer.toString());
		}
		String buffer = StringUtils.join(nodes, ",\r");
		map.put("treeMap", buffer);
		return forward("/admin/admin-channel/olUpdate.ftl");
	}

	// @RequiresPermissions("admin:channel:online")
	@RequestMapping("online")
	public String online(Map<String, Object> map) {
		List<SdItems> channels = itemsService.query("select", null);
		return ZTreeOnLine(channels, map, ",open:true,isParent:true");
	}

	/**
	 * 在线管理
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:channel:olInsert")
	@ResponseBody
	@RequestMapping("olInsert")
	public Map<String, Object> olInsert(SdItems channel) {

		Map<String, Object> result = new HashMap<>();
		try {
			channel.setStatus(1);
			itemsService.insert("insert", channel);
			result.put("code", "200");
			result.put("msg", "添加成功");
			result.put("channel", channel);
		} catch (Exception e) {
			result.put("code", "500");
			result.put("msg", "添加失败");
		}
		return result;
	}

	@RequiresPermissions("admin:channel:olUpdate")
	@ResponseBody
	@RequestMapping("olUpdate")
	public Map<String, String> olUpdate(Channel channel) {

		Map<String, String> result = new HashMap<>();
		try {
			SdItems channel2 = itemsService.get("get", channel.getId() + "");
			channel2.setName(channel.getName());
			itemsService.update("update", channel2);
			result.put("code", "200");
			result.put("msg", "修改成功");
		} catch (Exception e) {
			result.put("code", "500");
			result.put("msg", "修改失败");
		}
		return result;
	}

	@RequiresPermissions("admin:channel:olDelete")
	@ResponseBody
	@RequestMapping("olDelete")
	public Map<String, String> olDelete(SdItems channel) {

		Map<String, String> result = new HashMap<>();
		try {
			SdItems channel2 = itemsService.get("get", channel.getItemid() + "");
			itemsService.delete("delete", channel2.getItemid() + "");
			result.put("code", "200");
			result.put("msg", "删除成功");
		} catch (Exception e) {
			result.put("code", "500");
			result.put("msg", "删除失败");
		}
		return result;
	}

	@RequiresPermissions("admin:channel:group")
	@RequestMapping(value = "group")
	public String group(Map<String, Object> map, BaseSearch search) {
		map.put("sniperUrl", "/admin-channel/groupdelete");

		ParamsToHtml html = new ParamsToHtml();
		Map<String, Object> params = new HashMap<>();
		if (ValidateUtil.isValid(search.getName())) {
			params.put("name", "%" + search.getName() + "%");
		}

		int count = pageIndexService.pageCount(params);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();

		params.put("order", "id desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdPageIndex> lists = pageIndexService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("sniperMenu", html);
		map.put("search", search);

		Map<String, String> depMap = getDep();
		map.put("deps", depMap);
		Map<String, SdItems> itemsMap2 = getItemsAll(depMap);
		// 设置栏目
		Map<String, String> itemsMap = new HashMap<>();
		for (Entry<String, SdItems> entry : itemsMap2.entrySet()) {
			// 名字组装，处室-》栏目
			StringBuffer buffer = new StringBuffer();
			String depName = depMap.get(String.valueOf(entry.getValue().getDeprtid()));
			buffer.append(depName);
			buffer.append("->");
			buffer.append(entry.getValue().getName());
			itemsMap.put(entry.getKey(), buffer.toString());
		}
		map.put("itemsMap", itemsMap);
		return forward("/admin/admin-channel/group.jsp");
	}

	/**
	 * ztree数据
	 * 
	 * @return
	 */

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:channel:groupinsert")
	@RequestMapping(value = "groupinsert", method = RequestMethod.GET)
	public String groupinsert(Map<String, Object> map) {
		Map<String, String> depMap = getDep();
		map.put("deps", depMap);
		map.put("itemsMap", getItemsByRedis(depMap));
		map.put("pageIndex", new SdPageIndex());
		return forward("/admin/admin-channel/save-group.jsp");
	}

	@RequiresPermissions("admin:channel:groupinsert")
	@RequestMapping(value = "groupinsert", method = RequestMethod.POST)
	public String groupinsert(Map<String, Object> map, SdPageIndex pageIndex, BindingResult result,
			@RequestParam(value = "itemid", required = false) String[] itemids) {
		try {
			if (result.getFieldErrorCount() == 0) {
				if (ValidateUtil.isValid(itemids)) {
					pageIndex.setItemid(StringUtils.join(itemids, ","));
				}
				pageIndexService.insert(pageIndex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirect("/admin-channel/groupinsert");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:channel:groupupdate")
	@RequestMapping(value = "groupupdate", method = RequestMethod.GET)
	public String groupupdate(@RequestParam(value = "id", required = false) String id, SdPageIndex pageIndex,
			Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {
			pageIndex = pageIndexService.get(id);
		} else {
			return redirect("/admin-channel/groupinsert");
		}
		Map<String, String> itmidsData = new HashMap<>();
		// 获取已存在的数据
		if (ValidateUtil.isValid(pageIndex.getItemid())) {
			String[] ids = pageIndex.getItemid().split(",");
			for (String string : ids) {
				itmidsData.put(string, "1");
			}
		}

		map.put("itmidsData", itmidsData);
		Map<String, String> depMap = getDep();
		map.put("deps", depMap);
		map.put("itemsMap", getItemsByRedis(depMap));
		map.put("pageIndex", pageIndex);
		return forward("/admin/admin-channel/save-group.jsp");
	}

	@RequiresPermissions("admin:channel:groupupdate")
	@RequestMapping(value = "groupupdate", method = RequestMethod.POST)
	public String groupupdate(SdPageIndex pageIndex, BindingResult result,
			@RequestParam(value = "itemid", required = false) String[] itemids) {

		try {
			if (result.getErrorCount() == 0) {
				if (ValidateUtil.isValid(itemids)) {
					pageIndex.setItemid(StringUtils.join(itemids, ","));
				}
				pageIndexService.update(pageIndex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return redirect("/admin-channel/groupupdate?id=" + pageIndex.getId());
	}

	@RequiresPermissions("admin:channel:groupdelete")
	@ResponseBody
	@RequestMapping(value = "groupdelete", method = RequestMethod.POST)
	public Map<String, Object> groupdelete(@RequestParam("delid") String[] delid,
			@RequestParam("menuType") String menuType, @RequestParam("menuValue") String menuValue) {

		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":
			try {
				pageIndexService.batchDelete("delete", delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;
		case "status":

		default:
			break;
		}

		return ajaxResult;
	}
}