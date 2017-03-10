package com.sniper.springmvc.utils;

import java.util.Calendar;
import java.util.List;

import com.sniper.springmvc.model.AdminRight;

public class BaseHref {

	private String url;
	private String menuId;
	private String pageTitle;
	private String baseHref;
	private String webUrl;
	private String zTreeMenu;
	private String adminPath;
	private AdminRight adminRight;
	private int nowYear;
	private boolean admin;
	private List<String> menuBar;

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

	public String getBaseHref() {
		return baseHref;
	}

	public void setBaseHref(String baseHref) {
		this.baseHref = baseHref;
	}

	public String getWebUrl() {
		return webUrl;
	}

	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}

	public BaseHref(String baseHref) {
		super();
		this.baseHref = baseHref;
	}

	public void setzTreeMenu(String zTreeMenu) {
		this.zTreeMenu = zTreeMenu;
	}

	public String getzTreeMenu() {
		return zTreeMenu;
	}

	public void setAdminPath(String adminPath) {
		this.adminPath = adminPath;
	}

	public String getAdminPath() {
		return adminPath;
	}

	public void setAdminRight(AdminRight adminRight) {
		this.adminRight = adminRight;
	}

	public AdminRight getAdminRight() {
		return adminRight;
	}

	public int getNowYear() {
		return nowYear;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public BaseHref() {

		Calendar calendar = Calendar.getInstance();
		nowYear = calendar.get(Calendar.YEAR);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getMenuBar() {
		return menuBar;
	}

	public void setMenuBar(List<String> menuBar) {
		this.menuBar = menuBar;
	}

	@Override
	public String toString() {
		return "BaseHref [url=" + url + ", menuId=" + menuId + ", pageTitle="
				+ pageTitle + ", baseHref=" + baseHref + ", webUrl=" + webUrl
				+ ", zTreeMenu=" + zTreeMenu + ", adminPath=" + adminPath
				+ ", adminRight=" + adminRight + ", nowYear=" + nowYear
				+ ", admin=" + admin + "]";
	}

}
