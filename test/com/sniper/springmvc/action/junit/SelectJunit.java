package com.sniper.springmvc.action.junit;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sniper.springmvc.model.SdLink;
import com.sniper.springmvc.model.SdLinkGroup;
import com.sniper.springmvc.mybatis.service.impl.SdLinkGroupService;
import com.sniper.springmvc.mybatis.service.impl.SdLinksService;
import com.sniper.springmvc.utils.HtmlUtil;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:beans.xml" })
public class SelectJunit {

	@Resource
	SdLinkGroupService linkGroupService;

	@Resource
	SdLinksService linkService;

	static Map<String, String> map = new HashMap<>();
	static {
		map.put("7", "/Users/suzhen/Desktop/国务院");
		map.put("8", "/Users/suzhen/Desktop/省政府");
		map.put("9", "/Users/suzhen/Desktop/市主管部门");
		map.put("10", "/Users/suzhen/Desktop/各市商务局");
		map.put("11", "/Users/suzhen/Desktop/各市招商局");
	}

	@Test
	public void group() throws IOException {
		// List<SdLinkGroup> groups = linkGroupService.query("select", null);
		// System.out.println(groups);
		for (Entry<String, String> entry : map.entrySet()) {
			Map<String, String> selects = HtmlUtil
					.selectAsMap(entry.getValue());
			SdLinkGroup group = linkGroupService.get(entry.getKey());
			for (Entry<String, String> select : selects.entrySet()) {
				SdLink link = new SdLink();
				link.setLinkGroup(group);
				link.setUrl(select.getKey());
				link.setName(select.getValue());
				link.setDescription(select.getValue());
				link.setDisplayorder(10);
				link.setViewnum(0);
				link.setLogo("");
				try {
					if (linkService.getLink(select.getValue(), select.getKey()) != null) {
						linkService.insert(link);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}
}
