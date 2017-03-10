package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.Comment;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface CommentService extends BaseService<Comment> {

	@DataSource(DataSourceValue.SLAVE)
	public List<Comment> getCommentsLastLimit(int limit);
}
