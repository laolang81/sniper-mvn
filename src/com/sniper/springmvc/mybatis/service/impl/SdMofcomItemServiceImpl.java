package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdMofcomItem;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("sdMofcomItemService")
public class SdMofcomItemServiceImpl extends BaseServiceImpl<SdMofcomItem>
		implements SdMofcomItemService {

	@Resource(name = "sdMofcomItemDao")
	@Override
	public void setDao(BaseDao<SdMofcomItem> dao) {
		super.setDao(dao);
	}

	@Override
	public Map<String, String> getMofcom() {
		Map<String, String> params = new HashMap<>();
		List<SdMofcomItem> items = this.query("select", null);
		for (SdMofcomItem sdMofcomItem : items) {
			params.put(String.valueOf(sdMofcomItem.getMid()),
					sdMofcomItem.getMname());
		}
		return params;
	}

}
