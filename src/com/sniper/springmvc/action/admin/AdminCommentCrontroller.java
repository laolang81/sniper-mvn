package com.sniper.springmvc.action.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.model.SdOpenApply;
import com.sniper.springmvc.model.SdTabLeaveword;
import com.sniper.springmvc.mybatis.service.impl.SdDepartmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdOpenApplyService;
import com.sniper.springmvc.mybatis.service.impl.SdTabLeavewordService;
import com.sniper.springmvc.searchUtil.CommentHandle;
import com.sniper.springmvc.searchUtil.CommentSearch;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.UserDetailsUtils;
import com.sniper.springmvc.utils.ValidateUtil;

@Controller
@RequestMapping("${adminPath}/admin-comment")
public class AdminCommentCrontroller extends AdminBaseController {
	public static Map<String, String> STATUS = new HashMap<>();
	static {
		STATUS.put("0", "新留言");
		STATUS.put("1", "已审");
		STATUS.put("2", "已回");
	}

	public static Map<String, String> TYPES = new HashMap<>();
	static {
		TYPES.put("0", "全部");
		TYPES.put("1", "资讯");
		TYPES.put("2", "留言");
		TYPES.put("3", "投诉");
		TYPES.put("4", "建议");
		TYPES.put("5", "在线访谈");
		TYPES.put("6", "厅长信箱");
		TYPES.put("7", "申请 在线受理");
		TYPES.put("8", "反映形式主义意见");
		TYPES.put("9", "反映官僚主义意见");
		TYPES.put("10", "反映享乐主义意见");
		TYPES.put("11", "反映奢靡之风意见");
		TYPES.put("12", "英文版");
		TYPES.put("13", "中韩自贸政策进万企");
		TYPES.put("702", "政府购买服务");
	}

	@Resource
	SdTabLeavewordService leavewordService;

	@Resource
	SdOpenApplyService openApplyService;

	@Resource
	SdDepartmentsService departmentsService;

	@RequiresPermissions("admin:comment:view")
	@RequestMapping("")
	public String view(Map<String, Object> map, CommentSearch commentSearch) {

		ParamsToHtml toHtml = new ParamsToHtml();

		toHtml.addMapValue("status", STATUS);

		Map<String, String> keys = new HashMap<>();
		keys.put("status", "变更状态");
		toHtml.setKeys(keys);
		Map<String, Object> params = new HashMap<>();

		if (!ValidateUtil.isValid(commentSearch.getFlag())) {
			params.put("stateGt", 0);
		}
		// 状态读取，排除回收站
		getParams(commentSearch, params);

		int count = leavewordService.pageCount(params);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		if (ValidateUtil.isValid(commentSearch.getOrder())) {
			params.put("order", " answer_diff " + commentSearch.getOrder());
		} else {
			params.put("order", "id desc");
		}

		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdTabLeaveword> lists = leavewordService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("toHtml", toHtml);
		map.put("commentSearch", commentSearch);
		map.put("delete", "Delete");
		map.put("type", TYPES);
		map.put("status", STATUS);
		map.put("departments", getDep());
		return forward("/admin/admin-comment/view.ftl");
	}

	/**
	 * 厅长信箱
	 * 
	 * @param map
	 * @param commentSearch
	 * @return
	 */
	@RequiresPermissions("admin:comment:ministermail")
	@RequestMapping("ministermail")
	public String ministermail(Map<String, Object> map,
			CommentSearch commentSearch) {

		ParamsToHtml toHtml = new ParamsToHtml();

		toHtml.addMapValue("status", STATUS);

		Map<String, String> keys = new HashMap<>();
		keys.put("status", "变更状态");
		toHtml.setKeys(keys);
		Map<String, Object> params = new HashMap<>();
		// 设置读取频道6
		commentSearch.setChannel("6");
		// 状态读取，排除回收站
		if (!ValidateUtil.isValid(commentSearch.getFlag())) {
			params.put("stateGt", 0);
		}
		getParams(commentSearch, params);

		int count = leavewordService.pageCount(params);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "id desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdTabLeaveword> lists = leavewordService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("toHtml", toHtml);
		map.put("commentSearch", commentSearch);
		map.put("delete", "Delete");
		map.put("type", TYPES);
		map.put("status", STATUS);
		map.put("departments", getDep());
		return forward("/admin/admin-comment/view.ftl");
	}

	/**
	 * 读取条件设置
	 * 
	 * @param channelSearch
	 * @param params
	 */
	private void getParams(CommentSearch commentSearch,
			Map<String, Object> params) {
		// 状态搜索
		if (ValidateUtil.isValid(commentSearch.getFlag())) {
			params.put("state", commentSearch.getFlag());
		}
		// 类型限制
		if (ValidateUtil.isValid(commentSearch.getChannel())) {
			if (commentSearch.getChannel().equals("6")) {
				params.put("minister", "1");
			} else {
				params.put("type", commentSearch.getChannel());
			}
		}
		// 处室限制
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		AdminUser adminUser = detailsUtils.getAminUser();
		// 如果不为空就是读取全部，
		if (!detailsUtils.hasRole("ROLE_ADMIN")) {
			List<Integer> siteids = new ArrayList<>();
			if (ValidateUtil.isValid(adminUser.getSiteid())) {
				String siteid = adminUser.getSiteid();
				siteids = detailsUtils.getSiteid(siteid);
			} else {
				// 当为空的时候不读取
				siteids.add(0);
			}
			params.put("depids", siteids);
		}

		// 内容搜索
		if (ValidateUtil.isValid(commentSearch.getName())) {
			params.put("name", "%" + commentSearch.getName() + "%");
		}

		// 日期限制
		if (ValidateUtil.isValid(commentSearch.getStartDate())) {
			params.put("stime", commentSearch.getStartDate());
		}
		if (ValidateUtil.isValid(commentSearch.getEndDate())) {
			params.put("etime", commentSearch.getEndDate());
		}
	}

	/**
	 * 回收站
	 * 
	 * @param map
	 * @param commentSearch
	 * @return
	 */
	@RequiresPermissions("admin:comment:recycle")
	@RequestMapping("recycle")
	public String recycle(Map<String, Object> map, CommentSearch commentSearch) {

		Map<String, Object> params = new HashMap<>();
		// 回收站
		commentSearch.setFlag(0);
		getParams(commentSearch, params);

		int count = leavewordService.pageCount(params);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "id desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdTabLeaveword> lists = leavewordService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("commentSearch", commentSearch);
		map.put("delete", "DeleteTrue");
		return forward("/admin/admin-comment/recycle.ftl");
	}

	@RequiresPermissions("admin:comment:handle")
	@ResponseBody
	@RequestMapping("handle")
	public Map<String, Object> commentHandle(CommentHandle handle) {
		Map<String, Object> map = new HashMap<>();
		SdTabLeaveword comment = leavewordService.get(handle.getId());
		map.put("msg", 0);
		switch (handle.getAction()) {
		case "Ban":
			// 隐藏
			comment.setbShow(0);
			leavewordService.update("update", comment);
			break;
		// 显示
		case "Enable":
			comment.setbShow(1);
			leavewordService.update("update", comment);
			break;
		// 移动处室
		case "Offices":
			comment.setOfficeMoveTime(new Date());
			comment.setDepId(handle.getOffice());
			leavewordService.update(comment);
			break;
		// 回复
		case "Reply":
			if (ValidateUtil.isValid(handle.getReplay())) {
				comment.setState(2);
				comment.setBopen(1);
				comment.setAnswerTime(new Date());
				comment.setAnswerUser(getAdminUser().getName());
				comment.setAnswer(handle.getReplay());
			}
			leavewordService.update(comment);
			break;
		// 移动到回收站
		case "Delete":
			try {
				comment.setState(0);
				leavewordService.update(comment);
				map.put("msg", 1);
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		// 删除
		case "DeleteTrue":
			try {
				leavewordService.delete(comment.getId() + "");
				map.put("msg", 1);
			} catch (Exception e) {
			}
			break;
		// 恢复状态
		case "DeleteBack":
			comment.setState(1);
			leavewordService.update("update", comment);
			break;

		default:
			break;
		}

		return map;
	}

	@RequiresPermissions("admin:comment:print")
	@RequestMapping("print")
	public String print(Map<String, Object> map, @RequestParam("id") String id) {

		SdTabLeaveword comment = leavewordService.get(id);
		map.put("comment", comment);
		List<SdDepartments> departments = departmentsService.getDep(new int[] {
				1, 2, 3, 6, 7 }, 1, 10);
		map.put("departments", departmentsService.getMapDep(departments));
		return forward("/admin/admin-comment/print.ftl");
	}

	@RequiresPermissions("admin:openapply:view")
	@RequestMapping("openapply")
	public String openApply(Map<String, Object> map, CommentSearch commentSearch) {

		ParamsToHtml toHtml = new ParamsToHtml();
		Map<String, String> keys = new HashMap<>();
		toHtml.setKeys(keys);

		Map<String, Object> params = new HashMap<>();

		if (ValidateUtil.isValid(commentSearch.getName())) {
			params.put("name", commentSearch.getName());
		}

		int count = openApplyService.pageCount(params);
		PageUtil page = new PageUtil(count, 50);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "id desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdOpenApply> lists = openApplyService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("toHtml", toHtml);
		map.put("commentSearch", commentSearch);
		map.put("delete", "DeleteTrue");
		map.put("status", STATUS);
		return forward("/admin/admin-comment/openapply.ftl");
	}

	@RequiresPermissions("admin:openapply:handle")
	@ResponseBody
	@RequestMapping("openapplyhandle")
	public Map<String, Object> openApplyHandle(CommentHandle handle) {
		Map<String, Object> map = new HashMap<>();
		SdOpenApply comment = openApplyService.get(handle.getId());
		map.put("msg", 0);
		switch (handle.getAction()) {
		// 显示
		case "Enable":
			if (handle.isDisplay()) {
				comment.setEnabled(1);
			} else {
				comment.setEnabled(0);
			}
			map.put("msg", 1);
			openApplyService.update(comment);
			break;

		// 移动到回收站
		case "Delete":
			try {
				comment.setEnabled(0);
				openApplyService.update(comment);
				map.put("msg", 1);
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		// 删除
		case "DeleteTrue":
			try {
				openApplyService.delete(comment.getId() + "");
				map.put("msg", 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		default:
			break;
		}

		return map;
	}

	@RequiresPermissions("admin:openapply:print")
	@RequestMapping("openapplyprint")
	public String openapplyprint(Map<String, Object> map,
			@RequestParam("id") String id) {

		SdOpenApply comment = openApplyService.get(id);
		map.put("comment", comment);
		return forward("/admin/admin-comment/openapplyprint.ftl");
	}

}