package com.sniper.springmvc.action.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.shiro.subject.Subject;
import org.msgpack.MessagePack;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import redis.clients.jedis.ShardedJedis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sniper.springmvc.action.RootController;
import com.sniper.springmvc.data.BaseData;
import com.sniper.springmvc.data.DataValues;
import com.sniper.springmvc.data.RedisDepItemData;
import com.sniper.springmvc.enums.UserPostValue;
import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.model.AdminRight;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.model.SdItems;
import com.sniper.springmvc.mybatis.service.impl.AdminRightService;
import com.sniper.springmvc.mybatis.service.impl.AdminUserService;
import com.sniper.springmvc.mybatis.service.impl.CollectService;
import com.sniper.springmvc.mybatis.service.impl.SdDepartmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdItemsService;
import com.sniper.springmvc.mybatis.service.impl.SdSubjectsService;
import com.sniper.springmvc.mybatis.service.impl.SdViewSubjectService;
import com.sniper.springmvc.utils.ItemsUtil;
import com.sniper.springmvc.utils.TreeZTreeUtil;
import com.sniper.springmvc.utils.UserDetailsUtils;
import com.sniper.springmvc.utils.ValidateUtil;

public abstract class AdminBaseController extends RootController {

	@Resource
	SdDepartmentsService departmentsService;

	@Resource
	SdItemsService itemsService;

	@Resource
	AdminRightService adminRightService;

	@Resource
	AdminUserService adminUserService;

	@Resource
	CollectService collectService;

	@Resource
	SdViewSubjectService viewSubjectService;

	@Resource
	SdSubjectsService subjectsService;

	/**
	 * 系统左侧菜单使用 组装ztree必备
	 */
	private static List<AdminRight> zTreeMenuData = new ArrayList<>();

	/**
	 * 用静态方法储存
	 * 
	 * @return
	 */
	public List<AdminRight> getzTreeMenuData() {

		if (zTreeMenuData.size() == 0) {
			Map<String, Object> params = new HashMap<>();
			params.put("theMenu", true);
			params.put("type", "admin");
			params.put("order", "sort asc");
			zTreeMenuData = adminRightService.query("select", params);
		}
		return zTreeMenuData;
	}

	public AdminBaseController() {
		super();
	}

	@Override
	@ModelAttribute
	public void init(Map<String, Object> map) {
		// ajax不需要获取下面数据
		// 先执行前边的
		super.init(map);
		if (isXMLHttpRequest()) {
			return;
		}
		if (request.getParameter("sniper") != null) {
			return;
		}
		runTimeData.addModelNode("AdminBase Init start");

		String url = this.request.getRequestURI().replaceFirst(
				this.request.getContextPath(), "");
		// 去除jsessionid，用于和后台地址比对
		if (url.indexOf(";") > -1) {
			url = url.substring(0, url.indexOf(";"));
		}
		baseHref.setUrl(url);
		// 组装左边菜单
		TreeZTreeUtil treeUtil = new TreeZTreeUtil();
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		// 下面是导航数据处理
		treeUtil.setUrlPath(url);
		treeUtil.setAdminPath(adminPath);
		treeUtil.setAdminRights(getzTreeMenuData());
		if (detailsUtils.getAminUser() != null) {
			treeUtil.setAdminGroups(detailsUtils.getAminUser().getAdminGroup());
		}
		treeUtil.checkAdminPath();
		treeUtil.init();

		// 增加栏目式的新闻链接
		map.put("menuTop", treeUtil.getMenuTop());
		map.put("menuSub", treeUtil.getMenuSub());
		map.put("menuThree", treeUtil.getMenuThree());

		// 检测是否是新闻添加也面
		if (url.indexOf("admin-post/insert") > -1
				|| url.indexOf("admin-post/update") > -1) {
			AdminRight adminRight = null;
			for (AdminRight ar : getzTreeMenuData()) {
				if ("/doftec/admin-post/items".equals(ar.getUrl())) {
					adminRight = ar;
					break;
				}
			}
			// 虚构一个adminright
			baseHref.setMenuId(adminRight.getId());
			baseHref.setPageTitle("");

			List<String> title = new ArrayList<>();
			title.add(adminRight.getName());
			getMenuBar(adminRight.getFid(), title);
			Collections.reverse(title);
			baseHref.setMenuBar(title);
		} else {
			// 获取当前url的实体
			AdminRight adminRight = treeUtil.getRightByUrl();
			if (null != adminRight) {
				baseHref.setMenuId(adminRight.getId());
				baseHref.setPageTitle(adminRight.getName());

				List<String> title = new ArrayList<>();
				title.add(adminRight.getName());
				getMenuBar(adminRight.getFid(), title);
				Collections.reverse(title);
				baseHref.setMenuBar(title);
			}
		}

		runTimeData.addModelNode("AdminBase init stop");

	}

	/**
	 * 获取导航数据
	 * 
	 * @return
	 */
	public String getMenuBar(String fid, List<String> title) {
		// 通过一个for循环读取adminRight的层级关系
		while (true) {
			if (ValidateUtil.isValid(fid)
					&& !fid.equals(TreeZTreeUtil.menuTopId)) {
				AdminRight adminRight2 = null;
				for (AdminRight adminRight : getzTreeMenuData()) {
					if (fid.equals(adminRight.getId())) {
						adminRight2 = adminRight;
						break;
					}
				}
				if (adminRight2 != null) {
					title.add(adminRight2.getName());
					return getMenuBar(adminRight2.getFid(), title);
				} else {
					break;
				}
			} else {
				break;
			}
		}
		return "";
	}

	/**
	 * 获取用户id用于和其他表关联
	 * 
	 * @return
	 */
	public AdminUser getAdminUser() {
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		return detailsUtils.getAminUser();
	}

	@Override
	protected String redirect(String dir) {
		StringBuilder builder = new StringBuilder();
		builder.append(UrlBasedViewResolver.REDIRECT_URL_PREFIX);
		builder.append(adminPath);
		builder.append(dir);
		return builder.toString();
	}

	@Override
	protected String forward(String dir) {
		StringBuilder builder = new StringBuilder();
		builder.append(site.getName());
		if (!dir.startsWith("/")) {
			builder.append("/");
		}
		builder.append(dir);
		return builder.toString();
	}

	/**
	 * 获取用户拥有的处室
	 * 
	 * @return
	 */
	protected List<String> getUserDeps() {
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		AdminUser adminUser = detailsUtils.getAminUser();
		String dep = adminUser.getSiteid();
		if (ValidateUtil.isValid(dep)) {
			ObjectMapper mapper = new ObjectMapper();
			try {
				@SuppressWarnings("unchecked")
				List<String> b = mapper.readValue(dep, List.class);
				return b;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<>();
	}

	/**
	 * 获取用户要显示的处室
	 * 
	 * @return
	 */
	public Map<String, String> getDep() {
		runTimeData.addModelNode("Redis read dep start");
		Map<String, String> deps = new HashMap<>();
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		// 保存当前用户处室,根据用户名储存
		String keyName = REDIS.getKeyName(detailsUtils.getPrincipal()
				+ BaseData.REDIS_KEY_DEP);
		// 先读取缓存
		deps = REDIS.hgetAll(keyName);
		if (ValidateUtil.isValid(deps) && !deps.isEmpty()) {
			return deps;
		}
		if (!detailsUtils.validRole(DataValues.ROLE_ADMIN)) {
			List<String> bs = getUserDeps();
			int length = bs.size();
			for (int i = 0; i < length; i++) {
				SdDepartments departments = departmentsService.get(bs.get(i));
				if (departments != null && departments.getDeTrue() == 1) {
					deps.put(bs.get(i), departments.getName());
				}
			}
		} else {
			deps = departmentsService.getMapDep(departmentsService.getDep(
					new int[] { 1, 2, 3, 6, 7 }, 1, 10));
		}
		if (ValidateUtil.isValid(deps)) {
			REDIS.hmset(keyName, deps, detailsUtils.getSubject().getSession()
					.getTimeout());
		}
		runTimeData.addModelNode("Redis read dep stop");
		return deps;
	}

	/**
	 * 上面按个类只作为一个私有方法处理这里将作为栏目读取处理，因为map可出储存 在整个网站测试中这里是最浪费时间的大约300毫秒左右
	 * 
	 * @param deps
	 * @return
	 */
	public Map<String, Map<String, String>> getItemsByRedis(
			Map<String, String> deps) {
		runTimeData.addModelNode("Redis: read item start");
		Map<String, Map<String, String>> itemsMap = new HashMap<>();
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		// key前缀
		String keyPrefix = REDIS.getKeyName(detailsUtils.getPrincipal())
				+ BaseData.REDIS_KEY_ITEM;
		MessagePack messagePack = new MessagePack();
		ShardedJedis shardedJedis = REDIS.getShardedPool().getResource();
		Subject subject = detailsUtils.getSubject();
		long timeOut = subject.getSession().getTimeout();
		// 临时储存数据只用

		try {
			if (shardedJedis.exists(keyPrefix)) {
				byte[] objects = shardedJedis.get(keyPrefix.getBytes());
				RedisDepItemData itemData = messagePack.read(objects,
						RedisDepItemData.class);
				runTimeData.addModelNode("Redis: write item stop");
				return itemData.getDepItems();
			} else {
				for (Entry<String, String> entry : deps.entrySet()) {
					List<SdItems> itemsTemp = itemsService.getItemsByDep(
							entry.getKey(), 1);
					if (ValidateUtil.isValid(itemsTemp)) {
						Map<String, SdItems> treeMap = ItemsUtil
								.sortNamed(itemsTemp);
						treeMap = ItemsUtil.sortItemidAsc(treeMap);
						Map<String, String> temp2 = new HashMap<>();
						for (Entry<String, SdItems> entry2 : treeMap.entrySet()) {
							temp2.put(entry2.getKey(), entry2.getValue()
									.getName());
						}
						itemsMap.put(entry.getKey(), temp2);
					}
				}
				RedisDepItemData itemData = new RedisDepItemData();
				itemData.setDepItems(itemsMap);
				shardedJedis.set(keyPrefix.getBytes(),
						messagePack.write(itemData));
				shardedJedis.pexpire(keyPrefix.getBytes(), timeOut);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			REDIS.close(shardedJedis);
		}
		runTimeData.addModelNode("Redis: read mysql item stop");
		return itemsMap;
	}

	/**
	 * 是否重新获取处室
	 * 
	 * @param deps
	 * @param reload
	 * @return
	 */
	public Map<String, Map<String, String>> getItems(Map<String, String> deps,
			boolean reload) {
		Map<String, Map<String, String>> itemsMap = new HashMap<>();
		if (reload) {
			for (Entry<String, String> entry : deps.entrySet()) {
				List<SdItems> itemsTemp = itemsService.getItemsByDep(
						entry.getKey(), 1);
				Map<String, SdItems> treeMap = ItemsUtil.sortNamed(itemsTemp);
				treeMap = ItemsUtil.sortItemidAsc(treeMap);
				Map<String, String> temp2 = new HashMap<>();
				for (Entry<String, SdItems> entry2 : treeMap.entrySet()) {
					temp2.put(entry2.getKey(), entry2.getValue().getName());
				}
				itemsMap.put(entry.getKey(), temp2);
			}
		} else {
			itemsMap = getItemsByRedis(deps);
		}
		return itemsMap;
	}

	/**
	 * 获取栏目，不是以处室为基础 栏目id为key
	 * 
	 * @param deps
	 * @return
	 */
	public Map<String, SdItems> getItemsAll(Map<String, String> deps) {

		Map<String, SdItems> treeMap = new HashMap<>();
		List<SdItems> items = new ArrayList<>();
		for (Entry<String, String> entry : deps.entrySet()) {
			List<SdItems> items1 = itemsService
					.getItemsByDep(entry.getKey(), 1);
			items.addAll(items1);
		}
		treeMap = ItemsUtil.sortNamed(items);
		treeMap = ItemsUtil.sortItemidAsc(treeMap);
		return treeMap;

	}

	/**
	 * 读取用户处理文章的权限
	 * 
	 * @return
	 */
	public int getUserPostInfo(UserPostValue type) {

		int status = 0;
		AdminUser adminUser = getAdminUser();
		List<AdminGroup> adminGroups = adminUser.getAdminGroup();
		for (AdminGroup adminGroup : adminGroups) {
			switch (type) {
			// 是否拥有审核权限
			case AUDIT:
				status = adminGroup.getAudit();
				break;
			// 审核之后新闻的进度
			case AUDIT_RESULT:
				status = adminGroup.getLookthrough();
				break;
			// 发布新闻的状态
			case START_POST:
				status = adminGroup.getStartlookthrough();
				break;
			// 审核新闻的状态
			case READ_POST:
				status = adminGroup.getReadLookthrough();
				break;
			// 是否可以移动新闻
			case MOVE_POST:
				status = adminGroup.getMove();
				break;
			default:
				// 默认都是不允许
				status = 0;
				break;
			}
		}
		return status;
	}

}