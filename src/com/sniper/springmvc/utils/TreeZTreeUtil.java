package com.sniper.springmvc.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.msgpack.annotation.Message;

import com.sniper.springmvc.model.AdminGroup;
import com.sniper.springmvc.model.AdminRight;

/**
 * 树形数据组装
 * 
 * @author sniper
 * 
 */
@Message
public class TreeZTreeUtil implements Serializable {

	private static final long serialVersionUID = 1L;
	// 后台地址
	private static String adminPath = "";
	// 是否检查权限
	private boolean checkPermisson = true;
	// 资源顶级id
	public static String menuTopId = "63055083-790d-4400-99f0-94bccdb51330";

	private String treeNodesAll;
	private String treeNodes;
	//
	private String contextPath;
	// 用户访问的url
	private String urlPath;
	// 是否已经有数据
	// 顶级菜单
	private List<AdminRight> menuTop = new ArrayList<>();
	// 二级菜单
	private Map<String, List<AdminRight>> menuSub = new HashMap<>();
	// 三级菜单
	private Map<String, List<AdminRight>> menuThree = new HashMap<>();
	// 所有资源
	private List<AdminRight> adminRights = new ArrayList<>();
	// 用户的用户组
	private List<AdminGroup> adminGroups = new ArrayList<>();

	public TreeZTreeUtil() {

	}

	public void setAdminPath(String adminPath) {
		TreeZTreeUtil.adminPath = adminPath;
	}

	public void setMenuTopId(String menuTopId) {
		TreeZTreeUtil.menuTopId = menuTopId;
	}

	public void setCheckPermisson(boolean checkPermisson) {
		this.checkPermisson = checkPermisson;
	}

	public boolean isCheckPermisson() {
		return checkPermisson;
	}

	public void setContextPath(String contentPath) {
		this.contextPath = contentPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

	public void setAdminRights(List<AdminRight> adminRights) {
		this.adminRights = adminRights;
	}

	public void setAdminGroups(List<AdminGroup> adminGroups) {
		this.adminGroups = adminGroups;
	}

	public Map<String, List<AdminRight>> getMenuSub() {

		return menuSub;
	}

	public List<AdminRight> getMenuTop() {
		return menuTop;
	}

	public Map<String, List<AdminRight>> getMenuThree() {
		return menuThree;
	}

	/**
	 * 检查地址中的后台地址，如果不正确就换成正确的的
	 */
	public void checkAdminPath() {
		if (!ValidateUtil.isValid(adminPath)) {
			return;
		}

		for (AdminRight right : adminRights) {
			if (right.getUrl().startsWith(adminPath)) {
				continue;
			}
			// 如果地址前缀和后台地址不一样则替换
			right.setUrl(getRealUrl(right.getUrl()));
		}
	}

	/**
	 * 检查url
	 * 
	 * @param url
	 * @return
	 */
	private String getRealUrl(String url) {
		int a = url.indexOf("/", 1);
		if (adminPath.endsWith("/")) {
			url = adminPath + url.substring(a + 1);
		} else {
			url = adminPath + url.substring(a);
		}

		if (url.startsWith("//")) {
			url = url.substring(1);
		}
		return url;
	}

	public void init() {
		// 读取顶级菜单
		for (AdminRight adminRight : adminRights) {
			if (adminRight.getFid().equals(menuTopId)) {
				// 判断用户是否拥有此权限
				if (isPermisson(adminRight)) {
					if (!menuTop.contains(adminRight)) {
						menuTop.add(adminRight);
					}
				}
			}
		}

		// 读取二级菜单
		for (AdminRight adminRight : menuTop) {
			for (AdminRight adminRight2 : adminRights) {
				if (adminRight2.getFid().equals(adminRight.getId())) {
					if (adminRight2.getTheMenu() && adminRight2.getTheShow()
							&& isPermisson(adminRight2)) {
						addMenuSub(adminRight2);
					}
				}
			}
		}

		// 获取三级菜单
		for (Entry<String, List<AdminRight>> entry : menuSub.entrySet()) {
			for (AdminRight adminRight : entry.getValue()) {
				for (AdminRight adminRight3 : adminRights) {
					if (adminRight3.getFid().equals(adminRight.getId())
							&& adminRight3.getTheMenu()
							&& adminRight3.getTheShow()
							&& isPermisson(adminRight3)) {
						addMenuThree(adminRight3);
					}
				}
			}
		}
	}

	/**
	 * 添加二级菜单
	 * 
	 * @param adminRight
	 */
	private void addMenuSub(AdminRight adminRight) {
		String id = String.valueOf(adminRight.getFid());
		if (menuSub.containsKey(id)) {
			if (!menuSub.get(id).contains(adminRight)) {
				menuSub.get(id).add(adminRight);
			}

		} else {
			List<AdminRight> adminRights = new ArrayList<>();
			adminRights.add(adminRight);
			menuSub.put(id, adminRights);
		}
	}

	/**
	 * 添加三级菜单
	 * 
	 * @param adminRight
	 */
	private void addMenuThree(AdminRight adminRight) {
		String id = String.valueOf(adminRight.getFid());
		if (menuThree.containsKey(id)) {
			if (!menuThree.get(id).contains(adminRight)) {
				menuThree.get(id).add(adminRight);
			}
		} else {
			List<AdminRight> adminRights = new ArrayList<>();
			adminRights.add(adminRight);
			menuThree.put(id, adminRights);
		}
	}

	/**
	 * 检查权限
	 * 
	 * @param adminRight
	 * @return
	 */
	private boolean isPermisson(AdminRight adminRight) {
		if (checkPermisson == false) {
			return true;
		}
		if (adminGroups.size() == 0) {
			return false;
		}
		for (AdminGroup adminGroup : adminGroups) {
			for (AdminRight adminRight2 : adminGroup.getAdminRight()) {
				// 要对group里面的url修改
				String groupUrl = adminRight2.getUrl();
				if (!groupUrl.endsWith(adminPath)) {
					groupUrl = adminPath
							+ groupUrl.substring(groupUrl.indexOf("/", 1));
				}
				if (groupUrl.equals(adminRight.getUrl())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 老版
	 */
	public void initBak() {

		RedisUtil jedis = RedisUtil.getInstance();
		String keyName = jedis.getKeyName(this.getClass(), getRedisKey());
		if (jedis.exists(keyName)) {
			this.treeNodes = jedis.get(keyName);
			return;
		}

		String isParent = "";
		String isHidden = "";
		String url = "";

		List<String> nodes = new ArrayList<>();

		for (AdminRight right : adminRights) {
			// 阻止没有权限的url显示

			// 被隐藏的
			if (!right.getTheShow()) {
				isHidden = ",isHidden:true";
			} else {
				isHidden = "";
			}

			if (right.getUrl().endsWith("#")) {
				url = "";
			} else {
				url = ",url:\"" + this.contextPath + right.getUrl() + "\"";
			}

			// 是否是父级
			isParent = isParent(right);
			if (!"".equals(isParent)) {
				// 父级不能带有url
				url = "";
			}

			StringBuffer buffer = new StringBuffer();
			buffer.append("{id:");
			buffer.append("'" + right.getId() + "'");
			buffer.append(",pId:");
			buffer.append("'" + right.getFid() + "'");
			buffer.append(",name:");
			buffer.append("\"");
			buffer.append(right.getName());
			buffer.append("\"");
			buffer.append(url);
			buffer.append(isParent);
			// buffer.append(classStyle);
			buffer.append(",target:\"");
			buffer.append(right.getTarget());
			buffer.append("\",title:\"");
			buffer.append(right.getName().trim());
			buffer.append("\"");
			buffer.append(isHidden);
			buffer.append("}");

			nodes.add(buffer.toString());
		}

		if (nodes.size() != 0) {
			treeNodes = StringUtils.join(nodes, ",\r");
			jedis.set(keyName, treeNodes);
		}
	}

	/**
	 * 老版 所有的
	 */
	public void initAll() {

		String isParent = "";
		String isOpen = "";

		List<String> nodes = new ArrayList<>();

		for (AdminRight right : adminRights) {

			// 是否是父级
			isParent = isParent(right);
			if (!"".equals(isParent)) {
				// 父级不能带有url
				// isOpen = ",open:true";
			} else {
				// isOpen = "";
			}

			if (right.getFid().equals("0")) {
				isOpen = ",open:true";
			} else {
				isOpen = "";
			}

			StringBuffer buffer = new StringBuffer();
			buffer.append("{id:");
			buffer.append("'" + right.getId() + "'");
			buffer.append(",pId:");
			buffer.append("'" + right.getFid() + "'");
			buffer.append(",name:");
			buffer.append("\"");
			buffer.append(right.getName());
			buffer.append("\"");
			buffer.append(isParent);
			buffer.append(isOpen);
			buffer.append("}");
			nodes.add(buffer.toString());
		}
		if (nodes.size() != 0) {
			treeNodesAll = StringUtils.join(nodes, ",\r");
		}
	}

	/**
	 * 获取当前url对象 ,如果当前url不能被显示就获取起父级菜单
	 * 
	 * @return
	 */
	public AdminRight getRightByUrl() {
		for (AdminRight right : adminRights) {
			String url = getRealUrl(right.getUrl());
			if (url.equalsIgnoreCase(urlPath)) {
				// 如果不能显示
				if (!right.getTheShow()) {
					AdminRight parentRight = getParent(right);
					if (parentRight != null) {
						return parentRight;
					}
				}
				return right;
			}
		}
		return null;
	}

	/**
	 * 获取父级
	 * 
	 * @param adminRight
	 * @return
	 */
	private AdminRight getParent(AdminRight adminRight) {
		for (AdminRight right : adminRights) {
			if (right.getId().equals(adminRight.getFid())) {
				return right;
			}
		}
		return null;

	}

	/**
	 * 是否是父级
	 * 
	 * @param adminRight
	 * @return
	 */
	private String isParent(AdminRight adminRight) {
		for (AdminRight right : adminRights) {
			if (adminRight.getId().equals(right.getFid())) {
				return ",isParent:true";
			}
		}

		return "";
	}

	public String getTreeNodes() {
		return treeNodes;
	}

	public void setTreeNodes(String treeNodes) {
		this.treeNodes = treeNodes;
	}

	public String getTreeNodesAll() {
		return treeNodesAll;
	}

	/**
	 * 获取redis保存的key
	 * 
	 * @return
	 */
	public String getRedisKey() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("treeNodes_");
		if (adminGroups.size() == 0) {
			return "";
		}
		for (AdminGroup g : adminGroups) {
			buffer.append(g.getName());
		}
		return buffer.toString();
	}

	public void clearCache() {

		RedisUtil jedis = RedisUtil.getInstance();
		for (AdminGroup g : adminGroups) {
			String keyName = jedis.getKeyName(this.getClass(),
					"treeNodes_" + g.getName());
			jedis.del(keyName);
		}
	}

	/**
	 * 清空数据
	 */
	public void clearMenus() {
		menuTop.clear();
		menuSub.clear();
		menuThree.clear();
	}

	public class TypeComparator implements Comparator<AdminRight> {
		@Override
		public int compare(AdminRight a, AdminRight b) {
			if (a.getSort() < b.getSort()) {
				return -1;
			} else if (a.getSort() == a.getSort()) {
				return 1;
			} else {
				return 0;
			}
		}
	}

}
