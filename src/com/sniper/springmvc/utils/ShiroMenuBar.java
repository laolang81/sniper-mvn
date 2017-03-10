package com.sniper.springmvc.utils;

import java.util.List;

import com.sniper.springmvc.model.AdminRight;

public class ShiroMenuBar {

	private AdminRight adminRight;
	private List<AdminRight> menuSub;

	public AdminRight getAdminRight() {
		return adminRight;
	}

	public void setAdminRight(AdminRight adminRight) {
		this.adminRight = adminRight;
	}

	public List<AdminRight> getMenuSub() {
		return menuSub;
	}

	public void setMenuSub(List<AdminRight> menuSub) {
		this.menuSub = menuSub;
	}
	


}