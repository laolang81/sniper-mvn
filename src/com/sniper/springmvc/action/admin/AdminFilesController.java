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

import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.mybatis.service.impl.SdAttachmentsService;
import com.sniper.springmvc.mybatis.service.impl.TablesRelationService;
import com.sniper.springmvc.searchUtil.ChannelSearch;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("${adminPath}/admin-files")
public class AdminFilesController extends AdminBaseController {

	@Resource
	TablesRelationService relationService;

	@Resource
	SdAttachmentsService attachmentsService;

	@RequiresPermissions("admin:files:view")
	@RequestMapping("/")
	public String index(Map<String, Object> map, ChannelSearch search) {

		map.put("sniperUrl", "/admin-files/delete");

		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, Object> params = new HashMap<>();

		if (ValidateUtil.isValid(search.getName())) {
			params.put("tags", search.getName());
		}
		int count = attachmentsService.pageCount(params);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "aid desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdAttachments> lists = attachmentsService.pageList(params);

		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("sniperMenu", toHtml);
		map.put("search", search);

		return forward("/admin/admin-files/index.ftl");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:files:update")
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) String id,
			SdAttachments files, Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {
			files = attachmentsService.get(id);
			if (files == null) {
				return redirect("/admin-files/");
			}
		}

		String type = FilesUtil.getFileExt(files.getFilename());

		map.put("type", type);
		map.put("files", files);
		return forward("/admin/admin-files/save-input.jsp");
	}

	@RequiresPermissions("admin:files:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(SdAttachments files, BindingResult result) {

		try {
			if (result.getFieldErrorCount() > 0) {
				return forward("/admin/admin-files/save-input.jsp");
			} else {
				attachmentsService.update(files);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return redirect("/admin-files/update?id=" + files.getAid());
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:files:delete")
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
					attachmentsService.delete(string);
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
