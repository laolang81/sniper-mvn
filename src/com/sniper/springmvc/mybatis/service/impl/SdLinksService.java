package com.sniper.springmvc.mybatis.service.impl;

import java.util.List;

import com.sniper.springmvc.datasource.DataSource;
import com.sniper.springmvc.datasource.DataSourceValue;
import com.sniper.springmvc.model.SdLink;
import com.sniper.springmvc.mybatis.service.BaseService;

public interface SdLinksService extends BaseService<SdLink> {

	@DataSource(DataSourceValue.SLAVE)
	public List<SdLink> getLinksByLid(String lid, int limit);

	@DataSource(DataSourceValue.SLAVE)
	public SdLink getLink(String name, String url);

}
