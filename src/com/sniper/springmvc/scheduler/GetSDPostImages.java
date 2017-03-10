package com.sniper.springmvc.scheduler;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sniper.springmvc.freemarker.ImageHelpUtil;
import com.sniper.springmvc.model.AdminUser;
import com.sniper.springmvc.model.Files;
import com.sniper.springmvc.mybatis.service.impl.FilesService;
import com.sniper.springmvc.security.SpringContextUtil;
import com.sniper.springmvc.utils.FilesUtil;
import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 商务网新闻拉取
 * 线程说明Thread一个是多个线程分别完成自己的任务，Runnable一个是多个线程共同完成一个任务。
 * @author sniper
 * 
 */
public class GetSDPostImages implements Job {

	public final static Logger LOGGER = LoggerFactory
			.getLogger(GetSDPostImages.class);

	public static String defaultCron = "0 0 23 * * ?";
	public static String jobName = "商务网新闻图片拉取";
	private String webSite = "http://shandongbusiness.gov.cn";
	/**
	 * 提交基本地址
	 */

	private String postBaseUrl = webSite + "/apii/sendPostImages/";

	private FilesService filesService;

	private int sid = 0;

	private String startDate;

	private String endDate;

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getStartDate() {
		return startDate;
	}

	public FilesService getFilesService() {
		if (filesService == null) {
			filesService = (FilesService) SpringContextUtil
					.getBean(FilesService.class);
		}
		return filesService;
	}

	public void setSid(int sid) {
		this.sid = sid;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * 数据不共享
	 * 
	 * @author sniper
	 * 
	 */
	class WriteImagesThread extends Thread {

		private String url;
		private Files files;
		private FilesService filesService;

		public WriteImagesThread() {
			this.setDaemon(true);
		}

		public WriteImagesThread(String url) {
			// 设置后台模式 ，否则 tomcat可能关不掉
			this.url = webSite + url;
			this.setDaemon(true);
		}

		public void setFiles(Files files) {
			this.files = files;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public void setFilesService(FilesService filesService) {
			this.filesService = filesService;
		}

		@Override
		public void run() {
			try {
				String filePath = FilesUtil.getSaveDir("image")
						+ getSaveFilename(this.url, files);
				String savePath = FilesUtil.getRootDir() + filePath;
				// 获取来源图片名称
				String sourcePathName = this.url.substring(
						this.url.lastIndexOf("/") + 1,
						this.url.lastIndexOf("."));
				files.setSourcePath(sourcePathName);
				File parentDir = new File(savePath);
				if (!parentDir.getParentFile().isDirectory()) {
					parentDir.getParentFile().mkdirs();
				}
				AdminUser adminUser = new AdminUser();
				adminUser.setId("1");
				files.setAdminUser(adminUser);
				files.setNewPath(filePath);
				// 两种远程保存图片的方法
				// saveResponseImage(this.url, savePath);
				if (readResponseAsInstream(this.url, savePath)) {
					// 生成2张缩略图
					if (filesService.getFileBySourcePath(sourcePathName) == null) {
						ImageHelpUtil.thumb(savePath, 310, 1095);
						ImageHelpUtil.thumb(savePath, 1024, 10950);
						filesService.insert(files);
					}
				}

			} catch (IOException e) {
				LOGGER.error("任务操作失败:" + e.getMessage());
				e.printStackTrace();
			}

		}
	}

	/**
	 * 把图片写入本地并保存 数据是可以共享的
	 * 
	 * @author sniper
	 * 
	 */
	class WriteImagesRunnable implements Runnable {

		@Override
		public void run() {

		}

	}

	/**
	 * 生成文件问名称
	 * 
	 * @return
	 */
	protected String getSaveFilename(String imgUrl, Files files) {
		String fileExt = imgUrl.substring(imgUrl.lastIndexOf(".") + 1)
				.toLowerCase();
		files.setSuffix(fileExt);
		String newFileName = FilesUtil.getUUIDName(fileExt, false);
		return newFileName;
	}

	/**
	 * 读取
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected String readResponseAsString(String url)
			throws ClientProtocolException, IOException {
		String result = "";
		BufferedReader in = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {

			HttpGet httpget = new HttpGet(url);
			CloseableHttpResponse response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() != 200) {
				return "";
			}
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream instream = entity.getContent();
					try {
						in = new BufferedReader(new InputStreamReader(instream));
						String line;
						while ((line = in.readLine()) != null) {
							result += line;
						}
						return result;
					} catch (IOException ex) {
						throw ex;
					} finally {
						instream.close();
					}
				}
			} finally {
				response.close();
			}
		} finally {
			httpclient.close();
		}

		return "";
	}

	/**
	 * 把远程图片保存到本地
	 * 
	 * @param fileUrl
	 * @param savePath
	 * @throws IOException
	 */
	protected boolean saveResponseImage(String fileUrl, String savePath)
			throws IOException {

		URL url = new URL(fileUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if (conn.getResponseCode() != 200) {
			LOGGER.info("文件不存在:" + fileUrl);
			return false;
		}
		conn.setRequestMethod("GET");
		DataInputStream in = new DataInputStream(conn.getInputStream());
		DataOutputStream out = new DataOutputStream(new FileOutputStream(
				savePath));
		byte[] buffer = new byte[4096];
		int count = 0;
		while ((count = in.read(buffer)) > 0) {
			out.write(buffer, 0, count);
		}
		out.close();
		in.close();
		conn.disconnect();
		return true;
	}

	/**
	 * 读取
	 * 
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	protected boolean readResponseAsInstream(String url, String savePath)
			throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();

		try {
			HttpGet httpget = new HttpGet(url);

			CloseableHttpResponse response = httpclient.execute(httpget);
			if (response.getStatusLine().getStatusCode() != 200) {
				return false;
			}
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream is = entity.getContent();
					FileOutputStream out = new FileOutputStream(savePath);
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						out.write(buffer, 0, len);
					}
					out.close();
					is.close();
					return true;
				}
			} catch (IOException e) {
				LOGGER.error("文件保存错误信息：" + e.getMessage());
			} finally {
				response.close();
			}
		} catch (IOException e) {
			LOGGER.error("文件拉取错误信息：" + e.getMessage());
		} finally {
			httpclient.close();
		}

		return true;
	}

	/**
	 * 任务具体执行
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws ParseException
	 */
	public void init() throws ClientProtocolException, IOException,
			ParseException {

		this.postBaseUrl += sid + "/";

		if (ValidateUtil.isValid(getStartDate())) {
			this.postBaseUrl += getStartDate() + "/";
		} else {
			this.postBaseUrl += "0/";
		}

		if (ValidateUtil.isValid(getEndDate())) {
			this.postBaseUrl += getEndDate();
		}
		System.out.println(this.postBaseUrl);
		LOGGER.info("操作地址:" + this.postBaseUrl);
		String response = this.readResponseAsString(postBaseUrl);
		if (response.length() <= 10) {
			return;
		}

		Set<Files> lists = new HashSet<>();

		ObjectMapper json = new ObjectMapper();
		@SuppressWarnings("unchecked")
		Map<String, SDPostImages> results = json.readValue(response, Map.class);

		for (Map.Entry<String, SDPostImages> entry : results.entrySet()) {

			@SuppressWarnings("unchecked")
			Map<String, Object> value1 = (Map<String, Object>) entry.getValue();
			Files files = new Files();
			for (Map.Entry<String, Object> entry2 : value1.entrySet()) {

				String weburl = "";
				Date dateTime;
				switch (entry2.getKey()) {
				case "subject":
					files.setOldName(String.valueOf(entry2.getValue()));
					break;
				case "weburl":
					weburl = String.valueOf(entry2.getValue());
					files.setPostUrl(weburl);
					break;
				case "dateTime":
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					dateTime = format.parse(String.valueOf(entry2.getValue()));
					files.setEventTimeDate(dateTime);
					break;
				default:
					break;
				}

				if (entry2.getKey().equals("attachment")) {

					@SuppressWarnings("unchecked")
					List<Map<String, String>> value2 = (List<Map<String, String>>) entry2
							.getValue();

					for (Map<String, String> map : value2) {

						for (Map.Entry<String, String> entry3 : map.entrySet()) {
							String url = "";
							switch (entry3.getKey()) {
							case "filetype":
								files.setFileType(String.valueOf(entry3
										.getValue()));
								break;
							case "filename":
								url = String.valueOf(entry3.getValue());
								Files urlFiles = new Files();
								urlFiles.setNewPath(url);
								urlFiles.setOldName(files.getOldName());
								urlFiles.setFileType(files.getFileType());
								urlFiles.setEventTimeDate(files
										.getEventTimeDate());
								urlFiles.setPostUrl(files.getPostUrl());
								lists.add(urlFiles);
								break;
							}
						}
					}
				}
			}
		}

		// 用任务尺执行
		ExecutorService executor = Executors.newCachedThreadPool();
		for (Files files2 : lists) {
			WriteImagesThread thread = new WriteImagesThread(
					files2.getNewPath());
			thread.setFiles(files2);
			thread.setFilesService(getFilesService());
			executor.execute(thread);
		}
		executor.shutdown();

	}

	@Override
	public void execute(JobExecutionContext job) throws JobExecutionException {

		try {
			this.init();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws ClientProtocolException,
			IOException, ParseException {
		GetSDPostImages images = new GetSDPostImages();
		images.setSid(0);
		images.setStartDate("2015-07-10");
		images.init();
	}

}