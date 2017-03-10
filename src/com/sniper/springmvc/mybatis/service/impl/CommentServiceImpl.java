package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.Comment;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("commentService")
public class CommentServiceImpl extends BaseServiceImpl<Comment> implements
		CommentService {

	@Resource(name = "commentDao")
	@Override
	public void setDao(BaseDao<Comment> dao) {
		super.setDao(dao);
	}

	@Override
	public List<Comment> getCommentsLastLimit(int limit) {

		Map<String, Object> params = new HashMap<>();
		params.put("order", "createDate desc");
		params.put("pageOffset", 0);
		params.put("pageSize", limit);
		return this.pageList(params);
	}

}