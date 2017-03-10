package com.sniper.springmvc.action.admin;

import java.util.HashMap;
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

import com.sniper.springmvc.model.OauthClient;
import com.sniper.springmvc.mybatis.service.impl.OauthClientService;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.HqlUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 用户oauth管理
 * 
 * @author suzhen
 * 
 */
@RequestMapping("${adminPath}/admin-oauth")
@Controller
public class AdminOauthController extends AdminBaseController {
	@Resource
	OauthClientService clientService;

	@RequiresPermissions("admin:oauth:view")
	@RequestMapping("/")
	public String index(Map<String, Object> map) {

		map.put("sniperUrl", "/admin-oauth/delete");

		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, String> enabled = new HashMap<>();
		enabled.put("true", "是");
		enabled.put("false", "否");
		toHtml.addMapValue("enabled", enabled);

		HqlUtils hqlUtils = new HqlUtils(OauthClient.class);
		int count = clientService.pageCount(hqlUtils.getParams());
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		hqlUtils.getParams().put("order", "ctime desc");
		hqlUtils.getParams().put("pageOffset", page.getFristRow());
		hqlUtils.getParams().put("pageSize", page.getListRow());
		List<OauthClient> lists = clientService.pageList(hqlUtils.getParams());

		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("toHtml", toHtml);

		return forward("/admin/admin-oauth/index.jsp");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:oauth:insert")
	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map) {

		OauthClient client = new OauthClient();
		map.put("oauthClient", client);
		return forward("/admin/admin-oauth/save-input.jsp");
	}

	@RequiresPermissions("admin:oauth:insert")
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(OauthClient oauthClient, BindingResult result) {
		try {
			if (result.getErrorCount() > 0) {
				return forward("/admin/admin-oauth/save-input.jsp");
			} else {
				oauthClient.setClientSecret(FilesUtil.getUUIDName("", true));
				clientService.insert(oauthClient);
			}
		} catch (Exception e) {

		}
		return redirect("/admin-oauth/insert");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:oauth:update")
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) String id,
			OauthClient OauthClient, Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {
			OauthClient = clientService.get(id);
		} else {
			return redirect("/admin-oauth/insert");
		}

		map.put("oauthClient", OauthClient);
		return forward("/admin/admin-oauth/save-input.jsp");
	}

	@RequiresPermissions("admin:oauth:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(OauthClient oauthClient, BindingResult result) {

		try {
			if (result.getErrorCount() > 0) {
				return forward("/admin/admin-oauth/save-input.jsp");
			} else {
				clientService.update(oauthClient);
			}
		} catch (Exception e) {

		}

		return redirect("/admin-oauth/update?id=" + oauthClient.getId());
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:oauth:delete")
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
				clientService.batchDelete("delete", delid);
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
					OauthClient client = clientService.get(string);
					client.setEnabled(DataUtil.stringToBoolean(menuValue));
					clientService.update(client);
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