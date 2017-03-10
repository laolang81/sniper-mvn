package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.model.Members;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface MembersService extends BaseService<Members> {

	@DataSource
	public Members validateByName(String username);

	@DataSource
	public Members validateByNickName(String username);

	@DataSource
	public Members validateByEmail(String email);

	@DataSource
	public List<Members> findListsByEmail(String email);

	@DataSource
	public boolean validateUser(String name, String password);

	@DataSource
	public List<Members> getUserByLevel(int gid);

}
