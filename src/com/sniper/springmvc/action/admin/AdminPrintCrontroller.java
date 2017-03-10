package com.sniper.springmvc.action.admin;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.model.SdOpenApply;
import com.sniper.springmvc.model.SdTabLeaveword;
import com.sniper.springmvc.mybatis.service.impl.SdDepartmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdOpenApplyService;
import com.sniper.springmvc.mybatis.service.impl.SdTabLeavewordService;

@Controller
@RequestMapping("${adminPath}/admin-print")
public class AdminPrintCrontroller extends AdminBaseController {

	@Resource
	SdTabLeavewordService leavewordService;

	@Resource
	SdDepartmentsService departmentsService;

	@Resource
	SdOpenApplyService openApplyService;

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

	@RequiresPermissions("admin:openapply:print")
	@RequestMapping("openapplyprint")
	public String openapplyprint(Map<String, Object> map,
			@RequestParam("id") String id) {

		SdOpenApply comment = openApplyService.get(id);
		map.put("comment", comment);
		return forward("/admin/admin-comment/openapplyprint.ftl");
	}

}