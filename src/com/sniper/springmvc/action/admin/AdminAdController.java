package com.sniper.springmvc.action.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.data.DataValues;
import com.sniper.springmvc.model.SdAd;
import com.sniper.springmvc.model.SdAdAddress;
import com.sniper.springmvc.mybatis.service.impl.ChannelService;
import com.sniper.springmvc.mybatis.service.impl.SdAdAddressService;
import com.sniper.springmvc.mybatis.service.impl.SdAdService;
import com.sniper.springmvc.mybatis.service.impl.SdAttachmentsService;
import com.sniper.springmvc.searchUtil.ChannelSearch;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.ValidateUtil;

@RequestMapping("${adminPath}/admin-ads")
@Controller
public class AdminAdController extends AdminBaseController {

	@Resource
	SdAdService adService;

	@Resource
	SdAdAddressService addressService;

	@Resource
	ChannelService channelService;

	@Resource
	SdAttachmentsService attachmentsService;

	/**
	 * 获取友情链接组
	 * 
	 * @return
	 */
	public List<SdAdAddress> getChannels() {
		List<SdAdAddress> channels = new ArrayList<>();
		if (channels.size() == 0) {
			channels = addressService.query("select", null);
		}
		return channels;
	}

	@RequiresPermissions("admin:ads:view")
	@RequestMapping("/")
	public String index(Map<String, Object> map, ChannelSearch search) {

		map.put("sniperUrl", "/admin-ads/delete");

		ParamsToHtml toHtml = new ParamsToHtml();

		toHtml.addMapValue("enabled", DataValues.YES_NO);

		Map<String, String> channelsMap = addressService
				.mapFormat(getChannels());

		toHtml.addMapValue("channels", channelsMap);
		toHtml.getKeys().put("enabled", "状态");
		toHtml.getKeys().put("channels", "频道");

		Map<String, Object> params = new HashMap<>();
		if (ValidateUtil.isValid(search.getType())) {
			params.put("address", search.getType());
		}

		if (ValidateUtil.isValid(search.getStatus())) {
			params.put("enabled", search.getStatus());
		}

		if (ValidateUtil.isValid(search.getName())) {
			params.put("name", "%" + search.getName() + "%");
		}

		int count = adService.pageCount(params);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "ad_id desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdAd> lists = adService.pageList(params);

		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("sniperMenu", toHtml);
		map.put("search", search);

		return forward("/admin/admin-ads/index.jsp");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:ads:insert")
	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map) {

		map.put("ad", new SdAd());
		Map<String, String> address = addressService.mapFormat(getChannels());
		map.put("address", address);
		map.put("yes_no", DataValues.YES_NO);
		return forward("/admin/admin-ads/save-input.jsp");
	}

	@RequiresPermissions("admin:ads:insert")
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(SdAd ad, BindingResult result, Map<String, Object> map) {

		try {
			if (result.getFieldErrorCount() > 0) {
				Map<String, String> address = addressService
						.mapFormat(getChannels());
				map.put("address", address);
				map.put("yes_no", DataValues.YES_NO);
				return forward("/admin/admin-ads/save-input.jsp");
			} else {
				ad.setCtime(new Date());
				ad.setUid(getAdminUser().getId());
				ad.setType(1);
				adService.insert(ad);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return redirect("/admin-ads/insert");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:ads:update")
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) String id, SdAd ad,
			Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {
			ad = adService.get(id);
		} else {
			return redirect("/admin-ads/insert");
		}

		map.put("ad", ad);
		Map<String, String> address = addressService.mapFormat(getChannels());
		map.put("address", address);
		map.put("yes_no", DataValues.YES_NO);
		return forward("/admin/admin-ads/save-input.jsp");
	}

	@RequiresPermissions("admin:ads:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(SdAd ad, BindingResult result, Map<String, Object> map) {

		try {
			if (result.getFieldErrorCount() > 0) {
				Map<String, String> address = addressService
						.mapFormat(getChannels());
				map.put("address", address);
				map.put("yes_no", DataValues.YES_NO);
				return redirect("/admin/admin-ads/save-input.jsp");
			} else {
				SdAd links2 = adService.get(ad.getId() + "");
				// 删除老的图片
				if (ValidateUtil.isValid(ad.getPath())) {
					if (ValidateUtil.isValid(links2.getPath())) {
						if (!ad.getPath().equals(links2.getPath())) {
							attachmentsService.deleteByPath(links2.getPath());
						}
					}
				}
				adService.update(ad);
			}
		} catch (Exception e) {

		}

		return redirect("/admin-ads/update?id=" + ad.getId());
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:ads:delete")
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Map<String, Object> delete(@RequestParam("delid") String[] delid,
			@RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) {

		// code 小于1表示有错误,大于0表示ok,==0表示未操作
		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":

			try {
				for (String string : delid) {
					adService.delete(string);
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;
		case "enabled":

			try {
				Map<String, String> result = new HashMap<>();
				int status = DataUtil.stringToInteger(menuValue);
				for (String string : delid) {
					SdAd ad = adService.get(string);
					ad.setEnabled(status);
					adService.update(ad);
					result.put("enabled", DataValues.YES_NO.get(menuValue));
					ajaxResult.put(string, result);
				}

				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}

			break;

		case "channels":
			Map<String, String> result = new HashMap<>();
			Map<String, String> address = addressService
					.mapFormat(getChannels());
			for (String string : delid) {
				SdAd ad = adService.get(string);
				ad.setAddress(menuValue);
				adService.update(ad);
				result.put("channels", address.get(menuValue));
				ajaxResult.put(string, result);
			}
			ajaxResult.put("code", 1);
			ajaxResult.put("msg", "success");
			break;
		default:
			break;
		}
		return ajaxResult;
	}

	@RequiresPermissions("admin:address:view")
	@RequestMapping("address")
	public String address(Map<String, Object> map, ChannelSearch search) {

		map.put("sniperUrl", "/admin-ads/addressdelete");

		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, Object> params = new HashMap<>();

		if (ValidateUtil.isValid(search.getName())) {
			params.put("name", "%" + search.getName() + "%");
		}

		int count = addressService.pageCount(params);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "aa_id desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdAdAddress> lists = addressService.pageList(params);

		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("sniperMenu", toHtml);
		map.put("search", search);

		return forward("/admin/admin-ads/address.jsp");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:address:insert")
	@RequestMapping(value = "addressinsert", method = RequestMethod.GET)
	public String addressinsert(Map<String, Object> map) {

		map.put("ad", new SdAdAddress());
		return forward("/admin/admin-ads/save-address.jsp");
	}

	@RequiresPermissions("admin:address:insert")
	@RequestMapping(value = "addressinsert", method = RequestMethod.POST)
	public String addressinsert(SdAdAddress ad, BindingResult result) {

		try {
			if (result.getFieldErrorCount() > 0) {
				return forward("/admin/admin-ads/save-address.jsp");
			} else {
				ad.setCtime(new Date());
				ad.setUid(getAdminUser().getId());
				ad.setKey("0");
				addressService.insert(ad);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return redirect("/admin-ads/addressinsert");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:address:update")
	@RequestMapping(value = "addressupdate", method = RequestMethod.GET)
	public String addressupdate(
			@RequestParam(value = "id", required = false) String id,
			SdAdAddress ad, Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {
			ad = addressService.get(id);
			if (ad != null) {
				map.put("ad", ad);
				return forward("/admin/admin-ads/save-address.jsp");
			}
		}
		return redirect("/admin-ads/addressinsert");

	}

	@RequiresPermissions("admin:address:update")
	@RequestMapping(value = "addressupdate", method = RequestMethod.POST)
	public String update(SdAdAddress ad, BindingResult result) {

		try {
			if (result.getFieldErrorCount() > 0) {
				return redirect("/admin/admin-ads/save-address.jsp");
			} else {
				addressService.update(ad);
			}
		} catch (Exception e) {

		}

		return redirect("/admin-ads/addressupdate?id=" + ad.getId());
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:address:delete")
	@ResponseBody
	@RequestMapping(value = "addressdelete", method = RequestMethod.POST)
	public Map<String, Object> addressdelete(
			@RequestParam("delid") String[] delid,
			@RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) {

		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":
			try {
				for (String string : delid) {
					Map<String, Object> params = new HashMap<>();
					params.put("address", string);
					if (adService.query("select", params) == null) {
						addressService.delete(string);
					}

				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}

			break;

		default:
			break;
		}
		return ajaxResult;
	}

}
