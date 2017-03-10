package com.sniper.springmvc.mybatis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.model.SdContent;
import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.model.SdItems;
import com.sniper.springmvc.model.SdMofcomInfo;
import com.sniper.springmvc.model.SdSubjects;
import com.sniper.springmvc.mybatis.dao.BaseDao;
import com.sniper.springmvc.solr.SolrViewUtil;
import com.sniper.springmvc.solr.SubjectViewModel;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 要重写所有新闻添加删除方法，因为要加入Solr查询
 * 
 * @author suzhen
 * 
 */
@Service("sdSubjectsService")
public class SdSubjectsServiceImpl extends BaseServiceImpl<SdSubjects>
		implements SdSubjectsService {

	@Resource
	AdminUserService adminUserService;

	@Resource
	SdItemsSubjectsService itemsSubjectsSrevice;

	@Resource
	SdSubjectLogsService logsService;

	@Resource
	SdAttachmentsService attachmentsService;

	@Resource
	SdMofcomInfoService mofcomInfoService;

	@Resource
	SdDepartmentsService departmentsService;

	@Resource
	SdItemsService itemsService;

	@Resource
	SdContentService contentService;
	// 新闻的进度说明
	public static Map<String, String> LOOK_THROUTH = new HashMap<>();
	static {
		LOOK_THROUTH.put("3", "一级发布");
		LOOK_THROUTH.put("1", "二级发布");
		LOOK_THROUTH.put("2", "已发布");
		LOOK_THROUTH.put("30", "回收站");
	}

	public static Map<String, String> LOOK_THROUTH_AUDIT = new HashMap<>();
	static {

	}

	public static Map<String, String> LOOK_THROUTH_AUDIT_ONE = new HashMap<>();
	static {

	}

	@Resource(name = "sdSubjectsDao")
	@Override
	public void setDao(BaseDao<SdSubjects> dao) {
		super.setDao(dao);
	}

	@Override
	public SdSubjects get(String id) {
		SdSubjects sdSubjects = super.get(id);
		SdContent content = contentService.get(id);
		if (content != null) {
			sdSubjects.setContent(content);
		}
		return sdSubjects;
	}

	@Override
	public SdSubjects getSubjectWithFiles(String id) {
		SdSubjects sdSubjects = get(id);
		// 读取附件
		List<SdAttachments> attachments = attachmentsService.getFiles(id, 1, 1);
		sdSubjects.setAttachments(attachments);
		return sdSubjects;
	}

	@Override
	public int insert(SdSubjects t) {
		// 处理一些可能为空的值,暂时只处理int
		// 辅助栏目，可能不存在
		if (!ValidateUtil.isValid(t.getIcId())) {
			t.setIcId(0);
		}

		int a = super.insert(t);
		// 操作Solr
		if (ValidateUtil.isValid(t.getContent().getContent())) {
			t.getContent().setSid(t.getSid());
			contentService.insert(t.getContent());
		}
		// 插入Solr
		updateSolr(t);
		return a;
	}

	/**
	 * 更新的时候排除新闻内容更改
	 */
	@Override
	public int update(SdSubjects t) {
		if (t.getContent() != null) {
			t.getContent().setSid(t.getSid());
			contentService.update(t.getContent());
		}
		int result = super.update(t);
		// 操作Solr,必须在更新之后操作
		SolrViewUtil.delete(t.getSid().toString());
		// 更新Solr数据
		updateSolr(t);
		return result;
	}

	/**
	 * 庚子年Solr
	 * 
	 * @param t
	 */
	private void updateSolr(SdSubjects t) {
		// 读取附件
		List<SdAttachments> attachments = attachmentsService.getFiles(t
				.getSid().toString(), null, null);
		t.setAttachments(attachments);
		// 处室
		SdDepartments departments = departmentsService.get(String.valueOf(t
				.getSiteid()));
		// 栏目
		SdItems items = itemsService.get("getItem",
				String.valueOf(t.getItemid()));
		// 下面保证在新插入的索引中内容不会为空
		if (t.getContent() == null) {
			SdContent content = contentService.get(t.getSid().toString());
			if (content != null) {
				t.setContent(content);
			}
		}
		// 获取栏目
		Integer[] itids = itemsSubjectsSrevice.getItems(String.valueOf(t
				.getSid()));
		SubjectViewModel model = SolrViewUtil.getModel(t, departments, items,
				itids);
		// 获取用户信息
		AdminUser adminUser = adminUserService.getUser(t.getAuthorid());
		if (ValidateUtil.isValid(adminUser)) {
			t.setAuthorid(adminUser.getName());
		}
		// 审核人
		if (ValidateUtil.isValid(t.getAuditUid())) {
			AdminUser adminUser1 = adminUserService.getUser(t.getAuditUid());
			if (adminUser1 != null) {
				t.setAuditUid(adminUser1.getName());
			}
		}
		SolrViewUtil.insert(model);
	}

	/**
	 * 重写新闻删除
	 */
	@Override
	public int delete(String id) {
		// 删除索引
		SolrViewUtil.delete(id);
		// 删除日志
		logsService.delete(id);
		// 删除新闻栏目关系
		itemsSubjectsSrevice.delete(id);
		// 删除新闻内容
		contentService.delete(id);
		// 删除商务部关系
		SdMofcomInfo mofcomInfo = mofcomInfoService.get(id);
		if (mofcomInfo != null) {
			mofcomInfo.setDel(1);
			mofcomInfo.setLastdate(DataUtil.getTime());
			// 删除栏目
			mofcomInfoService.update(mofcomInfo);
		}
		// 删除附件关系
		List<SdAttachments> attachments = attachmentsService.getFiles(id, null,
				null);
		if (ValidateUtil.isValid(attachments)) {
			for (SdAttachments sdAttachments : attachments) {
				attachmentsService
						.delete(String.valueOf(sdAttachments.getAid()));
			}
		}
		return super.delete(id);
	}

	/**
	 * 移动处室
	 */
	@Override
	public int changeSiteid(int siteid, int siteidMove, int itemid,
			int itemidMove) {
		SdSubjects sdSubjects = new SdSubjects();
		sdSubjects.setSid(siteid);
		sdSubjects.setSiteid(siteidMove);
		sdSubjects.setItemid(itemidMove);
		sdSubjects.setBhot(itemid);
		return this.update("updateSiteid", sdSubjects);
	}

	@Override
	public Map<String, Object> getSubject(String id) {
		SdSubjects subjects = super.get(id);
		Map<String, Object> result = new HashMap<>();
		result.put("a", subjects);
		SdContent content = contentService.get(id);
		result.put("b", content);
		// 获取首发栏目
		SdItems items = itemsService.get(String.valueOf(subjects.getItemid()));
		result.put("d", items);

		SdDepartments departments = departmentsService.get(String.valueOf(items
				.getDeprtid()));
		result.put("c", departments);
		return result;
	}

	@Override
	public int setSubjectViewZero(String date) {
		Map<String, Object> params = new HashMap<>();
		params.put("date", date);
		return this.dao.update("subjectViewZero", params);
	}
}
