package com.sniper.springmvc.action.admin;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sniper.springmvc.data.DataValues;
import com.sniper.springmvc.freemarker.FreeMarkerUtil;
import com.sniper.springmvc.model.Survey;
import com.sniper.springmvc.model.SurveyPage;
import com.sniper.springmvc.model.SurveyQuestion;
import com.sniper.springmvc.model.SurveyQuestionOption;
import com.sniper.springmvc.model.SurveyResult;
import com.sniper.springmvc.model.SurveyResultData;
import com.sniper.springmvc.mybatis.service.impl.SurveyPageService;
import com.sniper.springmvc.mybatis.service.impl.SurveyQuestionOptionService;
import com.sniper.springmvc.mybatis.service.impl.SurveyQuestionServie;
import com.sniper.springmvc.mybatis.service.impl.SurveyResultDataService;
import com.sniper.springmvc.mybatis.service.impl.SurveyResultService;
import com.sniper.springmvc.mybatis.service.impl.SurveyService;
import com.sniper.springmvc.searchUtil.BaseSearch;
import com.sniper.springmvc.searchUtil.ChannelSearch;
import com.sniper.springmvc.survey.SurveyAnalysisUtil;
import com.sniper.springmvc.survey.SurveyAnswerValue;
import com.sniper.springmvc.survey.SurveyResultExportUtil;
import com.sniper.springmvc.survey.SurveyTemplateUtil;
import com.sniper.springmvc.survey.SurveyUtils;
import com.sniper.springmvc.utils.DataUtil;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.MapUtil;
import com.sniper.springmvc.utils.PageUtil;
import com.sniper.springmvc.utils.ParamsToHtml;
import com.sniper.springmvc.utils.PropertiesUtil;
import com.sniper.springmvc.utils.UserDetailsUtils;
import com.sniper.springmvc.utils.ValidateUtil;

import freemarker.template.TemplateHashModel;

@Controller
@RequestMapping("${adminPath}/admin-survey")
public class AdminSurveyController extends AdminBaseController {

	private static TemplateHashModel surveyModel = FreeMarkerUtil
			.getFreeMarkerStaticModel(SurveyTemplateUtil.class);

	private static TemplateHashModel analysisModel = FreeMarkerUtil
			.getFreeMarkerStaticModel(SurveyAnalysisUtil.class);

	/**
	 * 对问题类型进行排序
	 * 
	 * @author sniper
	 * 
	 */
	public class TypeSort implements Comparator<Map.Entry<String, String>> {

		@Override
		public int compare(Entry<String, String> arg0,
				Entry<String, String> arg1) {
			return 0;
		}

	}

	private static Map<String, String> surveyType = new LinkedHashMap<>();
	static {
		//
		InputStream in = AdminSurveyController.class.getClassLoader()
				.getResourceAsStream("properties/surveytypes.properties");
		PropertiesUtil util = new PropertiesUtil(in);
		surveyType = MapUtil.sortTreeMapByKey(util.getValues());

	}

	private static Map<String, String> listStyles = new HashMap<>();

	static {
		listStyles.put("none", "空");
		listStyles.put("decimal", "阿拉伯数字");
		listStyles.put("upper-alpha", "大写字母");
		listStyles.put("upper-roman", "大写罗马");
	}

	@Resource
	private SurveyService surveyService;

	@Resource
	private SurveyPageService pageService;

	@Resource
	private SurveyQuestionServie questionServie;

	@Resource
	private SurveyResultDataService resultDataService;

	@Resource
	private SurveyResultService resultService;

	@Resource
	private SurveyQuestionOptionService optionService;

	@ModelAttribute
	@Override
	public void init(Map<String, Object> map) {
		super.init(map);
	}

	@RequiresPermissions("admin:survey:view")
	@RequestMapping("/")
	public String index(Map<String, Object> map, ChannelSearch search) {

		map.put("sniperUrl", "/admin-survey/delete");
		ParamsToHtml toHtml = new ParamsToHtml();

		Map<String, String> locked = new HashMap<>();
		locked.put("true", "是");
		locked.put("false", "否");

		toHtml.addMapValue("locked", locked);

		Map<String, String> copy = new HashMap<>();
		copy.put("copy", "复制");
		toHtml.addMapValue("copy", copy);

		Map<String, String> clear = new HashMap<>();
		clear.put("clear", "清空结果");
		toHtml.addMapValue("clear", clear);

		Map<String, String> keys = new HashMap<>();
		keys.put("locked", "锁定");
		keys.put("copy", "复制");
		keys.put("clear", "清空结果");

		toHtml.setKeys(keys);

		Map<String, Object> params = new LinkedHashMap<>();

		if (ValidateUtil.isValid(search.getStatus())) {
			params.put("locked", search.getStatus());
		}

		if (ValidateUtil.isValid(search.getName())) {
			params.put("name", "%" + search.getName() + "%");
		}

		UserDetailsUtils detailsUtils = new UserDetailsUtils();

		if (!detailsUtils.hasRole("ROLE_ADMIN")) {
			params.put("uid", getAdminUser().getId());
		}

		int count = surveyService.pageCount(params);
		PageUtil page = new PageUtil(count, 10);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "cTime desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<Survey> lists = surveyService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		map.put("sniperMenu", toHtml);
		return forward("/admin/admin-survey/index.jsp");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:survey:insert")
	@RequestMapping(value = "insert", method = RequestMethod.GET)
	public String insert(Map<String, Object> map) {

		map.put("survey", new Survey());
		map.put("listStyles", listStyles);
		return forward("/admin/admin-survey/save-input.jsp");
	}

	@RequiresPermissions("admin:survey:insert")
	@RequestMapping(value = "insert", method = RequestMethod.POST)
	public String insert(Survey survey, BindingResult result,
			Map<String, Object> map) {

		try {
			if (result.getFieldErrorCount() > 0) {
				List<String> errors = new ArrayList<>();
				// 设置选中的栏目
				List<FieldError> fieldErrors = result.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					errors.add(fieldError.getDefaultMessage());
				}
				System.out.println(errors);
				errors.add("添加失败");
				map.put("errors", errors);
				map.put("listStyles", listStyles);
				return forward("/admin/admin-survey/save-input.jsp");
			} else {
				survey.setId(FilesUtil.UUID());
				survey.setAdminUser(getAdminUser());
				surveyService.insert(survey);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return redirect("/admin-survey/insert");
	}

	/**
	 * 更新展示,修改展示
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequiresPermissions("admin:survey:update")
	@RequestMapping(value = "update", method = RequestMethod.GET)
	public String update(
			@RequestParam(value = "id", required = false) String id,
			Survey survey, Map<String, Object> map) {

		if (ValidateUtil.isValid(id)) {
			survey = surveyService.get(id);
			UserDetailsUtils detailsUtils = new UserDetailsUtils();
			if (!detailsUtils.hasRole(DataValues.ROLE_ADMIN)) {
				if (survey.getAdminUser().getId() != getAdminUser().getId()) {
					return redirect("/admin-survey/");
				}
			}
		} else {
			return redirect("/admin-survey/insert");
		}
		map.put("listStyles", listStyles);
		map.put("survey", survey);
		return forward("/admin/admin-survey/save-input.jsp");
	}

	@RequiresPermissions("admin:survey:update")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public String update(Survey survey, BindingResult result,
			Map<String, Object> map) {
		try {
			surveyService.update(survey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return redirect("/admin-survey/update?id=" + survey.getId());
	}

	@RequiresPermissions("admin:survey:survey")
	@RequestMapping("survey")
	public String survey(@RequestParam("id") String id, Map<String, Object> map) {

		Survey survey = surveyService.getSurvey(id);
		if (survey == null) {
			return redirect("/admin-survey/");
		}

		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		if (!detailsUtils.hasRole(DataValues.ROLE_ADMIN)) {
			if (survey.getAdminUser().getId() != getAdminUser().getId()) {
				return redirect("/admin-survey/");
			}
		}
		map.put("survey", survey);
		ParamsToHtml toHtml = new ParamsToHtml();
		toHtml.setKeys(surveyType);
		map.put("toHtml", toHtml);
		return forward("admin/admin-survey/survey-input.ftl");
	}

	@RequiresPermissions("admin:survey:delete")
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Map<String, Object> delete(@RequestParam("delid") String[] delid,
			@RequestParam("menuType") String menuType,
			@RequestParam("menuValue") String menuValue) {
		Map<String, Object> ajaxResult = new HashMap<>();
		switch (menuType) {
		case "delete":
			try {
				for (int i = 0; i < delid.length; i++) {
					surveyService.delete(delid[i]);
					// 删除多有答案
					List<SurveyResultData> datas = resultDataService
							.getSurveyResultData(delid[i]);
					for (SurveyResultData surveyResultData : datas) {
						resultDataService.delete(surveyResultData.getId());
					}
					surveyService.delete(delid[i]);
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");

			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}
			break;
		case "clear":
			try {
				for (int i = 0; i < delid.length; i++) {
					Survey survey = surveyService.getSurvey(delid[i]);
					survey.setPeopleNum(0);
					surveyService.update(survey);
					// 删除多有答案
					List<SurveyResultData> datas = resultDataService
							.getSurveyResultData(delid[i]);
					for (SurveyResultData surveyResultData : datas) {
						resultDataService.delete(surveyResultData.getId());
					}
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");

			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", "删除失败");
			}
			break;
		case "locked":
			try {
				for (String string : delid) {
					Survey survey = surveyService.get(string);
					survey.setLocked(DataUtil.stringToBoolean(menuValue));
					surveyService.update(survey);
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}
			break;
		case "copy":
			try {
				List<Survey> lists = surveyService.surveyCopy(delid);
				for (Survey survey : lists) {
					survey.setPeopleNum(0);
					surveyService.insert(survey);
					for (SurveyPage page : survey.getSurveyPages()) {
						pageService.insert(page);
						for (SurveyQuestion question : page.getSq()) {
							questionServie.insert(question);
							for (SurveyQuestionOption option : question
									.getOptions()) {
								optionService.insert(option);
							}
						}
					}
				}
				ajaxResult.put("code", 1);
				ajaxResult.put("msg", "success");
			} catch (Exception e) {
				ajaxResult.put("code", -1);
				ajaxResult.put("msg", e.getMessage());
			}
			break;

		default:
			break;
		}

		return ajaxResult;
	}

	@RequiresPermissions("admin:survey:answer")
	@RequestMapping(value = "answer", method = RequestMethod.GET)
	public String answer(Map<String, Object> map,
			@RequestParam(value = "id") String id,
			@RequestParam(value = "sdid", required = false) String sdid) {
		// 所有问卷的内容
		Survey survey = surveyService.getSurvey(id);
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		if (!detailsUtils.hasRole("ROLE_ADMIN")) {
			if (survey.getAdminUser().getId() != getAdminUser().getId()) {
				return redirect("/admin-survey/");
			}
		}
		if (ValidateUtil.isValid(sdid)) {
			Map<String, List<String>> answers = resultService.getAnswer(id,
					sdid);

			map.put("answers", answers);
		}

		map.put("survey", survey);
		map.put("surveyModel", surveyModel);
		return forward("admin/admin-survey/answer.ftl");
	}

	/**
	 * 提交问卷 没有用到 包含内容会显
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("admin:survey:answer")
	@RequestMapping(value = "answer", method = RequestMethod.POST)
	public String show(Map<String, Object> map,
			@RequestParam(value = "id") String id, SurveyAnswerValue answer) {
		// 所有问卷的内容

		SurveyUtils surveyUtils = new SurveyUtils();
		surveyUtils.setRequest(request);
		surveyUtils.setSurveyId(id);
		surveyUtils.setUid(getAdminUser().getId());
		surveyUtils.setParameters(answer.getAnswer());
		surveyUtils.run();
		List<SurveyResult> results = surveyUtils.getResults();
		Map<String, List<String>> resultErrors = new HashMap<>();
		resultErrors = surveyUtils.getErrors();
		// 数据保存
		SurveyResultData resultData = surveyUtils.getResultData();

		if (resultErrors.size() == 0) {
			resultDataService.insert(resultData);
			for (SurveyResult surveyResult : results) {
				surveyResult.setResultData(resultData);
				resultService.insert(surveyResult);
			}
		} else {
			map.put("resultErrors", resultErrors);
		}

		// Map<String, List<String>> answers = resultService.getAnswer(id, "1");

		Survey model = surveyService.getSurvey(id);
		map.put("survey", model);
		// map.put("answers", answers);
		map.put("surveyModel", surveyModel);
		return forward("admin/admin-survey/answer.ftl");
	}

	@RequiresPermissions("admin:survey:analysis")
	@RequestMapping("analysis")
	public String analysis(Map<String, Object> map,
			@RequestParam(value = "id") String id) {
		// 所有问卷的内容
		Survey survey = surveyService.getSurvey(id);
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		if (!detailsUtils.hasRole(DataValues.ROLE_ADMIN)) {
			if (survey.getAdminUser().getId().equals(getAdminUser().getId())) {
				return redirect("/admin-survey/");
			}
		}
		Map<String, Integer> answersCount = resultService
				.getSurveyResult(survey);

		map.put("answersCount", answersCount);
		map.put("survey", survey);
		map.put("analysisModel", analysisModel);
		return forward("admin/admin-survey/analysis.ftl");
	}

	@RequiresPermissions("admin:survey:download")
	@RequestMapping("download")
	public String download(@RequestParam(value = "id") String id,
			Map<String, Object> map) throws IOException {
		// 所有问卷的内容
		Survey survey = surveyService.getSurvey(id);
		UserDetailsUtils detailsUtils = new UserDetailsUtils();
		if (!detailsUtils.hasRole(DataValues.ROLE_ADMIN)) {
			if (survey.getAdminUser().getId().equals(getAdminUser().getId())) {
				return redirect("/admin-survey/");
			}
		}
		List<SurveyResultData> datas = resultDataService
				.getSurveyResultData(id);
		Map<String, Map<String, List<String>>> results = resultService
				.exportResult(datas, id);

		SurveyResultExportUtil exportUtil = new SurveyResultExportUtil();
		exportUtil.setSurvey(survey);
		exportUtil.setResults(results);
		exportUtil.run();
		exportUtil.write("export");
		map.put("path", "/export/export.xlsx");
		map.put("fileName", "export.xlsx");
		return "redirect:/file-info/download";
	}

	@RequiresPermissions("admin:survey:analysisuser")
	@RequestMapping("analysisuser")
	public String analysisUser(@RequestParam(value = "id") String id,
			Map<String, Object> map, BaseSearch search) {

		Map<String, Object> params = new LinkedHashMap<>();
		params.put("sid", id);
		int count = resultDataService.pageCount(params);
		PageUtil page = new PageUtil(count, 20);
		page.setRequest(request);
		String pageHtml = page.show();
		params.put("order", "cTime desc");
		params.put("pageOffset", page.getFristRow());
		params.put("pageSize", page.getListRow());
		List<SurveyResultData> lists = resultDataService.pageList(params);
		map.put("lists", lists);
		map.put("pageHtml", pageHtml);
		return forward("admin/admin-survey/analysisuser.ftl");
	}
}