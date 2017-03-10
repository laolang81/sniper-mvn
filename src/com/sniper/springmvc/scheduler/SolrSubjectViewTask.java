package com.sniper.springmvc.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.SdDepartments;
import com.sniper.springmvc.model.SdItems;
import com.sniper.springmvc.model.SdSubjects;
import com.sniper.springmvc.mybatis.service.impl.AdminUserService;
import com.sniper.springmvc.mybatis.service.impl.SdDepartmentsService;
import com.sniper.springmvc.mybatis.service.impl.SdItemsService;
import com.sniper.springmvc.mybatis.service.impl.SdItemsSubjectsService;
/**
 * 并行框架
 */
import com.sniper.springmvc.mybatis.service.impl.SdSubjectsService;
import com.sniper.springmvc.security.SpringContextUtil;
import com.sniper.springmvc.solr.SolrViewUtil;
import com.sniper.springmvc.solr.SubjectViewModel;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * Solr新闻导入 http://blog.sina.com.cn/s/blog_145f07e7b0102x3dy.html
 * 
 * @author suzhen
 * 
 */
public class SolrSubjectViewTask implements Job {

	/**
	 * 新闻读取状态设置
	 */
	// 只读取
	private static String ENABLED_EQ;
	public static String jobName = "Solr初始化";
	/**
	 * 数据库操作
	 */
	private static SdSubjectsService subjectsService;
	static {
		if (subjectsService == null) {
			subjectsService = (SdSubjectsService) SpringContextUtil
					.getBean(SdSubjectsService.class);
		}
	}

	private static AdminUserService adminUserService;
	static {
		if (adminUserService == null) {
			adminUserService = (AdminUserService) SpringContextUtil
					.getBean(AdminUserService.class);
		}
	}
	private static SdDepartmentsService departmentsService;
	static {
		if (departmentsService == null) {
			departmentsService = (SdDepartmentsService) SpringContextUtil
					.getBean(SdDepartmentsService.class);
		}
	}
	private static SdItemsService itemsService;
	static {
		if (itemsService == null) {
			itemsService = (SdItemsService) SpringContextUtil
					.getBean(SdItemsService.class);
		}
	}
	private static SdItemsSubjectsService itemsSubjectsService;
	static {
		if (itemsSubjectsService == null) {
			itemsSubjectsService = (SdItemsSubjectsService) SpringContextUtil
					.getBean(SdItemsSubjectsService.class);
		}
	}
	/**
	 * 所有数据必须设置为静态的，因为任务调度变量使用新的类
	 */
	// 新闻最低值
	private static int greaterThenId = 0;
	// 每隔线程处理的数据大小,每次处理的数据大小等一线程数*单线程处理新闻数
	private static int pageSize = 5000;
	private static int pageOffset = 0;
	// 处理的最大记录数
	private static int maxCount = 0;
	private static int maxThreadCount = 400;

	// 规定每隔线程最大执行时间超时强制关闭,10分钟
	final long awaitTime = 10 * 1000;

	public static int getGreaterThenId() {
		return greaterThenId;
	}

	public static void setGreaterThenId(int greaterThenId) {
		SolrSubjectViewTask.greaterThenId = greaterThenId;
	}

	public static void setMaxCount(int maxCount) {
		SolrSubjectViewTask.maxCount = maxCount;
	}

	public static void setPageSize(int pageSize) {
		SolrSubjectViewTask.pageSize = pageSize;
	}

	public static void setMaxThreadCount(int maxThreadCount) {
		SolrSubjectViewTask.maxThreadCount = maxThreadCount;
	}

	public static void setPageOffset(int pageOffset) {
		SolrSubjectViewTask.pageOffset = pageOffset;
	}

	public static void setENABLED_EQ(String eNABLED_EQ) {
		ENABLED_EQ = eNABLED_EQ;
	}

	public static String getENABLED_EQ() {
		return ENABLED_EQ;
	}

	/**
	 * 具体执行的线程 http://www.cnblogs.com/wanqieddy/p/3853863.html
	 * 
	 * @author suzhen
	 * 
	 */
	public class SolrSubjectThread extends Thread {

		List<SdSubjects> subjects = new ArrayList<>();

		public SolrSubjectThread(List<SdSubjects> subjects) {
			this.subjects = subjects;
			// 设为守护进程,守护进程随着虚拟机退出退出，非守护只有执行完毕之后才推出
			this.setDaemon(true);
		}

		@Override
		public void run() {
			// 这里的subject只包含基本信息不包含我们想要的很多信息
			for (SdSubjects subject : subjects) {
				subject = subjectsService.get(subject.getSid().toString());
				if (subject == null) {
					continue;
				}
				SdDepartments departments = departmentsService.get(subject
						.getSiteid().toString());
				SdItems items = itemsService.get("getItem", subject.getItemid()
						.toString());
				// 获取栏目
				Integer[] itids = itemsSubjectsService.getItems(subject
						.getSid().toString());
				// 获取用户信息
				AdminUser adminUser = adminUserService.getUser(subject
						.getAuthorid());
				if (ValidateUtil.isValid(adminUser)) {
					subject.setAuthorid(adminUser.getName());
				}
				// 审核人
				if (ValidateUtil.isValid(subject.getAuditUid())) {
					AdminUser adminUser1 = adminUserService.getUser(subject
							.getAuditUid());
					if (adminUser1 != null) {
						subject.setAuditUid(adminUser1.getName());
					}
				}
				SubjectViewModel model = SolrViewUtil.getModel(subject,
						departments, items, itids);

				SolrViewUtil.delete(model.getId());
				SolrViewUtil.insert(model);
			}
			super.run();
		}
	}

	/**
	 * 获取新闻数量
	 * 
	 * @return
	 */
	public static int getCount() {
		Map<String, Object> params = new HashMap<>();
		// params.put("enabledNotEq", ENABLED_NOT_EQ);
		return subjectsService.pageCount(params);
	}

	/**
	 * 
	 */
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		Map<String, Object> params = new HashMap<>();
		// 新闻状态设置
		if (ValidateUtil.isValid(ENABLED_EQ)) {
			params.put("enabled", ENABLED_EQ);
		}

		if (greaterThenId > 0) {
			params.put("greaterThenId", greaterThenId);
		}
		int count = subjectsService.pageCount(params);
		if (maxCount > 0 && count > maxCount) {
			count = maxCount;
		}
		params.put("order", "s.sid desc");

		// 计算一共多少页
		int pageCount = (int) Math.ceil(count / pageSize);
		if (count % pageSize > 0) {
			pageCount += 1;
		}
		if (maxThreadCount > pageCount) {
			maxThreadCount = pageCount;
		}
		// 设置最大线程池
		ExecutorService executor = Executors.newCachedThreadPool();
		// 读取每一页的数据
		for (int i = 0; i < pageCount; i++) {
			params.put("pageOffset", pageOffset);
			params.put("pageSize", pageSize);
			// 为下一页做准备
			pageOffset += pageSize;
			List<SdSubjects> subjects = subjectsService.pageList(params);
			SolrSubjectThread solrSubjectThread = new SolrSubjectThread(
					subjects);
			executor.execute(solrSubjectThread);
		}
		try {
			// 向学生传达“问题解答完毕后请举手示意！”
			executor.shutdown();
			// 向学生传达“XX分之内解答不完的问题全部带回去作为课后作业！”后老师等待学生答题
			// (所有的任务都结束的时候，返回TRUE)
			if (!executor.awaitTermination(awaitTime, TimeUnit.MILLISECONDS)) {
				// 超时的时候向线程池中所有的线程发出中断(interrupted)。
				executor.shutdownNow();
			}
		} catch (InterruptedException e) {
			// awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
			System.out.println("awaitTermination interrupted: " + e);
			executor.shutdownNow();
		}

	}
}