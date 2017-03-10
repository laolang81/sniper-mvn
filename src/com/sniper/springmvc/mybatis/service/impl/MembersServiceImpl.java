package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.Members;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("membersService")
public class MembersServiceImpl extends BaseServiceImpl<Members> implements
		MembersService {

	@Resource(name = "membersDao")
	@Override
	public void setDao(BaseDao<Members> dao) {
		super.setDao(dao);
	}

	@Override
	public Members validateByName(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Members validateByNickName(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Members validateByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Members> findListsByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validateUser(String name, String password) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Members> getUserByLevel(int gid) {
		// TODO Auto-generated method stub
		return null;
	}

	

}
