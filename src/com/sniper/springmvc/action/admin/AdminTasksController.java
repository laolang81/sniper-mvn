package com.sniper.springmvc.action.admin;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.http.client.ClientProtocolException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sniper.springmvc.model.SdViewSubject;
import com.sniper.springmvc.scheduler.BackDBToFile;
import com.sniper.springmvc.scheduler.GetSDPostImages;
import com.sniper.springmvc.scheduler.SolrSubjectTask;
import com.sniper.springmvc.scheduler.SolrSubjectViewTask;
import com.sniper.springmvc.scheduler.TaskStatus;
import com.sniper.springmvc.searchUtil.SchedulerSearch;
import com.sniper.springmvc.shiro.scheduler.QuartzManagerUtil;
import com.sniper.springmvc.solr.SolrViewUtil;
import com.sniper.springmvc.solr.SubjectViewModel;
import com.sniper.springmvc.utils.PathUtil;
import com.sniper.springmvc.utils.ScheduleJob;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 数据库管理
 * 
 * @author sniper
 * 
 */
@Controller
@RequestMapping("${adminPath}/admin-tasks")
public class AdminTasksController extends AdminBaseController {

	@Resource(name = "dataSource_main")
	private ComboPooledDataSource dataSource;

	@RequiresPermissions("admin:tasks:view")
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index(Map<String, Object> map) throws SchedulerException {

		map.put("jobs", QuartzManagerUtil.getCronJobs());
		return forward("/admin/admin-tasks/index.ftl");
	}

	@RequiresPermissions("admin:tasks:view")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public String index(Map<String, Object> map, SchedulerSearch search)
			throws SchedulerException {

		String jobName = "";
		if (ValidateUtil.isValid(search.getJobName())) {
			jobName = search.getJobName().trim();
		}

		String submit = search.getSubmit().trim();
		switch (TaskStatus.toTaskStatus(submit)) {
		case START:
			QuartzManagerUtil.start();
			break;
		case PAUSE:
			QuartzManagerUtil.pauseCronJob(jobName);
			break;
		case CONTINUE:
			QuartzManagerUtil.resumeJob(jobName);
			break;
		case STOP:
			QuartzManagerUtil.stop();
		case DELETE:
			QuartzManagerUtil.removeCronJob(jobName);
		case RUNONCE:
			break;

		default:
			break;
		}

		List<ScheduleJob> jobs = new ArrayList<>();
		// 获取任务列表
		jobs = QuartzManagerUtil.getCronJobs();
		jobs.addAll(QuartzManagerUtil.getCronExecutingJobs());
		map.put("jobs", jobs);

		return forward("/admin/admin-tasks/index.ftl");
	}

	@RequiresPermissions("admin:tasks:job")
	@ResponseBody
	@RequestMapping(value = "job", method = RequestMethod.POST)
	public String job(SchedulerSearch search) throws SchedulerException {

		String jobName = search.getJobName();
		String submit = search.getSubmit();

		switch (TaskStatus.toTaskStatus(submit)) {
		case START:
			QuartzManagerUtil.start();
			break;
		case PAUSE:
			QuartzManagerUtil.pauseCronJob(jobName);
			break;
		case CONTINUE:
			QuartzManagerUtil.resumeJob(jobName);
			break;
		case STOP:
			QuartzManagerUtil.stop();
		case RUNONCE:
			break;

		default:
			break;
		}

		return "";
	}

	@RequiresPermissions("admin:tasks:dbBackToFile")
	@RequestMapping(value = "dbBackToFile", method = RequestMethod.GET)
	public String backDBToFile(Map<String, Object> map)
			throws SchedulerException, IllegalAccessException {
		// 数据初始化
		// 储存地址初始化
		String jobName = BackDBToFile.jobName;
		SchedulerSearch search = new SchedulerSearch();
		ScheduleJob job = QuartzManagerUtil.getScheduleJob(jobName);
		if (job != null) {
			search.setCron(job.getCronExpression());
		}
		String webRootPath = PathUtil.getWebRoot();
		search.setCount(BackDBToFile.getFileCount());
		search.setPath(BackDBToFile.defaultPath);
		// 读取文件列表
		if (ValidateUtil.isValid(BackDBToFile.getBackToFilePath())) {
			search.setPath(BackDBToFile.getBackToFilePath());
			// 获取url后缀,用于下载
			String webRootPathSuffix = BackDBToFile.getBackToFilePath()
					.substring(webRootPath.length());
			map.put("webRootPathSuffix", webRootPathSuffix);
		}

		map.put("search", search);
		map.put("job", job);
		map.put("files", BackDBToFile.getFiles());
		return forward("/admin/admin-tasks/backdbtofile.ftl");
	}

	@RequiresPermissions("admin:tasks:dbBackToFile")
	@RequestMapping(value = "dbBackToFile", method = RequestMethod.POST)
	public String backDBToFile(Map<String, Object> map, SchedulerSearch search)
			throws SchedulerException, IllegalAccessException {

		String jobName = BackDBToFile.jobName;
		BackDBToFile back = new BackDBToFile();

		QuartzManagerUtil.start();
		// 更新任务时间
		String path = search.getPath().trim();
		String cron = search.getCron();
		String submit = search.getSubmit();
		int count = search.getCount();

		BackDBToFile.setDataSource(dataSource);
		if (ValidateUtil.isValid(path)) {
			BackDBToFile.setBackToFilePath(path);
		}
		if (ValidateUtil.isValid(count)) {
			BackDBToFile.setFileCount(count);
		}

		switch (TaskStatus.toTaskStatus(submit)) {
		case START:
			if (ValidateUtil.isValid(cron)) {
				QuartzManagerUtil.addCronJob(jobName, back.getClass(), cron);
			}
			break;
		case PAUSE:
			QuartzManagerUtil.pauseCronJob(jobName);
			break;
		case CONTINUE:
			QuartzManagerUtil.resumeJob(jobName);
			break;
		case STOP:
			QuartzManagerUtil.removeCronJob(jobName);
			break;
		case RUNONCE:
			back.init();
			break;

		default:
			break;
		}

		String webRootPath = PathUtil.getWebRoot();
		if (ValidateUtil.isValid(BackDBToFile.getBackToFilePath())) {
			search.setPath(BackDBToFile.getBackToFilePath());
			// 获取url后缀,用于下载
			String webRootPathSuffix = BackDBToFile.getBackToFilePath()
					.substring(webRootPath.length());
			map.put("webRootPathSuffix", webRootPathSuffix);

		}

		ScheduleJob job = QuartzManagerUtil.getScheduleJob(jobName);
		if (job != null) {
			search.setCron(job.getCronExpression());
		}

		map.put("files", BackDBToFile.getFiles());
		map.put("job", job);
		map.put("search", search);
		return forward("/admin/admin-tasks/backdbtofile.ftl");
	}

	@RequiresPermissions("admin:tasks:getSdImages")
	@RequestMapping(value = "getSdImages", method = RequestMethod.GET)
	public String getSdImages(Map<String, Object> map)
			throws SchedulerException {

		String jobName = GetSDPostImages.jobName;
		SchedulerSearch search = new SchedulerSearch();
		ScheduleJob job = QuartzManagerUtil.getScheduleJob(jobName);
		if (job != null) {
			search.setCron(job.getCronExpression());
		}
		map.put("job", job);
		map.put("search", search);
		return forward("/admin/admin-tasks/getSdImages.ftl");
	}

	@RequiresPermissions("admin:tasks:getSdImages")
	@RequestMapping(value = "getSdImages", method = RequestMethod.POST)
	public String getSdImages(Map<String, Object> map, SchedulerSearch search)
			throws SchedulerException, ClientProtocolException, IOException,
			ParseException {
		String jobName = GetSDPostImages.jobName;
		GetSDPostImages back = new GetSDPostImages();

		QuartzManagerUtil.start();
		// 更新任务时间
		String cron = search.getCron();
		String submit = search.getSubmit();

		back.setSid(search.getId());
		back.setStartDate(search.getStartDate());
		back.setEndDate(search.getEndDate());
		switch (TaskStatus.toTaskStatus(submit)) {
		case START:
			if (ValidateUtil.isValid(cron)) {
				QuartzManagerUtil.addCronJob(jobName, back.getClass(), cron);
			}
			break;
		case PAUSE:
			QuartzManagerUtil.pauseCronJob(jobName);
			break;
		case CONTINUE:
			QuartzManagerUtil.resumeJob(jobName);
			break;
		case STOP:
			QuartzManagerUtil.removeCronJob(jobName);
			break;
		case RUNONCE:
			back.init();
			break;

		default:
			break;
		}

		ScheduleJob job = QuartzManagerUtil.getScheduleJob(jobName);
		if (job != null) {
			search.setCron(job.getCronExpression());
		}

		map.put("search", search);
		map.put("job", job);
		return forward("/admin/admin-tasks/getSdImages.ftl");
	}

	

	/**
	 * Solr新闻导入
	 * 
	 * @param map
	 * @return
	 * @throws SchedulerException
	 */
	@RequestMapping(value = "solrSubject", method = RequestMethod.GET)
	@RequiresPermissions("admin:tasks:solrSubject")
	public String solrSubject(Map<String, Object> map)
			throws SchedulerException {
		SchedulerSearch search = new SchedulerSearch();
		search.setGreaterThenId(SolrSubjectViewTask.getGreaterThenId());

		String jobName = SolrSubjectTask.jobName;
		ScheduleJob job = QuartzManagerUtil.getScheduleJob(jobName);
		int count = SolrSubjectViewTask.getCount();
		long solrCount = SolrViewUtil.getCount(SubjectViewModel.CORE_NAME);
		SubjectViewModel lastModel = SolrViewUtil.getLastModel();
		if (lastModel != null) {
			search.setGreaterThenId(lastModel.getSid());
		}
		map.put("lastModel", lastModel);
		SdViewSubject lastViewSubject = viewSubjectService.getLastSubject();
		map.put("lastViewSubject", lastViewSubject);
		map.put("solrCount", solrCount);
		map.put("job", job);
		map.put("count", count);
		map.put("search", search);
		return forward("/admin/admin-tasks/solr-subject.ftl");
	}

	@RequestMapping(value = "solrSubject", method = RequestMethod.POST)
	@RequiresPermissions("admin:tasks:solrSubject")
	public String solrSubject(Map<String, Object> map, SchedulerSearch search)
			throws SchedulerException {
		// 设置基本参数
		SolrSubjectTask.setMaxThreadCount(search.getMaxThreadCount());
		SolrSubjectTask.setPageSize(search.getPageSize());
		SolrSubjectTask.setGreaterThenId(search.getGreaterThenId());

		String jobName = SolrSubjectTask.jobName;
		QuartzManagerUtil.start();
		// 更新任务时间
		String submit = search.getSubmit();

		switch (TaskStatus.toTaskStatus(submit)) {
		case START:
			// 添加重复任务，设置属性 1秒间隔，执行一次，立即开始
			QuartzManagerUtil.addSimpleJob(jobName, SolrSubjectViewTask.class,
					new Date());
			break;
		case PAUSE:
			QuartzManagerUtil.pauseCronJob(jobName);
			break;
		case CONTINUE:
			QuartzManagerUtil.resumeJob(jobName);
			break;
		case STOP:
			QuartzManagerUtil.removeCronJob(jobName);
			break;

		default:
			break;
		}

		ScheduleJob job = QuartzManagerUtil.getScheduleJob(jobName);
		// 最后solr新闻
		SubjectViewModel lastModel = SolrViewUtil.getLastModel();
		SdViewSubject lastViewSubject = viewSubjectService.getLastSubject();
		map.put("lastViewSubject", lastViewSubject);
		map.put("lastModel", lastModel);
		long solrCount = SolrViewUtil.getCount(SubjectViewModel.CORE_NAME);
		map.put("solrCount", solrCount);
		map.put("job", job);
		map.put("count", SolrSubjectViewTask.getCount());
		map.put("search", search);
		return forward("/admin/admin-tasks/solr-subject.ftl");
	}
}