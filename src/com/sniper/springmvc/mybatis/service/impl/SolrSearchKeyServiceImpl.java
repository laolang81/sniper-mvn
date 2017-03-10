package com.sniper.springmvc.mybatis.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SolrSearchKey;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("solrSearchKeyService")
public class SolrSearchKeyServiceImpl extends BaseServiceImpl<SolrSearchKey>
		implements SolrSearchKeyService {

	@Resource(name = "solrSearchKeyDao")
	@Override
	public void setDao(BaseDao<SolrSearchKey> dao) {

		super.setDao(dao);
	}

}
