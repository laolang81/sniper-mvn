package com.sniper.springmvc.mybatis.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.model.TablesRelation;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.FilesUtil;

@Service("tablesRelationService")
public class TablesRelationServiceImpl extends BaseServiceImpl<TablesRelation>
		implements TablesRelationService {

	@Resource
	ChannelService channelService;

	@Resource
	SdAttachmentsService attachmentsService;

	@Resource(name = "tablesRelationDao")
	@Override
	public void setDao(BaseDao<TablesRelation> dao) {
		super.setDao(dao);
	}

	@Override
	public List<TablesRelation> getTableRelation(String source, String type) {
		Map<String, Object> params = new HashMap<>();
		params.put("source", source);
		params.put("type", type);
		return this.query("find", params);
	}

	@Override
	public List<TablesRelation> getTableRelationBind(String bind, String type) {
		Map<String, Object> params = new HashMap<>();
		params.put("bind", bind);
		params.put("type", type);
		return this.query("find", params);
	}

	/**
	 * 因为频道关系只有一个
	 */
	@Override
	public List<Channel> getChannel(List<TablesRelation> relations) {
		List<Channel> channels = new ArrayList<>();
		for (TablesRelation relation : relations) {
			Channel channel = channelService.get(relation.getBind());
			channels.add(channel);
		}
		return channels;
	}

	@Override
	public List<SdAttachments> getFiles(List<TablesRelation> relations) {
		List<SdAttachments> files = new ArrayList<>();
		for (TablesRelation relation : relations) {
			SdAttachments files2 = attachmentsService.get(relation.getBind());
			if (files2 != null) {
				files.add(files2);
			}

		}
		return files;
	}

	@Override
	public int saveTableRelation(List<TablesRelation> relations, String type) {

		return 0;
	}

	/**
	 * 
	 */
	@Override
	public int saveTableRelation(String source, String bind, String type) {
		String[] binds = bind.split(",");
		int result = 0;
		if (binds.length == 0) {
			return 0;
		}
		int sort = Long.valueOf(System.currentTimeMillis() / 1000).intValue();
		for (int j = 0; j < binds.length; j++) {
			TablesRelation relation = new TablesRelation();
			relation.setSource(source);
			relation.setBind(binds[j]);
			relation.setType(type);
			relation.setId(FilesUtil.getUUIDName("", false));
			sort = sort + 10;
			relation.setOrdersort(sort);
			result += this.insert(relation);
		}
		return result;
	}

	@Override
	public int removeTableRelation(String source, String type) {
		Map<String, Object> params = new HashMap<>();
		params.put("source", source);
		params.put("type", type);
		return this.delete("delete", params);
	}

	@Override
	public int removeTableRelation(String source, String bind, String type) {
		Map<String, Object> params = new HashMap<>();
		params.put("source", source);
		params.put("bind", bind);
		params.put("type", type);

		return this.delete("delete", params);
	}

	public static void main(String[] args) {
		String a = "193102ec-38d3-433d-8821-d52f420e395c,71a2a5a3-0c70-4817-9d93-6bb2cf2c6422,d98215f0-5b6e-4937-9d33-5a7e37d91982,221d9474-c0b0-4750-8b3e-3386394ef41a,228cde51-1ba1-4ff2-878a-ec2b16bedc44,6c8ee85c-c9e4-4138-a07b-25ff1291304f,881056d6-c51c-4bc2-a05c-dc7917709e2b,d3c4c9ea-a7c2-4c93-bb2b-f54338c2b05b";
		String[] aa = a.split(",");
		System.out.println(a);
		for (int i = 0; i < aa.length; i++) {
			System.out.println(aa[i]);
		}
	}
}
