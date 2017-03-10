package com.sniper.springmvc.mybatis.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import com.sniper.springmvc.model.Channel;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.ValidateUtil;

@Service("channelService")
public class ChannelServiceImpl extends BaseServiceImpl<Channel> implements
		ChannelService {

	@Resource
	private FilesService filesService;

	@Override
	@Resource(name = "channelDao")
	public void setDao(BaseDao<Channel> dao) {

		super.setDao(dao);
	}

	@Override
	public List<Channel> getChannels(String fid, Boolean status,
			Boolean showHome, int limit, String sort) {
		Map<String, Object> params = new HashMap<>();
		params.put("fid", fid);
		params.put("status", status);
		params.put("showHome", showHome);
		params.put("order", sort);

		if (limit > 0) {
			params.put("pageOffset", 0);
			params.put("pageSize", limit);
			return this.query("page", params);
		}

		return this.query("select", params);
	}

	@Override
	public List<Channel> getChannelsByType(Integer[] type, Boolean enabled,
			String sort) {
		Map<String, Object> params = new HashMap<>();
		params.put("showType", type);
		params.put("status", enabled);
		params.put("order", sort);
		return this.query("select", params);
	}

	@Override
	public String changeChannelsToTree(List<Channel> channels,
			String treeFidAttr) {
		Set<String> fids = new HashSet<>();
		for (Channel channel : channels) {
			if (!channel.getFid().equals("0")) {
				fids.add(channel.getFid());
			}
		}
		String[] fidss = fids.toArray(new String[fids.size()]);
		Arrays.sort(fidss);
		List<String> nodes = new ArrayList<>();
		for (Channel c : channels) {

			StringBuffer buffer = new StringBuffer();
			buffer.append("{id:");
			buffer.append("'" + c.getId() + "'");
			buffer.append(",pId:");
			buffer.append("'" + c.getFid() + "'");
			buffer.append(",name:");
			buffer.append("\"");
			buffer.append(HtmlUtils.htmlEscape(c.getName().trim()));
			buffer.append("\"");
			buffer.append(",title:\"");
			buffer.append(HtmlUtils.htmlEscape(c.getName().trim()));
			buffer.append("\"");
			if (Arrays.binarySearch(fidss, c.getId()) > -1) {
				buffer.append(treeFidAttr);
			}
			buffer.append(",target:\"_blank\"");
			buffer.append("}");
			nodes.add(buffer.toString());
		}

		return StringUtils.join(nodes, ",\r");
	}

	@Override
	public int update(Channel t) {

		Channel channel2 = super.get("get", t.getId());
		// 删除老的图片
		if (ValidateUtil.isValid(t.getAttachement())) {
			if (ValidateUtil.isValid(channel2.getAttachement())) {
				if (!t.getAttachement().equals(channel2.getAttachement())) {
					FilesUtil.delFileByPath(channel2.getAttachement());
				}
			}
		} else {
			// 如若提交为空
			if (ValidateUtil.isValid(channel2.getAttachement())) {
				FilesUtil.delFileByPath(channel2.getAttachement());
			}
		}

		return super.update(t);
	}

	@Override
	public int delete(String id) {
		Channel channel2 = super.get("get", id);
		if (ValidateUtil.isValid(channel2.getAttachement())) {
			FilesUtil.delFileByPath(channel2.getAttachement());
		}
		return super.delete(id);
	}

	@Override
	public Map<String, String> getPostChannelsMap(List<Channel> channels) {
		Map<String, String> postChannels = new LinkedHashMap<>();

		if (!channels.isEmpty()) {
			for (Channel c : channels) {
				postChannels.put(String.valueOf(c.getId()), c.getName());
			}
		}
		return postChannels;
	}

	@Override
	public Channel checkName(String name) {
		Map<String, Object> params = new HashMap<>();
		params.put("cname", name);
		List<Channel> channels = this.query("select", params);
		if (channels != null && channels.size() > 0) {
			return channels.get(0);
		}
		return null;
	}
}
