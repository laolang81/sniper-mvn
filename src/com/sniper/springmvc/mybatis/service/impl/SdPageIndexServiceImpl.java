package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdItems;
import com.sniper.springmvc.model.SdPageIndex;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("sdPageIndexService")
public class SdPageIndexServiceImpl extends BaseServiceImpl<SdPageIndex>
		implements SdPageIndexService {

	@Resource
	SdItemsService itemsService;

	@Resource(name = "sdPageIndexDao")
	@Override
	public void setDao(BaseDao<SdPageIndex> dao) {
		super.setDao(dao);
	}

	@Override
	public Map<String, SdPageIndex> getPageIndex() {
		List<SdPageIndex> indexs = this.query("select", null);
		Map<String, SdPageIndex> pages = new HashMap<>();
		if (ValidateUtil.isValid(indexs)) {
			for (SdPageIndex sdPageIndex : indexs) {
				pages.put(sdPageIndex.getId().toString(), sdPageIndex);
			}
		}
		return pages;
	}

	@Override
	public Map<String, SdItems> getItems(int id) {
		SdPageIndex index = get(String.valueOf(id));
		Map<String, SdItems> itemsMap = new HashMap<>();
		if (index != null) {
			String item = index.getItemid();
			if (ValidateUtil.isValid(item)) {
				String[] items = item.split(",");
				for (String string : items) {
					SdItems sdItems = itemsService.get(string);
					if (sdItems != null && sdItems.getStatus() == 1) {
						itemsMap.put(string, sdItems);
					}
				}
			}
		}
		return itemsMap;
	}

	@Override
	public Map<String, String> pageIndexMap(Map<String, SdPageIndex> map) {
		Map<String, String> indexNames = new HashMap<>();
		// 存放处室统计结果，存放栏目统计结果
		for (Entry<String, SdPageIndex> entry : map.entrySet()) {
			indexNames.put(entry.getKey(), entry.getValue().getName());
		}
		return indexNames;
	}
}
