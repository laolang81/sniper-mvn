package com.sniper.springmvc.action.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.data.AdminPostSolrData;
import com.sniper.springmvc.model.SystemConfig;
import com.sniper.springmvc.mybatis.service.impl.SystemConfigService;
import com.sniper.springmvc.scheduler.SolrSubjectTask;
import com.sniper.springmvc.scheduler.SolrSubjectViewTask;
import com.sniper.springmvc.searchUtil.AdminGroupSearch;
import com.sniper.springmvc.shiro.scheduler.QuartzManagerUtil;
import com.sniper.springmvc.solr.SolrViewUtil;
import com.sniper.springmvc.solr.SubjectViewModel;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.RedisUtil;
import com.sniper.springmvc.utils.SystemConfigUtil;
import com.sniper.springmvc.utils.TreeZTreeUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 
 * @author sniper
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/admin-config")
public class AdminConfigController extends AdminBaseController {

	@Resource
	private SystemConfigService configService;

	@RequiresPermissions("admin:config:view")
	@RequestMapping("/")
	public String index(Map<String, Object> map, AdminGroupSearch groupSearch) {

		map.put("sniperUrl", "/admin-config/delete");
		map.put("groupSearch", groupSearch);

		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, String> menu = new HashMap<>();
		menu.put("true", "是");
		menu.put("false", "否");

		toHtml.addMapValue("autoload", menu);
		Map<String, String> keys = new HashMap<>();
		keys.put("autoload", "自动加载");

		toHtml.setKeys(keys);
		Map<String, Object> params = new HashMap<>();
		if (ValidateUtil.isValid(groupSearch.getAutoload())) {
			params.put("autoload", groupSearch.getAutoload());
		}
		int count = configService.pageCount(params);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "id desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SystemConfig> lists = configService.pageList(params);
		for (SystemConfig systemConfig : lists) {
			if (systemConfig.getKeyName().equals("rootDir")) {
				systemConfig.setKeyInfo(systemConfig.getKeyInfo() + ",系统获取的参考路径:" + FilesUtil.getRootDir());
			}
		}
		map.put("pageHtml", pageHtml);
		map.put("lists", lists);
		map.put("sniperMenu", toHtml);
		return forward("/admin/admin-config/index.jsp");
	}

	@RequiresPermissions("admin:config:insert")
	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map) {
		map.put("config", new SystemConfig());
		return forward("/admin/admin-config/save-input.jsp");
	}

	@RequiresPermissions("admin:config:insert")
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(@ModelAttribute("config") SystemConfig config, BindingResult result) {

		if (result.getErrorCount() > 0) {
			return forward("/admin/admin-config/save-input.jsp");
		} else {
			config.setId(FilesUtil.getUUIDName("", false));
			configService.insert(config);
		}
		return redirect("/admin-config/insert");
	}

	@RequiresPermissions("admin:config:update")
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(@RequestParam(value = "id", required = false) String id, Map<String, Object> map,
			@ModelAttribute("config") SystemConfig config) {
		if (ValidateUtil.isValid(id)) {
			config = configService.get(id);
		} else {
			return redirect("/admin-config/insert");
		}
		map.put("config", config);
		return forward("/admin/admin-config/save-input.jsp");
	}

	@RequiresPermissions("admin:config:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(@ModelAttribute("config") SystemConfig config, BindingResult result) {
		if (result.getFieldErrorCount() == 0) {
			configService.update(config);
		}
		return forward("/admin/admin-config/save-input.jsp");
	}

	@RequiresPermissions("admin:config:delete")
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Map<String, Object> delete(@RequestParam("delid") String[] delid, @RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) {
		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":
			try {
				configService.batchDelete("delete", delid);
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;
		case "autoload":
			try {
				for (String string : delid) {
					SystemConfig config = configService.get(string);
					config.setAutoload(DataUtil.stringToBoolean(menuValue));
					configService.update(config);
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
	 * 操作
	 * 
	 * @author suzhen
	 * 
	 */
	public final class KeyMap {
		public final Map<Integer, String> keyMap = new LinkedHashMap<>();
		{
			keyMap.put(1, "系统: 更新左侧菜单。");
			keyMap.put(2, "系统: 更新系统配置。");
			keyMap.put(3, "Html: 更新首页缓存。");
			keyMap.put(4, "Solr: 新闻初始化。");
			keyMap.put(5, "Solr: 新闻更新。");
			keyMap.put(6, "Solr: 新闻优化,优化的时候会导致访问变慢。");
			keyMap.put(7, "Solr: 新闻清空，清空之后新闻无法获取");
			keyMap.put(8, "Solr: 更新未审核新闻");
			keyMap.put(9, "Solr: 开启后台新闻Solr读取");
			keyMap.put(10, "Solr: 停止后台新闻Solr读取");
			keyMap.put(11, "Quartz: 停止调度器执行");
			keyMap.put(100, "Redis: 清空当前数据库");
			keyMap.put(101, "Redis: 清空所有数据库");

		}

		public Map<Integer, String> getKeyMap() {
			return keyMap;
		}

		public String getValue(Integer key) {
			if (keyMap.containsKey(key)) {
				return keyMap.get(key);
			}
			return "";
		}
	}

	@RequiresPermissions("admin:config:cache")
	@RequestMapping(value = "cache", method = RequestMethod.GET)
	public String cache(Map<String, Object> map) {
		map.put("keyMap", new KeyMap());
		return site.getName() + "/admin/admin-config/cache.ftl";
	}

	/**
	 * 
	 * @param map
	 * @param clearType
	 * @return
	 * @throws InterruptedException
	 * @throws SchedulerException
	 */
	@RequiresPermissions("admin:config:cache")
	@RequestMapping(value = "cache", method = RequestMethod.POST)
	public String cache(Map<String, Object> map, @RequestParam("clearType") Integer[] clearType)
			throws InterruptedException, SchedulerException {
		// 清空tree菜单数据
		// 数据排序
		List<String> result = new ArrayList<>();
		Arrays.sort(clearType);
		// 数组查询位置,返回其位置索引
		RedisUtil redisUtil = RedisUtil.getInstance();
		if (Arrays.binarySearch(clearType, 1) > -1) {
			try {
				// 清空菜单数据
				// getzTreeMenuData().clear();
				// 清空redis存放的已经生成的数据,清空所有用户组的列表
				// 清空
				TreeZTreeUtil treeUtil = new TreeZTreeUtil();
				treeUtil.clearMenus();
				getzTreeMenuData().clear();
				result.add("左侧菜单:" + new KeyMap().keyMap.get(1) + "<a href=\"" + adminPath.substring(1)
						+ "/admin-config/cache\">[执行]后点击我</a>:操作成功");
			} catch (Exception e) {
				e.printStackTrace();
				result.add(e.getMessage());
			}
		}
		// 操作系统配置
		if (Arrays.binarySearch(clearType, 2) > -1) {
			SystemConfigUtil.clear();
			SystemConfigUtil.init();
			result.add(new KeyMap().keyMap.get(2) + ":操作成功");
		}

		if (Arrays.binarySearch(clearType, 3) > -1) {
			result.add(new KeyMap().keyMap.get(3) + ":操作成功");
		}
		// 初始化
		if (Arrays.binarySearch(clearType, 4) > -1) {
			SolrSubjectViewTask subjectTask = new SolrSubjectViewTask();
			QuartzManagerUtil.start();
			// 添加重复任务，设置属性 1秒间隔，执行一次，立即开始
			QuartzManagerUtil.addSimpleJob(SolrSubjectTask.jobName, subjectTask.getClass(), new Date());
			result.add(new KeyMap().keyMap.get(4) + ":操作成功");
		}
		// 更新
		if (Arrays.binarySearch(clearType, 5) > -1) {
			SolrSubjectViewTask subjectTask = new SolrSubjectViewTask();
			QuartzManagerUtil.start();
			// 添加重复任务，设置属性 1秒间隔，执行一次，立即开始
			QuartzManagerUtil.addSimpleJob(SolrSubjectTask.jobName, subjectTask.getClass(), new Date());
			result.add(new KeyMap().keyMap.get(5) + ":操作成功");
		}
		if (Arrays.binarySearch(clearType, 6) > -1) {
			SolrViewUtil.optimize(SubjectViewModel.CORE_NAME);
			result.add(new KeyMap().keyMap.get(6) + ":操作成功");
		}
		if (Arrays.binarySearch(clearType, 7) > -1) {
			SolrViewUtil.clear(SubjectViewModel.CORE_NAME);
			result.add(new KeyMap().keyMap.get(7) + ":操作成功");
		}

		if (Arrays.binarySearch(clearType, 8) > -1) {
			QuartzManagerUtil.start();
			// 设置新闻读取,读取3种可能的新闻状态
			SolrSubjectViewTask.setENABLED_EQ("0,1,3");
			// 更新任务时间
			QuartzManagerUtil.addSimpleJob(SolrSubjectViewTask.jobName, SolrSubjectViewTask.class, new Date());
			// 执行完毕之后要情况
			SolrSubjectViewTask.setENABLED_EQ("");
			result.add(new KeyMap().keyMap.get(8) + ":操作成功");
		}

		if (Arrays.binarySearch(clearType, 9) > -1) {
			AdminPostSolrData.SOLR = true;
			result.add(new KeyMap().keyMap.get(9) + ":操作成功");
		}

		if (Arrays.binarySearch(clearType, 10) > -1) {
			AdminPostSolrData.SOLR = false;
			result.add(new KeyMap().keyMap.get(9) + ":操作成功");
		}

		if (Arrays.binarySearch(clearType, 11) > -1) {
			QuartzManagerUtil.stop();
			result.add(new KeyMap().keyMap.get(9) + ":操作成功");
		}
		if (Arrays.binarySearch(clearType, 100) > -1) {
			redisUtil.flushDB();
			result.add(new KeyMap().keyMap.get(100) + ":操作成功");
		}

		if (Arrays.binarySearch(clearType, 101) > -1) {
			redisUtil.flushAll();
			result.add(new KeyMap().keyMap.get(101) + ":操作成功");
		}

		map.put("keyMap", new KeyMap());
		map.put("result", result);
		return site.getName() + "/admin/admin-config/cache.ftl";
	}
}