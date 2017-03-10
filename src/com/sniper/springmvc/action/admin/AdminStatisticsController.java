package com.sniper.springmvc.action.admin;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.sniper.springmvc.model.SdPageIndex;
import com.sniper.springmvc.model.SdStatIndexSum;
import com.sniper.springmvc.mybatis.service.impl.SdHistorySiService;
import com.sniper.springmvc.mybatis.service.impl.SdItemsService;
import com.sniper.springmvc.mybatis.service.impl.SdPageIndexService;
import com.sniper.springmvc.mybatis.service.impl.SdStatIndexSumService;
import com.sniper.springmvc.mybatis.service.impl.SdSubjectsService;
import com.sniper.springmvc.mybatis.service.impl.SdTabLeavewordService;
import com.sniper.springmvc.mybatis.service.impl.SdViewSubjectService;
import com.sniper.springmvc.searchUtil.PostSearch;
import com.sniper.springmvc.searchUtil.SolrSearch;
import com.sniper.springmvc.solr.SolrViewUtil;
import com.sniper.springmvc.solr.SubjectViewModel;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.ExeclStaticExportUtils;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ValidateUtil;
import com.sniper.springmvc.utils.model.Statistics;

/**
 * 数据统计
 * 
 * @author suzhen
 * 
 */
@RequestMapping("${adminPath}/admin-statistics")
@Controller
public class AdminStatisticsController extends AdminBaseController {

	@Resource
	SdHistorySiService historySiService;

	@Resource
	SdSubjectsService postService;

	@Resource
	SdViewSubjectService viewSubjectService;

	@Resource
	SdTabLeavewordService leavewordService;

	@Resource
	SdItemsService itemsService;

	@Resource
	SdPageIndexService pageIndexService;

	@Resource
	SdStatIndexSumService statIndexSumService;

	/**
	 * 处室栏目统计
	 * 
	 * @param map
	 * @param search
	 * @return
	 */
	@RequestMapping(value = "postCount", method = RequestMethod.GET)
	@RequiresPermissions("admin:statistics:postCount")
	public String postCount(Map<String, Object> map) {
		map.put("deps", getDep());
		map.put("search", new PostSearch());
		return forward("/admin/admin-statistics/postCount.jsp");

	}

	@RequestMapping(value = "postCount", method = RequestMethod.POST)
	@RequiresPermissions("admin:statistics:postCount")
	public String postCount(Map<String, Object> map, PostSearch search)
			throws IOException {
		// 处室
		Map<String, String> deps = getDep();
		Map<String, String> searchDeps = new HashMap<>();
		// 获取要查询的处室
		if (ValidateUtil.isValid(search.getSiteid())) {
			searchDeps.put(String.valueOf(search.getSiteid()),
					deps.get(String.valueOf(search.getSiteid())));
		} else {
			searchDeps.putAll(deps);
		}

		// 存放处室统计结果，存放栏目统计结果
		Map<String, Map<String, Object>> siteidCount = new LinkedHashMap<>();
		// 存放处室栏目统计，处室id为key
		Map<String, Map<String, Map<String, Object>>> siteidItemidCount = new LinkedHashMap<>();
		// 获取处室
		Map<String, Map<String, String>> itemsMap = getItems(searchDeps, true);

		// 查询数据,循环栏目，key是处室
		for (Entry<String, String> entry : searchDeps.entrySet()) {
			// 查询处室
			Map<String, Object> siteid = new HashMap<>();
			siteid.putAll(viewSubjectService.siteidCount(
					Integer.valueOf(entry.getKey()), search.getStartDate(),
					search.getEndDate()));
			// 留言统计
			int worldCount = leavewordService.wordCount(
					Integer.valueOf(entry.getKey()), 0, search.getStartDate(),
					search.getEndDate());
			// 回复留言统计
			int worldAnswerCount = leavewordService.wordCount(
					Integer.valueOf(entry.getKey()), 1, search.getStartDate(),
					search.getEndDate());
			siteid.put("worldCount", worldCount);
			siteid.put("worldAnswerCount", worldAnswerCount);
			// 查找留言
			siteidCount.put(entry.getKey(), siteid);
			// 查询栏目
			Map<String, Map<String, Object>> siteidItems = viewSubjectService
					.listSiteidItemidCount(Integer.valueOf(entry.getKey()),
							search.getStartDate(), search.getEndDate(),
							search.getItemDateStart());
			// 储存栏目统计结果
			siteidItemidCount.put(entry.getKey(), siteidItems);
		}

		if (ValidateUtil.isValid(search.getSubmit())
				&& search.getSubmit().equals("export")) {
			ExeclStaticExportUtils exportUtils = new ExeclStaticExportUtils();
			exportUtils.setRootDir(FilesUtil.getRootDir());
			Statistics statistics = new Statistics();
			// 栏目统计结果
			statistics.setSiteidItemidCount(siteidItemidCount);
			statistics.setItemsMap(itemsMap);
			// 处室列表
			statistics.setSearchDeps(searchDeps);
			// 处室统计结果
			statistics.setSiteidCount(siteidCount);
			exportUtils.setStatistics(statistics);
			List<Map<String, String>> headFieldsAll = new ArrayList<>();
			Map<String, String> headFields = new HashMap<>();
			headFields.put("1", "处室 > 栏目");
			headFields.put("2", "新闻访问量");
			headFields.put("3", "新闻发布量");
			headFields.put("4", "最后新闻发布日期");
			headFields.put("5", "留言量/回复量");
			headFieldsAll.add(headFields);
			exportUtils.setHeadFieldsAll(headFieldsAll);
			String exportPath = FilesUtil.getSaveDir("") + "export.xlsx";
			exportUtils.exportPostCount(exportPath);
			// 非浏览器下载
			// 直接文件下载
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX
					+ FilesUtil.getWebUrl() + exportPath;
		}
		map.put("search", search);
		map.put("siteidCount", siteidCount);
		map.put("siteidItemidCount", siteidItemidCount);
		map.put("deps", deps);
		map.put("itemsMap", itemsMap);
		map.put("searchDeps", searchDeps);
		return forward("/admin/admin-statistics/postCount.jsp");
	}

	/**
	 * 查询聚合栏目统计
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:statistics:channelPostCount")
	@RequestMapping(value = "channelPostCount", method = RequestMethod.GET)
	public String channelPostCount(Map<String, Object> map) {

		PostSearch search = new PostSearch();
		map.put("search", search);
		Map<String, SdPageIndex> indexs = pageIndexService.getPageIndex();
		map.put("indexs", indexs);
		Map<String, String> indexNames = pageIndexService.pageIndexMap(indexs);
		map.put("indexNames", indexNames);
		return forward("/admin/admin-statistics/channelPostCount.jsp");
	}

	/**
	 * 聚合栏目统计
	 * 
	 * @param map
	 * @param search
	 * @return
	 */
	@RequiresPermissions("admin:statistics:channelPostCount")
	@RequestMapping(value = "channelPostCount", method = RequestMethod.POST)
	public String channelPostCount(Map<String, Object> map, PostSearch search) {
		// 栏目列表
		Map<String, String> deps = getDep();
		Map<String, Map<String, String>> items = getItemsByRedis(deps);
		// 临时储存聚合栏目
		Map<String, SdPageIndex> indexs = new HashMap<>();
		// 读取所有聚合栏目
		Map<String, SdPageIndex> pageIndex = pageIndexService.getPageIndex();
		if (ValidateUtil.isValid(search.getGroup())) {
			SdPageIndex sdPageIndex = pageIndexService.get(search.getGroup()
					.toString());
			indexs.put(search.getGroup().toString(), sdPageIndex);
		} else {
			indexs = pageIndex;
		}

		Map<String, String> indexNames = pageIndexService
				.pageIndexMap(pageIndex);
		// 存放处室统计结果，存放栏目统计结果
		Map<String, Map<String, Map<String, Object>>> indexCounts = new HashMap<>();
		// 统计过程
		for (Entry<String, SdPageIndex> entry : indexs.entrySet()) {
			// 查询处室栏目的新闻访问量，发布新闻数量，每个栏目最后新闻发布日期
			String itemid = entry.getValue().getItemid();
			if (!ValidateUtil.isValid(itemid)) {
				continue;
			}
			// 解析出id
			String[] itemids = itemid.split(",");
			List<String> ids = Arrays.asList(itemids);
			// 获取栏目的名称
			// string转成int
			List<Integer> intItemids = DataUtil.listStringToInt(ids);

			Map<String, Map<String, Object>> resultCount = viewSubjectService
					.listItemidCount(intItemids, search.getStartDate(),
							search.getEndDate());
			indexCounts.put(entry.getKey(), resultCount);

		}
		// 结果
		map.put("indexCounts", indexCounts);
		// 聚合列表
		map.put("indexNames", indexNames);
		map.put("search", search);
		map.put("indexs", indexs);
		map.put("deps", deps);
		map.put("items", items);
		return forward("/admin/admin-statistics/channelPostCount.jsp");
	}

	/**
	 * 新闻浏览统计
	 * 
	 * @return
	 * @throws ParseException
	 * @throws IOException
	 * @throws SolrServerException
	 */
	@RequiresPermissions("admin:statistics:postViewCount")
	@RequestMapping(value = "postViewCount")
	public String postViewCount(Map<String, Object> map, SolrSearch search)
			throws ParseException, SolrServerException, IOException {

		Map<String, String> deps = getDep();
		Map<String, Map<String, String>> items = getItemsByRedis(deps);

		map.put("deps", deps);
		map.put("items", items);

		HttpSolrClient solrClient = SolrViewUtil
				.getInstance(SubjectViewModel.CORE_NAME);
		int pageSize = search.getLimit();
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
		params.set("q", "*:*");
		SolrQuery filterQuery = new SolrQuery();
		if (ValidateUtil.isValid(search.getName())) {
			filterQuery.addFilterQuery("subject:" + search.getName());
			filterQuery.addFilterQuery("subjectContent:" + search.getName());
		}
		filterQuery.addFilterQuery("lookthroughed_i:2");

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
		if (ValidateUtil.isValid(search.getEndDate())
				|| ValidateUtil.isValid(search.getStartDate())) {
			filterQuery.addDateRangeFacet("date_dt", startDate, endDate,
					"+1DAY");
		}
		Set<String> solrReadSiteid = new HashSet<>();
		if (ValidateUtil.isValid(search.getSiteid())) {
			solrReadSiteid.add(String.valueOf(search.getSiteid()));
		} else {
			for (Entry<String, String> entry : deps.entrySet()) {
				solrReadSiteid.add(entry.getKey());
			}
		}
		// 处室限制
		if (solrReadSiteid.size() > 0) {
			List<String> siteidList = new ArrayList<>();
			for (String srs : solrReadSiteid) {
				siteidList.add("siteid_i:" + srs);
			}
			filterQuery.addFilterQuery(StringUtils.join(siteidList, " or "));
		}

		if (ValidateUtil.isValid(search.getItemid())) {
			filterQuery.addFilterQuery("itemid_i:" + search.getItemid());
		}

		filterQuery.setSort("view_i", ORDER.desc);
		params.add(filterQuery);

		params.set("start", pageOffset);
		params.set("rows", pageSize);
		params.set("fl", "*");
		System.out.println(params.toQueryString());
		QueryResponse response = solrClient.query(params);
		SolrDocumentList list = response.getResults();
		List<SubjectViewModel> lists = response
				.getBeans(SubjectViewModel.class);

		PageUtil page = new PageUtil(Long.valueOf(list.getNumFound())
				.intValue(), pageSize);
		page.setRequest(request);
		String pageHtml = page.show();

		map.put("lists", lists);
		map.put("search", search);
		map.put("pageHtml", pageHtml);
		return forward("/admin/admin-statistics/postViewCount.jsp");
	}

	/**
	 * 新闻新闻栏目月统计
	 * 
	 * @return
	 */
	@RequiresPermissions("admin:statistics:postMonthCount")
	@RequestMapping(value = "postMonthCount", method = RequestMethod.GET)
	public String postMonthCount(Map<String, Object> map) {

		PostSearch search = new PostSearch();
		Map<String, SdPageIndex> indexs = pageIndexService.getPageIndex();
		map.put("indexs", indexs);
		Map<String, String> indexNames = pageIndexService.pageIndexMap(indexs);
		map.put("indexNames", indexNames);
		map.put("search", search);
		map.put("deps", getDep());
		return forward("/admin/admin-statistics/postMonthCount.jsp");

	}

	@RequiresPermissions("admin:statistics:postMonthCount")
	@RequestMapping(value = "postMonthCount", method = RequestMethod.POST)
	public String postMonthCount(Map<String, Object> map, PostSearch search)
			throws ParseException, IOException {

		// 接受聚合栏目
		Map<String, SdPageIndex> indexs = pageIndexService.getPageIndex();
		map.put("indexs", indexs);
		Map<String, String> indexNames = pageIndexService.pageIndexMap(indexs);
		List<Map<String, Object>> postMonthCount = new ArrayList<>();
		if (ValidateUtil.isValid(search.getItemid())) {
			postMonthCount = viewSubjectService.monthCount(search.getItemid(),
					search.getStartDate(), search.getEndDate());
		} else if (ValidateUtil.isValid(search.getGroup())) {
			postMonthCount = viewSubjectService.monthGroupCount(
					Integer.valueOf(search.getGroup()), search.getStartDate(),
					search.getEndDate());
		}

		if (ValidateUtil.isValid(search.getSubmit())
				&& search.getSubmit().equals("export")) {
			ExeclStaticExportUtils exportUtils = new ExeclStaticExportUtils();
			exportUtils.setRootDir(FilesUtil.getRootDir());
			List<Map<String, String>> headFieldsAll = new ArrayList<>();
			Map<String, String> headFields = new LinkedHashMap<>();
			headFields.put("id", "ID");
			headFields.put("ym", "日期");
			headFields.put("total", "发布量");
			headFieldsAll.add(headFields);
			exportUtils.setHeaderFields(headFields);
			exportUtils.setHeadFieldsAll(headFieldsAll);
			String exportPath = FilesUtil.getSaveDir("") + "export.xlsx";
			exportUtils.exportMapKeyValue(exportPath, postMonthCount);
			// 非浏览器下载
			// 直接文件下载
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX
					+ FilesUtil.getWebUrl() + exportPath;
		}

		map.put("postMonthCount", postMonthCount);
		map.put("indexNames", indexNames);
		map.put("search", search);
		map.put("indexs", indexs);
		return forward("/admin/admin-statistics/postMonthCount.jsp");
	}

	/**
	 * 访问量控制
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequiresPermissions("admin:statistics:viewCount")
	@RequestMapping(value = "viewCount", method = RequestMethod.GET)
	public String viewCount(Map<String, Object> map, PostSearch search)
			throws IOException {

		Map<String, String> showType = new LinkedHashMap<>();
		showType.put("day", "日统计");
		showType.put("month", "月统计");
		showType.put("year", "年统计");

		Map<String, Object> params = new HashMap<>();

		if (ValidateUtil.isValid(search.getStartDate())) {
			params.put("stime", search.getStartDate());
		}

		if (ValidateUtil.isValid(search.getEndDate())) {
			params.put("etime", search.getEndDate());
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> listsMap = new ArrayList<>();
		if (ValidateUtil.isValid(search.getSubmit())
				&& search.getSubmit().equals("export")) {
			ExeclStaticExportUtils exportUtils = new ExeclStaticExportUtils();
			exportUtils.setRootDir(FilesUtil.getRootDir());
			List<Map<String, String>> headFieldsAll = new ArrayList<>();
			Map<String, String> headFields = new LinkedHashMap<>();
			headFields.put("id", "ID");
			headFields.put("d", "日期");
			headFields.put("v", "访问量");
			headFieldsAll.add(headFields);
			exportUtils.setHeaderFields(headFields);
			exportUtils.setHeadFieldsAll(headFieldsAll);
			String exportPath = FilesUtil.getSaveDir("") + "export.xlsx";
			// 数据组装
			params.put("pageSize", search.getLimit());
			if (ValidateUtil.isValid(search.getMenuType())) {
				switch (search.getMenuType()) {
				case "day":
					// 如果没有开始日期，读取2年的
					listsMap = statIndexSumService
							.queryMap("selectMap", params);
					break;
				case "month":
					listsMap = statIndexSumService
							.queryMap("statMonth", params);
					break;
				case "year":
					listsMap = statIndexSumService.queryMap("statYear", params);
					break;
				default:
					break;
				}

			}

			exportUtils.exportMapKeyValue(exportPath, listsMap);
			// 非浏览器下载,直接文件下载
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX
					+ FilesUtil.getWebUrl() + exportPath;
		}

		if (ValidateUtil.isValid(search.getMenuType())
				&& !search.getMenuType().equals("day")) {
			params.put("pageSize", search.getLimit());
			if (search.getMenuType().equals("month")) {
				listsMap = statIndexSumService.queryMap("statMonth", params);
			} else if (search.getMenuType().equals("year")) {
				listsMap = statIndexSumService.queryMap("statYear", params);
			}

		} else {
			int count = statIndexSumService.pageCount(params);
			PageUtil page = new PageUtil(count, search.getLimit());
			page.setRequest(request);
			String pageHtml = page.show();

			params.put("order", "id desc");
			params.put("pageOffset", page.getFristRow());
			params.put("pageSize", page.getListRow());
			List<SdStatIndexSum> lists = statIndexSumService.pageList(params);
			// 主要是把date变成字符串
			for (SdStatIndexSum sdStatIndexSum : lists) {
				Map<String, Object> map2 = new HashMap<>();
				map2.put("d", dateFormat.format(sdStatIndexSum.getDate()));
				map2.put("v", sdStatIndexSum.getView());
				listsMap.add(map2);
			}
			map.put("pageHtml", pageHtml);

		}
		map.put("lists", listsMap);
		map.put("search", search);
		map.put("showType", showType);
		return forward("/admin/admin-statistics/viewCount.jsp");
	}

	public static void main(String[] args) {
		System.out.println("--");
	}

}