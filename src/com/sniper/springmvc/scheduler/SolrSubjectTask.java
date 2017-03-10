package com.sniper.springmvc.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.solr.client.solrj.response.UpdateResponse;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.model.SdItems;
import com.sniper.springmvc.model.SdSubjects;
import com.sniper.springmvc.mybatis.service.impl.SdDepartmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdItemsService;
/**
 * 并行框架
 */
import com.sniper.springmvc.mybatis.service.impl.SdSubjectsService;
import com.sniper.springmvc.security.SpringContextUtil;
import com.sniper.springmvc.solr.SolrUtil;
import com.sniper.springmvc.solr.SubjectModel;

/**
 * Solr新闻导入 http://blog.sina.com.cn/s/blog_145f07e7b0102x3dy.html
 * 
 * @author suzhen
 * 
 */
public class SolrSubjectTask implements Job {

	public static String jobName = "Solr初始化";
	/**
	 * 数据库操作
	 */
	private SdSubjectsService subjectsService;
	private SdDepartmentsService departmentsService;
	private SdItemsService itemsService;
	/**
	 * 所有数据必须设置为静态的，因为任务调度变量使用新的类
	 */
	// 新闻最低值
	private static int greaterThenId = 0;
	private static int pageSize = 2000;
	private static int pageOffset = 0;
	// 处理的最大记录数
	private static int maxCount = 0;
	private static int maxThreadCount = 100;

	// 规定每隔线程最大执行时间超时强制关闭
	final long awaitTime = 5 * 1000;

	public SdSubjectsService getSubjectsService() {
		if (subjectsService == null) {
			subjectsService = (SdSubjectsService) SpringContextUtil
					.getBean(SdSubjectsService.class);
		}
		return subjectsService;
	}

	public SdDepartmentsService getDepartmentsService() {
		if (departmentsService == null) {
			departmentsService = (SdDepartmentsService) SpringContextUtil
					.getBean(SdDepartmentsService.class);
		}
		return departmentsService;
	}

	public SdItemsService getItemsService() {
		if (itemsService == null) {
			itemsService = (SdItemsService) SpringContextUtil
					.getBean(SdItemsService.class);
		}
		return itemsService;
	}

	public static void setGreaterThenId(int greaterThenId) {
		SolrSubjectTask.greaterThenId = greaterThenId;
	}

	public static void setMaxCount(int maxCount) {
		SolrSubjectTask.maxCount = maxCount;
	}

	public static void setPageSize(int pageSize) {
		SolrSubjectTask.pageSize = pageSize;
	}

	public static void setMaxThreadCount(int maxThreadCount) {
		SolrSubjectTask.maxThreadCount = maxThreadCount;
	}

	public static void setPageOffset(int pageOffset) {
		SolrSubjectTask.pageOffset = pageOffset;
	}

	/**
	 * 具体执行的线程 http://www.cnblogs.com/wanqieddy/p/3853863.html
	 * 
	 * @author suzhen
	 * 
	 */
	public class SolrSubjectThread extends Thread {

		private UpdateResponse result;

		List<SdSubjects> subjects = new ArrayList<>();

		public SolrSubjectThread(List<SdSubjects> subjects) {
			this.subjects = subjects;
			// 设为守护进程,守护进程随着虚拟机退出退出，非守护只有执行完毕之后才推出
			this.setDaemon(true);
		}

		public UpdateResponse getResult() {
			return result;
		}

		@Override
		public void run() {
			// 这里的subject只包含基本信息不包含我们想要的很多信息
			for (SdSubjects subject : subjects) {
				subject = getSubjectsService().get(subject.getSid().toString());
				if (subject == null) {
					continue;
				}
				SdDepartments departments = getDepartmentsService().get(
						subject.getSiteid().toString());
				SdItems items = getItemsService().get(
						subject.getItemid().toString());
				SubjectModel model = new SubjectModel();
				if (departments == null || items == null) {
					model = SolrUtil.getModel(subject);
				} else {
					model = SolrUtil.getModel(subject, departments, items);
				}
				result = SolrUtil.insert(model);
			}
			super.run();
		}
	}

	/**
	 * 获取新闻数量
	 * 
	 * @return
	 */
	public int getCount() {
		Map<String, Object> params = new HashMap<>();
		params.put("enabled", 2);
		return getSubjectsService().pageCount(params);
	}

	public List<SdSubjects> getSubjects(Map<String, Object> params) {
		return getSubjectsService().pageList(params);
	}
	/**
	 * 
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		Map<String, Object> params = new HashMap<>();
		params.put("enabled", 2);
		if (greaterThenId > 0) {
			params.put("greaterThenId", greaterThenId);
		}
		int count = getSubjectsService().pageCount(params);
		if (maxCount > 0 && count > maxCount) {
			count = maxCount;
		}
		params.put("order", "s.sid desc");
		ExecutorService executor = Executors.newFixedThreadPool(maxThreadCount);
		// 计算一共多少页
		int pageCount = (int) Math.ceil(count / pageSize);
		if (count % pageSize > 0) {
			pageCount += 1;
		}
		// 读取每一页的数据
		for (int i = 0; i < pageCount; i++) {
			params.put("pageOffset", pageOffset);
			params.put("pageSize", pageSize);
			// 为下一页做准备
			pageOffset += pageSize;
			List<SdSubjects> subjects = getSubjectsService().pageList(params);
			SolrSubjectThread solrSubjectThread = new SolrSubjectThread(
					subjects);
			executor.execute(solrSubjectThread);
		}
	}
}