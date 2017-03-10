package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.SdLink;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.FilesUtil;

@Service("linksService")
public class SdLinksServiceImpl extends BaseServiceImpl<SdLink> implements
		SdLinksService {

	@Resource
	FilesService filesService;

	static Map<String, String> map = new HashMap<>();
	static {
		map.put("7", "/Users/suzhen/Desktop/国务院");
		map.put("8", "/Users/suzhen/Desktop/省政府");
		map.put("9", "/Users/suzhen/Desktop/市主管部门");
		map.put("10", "/Users/suzhen/Desktop/各市商务局");
		map.put("11", "/Users/suzhen/Desktop/各市招商局");
	}

	@Resource(name = "linksDao")
	@Override
	public void setDao(BaseDao<SdLink> dao) {
		super.setDao(dao);
	}

	@Override
	public List<SdLink> getLinksByLid(String lid, int limit) {
		Map<String, Object> params = new HashMap<>();
		params.put("enabled", 1);
		params.put("group", lid);
		params.put("pageOffset", 0);
		params.put("pageSize", limit);
		return this.pageList(params);
	}

	@Override
	public int update(SdLink t) {
		SdLink links = this.get(t.getLinkid() + "");
		if (!links.getLogo().equals(t.getLogo())) {
			if (!links.getLogo().equals("")) {
				FilesUtil.delFileByPath(links.getLogo());
			}
		}
		return super.update(t);
	}

	@Override
	public int delete(String id) {
		SdLink links = this.get(id);
		if (!links.getLogo().equals("")) {
			FilesUtil.delFileByPath(links.getLogo());
		}
		return super.delete(id);
	}

	@Override
	public SdLink getLink(String name, String url) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", name);
		params.put("url", url);
		List<SdLink> links = this.query("select", params);
		if (links != null && links.size() > 1) {
			return links.get(0);
		}
		return null;
	}

}
