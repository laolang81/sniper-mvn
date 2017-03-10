package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.Tags;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("tagsService")
public class TagsServiceImpl extends BaseServiceImpl<Tags> implements
		TagsService {

	@Override
	@Resource(name = "tagsDao")
	public void setDao(BaseDao<Tags> dao) {
		super.setDao(dao);
	}

	@Override
	public List<Tags> getTagsByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void viewAdd(String name) {
		// TODO Auto-generated method stub

	}

	@Override
	public int insertLocale(Tags t) {
		return this.insert(t);
	}

	@Override
	public Tags getLocale(String id) {
		return super.get(id);
	}

}
