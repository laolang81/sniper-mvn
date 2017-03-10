package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.SdUser;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface AdminUserService extends BaseService<AdminUser> {

	@DataSource(DataSourceValue.SLAVE)
	public AdminUser validateByName(String username);

	@DataSource(DataSourceValue.SLAVE)
	public AdminUser getUserAndGroupAndRight(String username);

	@DataSource(DataSourceValue.SLAVE)
	public AdminUser validateByNickName(String username);

	@DataSource(DataSourceValue.SLAVE)
	public AdminUser validateByEmail(String email);

	@DataSource(DataSourceValue.SLAVE)
	public List<AdminUser> findListByEmail(String email);

	@DataSource(DataSourceValue.SLAVE)
	public boolean validateUser(String name, String password);

	@DataSource
	public void changePassword(String password_old, String password_c);

	@DataSource
	public AdminUser regUser(SdUser sdUser, String password);

	@DataSource(DataSourceValue.SLAVE)
	public AdminUser getUser(String sourceUid);

	@DataSource
	public int changePasswd(String username, String passwd);

}
