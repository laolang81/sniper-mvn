package com.sniper.springmvc.scheduler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.sniper.springmvc.utils.PathUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 数据库备份到文件
 * 
 * @author sniper
 * 
 */
public class BackDBToFile implements Job {

	// mysqldump -hhostname -uusername -ppassword databasename | gzip >
	// backupfile.sql.gz
	public static String defaultCron = "0 0 23 * * ?";
	public static String jobName = "数据库备份任务";
	public static String defaultPath;
	static {
		defaultPath = PathUtil.getWebRoot() + "sniper/dbback/";
	}

	/**
	 * 数据源
	 */
	private static ComboPooledDataSource dataSource;
	/**
	 * 备份路径
	 */
	private static String backToFilePath;
	/**
	 * 备份文件限制数量
	 */
	private static int fileCount = 30;

	public static void setBackToFilePath(String backToFilePath) {
		if (!backToFilePath.endsWith("/")) {
			backToFilePath = backToFilePath + "/";
		}
		BackDBToFile.backToFilePath = backToFilePath;
	}

	public static String getBackToFilePath() {
		if (!ValidateUtil.isValid(BackDBToFile.backToFilePath)) {
			backToFilePath = defaultPath;
		}
		return backToFilePath;
	}

	public static void setDataSource(ComboPooledDataSource dataSource) {
		BackDBToFile.dataSource = dataSource;
	}

	public static void setFileCount(int fileCount) {
		BackDBToFile.fileCount = fileCount;
	}

	public static int getFileCount() {
		return fileCount;
	}

	public static ComboPooledDataSource getDataSource() {
		return dataSource;
	}

	public static File[] getFiles() {
		if (ValidateUtil.isValid(getBackToFilePath())) {
			File file = new File(BackDBToFile.getBackToFilePath());
			File[] files = file.listFiles();
			if (files.length > 0) {
				for (File file2 : files) {
					file2.length();
				}
			}

			return files;
		}

		return null;

	}

	public void init() {

		String url = dataSource.getJdbcUrl();
		String host = url.substring(url.indexOf("//") + 2, url.lastIndexOf("/"));
		String ip = host.substring(0, host.indexOf(":"));
		String db = url.substring(url.lastIndexOf("/") + 1, url.indexOf("?"));
		String port = host.substring(host.indexOf(":") + 1);
		String user = dataSource.getUser();
		String pwd = dataSource.getPassword();

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		File filedir = new File(getBackToFilePath());
		if (!filedir.isDirectory()) {
			filedir.mkdirs();
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("mysqldump -h");
		buffer.append(ip);
		buffer.append(" -p");
		buffer.append(port);
		buffer.append(" -u");
		buffer.append(user);
		buffer.append(" -p");
		buffer.append(pwd);
		buffer.append(" ");
		buffer.append(db);
		buffer.append(" | gzip > ");
		buffer.append(getBackToFilePath());
		buffer.append(dateFormat.format(date));
		buffer.append(".sql.gz");
		try {
			// System.out.println(buffer.toString());
			Runtime.getRuntime().exec(new String[] { "sh", "-c", buffer.toString() });
		} catch (IOException e) {
			e.printStackTrace();
		}

		File file = new File(getBackToFilePath());
		if (file.isDirectory()) {
			if (file.listFiles().length > getFileCount()) {
				// 删除多余的文件
				// 根据文件名称排序,
				File[] files = file.listFiles();
				int fileLength = files.length;
				int fileDiff = fileLength - getFileCount();
				// 正序删除,删除排名前几个的
				for (int i = 0; i < fileLength; i++) {
					if (i <= fileDiff) {
						files[i].delete();
					}
				}
			}
		}
	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {

		this.init();

		// JobKey key = context.getJobDetail().getKey();
		// JobDataMap dataMap = context.getJobDetail().getJobDataMap();

	}

}
