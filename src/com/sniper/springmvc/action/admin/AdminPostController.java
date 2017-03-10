package com.sniper.springmvc.action.admin;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.web.util.WebUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.msgpack.MessagePack;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.sniper.springmvc.data.AdminPostSolrData;
import com.sniper.springmvc.data.BaseData;
import com.sniper.springmvc.data.DataValues;
import com.sniper.springmvc.enums.UserPostValue;
import com.sniper.springmvc.freemarker.ViewHomeUtils;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.SdAttachments;
import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.model.SdItems;
import com.sniper.springmvc.model.SdMofcomInfo;
import com.sniper.springmvc.model.SdMofcomItem;
import com.sniper.springmvc.model.SdPageIndex;
import com.sniper.springmvc.model.SdSubjectLogs;
import com.sniper.springmvc.model.SdSubjects;
import com.sniper.springmvc.model.SdTabInfoClass;
import com.sniper.springmvc.model.SdViewSubject;
import com.sniper.springmvc.mybatis.service.impl.SdAttachmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdItemsSubjectsService;
import com.sniper.springmvc.mybatis.service.impl.SdMofcomInfoService;
import com.sniper.springmvc.mybatis.service.impl.SdMofcomItemService;
import com.sniper.springmvc.mybatis.service.impl.SdPageIndexService;
import com.sniper.springmvc.mybatis.service.impl.SdSubjectLogsService;
import com.sniper.springmvc.mybatis.service.impl.SdSubjectLogsServiceImpl;
import com.sniper.springmvc.mybatis.service.impl.SdSubjectsServiceImpl;
import com.sniper.springmvc.mybatis.service.impl.SdTabInfoClassService;
import com.sniper.springmvc.searchUtil.PostSearch;
import com.sniper.springmvc.solr.SolrViewUtil;
import com.sniper.springmvc.solr.SubjectViewModel;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.ExeclStaticExportUtils;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.HttpRequestUtils;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.ParamsToHtmlButton;
import com.sniper.springmvc.utils.SystemConfigUtil;
import com.sniper.springmvc.utils.UserDetailsUtils;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 要实现的读取，未审核，主站图片，图片，置顶新闻，要闻管理，推荐新闻，所有文章，回收站，文章移动，聚合文章，文章搜索,
 * 
 * @author suzhen
 * 
 */
@RequestMapping("${adminPath}/admin-post")
@Controller
public class AdminPostController extends AdminBaseController {

	@Resource
	SdAttachmentsService attachmentsService;

	@Resource
	SdTabInfoClassService infoClassService;

	@Resource
	SdSubjectLogsService subjectLogsService;

	@Resource
	SdItemsSubjectsService itemsSubjectsSrevice;

	@Resource
	SdPageIndexService pageIndexService;

	@Resource
	SdMofcomInfoService mofcomInfoService;

	@Resource
	SdMofcomItemService mofcomItemService;

	/**
	 * 新闻添加栏目列表
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:post:items")
	@RequestMapping("items")
	public String postItems(Map<String, Object> map) {
		Map<String, String> deps = getDep();
		Map<String, Map<String, String>> items = getItemsByRedis(deps);
		map.put("deps", getDep());
		map.put("items", items);
		return forward("/admin/admin-post/post-items.ftl");
	}

	/**
	 * 文章管理，所有新闻(不包含回收站)
	 * 
	 * @param map
	 * @param search
	 * @return
	 * @throws ParseException
	 */
	@RequiresPermissions("admin:post:view")
	@RequestMapping("/")
	public String index(Map<String, Object> map, PostSearch search) throws ParseException {
		ParamsToHtml toHtml = new ParamsToHtml();
		Map<String, String> keys = new HashMap<>();

		toHtml.setKeys(keys);
		// 只可以看到已审核的新闻
		search.getLookthroughed().add(2);
		return post(map, search, toHtml);
	}

	/**
	 * 未审核新闻读取
	 * 
	 * @param map
	 * @param search
	 * @return
	 * @throws ParseException
	 */
	@RequiresPermissions("admin:post:audit")
	@RequestMapping("audit")
	public String audit(Map<String, Object> map, PostSearch search) throws ParseException {

		ParamsToHtml toHtml = new ParamsToHtml();
		Map<String, String> keys = new HashMap<>();
		toHtml.setKeys(keys);

		// 设置新闻读取状态
		// 0shiro新发布的新闻，1shiro特殊处室发布的新闻
		// 0是厨师
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		if (detailsUtils.validRole(DataValues.ROLE_ADMIN)) {
			// 0本该没有，但是怕
			search.getLookthroughed().add(0);
			// 1是用户默认发布状态
			search.getLookthroughed().add(1);
		} else {
			// 不是管理员
			search.getLookthroughed().add(getUserPostInfo(UserPostValue.READ_POST));
		}

		return post(map, search, toHtml);
	}

	/**
	 * 回收站读取
	 * 
	 * @param map
	 * @param search
	 * @return
	 * @throws ParseException
	 */
	@RequiresPermissions("admin:post:recyle")
	@RequestMapping("recyle")
	public String recyle(Map<String, Object> map, PostSearch search) throws ParseException {

		ParamsToHtml toHtml = new ParamsToHtml();
		Map<String, String> keys = new HashMap<>();
		toHtml.setKeys(keys);
		if (getUserPostInfo(UserPostValue.AUDIT) > 0) {
			ParamsToHtmlButton auditItem = new ParamsToHtmlButton();
			auditItem.setColor("success");
			auditItem.setName("恢复");
			auditItem.setType("audit");
			auditItem.setValue("audit");
			toHtml.getButtons().add(auditItem);
		}
		// 设置新闻读取状态
		search.getLookthroughed().add(30);
		return post(map, search, toHtml);
	}

	/**
	 * 聚合栏目新闻读取
	 * 
	 * @param map
	 * @param search
	 * @return
	 * @throws ParseException
	 */
	@RequiresPermissions("admin:post:group")
	@RequestMapping("group")
	public String group(Map<String, Object> map, PostSearch search) throws ParseException {

		ParamsToHtml toHtml = new ParamsToHtml();
		Map<String, String> keys = new HashMap<>();
		toHtml.setKeys(keys);
		// 读取聚合栏目
		List<SdPageIndex> indexs = pageIndexService.query("select", null);
		Map<String, String> indexMap = new HashMap<>();
		for (SdPageIndex sdPageIndex : indexs) {
			indexMap.put(sdPageIndex.getId().toString(), sdPageIndex.getName());
		}
		map.put("indexMap", indexMap);
		// 热点新闻条件
		Map<String, String> hotMap = new HashMap<>();
		hotMap.put("0", "全部");
		hotMap.put("1", "首页要闻");
		map.put("hotMap", hotMap);
		// 设置新闻读取状态
		search.getLookthroughed().add(2);
		return post(map, search, toHtml);
	}

	/**
	 * 指定新闻
	 * 
	 * @param map
	 * @param search
	 * @return
	 * @throws ParseException
	 */
	@RequiresPermissions("admin:post:top")
	@RequestMapping("top")
	public String top(Map<String, Object> map, PostSearch search) throws ParseException {

		ParamsToHtml toHtml = new ParamsToHtml();
		Map<String, String> keys = new HashMap<>();
		search.setPreid(4);
		// 拥有审核权限
		toHtml.setKeys(keys);
		// 设置新闻读取状态
		search.getLookthroughed().add(2);
		return post(map, search, toHtml);
	}

	/**
	 * 要闻管理
	 * 
	 * @param map
	 * @param search
	 * @return
	 * @throws ParseException
	 */
	@RequiresPermissions("admin:post:hot")
	@RequestMapping("hot")
	public String hot(Map<String, Object> map, PostSearch search) throws ParseException {

		ParamsToHtml toHtml = new ParamsToHtml();
		Map<String, String> keys = new HashMap<>();

		toHtml.setKeys(keys);
		search.setBhot(1);

		// 设置新闻读取状态
		search.getLookthroughed().add(2);
		return post(map, search, toHtml);
	}

	@RequiresPermissions("admin:post:suggested")
	@RequestMapping("suggested")
	public String suggested(Map<String, Object> map, PostSearch search) throws ParseException {

		ParamsToHtml toHtml = new ParamsToHtml();
		Map<String, String> keys = new HashMap<>();
		toHtml.setKeys(keys);
		search.setSuggested(1);
		// 设置新闻读取状态
		search.getLookthroughed().add(2);
		return post(map, search, toHtml);
	}

	/**
	 * 推荐到商务部
	 * 
	 * @param map
	 * @param search
	 * @return
	 * @throws ParseException
	 */
	@RequiresPermissions("admin:post:moftec")
	@RequestMapping(value = "moftec")
	public String moftec(Map<String, Object> map, PostSearch search) throws ParseException {

		ParamsToHtml toHtml = new ParamsToHtml();
		Map<String, String> keys = new HashMap<>();
		search.setMofcom(1);
		toHtml.setKeys(keys);
		search.getLookthroughed().add(2);
		return post(map, search, toHtml);
	}

	/**
	 * 图片新闻
	 * 
	 * @param map
	 * @param search
	 * @return
	 * @throws ParseException
	 */
	@RequiresPermissions("admin:post:images")
	@RequestMapping("images")
	public String images(Map<String, Object> map, PostSearch search) throws ParseException {

		ParamsToHtml toHtml = new ParamsToHtml();
		Map<String, String> keys = new HashMap<>();

		toHtml.setKeys(keys);
		// 设置值读取you图片的新闻
		search.setIsprimeimage(1);
		// 设置新闻读取状态
		search.getLookthroughed().add(2);

		Map<String, Object> params = new HashMap<>();
		// 图片必须
		params.put("isimage", 1);
		if (ValidateUtil.isValid(search.getMainsite())) {
			params.put("mainsite", 1);
		}
		// 文章不得为空
		params.put("sidYes", "1");

		int count = attachmentsService.pageCount(params);
		PageUtil page = new PageUtil(count, search.getLimit());
		page.setRequest(request);
		String pageHtml = page.show();

		params.put("order", "g.aid desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdAttachments> lists = attachmentsService.pageList(params);
		// 组装新闻
		for (SdAttachments sdAttachments : lists) {
			if (ValidateUtil.isValid(sdAttachments.getSid())) {
				SdSubjects subjects = subjectsService.get("getSubject", sdAttachments.getSid() + "");
				sdAttachments.setSubjects(subjects);
			}
		}

		// 获取用户可以看到的处室
		Map<String, String> departments = new LinkedHashMap<>();
		departments.putAll(getDep());
		map.put("departments", departments);
		map.put("postitems", getItemsByRedis(getDep()));
		map.put("lookthrouth", SdSubjectsServiceImpl.LOOK_THROUTH);
		// 栏目
		map.put("pageHtml", pageHtml);
		map.put("lists", lists);
		map.put("sniperMenu", toHtml);
		map.put("search", search);
		map.put("imageType", DataValues.IMAGES_MIN);
		map.put("adminUser", getAdminUser());
		return forward("/admin/admin-post/image.jsp");
	}

	@RequiresPermissions("admin:post:imageshow")
	@RequestMapping("imageshow")
	public String imageshow(Map<String, Object> map, PostSearch search) throws ParseException {

		ParamsToHtml toHtml = new ParamsToHtml();
		Map<String, String> keys = new HashMap<>();

		toHtml.setKeys(keys);
		// 设置值读取you图片的新闻
		search.setIsprimeimage(1);
		// 设置新闻读取状态
		search.getLookthroughed().add(2);

		Map<String, Object> params = new HashMap<>();
		// 图片必须
		params.put("isimage", 1);
		params.put("mainsite", 1);
		// 文章不得为空
		params.put("sidYes", "1");

		int count = attachmentsService.pageCount(params);
		PageUtil page = new PageUtil(count, search.getLimit());
		page.setRequest(request);
		String pageHtml = page.show();

		params.put("order", "g.aid desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SdAttachments> lists = attachmentsService.pageList(params);
		// 组装新闻
		for (SdAttachments sdAttachments : lists) {
			if (ValidateUtil.isValid(sdAttachments.getSid())) {
				SdSubjects subjects = subjectsService.get("getSubject", String.valueOf(sdAttachments.getSid()));
				sdAttachments.setSubjects(subjects);
			}
		}

		// 获取用户可以看到的处室
		Map<String, String> departments = new LinkedHashMap<>();
		departments.putAll(getDep());
		map.put("departments", departments);
		map.put("postitems", getItemsByRedis(getDep()));
		map.put("lookthrouth", SdSubjectsServiceImpl.LOOK_THROUTH);
		// 栏目
		map.put("pageHtml", pageHtml);
		map.put("lists", lists);
		map.put("sniperMenu", toHtml);
		map.put("search", search);
		map.put("imageType", DataValues.IMAGE_TYPE);
		map.put("adminUser", getAdminUser());
		return forward("/admin/admin-post/image.jsp");
	}

	/**
	 * 普通新闻读取列表 为设置新闻搜索条件
	 * 
	 * @param map
	 * @param search
	 * @param toHtml
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 */
	private String post(Map<String, Object> map, PostSearch search, ParamsToHtml toHtml) throws ParseException {

		runTimeData.addModelNode("Post read start");
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		// 下面是导航数据处理
		byte[] menuKeyName = REDIS.getKeyName(detailsUtils.getPrincipal() + BaseData.REDIS_KEY_MENU).getBytes();
		MessagePack messagePack = new MessagePack();
		if (REDIS.exists(menuKeyName)) {
			runTimeData.addModelNode("Redis MenuBar before");
			byte[] menuObject = REDIS.get(menuKeyName);
			try {
				toHtml = messagePack.read(menuObject, ParamsToHtml.class);
			} catch (IOException e) {
				e.printStackTrace();
			}
			runTimeData.addModelNode("Redis MenuBar after");
		} else {
			// 关闭删除按钮
			toHtml.setDelBotton(false);
			// 储存
			// 审核，移动首栏目，交换排序，置顶操作，首页新闻，提交商务部，移动到回收站
			// 拥有审核权限
			if (detailsUtils.validRole(DataValues.ROLE_ADMIN)) {
				toHtml.addMapValue("top", DataValues.POST_BASE);
				toHtml.getKeys().put("top", "置顶");
				toHtml.addMapValue("hot", DataValues.POST_BASE);
				toHtml.getKeys().put("hot", "要闻");
				// 显示删除按钮
				toHtml.setDelBotton(true);
			}

			// 任何人都有移动的权利
			if (getUserPostInfo(UserPostValue.MOVE_POST) > 0) {
				// 添加一个按钮，并在html添加相关代码
				ParamsToHtmlButton moveItem = new ParamsToHtmlButton();
				moveItem.setColor("primary");
				moveItem.setName("移动首栏目");
				moveItem.setType("move");
				moveItem.setValue("moveItem");
				moveItem.setTarget("#moveItem");
				toHtml.getButtons().add(moveItem);
			}

			if (detailsUtils.validRole(DataValues.ROLE_ADMIN)) {
				ParamsToHtmlButton moftecItem = new ParamsToHtmlButton();
				moftecItem.setColor(ParamsToHtmlButton.COLOE_INFO);
				moftecItem.setName("推荐到商务部");
				moftecItem.setType("mofcom");
				moftecItem.setValue("mofcom");
				moftecItem.setTarget("#mofcom");
				toHtml.getButtons().add(moftecItem);

				Map<String, String> mofcoms = mofcomItemService.getMofcom();
				map.put("mofcoms", mofcoms);

				ParamsToHtmlButton moftecNoItem = new ParamsToHtmlButton();
				moftecNoItem.setColor(ParamsToHtmlButton.COLOE_INFO);
				moftecNoItem.setName("取消商务部");
				moftecNoItem.setType("nomofcom");
				moftecNoItem.setValue("nomofcom");
				toHtml.getButtons().add(moftecNoItem);
			}

			// 排序按钮
			ParamsToHtmlButton sortButton = new ParamsToHtmlButton();
			sortButton.setColor(ParamsToHtmlButton.COLOE_INFO);
			sortButton.setName("交换排序");
			sortButton.setType("sort");
			sortButton.setValue("sort");
			toHtml.getButtons().add(sortButton);

			if (getUserPostInfo(UserPostValue.AUDIT) > 0) {
				ParamsToHtmlButton auditItem = new ParamsToHtmlButton();
				auditItem.setColor(ParamsToHtmlButton.COLOE_SUCCESS);
				auditItem.setName("审核");
				auditItem.setType("audit");
				auditItem.setValue("audit");
				toHtml.getButtons().add(auditItem);
			}
			// 导航数据
			try {
				REDIS.set(menuKeyName, messagePack.write(toHtml), detailsUtils.getSubject().getSession().getTimeout());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		map.put("sniperMenu", toHtml);
		// 传输数据
		// 处室

		Map<String, String> deps = getDep();
		map.put("departments", deps);
		// 栏目
		map.put("postitems", getItemsByRedis(deps));
		// 新闻状态
		map.put("lookthrouth", SdSubjectsServiceImpl.LOOK_THROUTH);
		// 导航数据处理结束
		map.put("search", search);
		map.put("sniperUrl", "/admin-post/delete");
		String pageHtml = "";
		// 同处室新闻限制
		List<Integer> siteids = new ArrayList<>();
		if (ValidateUtil.isValid(search.getSiteid())) {
			// 搜索功能
			siteids.add(search.getSiteid());
			// 管理员
		} else if (!detailsUtils.hasRole(DataValues.ROLE_ADMIN)) {
			for (Entry<String, String> entry : deps.entrySet()) {
				siteids.add(Integer.valueOf(entry.getKey()));
			}
			// 添加一个默认值，因为默认值是0，所以看不到任何新闻
			if (siteids.size() == 0) {
				siteids.add(0);
			}
		}

		// 栏目处理
		List<String> itemids = new ArrayList<>();
		// 聚合栏目处理
		if (ValidateUtil.isValid(search.getGroup())) {
			SdPageIndex pageIndex = pageIndexService.get(search.getGroup());
			if (ValidateUtil.isValid(pageIndex.getItemid())) {
				String[] a = pageIndex.getItemid().split(",");
				itemids.addAll(Arrays.asList(a));
			}
		}
		// 普通栏目处理
		if (ValidateUtil.isValid(search.getItemid()) && search.getItemid() > 0) {
			itemids.add(String.valueOf(search.getItemid()));
		}
		// 通过url配置是否使用Solr
		if (search.getSolr()) {
			AdminPostSolrData.SOLR = search.getSolr();
		}

		// 开启Solr读取
		if (AdminPostSolrData.SOLR) {
			runTimeData.addModelNode("Solr before");
			HttpSolrClient solrClient = SolrViewUtil.getInstance(SubjectViewModel.CORE_NAME);
			// 每页数量
			int pageSize = search.getLimit();
			// 每页偏移
			int pageOffset = 0;
			String pageNoStr = WebUtils.getCleanParam(request, "pageNo");
			if (!ValidateUtil.isValid(pageNoStr)) {
				pageNoStr = "1";
			}
			int pageNo = Integer.valueOf(pageNoStr).intValue();
			if (pageNo == 1) {
				pageNo = 1;
				pageOffset = 0;
			} else {
				pageOffset = (pageNo - 1) * pageSize;
			}

			ModifiableSolrParams params = new ModifiableSolrParams();
			SolrQuery filterQuery = new SolrQuery();
			if (ValidateUtil.isValid(search.getName())) {
				filterQuery.addFilterQuery("subject:" + search.getName());
				filterQuery.addFilterQuery("subjectContent:" + search.getName());
			}
			if (ValidateUtil.isValid(search.getLookthroughed())) {
				List<String> lkts = new ArrayList<>();
				for (Integer lkt : search.getLookthroughed()) {
					lkts.add("lookthroughed_i:" + lkt);
				}
				filterQuery.addFilterQuery(StringUtils.join(lkts, " or "));
			}

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			// 默认结束日日期
			Date endDate = new Date();
			Date startDate = new Date(0);
			if (ValidateUtil.isValid(search.getEndDate())) {
				endDate = dateFormat.parse(search.getEndDate());
			}
			if (ValidateUtil.isValid(search.getStartDate())) {
				startDate = dateFormat.parse(search.getStartDate());
			}
			if (ValidateUtil.isValid(search.getEndDate()) || ValidateUtil.isValid(search.getStartDate())) {
				filterQuery.addDateRangeFacet("date_dt", startDate, endDate, "+1DAY");
			}

			// 处室限制
			if (siteids.size() > 0) {
				List<String> siteidList = new ArrayList<>();
				for (Integer s : siteids) {
					siteidList.add("siteid_i:" + s);
				}
				filterQuery.addFilterQuery(StringUtils.join(siteidList, " or "));
			}
			// 栏目处理
			if (itemids.size() > 0) {
				List<String> siteidList = new ArrayList<>();
				for (String s : itemids) {
					siteidList.add("itemid_i:" + s);
				}
				filterQuery.addFilterQuery(StringUtils.join(siteidList, " or "));
			}

			// 推荐到商务部
			if (ValidateUtil.isValid(search.getMofcom())) {
				filterQuery.addFilterQuery("moftec_i:" + search.getMofcom());
			}
			// 辅助栏目
			if (ValidateUtil.isValid(search.getIcid())) {
				filterQuery.addFilterQuery("icId_i:" + search.getIcid());
			}
			// 重要程度
			if (ValidateUtil.isValid(search.getPreid())) {
				filterQuery.addFilterQuery("preid_i:" + search.getPreid());
			}
			// 推荐
			if (ValidateUtil.isValid(search.getSuggested())) {
				filterQuery.addFilterQuery("suggested_i:" + search.getSuggested());
			}
			// 热点
			if (ValidateUtil.isValid(search.getBhot()) && search.getBhot() == 1) {
				filterQuery.addFilterQuery("bhot_i:" + search.getBhot());
			}

			filterQuery.setSort("sid", ORDER.desc);
			params.set("q", "*:*");
			params.add(filterQuery);
			params.set("fl", "*");
			try {
				// 设置导出操作
				if (ValidateUtil.isValid(search.getSubmit()) && search.getSubmit().equals("export")) {
					map.remove("sniperUrl");
					String exportResult = exportPost(search, solrClient, params, dateFormat);
					if (ValidateUtil.isValid(exportResult)) {
						return exportResult;
					}
				}
				// 上面用于熟读导出
				params.set("start", pageOffset);
				params.set("rows", pageSize);
				QueryResponse response = solrClient.query(params);
				SolrDocumentList list = response.getResults();
				List<SubjectViewModel> lists = response.getBeans(SubjectViewModel.class);

				PageUtil page = new PageUtil(Long.valueOf(list.getNumFound()).intValue(), pageSize);
				page.setRequest(request);
				pageHtml = page.show();
				map.put("lists", lists);
				map.put("pageHtml", pageHtml);
				runTimeData.addModelNode("Solr after");
				map.put("runTimeData", runTimeData);
				return forward("/admin/admin-post/solr.jsp");
				// 直接返回模板
			} catch (SolrServerException | IOException e) {
				e.printStackTrace();
			}
		}
		// 数据库模式
		Map<String, Object> params = new HashMap<>();

		if (ValidateUtil.isValid(search.getName())) {
			params.put("name", search.getName());
		}
		// 状态读取时核心,这个属于制定属性，页面不提供参数
		if (ValidateUtil.isValid(search.getLookthroughed())) {
			params.put("lookthroughed", search.getLookthroughed());
		}

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if (ValidateUtil.isValid(search.getStartDate())) {
			dateFormat.parse(search.getStartDate());
			params.put("stime", dateFormat.getCalendar().getTimeInMillis() / 1000);
		}

		if (ValidateUtil.isValid(search.getEndDate())) {
			dateFormat.parse(search.getEndDate());
			params.put("etime", dateFormat.getCalendar().getTimeInMillis() / 1000);
		}

		// 处室限制
		params.put("siteid", siteids);
		// 栏目处理
		params.put("itemid", itemids);

		// 推荐到商务部
		if (ValidateUtil.isValid(search.getMofcom())) {
			params.put("mofcom", search.getMofcom());
		}
		// 辅助栏目
		if (ValidateUtil.isValid(search.getIcid())) {
			params.put("icId", search.getIcid());
		}
		// 重要程度
		if (ValidateUtil.isValid(search.getPreid())) {
			params.put("preid", search.getPreid());
		}
		// 推荐
		if (ValidateUtil.isValid(search.getSuggested())) {
			params.put("suggested", search.getSuggested());
		}
		// 热点
		if (ValidateUtil.isValid(search.getBhot()) && search.getBhot() == 1) {
			params.put("bhot", search.getBhot());
		}

		// 去除唯一值
		params.put("group", "s.sid");
		// 读取处室
		// 设置导出操作
		if (ValidateUtil.isValid(search.getSubmit()) && search.getSubmit().equals("export")) {
			map.remove("sniperUrl");
			String exportResult = exportPost(search, deps, params, dateFormat);
			if (ValidateUtil.isValid(exportResult)) {
				return exportResult;
			}
		}

		int count = viewSubjectService.pageCount(params);

		PageUtil page = new PageUtil(count, search.getLimit());
		page.setRequest(request);
		pageHtml = page.show();

		params.put("order", "s.sid desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());

		List<SdViewSubject> lists = viewSubjectService.pageList(params);

		for (SdViewSubject sdViewSubject : lists) {
			// 发布人
			AdminUser adminUser = adminUserService.getUser(sdViewSubject.getAuthorid());
			if (ValidateUtil.isValid(adminUser)) {
				sdViewSubject.setAuthorid(adminUser.getName());
			}
			// 审核人
			// auditUid
			if (ValidateUtil.isValid(sdViewSubject.getAuditUid())) {
				AdminUser adminUser1 = adminUserService.getUser(sdViewSubject.getAuditUid());
				if (adminUser1 != null) {
					sdViewSubject.setAuditUid(adminUser1.getName());
				}
			}
		}
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		runTimeData.addModelNode("Post after");
		map.put("runTimeData", runTimeData);
		return forward("/admin/admin-post/index.jsp");
	}

	/**
	 * 新闻导出
	 * 
	 * @param search
	 * @param deps
	 * @param params
	 * @param dateFormat
	 * @return
	 */
	private String exportPost(PostSearch search, Map<String, String> deps, Map<String, Object> params,
			SimpleDateFormat dateFormat) {
		ExeclStaticExportUtils exportUtils = new ExeclStaticExportUtils();
		exportUtils.setRootDir(FilesUtil.getRootDir());
		// 主要的数据读取
		Map<String, String> headerFields = new LinkedHashMap<>();
		headerFields.put("id", "序号");
		headerFields.put("subject", "标题");
		headerFields.put("fromsite", "处室");
		// noField 开头的表示，值会返回空
		// headerFields.put("noField1", "处室负责人");
		// headerFields.put("noField2", "办公室负责人");

		String sd = "";
		if (ValidateUtil.isValid(search.getStartDate())) {
			sd = search.getStartDate();
		} else {
			sd = dateFormat.format(new Date());
		}
		Map<String, String> HEAD_FIELDS_TWO = new LinkedHashMap<>();
		HEAD_FIELDS_TWO.put(":0-4", "山东省商务厅信息公开保密审查表(" + sd + ")");

		List<Map<String, String>> HEAD_FIELDS_ALL = new ArrayList<>();
		HEAD_FIELDS_ALL.add(HEAD_FIELDS_TWO);
		HEAD_FIELDS_ALL.add(headerFields);

		exportUtils.setHeaderFields(headerFields);
		exportUtils.setHeadFieldsAll(HEAD_FIELDS_ALL);

		params.put("order", "s.sid desc");
		params.put("pageOffset", 0);
		// 设置最大读取值
		params.put("pageSize", 20000);
		List<SdViewSubject> sdViewSubjects = viewSubjectService.pageList(params);
		// 处室处室
		for (SdViewSubject sdViewSubject : sdViewSubjects) {
			sdViewSubject.setFromsite(deps.get(String.valueOf(sdViewSubject.getSiteid())));
		}
		try {

			String exportPath = FilesUtil.getSaveDir("") + "export.xlsx";
			exportUtils.exportGroupSubject(exportPath, sdViewSubjects);
			// 非浏览器下载
			// 直接文件下载
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX + FilesUtil.getWebUrl() + exportPath;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * solr数据导出
	 * 
	 * @param search
	 * @param solrClient
	 * @param params
	 * @param dateFormat
	 * @return
	 * @throws SolrServerException
	 * @throws IOException
	 */
	private String exportPost(PostSearch search, HttpSolrClient solrClient, ModifiableSolrParams params,
			SimpleDateFormat dateFormat) throws SolrServerException, IOException {
		ExeclStaticExportUtils exportUtils = new ExeclStaticExportUtils();
		exportUtils.setRootDir(FilesUtil.getRootDir());
		// 主要的数据读取
		Map<String, String> headerFields = new LinkedHashMap<>();
		headerFields.put("id", "序号");
		headerFields.put("subject", "标题");
		headerFields.put("siteidName", "处室");
		// noField 开头的表示，值会返回空
		// headerFields.put("noField1", "处室负责人");

		String sd = "";
		if (ValidateUtil.isValid(search.getStartDate())) {
			sd = search.getStartDate();
		} else {
			sd = dateFormat.format(new Date());
		}
		Map<String, String> HEAD_FIELDS_TWO = new LinkedHashMap<>();
		HEAD_FIELDS_TWO.put(":0-4", "山东省商务厅信息公开保密审查表(" + sd + ")");

		List<Map<String, String>> HEAD_FIELDS_ALL = new ArrayList<>();
		HEAD_FIELDS_ALL.add(HEAD_FIELDS_TWO);
		HEAD_FIELDS_ALL.add(headerFields);

		exportUtils.setHeaderFields(headerFields);
		exportUtils.setHeadFieldsAll(HEAD_FIELDS_ALL);

		params.set("start", 0);
		// 设置最大读取值
		params.set("rows", 20000);

		QueryResponse response = solrClient.query(params);
		List<SubjectViewModel> lists = response.getBeans(SubjectViewModel.class);
		try {
			String exportPath = FilesUtil.getSaveDir("") + "export.xlsx";
			exportUtils.exportSolrSubject(exportPath, lists);
			// 非浏览器下载
			// 直接文件下载
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX + FilesUtil.getWebUrl() + exportPath;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取一行新闻数据
	 * 
	 * @param subjects
	 * @return
	 * @throws ParseException
	 */
	private String getSingleSubject(SdSubjects subjects) throws ParseException {

		// 发布人
		AdminUser adminUser = new AdminUser();
		if (StringUtils.isNumeric(subjects.getAuthorid())) {
			adminUser = adminUserService.getUser(subjects.getAuthorid());
		} else {
			adminUser = adminUserService.get(subjects.getAuthorid());
		}
		subjects.setAuthorid(adminUser.getName());
		// 审核人
		// auditUid
		if (ValidateUtil.isValid(subjects.getAuditUid())) {
			AdminUser adminUser1 = new AdminUser();
			if (StringUtils.isNumeric(subjects.getAuditUid())) {
				adminUser1 = adminUserService.getUser(subjects.getAuditUid());
			} else {
				adminUser1 = adminUserService.get(subjects.getAuditUid());
			}
			if (adminUser1 != null) {
				subjects.setAuditUid(adminUser1.getName());
			}
		}
		StringBuffer buffer = new StringBuffer("<tr id=\"sl_" + subjects.getSid() + "\">");
		buffer.append("<td>");
		buffer.append("<input type=\"checkbox\" name=\"list.id\" value=" + subjects.getSid() + " />");
		buffer.append("<a href=\"." + adminPath + "/admin-post/update?sid=" + subjects.getSid()
				+ "\" target=\"_blank\">" + subjects.getSid() + "</a>");
		buffer.append("</td>");
		buffer.append("<td>");
		buffer.append("<a href=\"news/" + subjects.getSid() + "\" target=\"_blank\" title=\"预览\">"
				+ subjects.getSubject() + "</a>");
		if (subjects.getBhot() == 1) {
			buffer.append("<i class=\"fa fa-home ml2\" title=\"首页要闻\">");
		}
		if (subjects.getPreid() == 4) {
			buffer.append("<i class=\"fa fa-star ml2\" title=\"首置顶\">");
		}
		if (subjects.getMoftec() == 1) {
			buffer.append("<i class=\"fa fa-share ml2\" title=\"推送到商务部\">");
		}
		List<SdAttachments> attachments = attachmentsService.getFiles(subjects.getSid() + "", 1, null);
		if (attachments != null && attachments.size() > 0) {
			buffer.append("<i class=\"fa fa-picture-o ml2\" title=\"拥有图片(" + attachments.size() + ")\">");
		}
		// 获取处室
		Map<String, String> deps = getDep();
		// 获取栏目
		Map<String, Map<String, String>> itemsMap = getItemsByRedis(getDep());
		buffer.append("</td>");
		buffer.append("<td data-type=\"siteid\">" + deps.get(String.valueOf(subjects.getSiteid())) + "</td>");
		buffer.append("<td data-type=\"itemid\">"
				+ itemsMap.get(String.valueOf(subjects.getSiteid())).get(String.valueOf(subjects.getItemid()))
				+ "</td>");
		buffer.append("<td>");
		buffer.append(subjects.getAuthorid());
		buffer.append("/");
		buffer.append(ViewHomeUtils.intToDateString(subjects.getDate()));
		buffer.append("</td>");
		buffer.append("<td>");
		buffer.append(subjects.getTodayView());
		buffer.append("/");
		buffer.append(subjects.getView());
		buffer.append("</td>");
		buffer.append("<td>");
		buffer.append(subjects.getAuditUid());
		buffer.append("(");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		buffer.append(dateFormat.format(subjects.getAuditDatetime()));
		buffer.append("</td>");
		buffer.append("<td>");
		buffer.append(SdSubjectsServiceImpl.LOOK_THROUTH.get(String.valueOf(subjects.getLookthroughed())));
		buffer.append("</td>");

		return buffer.toString();
	}

	/**
	 * 文章批量移动
	 * 
	 * @param map
	 * @param search
	 * @return
	 */
	@RequiresPermissions("admin:post:move")
	@RequestMapping(value = "move", method = RequestMethod.GET)
	public String move(Map<String, Object> map) {

		// 读取处室
		Map<String, String> deps = getDep();
		map.put("deps", deps);
		PostSearch search = new PostSearch();
		map.put("search", search);
		// 移动受栏目
		return forward("/admin/admin-post/move.ftl");
	}

	/**
	 * 文章批量移动
	 * 
	 * @param map
	 * @param search
	 * @return
	 */
	@RequiresPermissions("admin:post:move")
	@RequestMapping(value = "move", method = RequestMethod.POST)
	public String move(Map<String, Object> map, PostSearch search) {

		// 读取处室
		Map<String, String> deps = getDep();
		map.put("deps", deps);
		int itemidCount = 0;
		int siteidCount = 0;
		if (ValidateUtil.isValid(search.getSiteid()) && ValidateUtil.isValid(search.getItemid())
				&& ValidateUtil.isValid(search.getSiteidMove()) && ValidateUtil.isValid(search.getItemidMove())) {

			// 栏目修改是和处室分开的
			if (search.getItemid() != search.getItemidMove()) {
				itemidCount = itemsSubjectsSrevice.changeItem(search.getItemid(), search.getItemidMove());
				// 修改处室
				siteidCount = subjectsService.changeSiteid(search.getSiteid(), search.getSiteidMove(),
						search.getItemid(), search.getItemidMove());
			}

		}
		map.put("itemidCount", itemidCount);
		map.put("siteidCount", siteidCount);
		map.put("search", search);
		// 移动受栏目
		return forward("/admin/admin-post/move.ftl");
	}

	/**
	 * 移动首栏目处理
	 * 
	 * @param search
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequiresPermissions("admin:post:itemid")
	@RequestMapping(value = "itemid", method = RequestMethod.POST)
	public Map<String, Object> itemid(PostSearch search) throws ParseException {

		Map<String, String> htmls = new HashMap<>();
		Map<String, Object> ajaxResult = new HashMap<>();
		if (ValidateUtil.isValid(search.getDelid()) && ValidateUtil.isValid(search.getSiteid())
				&& ValidateUtil.isValid(search.getItemid())) {
			String[] delid = search.getDelid().split(",");
			for (String id : delid) {
				// 获取新闻
				SdSubjects sdSubjects = subjectsService.get(id);
				// 更改受栏目
				sdSubjects.setSiteid(search.getSiteid());
				sdSubjects.setItemid(search.getItemid());
				subjectsService.update(sdSubjects);
				// 更新栏目
				itemsSubjectsSrevice.changeFirstItem(Integer.valueOf(id), sdSubjects.getItemid(), search.getItemid());

				// 处理日志
				subjectLogsService.log(sdSubjects.getSid(), sdSubjects.getAuthorid() + "", "4", "");
				htmls.put(id, getSingleSubject(sdSubjects));
			}
		}
		ajaxResult.put("html", htmls);
		ajaxResult.put("code", 200);
		ajaxResult.put("msg", "success");
		return ajaxResult;
	}

	/**
	 * 提交到商务部
	 * 
	 * @param search
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequiresPermissions("admin:post:mofcom")
	@RequestMapping(value = "mofcom", method = RequestMethod.POST)
	public Map<String, Object> mofcom(PostSearch search) throws ParseException {
		Map<String, String> htmls = new HashMap<>();
		Map<String, Object> ajaxResult = new HashMap<>();
		if (ValidateUtil.isValid(search.getDelid()) && ValidateUtil.isValid(search.getMofcom())) {
			String[] delid = search.getDelid().split(",");
			for (String id : delid) {
				// 获取新闻
				SdMofcomItem mofcomItem = new SdMofcomItem();
				mofcomItem.setMid(search.getMofcom());
				mofcomInfoService.updateMofcom(id, mofcomItem);
				SdSubjects sdSubjects = subjectsService.get(id);
				sdSubjects.setMoftec(1);
				subjectsService.update(sdSubjects);
				// 处理日志
				subjectLogsService.log(sdSubjects.getSid(), sdSubjects.getAuthorid() + "", "10", "");
				htmls.put(id, getSingleSubject(sdSubjects));
			}
		}
		ajaxResult.put("html", htmls);
		ajaxResult.put("code", 200);
		ajaxResult.put("msg", "success");
		return ajaxResult;
	}

	/**
	 * 添加新闻
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:post:insert")
	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map, @RequestParam("id") String id) {

		SdSubjects post = new SdSubjects();
		post.setFromsite(SystemConfigUtil.get("webName"));
		map.put("post", post);
		map.put("preid", DataValues.PREIDS);
		Map<String, String> deps = getDep();
		map.put("departments", deps);
		map.put("treeData", getItemsByRedis(deps));
		map.put("suggesteds", DataValues.YES_NO);

		List<SdTabInfoClass> infoClasses = infoClassService.getTabInfo(Integer.valueOf(id), 1);
		map.put("infoClasses", infoClassService.getMapInfoClass(infoClasses));

		// 设置初始栏目和初始
		SdItems items = itemsService.get(id);
		SdDepartments departments = departmentsService.get(String.valueOf(items.getDeprtid()));
		post.setItemid(items.getItemid());
		post.setSiteid(items.getDeprtid());
		map.put("stime", new Date());
		map.put("itemsHtml", items);
		map.put("depHtml", departments);
		map.put("tempSort", DataValues.TempSort);

		return forward("/admin/admin-post/save-input.jsp");
	}

	@ResponseBody
	@RequiresPermissions("admin:post:insert")
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public List<String> insert(SdSubjects post, BindingResult result, @RequestParam("postFiles") String filesPost,
			@RequestParam(value = "itemid", required = false) String[] itemids, @RequestParam("id") String id,
			@RequestParam("stime") String stime, Map<String, Object> map) {

		List<String> errors = new ArrayList<>();
		try {
			if (result.getErrorCount() > 0) {
				// 设置选中的栏目
				List<FieldError> fieldErrors = result.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					errors.add(fieldError.getDefaultMessage());
				}
				errors.add("添加失败");
			} else {

				if (ValidateUtil.isValid(post.getSubject())) {
					post.setSjIp(HttpRequestUtils.getRealIP(request));
					post.setAuthorid(getAdminUser().getId());
					post.setAuthorname(getAdminUser().getName());
					post.setDate(ViewHomeUtils.getTimeMillis(stime));
					// 处理新闻处室状态
					post.setLookthroughed(getUserPostInfo(UserPostValue.START_POST));
					subjectsService.insert(post);
					// 处理栏目
					itemsSubjectsSrevice.itemsSubject(post, itemids);
					// 处理图片
					setSubjectFile(post, filesPost);
					// if (post.getTemp() != 0) {
					// post.setDisplayorder(post.getDisplayorder()
					// + post.getTemp() * 86400);
					// }
					post.setTemp2(post.getTemp());
					// 处理日志
					subjectLogsService.log(post.getSid(), post.getAuthorid() + "", "13", "");
					errors.add("添加成功，继续添加请刷新页面，或者点击左侧导航做其他事。");
					errors.add(post.getSid().toString());
				}
			}
		} catch (Exception e) {
			errors.add("操作失败：" + e.getMessage());
			e.printStackTrace();
		}
		return errors;
	}

	/**
	 * 处理新闻和图片之间的关系
	 * 
	 * @param subjects
	 * @param filesPost
	 */
	public void setSubjectFile(SdSubjects subjects, String filesPost) {
		if (ValidateUtil.isValid(filesPost)) {
			String[] ids = filesPost.split(",");
			if (ids.length > 0) {
				for (int i = 0; i < ids.length; i++) {
					if (ValidateUtil.isValid(ids[i])) {
						SdAttachments attachments = attachmentsService.get(ids[i]);
						attachments.setSid(subjects.getSid());
						attachmentsService.update(attachments);
						subjects.getAttachments().add(attachments);
					}
				}
			}
		}
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:post:update")
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(@RequestParam(value = "sid") String id, Map<String, Object> map, SdSubjects post) {
		post = subjectsService.get(id);
		if (!ValidateUtil.isValid(post)) {
			return redirect("/admin-post/insert");
		}
		Date stime = ViewHomeUtils.intToDate(post.getDate());
		map.put("stime", stime);
		map.put("preid", DataValues.PREIDS);
		map.put("post", post);
		Map<String, String> deps = getDep();
		map.put("departments", deps);
		map.put("treeData", getItemsByRedis(deps));
		map.put("suggesteds", DataValues.YES_NO);
		// 获取多栏目列表
		Integer[] its = itemsSubjectsSrevice.getItems(id);
		Map<String, Integer> itmidsMap = new HashMap<>();
		for (int i = 0; i < its.length; i++) {
			itmidsMap.put(String.valueOf(its[i]), its[i]);
		}
		map.put("itmidsMap", itmidsMap);
		// 获取栏目信息
		SdItems items = itemsService.get(String.valueOf(post.getItemid()));
		SdDepartments departments = departmentsService.get(String.valueOf(post.getSiteid()));
		map.put("itemsHtml", items);
		map.put("depHtml", departments);

		List<SdTabInfoClass> infoClasses = infoClassService.getTabInfo(Integer.valueOf(id), 1);
		map.put("infoClasses", infoClassService.getMapInfoClass(infoClasses));

		// 获取新闻的操作记录
		List<SdSubjectLogs> subjectLogs = subjectLogsService.getLogs(id);
		map.put("subjectLogs", subjectLogs);
		map.put("logcodes", SdSubjectLogsServiceImpl.LOG_CODE);
		// 读取所有附件
		List<SdAttachments> attachments = attachmentsService.getFiles(id, null, null);
		map.put("attachments", attachments);
		List<String> aids = new ArrayList<>();
		for (SdAttachments sdAttachments : attachments) {
			aids.add(String.valueOf(sdAttachments.getAid()));
		}
		map.put("postFilesNo", StringUtils.join(aids, ","));
		map.put("tempSort", DataValues.TempSort);
		return forward("/admin/admin-post/save-input.jsp");
	}

	@ResponseBody
	@RequiresPermissions("admin:post:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public List<String> update(SdSubjects post, BindingResult result, @RequestParam("postFiles") String filesPost,
			@RequestParam(value = "itemid", required = true) String[] itemids, @RequestParam("stime") String stime,
			Map<String, Object> map) {
		List<String> errors = new ArrayList<>();

		try {
			if (result.getFieldErrorCount() > 0) {
				// 设置选中的栏目
				List<FieldError> fieldErrors = result.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					errors.add(fieldError.getDefaultMessage());
				}
				errors.add("操作失败");
				map.put("errors", errors);
			} else {
				UserDetailsUtils detailsUtils = new UserDetailsUtils();
				boolean userEdit = true;
				if (!detailsUtils.hasRole(DataValues.ROLE_ADMIN)) {
					// 检查用户是否拥有全选
					List<String> deps = getUserDeps();
					// 如果处室被锁定可能无法编辑信息,监测文章吃屎是否和新闻处室一致
					if (deps != null) {
						if (!deps.contains(String.valueOf(post.getSiteid()))) {
							userEdit = false;
						}
					}
					// 如果非管理员,就判断用户是否一致
					if (!detailsUtils.hasRole(DataValues.ROLE_MANGER)) {
						if (!detailsUtils.getAminUser().getId().equals(post.getAuthorid())) {
							userEdit = false;
						}
					}
				}

				if (userEdit) {
					// 修改日期
					post.setDate(ViewHomeUtils.getTimeMillis(stime));
					post.setEditDate(DataUtil.getTime());
					// 修改人
					post.setEditUser(getAdminUser().getId());
					// 处理新闻处室状态
					post.setLookthroughed(getUserPostInfo(UserPostValue.START_POST));
					// 修改ip
					post.setSjLastIp(HttpRequestUtils.getRealIP(request));
					// 更新新闻
					subjectsService.update(post);
					// 处理栏目
					itemsSubjectsSrevice.itemsSubject(post, itemids);
					// 更新图片
					setSubjectFile(post, filesPost);
					// 检测temp是否和temp2相等
					// if (post.getTemp() != 0
					// && post.getTemp() != post.getTemp2()) {
					// post.setDisplayorder(post.getDisplayorder()
					// + post.getTemp() * 86400);
					// }
					post.setTemp2(post.getTemp());
					// 新闻日志
					subjectLogsService.log(post.getSid(), getAdminUser().getId(), "14",
							SdSubjectLogsServiceImpl.LOG_CODE.get("14"));
					errors.add("修改成功, 你可以选择关闭当前页面。");
				} else {
					errors.add("处室不对无法修改新闻，请确定是否拥有此处室，处室没有被禁用。");
				}
			}
		} catch (Exception e) {
			errors.add("操作失败：" + e.getMessage());
			LOGGER.error("新闻修改失败, ID:" + post.getSid(), e);
			e.printStackTrace();
		}
		return errors;
	}

	@RequiresPermissions("admin:post:delete")
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Map<String, Object> delete(@RequestParam("delid") String[] delid, @RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) throws Exception {
		// code 小于1表示有错误,大于0表示ok,==0表示未操作
		Map<String, Object> ajaxResult = new HashMap<>();
		Map<String, String> htmls = new HashMap<>();
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		switch (menuType) {
		case "delete":
			ajaxResult.put("code", 1);
			ajaxResult.put("msg", "failed");
			try {
				for (String id : delid) {
					String referer = request.getHeader("referer");
					// 是否是回收站
					if (referer.indexOf("recyle") > -1) {
						SdSubjects sdSubjects = subjectsService.get("getSubject", id);
						// 删除新闻设置飞管理员，和本人不得删除
						if (detailsUtils.hasRole("ROLE_ADMIN") || sdSubjects.getAuthorid() == getAdminUser().getId()) {
							subjectsService.delete(id);
						}
						ajaxResult.put("code", 1);
						ajaxResult.put("msg", "success");
						// 是否是图片
					} else if (referer.indexOf("image") > -1) {
						if (detailsUtils.hasRole("ROLE_ADMIN")) {
							attachmentsService.delete(id);
							ajaxResult.put("code", 1);
							ajaxResult.put("msg", "success");
						}
						// 默认新闻处理
					} else {
						SdSubjects sdSubjects = subjectsService.get("getSubject", id);
						sdSubjects.setLookthroughed(30);
						if (detailsUtils.hasRole("ROLE_ADMIN") || sdSubjects.getAuthorid() == getAdminUser().getId()) {
							subjectsService.update(sdSubjects);
							String code = "12";
							subjectLogsService.log(Integer.valueOf(id), getAdminUser().getId(), code,
									SdSubjectLogsServiceImpl.LOG_CODE.get(code));
							ajaxResult.put("code", 1);
							ajaxResult.put("msg", "success");
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}
			break;
		// 置顶
		case "top":
			for (String id : delid) {
				SdSubjects post = subjectsService.get("getSubject", id);
				String code = "6";
				switch (menuValue) {
				case "0":
					code = "6";
					post.setPreid(0);
					break;
				case "1":
					code = "7";
					post.setPreid(4);
					break;
				}
				subjectsService.update(post);
				// 添加日志
				subjectLogsService.log(Integer.valueOf(id), getAdminUser().getId(), code,
						SdSubjectLogsServiceImpl.LOG_CODE.get(code));

				htmls.put(id, getSingleSubject(post));
			}
			ajaxResult.put("code", 1);
			ajaxResult.put("msg", "success");
			ajaxResult.put("html", htmls);
			break;
		// 审核
		case "audit":
			for (String id : delid) {
				SdSubjects post = subjectsService.get("getSubject", id);
				String code = "1";
				// 获取用户审核的结果
				int audit = getUserPostInfo(UserPostValue.AUDIT_RESULT);
				post.setLookthroughed(audit);
				post.setAuditDatetime(new Date());
				post.setAuditIp(HttpRequestUtils.getRealIP(request));
				post.setAuditUid(getAdminUser().getId());
				subjectsService.update(post);
				// 添加日志
				subjectLogsService.log(Integer.valueOf(id), getAdminUser().getId(), code,
						SdSubjectLogsServiceImpl.LOG_CODE.get(code));
				htmls.put(id, getSingleSubject(post));
			}
			ajaxResult.put("code", 1);
			ajaxResult.put("msg", "success");
			ajaxResult.put("html", htmls);
			break;
		// 要闻
		case "hot":
			for (String id : delid) {
				SdSubjects post = subjectsService.get("getSubject", id);
				String code = "8";
				switch (menuValue) {
				case "0":
					code = "8";
					post.setBhot(0);
					break;
				case "1":
					code = "9";
					post.setBhot(1);
					break;
				}
				subjectsService.update(post);
				// 添加日志
				subjectLogsService.log(Integer.valueOf(id), getAdminUser().getId(), code,
						SdSubjectLogsServiceImpl.LOG_CODE.get(code));
				htmls.put(id, getSingleSubject(post));
			}
			ajaxResult.put("code", 1);
			ajaxResult.put("msg", "success");
			ajaxResult.put("html", htmls);
		case "nomofcom":
			for (String id : delid) {
				SdSubjects post = subjectsService.get("getSubject", id);

				SdMofcomInfo mofcomInfo = mofcomInfoService.get(id);
				mofcomInfo.setDel(1);
				mofcomInfo.setLastdate(DataUtil.getTime());
				String code = "11";
				// 删除栏目
				mofcomInfoService.update(mofcomInfo);

				post.setMoftec(0);
				subjectsService.update(post);
				// 添加日志
				subjectLogsService.log(Integer.valueOf(id), getAdminUser().getId(), code,
						SdSubjectLogsServiceImpl.LOG_CODE.get(code));
				htmls.put(id, getSingleSubject(post));
			}
			ajaxResult.put("code", 1);
			ajaxResult.put("msg", "success");
			ajaxResult.put("html", htmls);
			break;
		case "image":
			for (String id : delid) {
				SdAttachments attachments = attachmentsService.get(id);
				attachments.setIsprimeimage(DataUtil.stringToInteger(menuValue));
				attachmentsService.update(attachments);
			}
			ajaxResult.put("code", 1);
			ajaxResult.put("msg", "success");
			break;
		case "mainsite":
			for (String id : delid) {
				SdAttachments attachments = attachmentsService.get(id);
				attachments.setMainsite(DataUtil.stringToInteger(menuValue));
				attachmentsService.update(attachments);
			}
			ajaxResult.put("code", 1);
			ajaxResult.put("msg", "success");
			break;
		case "sort":
			if (delid.length == 2) {
				SdSubjects post = subjectsService.get("getSubject", delid[0]);
				SdSubjects post2 = subjectsService.get("getSubject", delid[1]);

				int sort = post.getDisplayorder();
				int sort2 = post2.getDisplayorder();

				post.setDisplayorder(sort2);
				post2.setDisplayorder(sort);

				try {
					subjectsService.update(post);
					subjectsService.update(post2);

					htmls.put(delid[1], getSingleSubject(post));
					htmls.put(delid[0], getSingleSubject(post2));

					ajaxResult.put("code", 1);
					ajaxResult.put("msg", "success");
					ajaxResult.put("html", htmls);
				} catch (Exception e) {
					ajaxResult.put("code", 0);
					ajaxResult.put("msg", "操作失败.");
					LOGGER.error("新闻修改排序", e);
				}
			} else {
				ajaxResult.put("code", 0);
				ajaxResult.put("msg", "操作失败,文章只能同时选择2个.");
			}
			break;
		default:
			break;
		}

		return ajaxResult;
	}
}