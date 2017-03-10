package com.sniper.springmvc.action.admin;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sniper.springmvc.data.DataValues;
import com.sniper.springmvc.model.LogSmb;
import com.sniper.springmvc.model.SdLoginLog;
import com.sniper.springmvc.mybatis.service.impl.LogSmbService;
import com.sniper.springmvc.mybatis.service.impl.SdLoginLogService;
import com.sniper.springmvc.searchUtil.ChannelSearch;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.UserDetailsUtils;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("${adminPath}/admin-logs")
public class AdminLogController extends AdminBaseController {
	@Resource
	private LogSmbService smbService;

	@Resource
	private SdLoginLogService loginLogService;

	/**
	 * 上传文件日志
	 * 
	 * @param map
	 * @param channelSearch
	 * @return
	 * @throws FileNotFoundException
	 */
	@RequiresPermissions("admin:logs:smb")
	@RequestMapping("smb")
	public String index(Map<String, Object> map, ChannelSearch channelSearch) throws FileNotFoundException {

		map.put("sniperUrl", "/admin-logs/delete");

		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, Object> params = new LinkedHashMap<>();

		if (ValidateUtil.isValid(channelSearch.getName())) {
			params.put("name", "%" + channelSearch.getName() + "%");
		}

		int count = smbService.pageCount(params);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "ctime desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<LogSmb> lists = smbService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("sniperMenu", toHtml);
		return forward("/admin/admin-logs/index.ftl");
	}

	/**
	 * 登录日志
	 * 
	 * @param map
	 * @param channelSearch
	 * @return
	 * @throws FileNotFoundException
	 */
	@RequiresPermissions("admin:logs:login")
	@RequestMapping("login")
	public String login(Map<String, Object> map, ChannelSearch channelSearch) throws FileNotFoundException {

		map.put("sniperUrl", "/admin-logs/delete");

		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, Object> params = new LinkedHashMap<>();
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		if (detailsUtils.hasRole(DataValues.ROLE_ADMIN)) {
			params.put("name", detailsUtils.getPrincipal());
		}

		int count = loginLogService.pageCount(params);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "ctime desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdLoginLog> lists = loginLogService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("sniperMenu", toHtml);
		return forward("/admin/admin-logs/login.ftl");
	}
}