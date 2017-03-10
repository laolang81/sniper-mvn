package com.sniper.springmvc.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Map;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sniper.springmvc.model.LogSmb;
import com.sniper.springmvc.mybatis.service.impl.LogSmbService;

/**
 * ENABLE_FILE_CONFIG false表示不适用配置文件直接使用下面设置的
 * "mount -t cifs //172.16.8.105/samba /approot/www/ROOT/public/ -o username=doftec,password=800613"
 * 发现如果把远程smb挂在到本地机器反应会很慢，特别是重启的时候，决定还是用smb发送文件吧
 * 
 * @author suzhen
 * 
 */
public class SmbUtils {

	private final static String SMB_CONFIG_FILE = "properties/smb.properties";
	// 是否使用配置文件
	private static boolean ENABLE_FILE_CONFIG = true;
	// 测试数据，挡上面的参数启用的时候配置文件为主
	private static String SMB_URL = "smb://doftec:800613@192.168.190.25/samba";
	// 是否开启使用
	private static boolean ENBALED = false;

	/**
	 * 保存到数据库
	 */
	private LogSmbService logSmbService = null;

	/**
	 * 创建文件夹
	 */
	public static String SMB_PUT_DIR = "smbPutDir";
	/**
	 * 上传文件
	 */
	public static String SMB_PUT_FILE = "smbPutFile";
	/**
	 * 下载文件
	 */
	public static String SMB_GET_FILE = "smbGetFile";
	/**
	 * 读取文件内容
	 */
	public static String SMB_GET_FILE_CONTENT = "smbGetFileContent";
	/**
	 * 删除文件
	 */
	public static String SMB_DEL_FILE = "smbDelFile";
	/**
	 * 删除文件夹
	 */
	public static String SMB_DEL_DIR = "smbDelDir";

	private SmbUtils() {
	}

	/**
	 * 日志
	 */
	private static Logger LOGGER = LoggerFactory.getLogger(SmbUtils.class);

	/**
	 * 
	 */
	private static SmbUtils SMB = null;

	/**
	 * 如果设置了此项,那么失败记录将会被记录
	 * 
	 * @return
	 */
	public void setLogSmbService(LogSmbService logSmbService) {
		this.logSmbService = logSmbService;
	}

	/**
	 * 初始化
	 * 
	 * @return
	 */
	public static SmbUtils getInstance() {
		if (SMB == null) {
			SMB = new SmbUtils();
			init();
		}
		return SMB;
	}

	public String getBaseUrl() {
		return SmbUtils.SMB_URL;
	}

	public boolean isEnabled() {
		return SmbUtils.ENBALED;
	}

	public void setEnbaleFileConfig(boolean enbaleFileConfig) {
		SmbUtils.ENABLE_FILE_CONFIG = enbaleFileConfig;
	}

	/**
	 * 获取smb地址
	 * 
	 * @return
	 */
	private static String init() {
		if (ENABLE_FILE_CONFIG == false) {
			// TRUE表示读取配置文件，否表示直接使用默认值
			return SMB_URL;
		}
		InputStream in = SmbUtils.class.getClassLoader().getResourceAsStream(
				SMB_CONFIG_FILE);
		PropertiesUtil pu = null;
		pu = new PropertiesUtil(in);
		Map<String, String> map = pu.getValues();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			switch (entry.getKey()) {
			case "smbUrl":
				SMB_URL = entry.getValue();
				break;
			case "enabled":
				if (entry.getValue().equals("1")) {
					ENBALED = true;
				} else {
					ENBALED = false;
				}
				break;
			default:
				break;
			}
		}
		return SMB_URL;
	}

	/**
	 * 远程创建目录
	 * 
	 * @param dir
	 * @throws Exception
	 */
	public void smbPutDir(String dir) throws Exception {
		dir = addSuffixSlash(dir);
		dir = addPrefixSlash(dir);
		SmbFile fp = new SmbFile(SMB_URL + dir);
		LogSmb logSmb = new LogSmb();
		logSmb.setId(FilesUtil.getUUIDName("", false));
		logSmb.setRemoteFile(fp.getCanonicalPath());
		logSmb.setType(LogSmbService.PUT_DIR);
		// 目录已存在创建文件夹
		if (!fp.exists() || !fp.isDirectory()) {
			// 目录不存在的情况下，会抛出异常
			try {
				fp.mkdirs();
				logSmb.setSuccess(true);
			} catch (Exception e) {
				logSmb.setSuccess(false);
				e.printStackTrace();
			}
		}
	}

	/**
	 * 上传文件，上传文件失败，应该做数据库记录的，一遍手工传送
	 * 
	 * @param localFilePath
	 *            本地文件地址
	 * @param remoteDir
	 *            远程目录
	 */
	public void smbPutFile(String localFilePath, String remoteDir) {

		InputStream in = null;
		OutputStream out = null;
		LogSmb logSmb = new LogSmb();
		logSmb.setId(FilesUtil.UUID());
		try {
			// 本地文件
			File localFile = new File(localFilePath);
			if (!localFile.exists()) {
				logSmb.setSuccess(false);
				logSmb.setNote("本地文件不存在");
				return;
			}
			// 本地文件名字
			String fileName = localFile.getName();
			// 读取远程文件
			String url = SMB_URL + addPrefixSlash(remoteDir)
					+ addPrefixSlash(fileName);
			// 创建远程目录
			smbPutDir(addPrefixSlash(remoteDir));
			SmbFile remoteFile = new SmbFile(url);
			logSmb.setLocalFile(localFile.getCanonicalPath());
			logSmb.setRemoteFile(remoteFile.getCanonicalPath());
			logSmb.setType(LogSmbService.PUT_FILE);
			in = new BufferedInputStream(new FileInputStream(localFile));
			out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));
			// 查看远程是否有重名的

			byte[] buffer = new byte[1024];
			int len;
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
			logSmb.setSuccess(true);
		} catch (Exception e) {
			logSmb.setSuccess(false);
			LOGGER.error("smb上传文件出错本地文件", e);
			if (logSmbService != null) {
				logSmbService.insert(logSmb);
			}
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 给目录添加前缀
	 * 
	 * @return
	 */
	private String addPrefixSlash(String name) {
		if (!name.startsWith("/")) {
			name = "/" + name;
		}
		return name;
	}

	private String addSuffixSlash(String name) {
		if (!name.endsWith("/")) {
			name += "/";
		}
		return name;
	}

	/**
	 * 下载文件
	 * 
	 * @param remoteUrl
	 * @param localDir
	 */
	public void smbGetFile(String remoteUrl, String localDir) {
		InputStream in = null;
		OutputStream out = null;
		try {
			SmbFile remoteFile = new SmbFile(SMB_URL
					+ addPrefixSlash(remoteUrl));
			String fileName = remoteFile.getName();
			File localFile = new File(localDir + File.separator + fileName);
			in = new BufferedInputStream(new SmbFileInputStream(remoteFile));

			out = new BufferedOutputStream(new FileOutputStream(localFile));
			byte[] buffer = new byte[1024];
			while (in.read(buffer) != -1) {
				out.write(buffer);
				buffer = new byte[1024];
			}
		} catch (Exception e) {
			LOGGER.error("smb下载文件", e);
			e.printStackTrace();
		} finally {
			try {
				out.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 判断远程文件是否存在
	 * 
	 * @param remoteUrl
	 * @param localDir
	 * @return
	 */
	public boolean smbIsFile(String remoteUrl) {
		try {
			SmbFile remoteFile = new SmbFile(SMB_URL
					+ addPrefixSlash(remoteUrl));
			if (remoteFile.isFile()) {
				return true;
			}
		} catch (MalformedURLException | SmbException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean smbIsDirF(String remoteUrl) {
		try {
			SmbFile remoteFile = new SmbFile(SMB_URL
					+ addPrefixSlash(remoteUrl));
			if (remoteFile.isDirectory()) {
				return true;
			}
		} catch (MalformedURLException | SmbException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 读取文件内容
	 * 
	 * @param remoteUrl
	 * @throws IOException
	 */
	public String smbGetFileContent(String remoteUrl) {
		StringBuffer result = new StringBuffer();
		try {

			SmbFile smbFile = new SmbFile(SMB_URL + addPrefixSlash(remoteUrl));
			SmbFileInputStream in;
			try {
				in = new SmbFileInputStream(smbFile);
				byte buffer[] = new byte[1024];
				int length = 0;
				try {
					while ((length = in.read(buffer)) != -1) {
						String a = new String(buffer, 0, length);
						result.append(a);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

			} catch (SmbException | MalformedURLException
					| UnknownHostException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return result.toString();
	}

	/**
	 * 删除文件
	 * 
	 * @param remoteFile
	 * @throws MalformedURLException
	 * @throws SmbException
	 */
	public LogSmb smbDelFile(String remoteFilePath) {
		LogSmb logSmb = new LogSmb();
		logSmb.setId(FilesUtil.getUUIDName("", false));
		logSmb.setType(LogSmbService.DEL_FILE);
		String path = SMB_URL + addPrefixSlash(remoteFilePath);

		try {
			SmbFile fp = new SmbFile(path);
			logSmb.setRemoteFile(fp.getCanonicalPath());
			// 目录已存在创建文件夹
			if (fp.exists() && fp.isFile()) {
				// 目录不存在的情况下，会抛出异常
				try {
					fp.delete();
					logSmb.setSuccess(true);
				} catch (Exception e) {
					logSmb.setNote("删除失败:" + e.getMessage());
					logSmb.setSuccess(false);
					e.printStackTrace();
				}
			}
		} catch (MalformedURLException | SmbException e1) {
			logSmb.setSuccess(false);
			logSmb.setNote("远程地址错误:" + e1.getMessage());
			if (logSmbService != null) {
				logSmbService.insert(logSmb);
			}
			e1.printStackTrace();
		}
		return logSmb;
	}

	/**
	 * 删除目录
	 * 
	 * @param remoteDir
	 *            目录结尾必须 / 结束
	 * @throws MalformedURLException
	 * @throws SmbException
	 */
	public void smbDelDir(String remoteDir) throws MalformedURLException,
			SmbException {

		SmbFile fp = new SmbFile(SMB_URL + addPrefixSlash(remoteDir));
		// 目录已存在创建文件夹
		if (fp.exists() && fp.isDirectory()) {
			// 目录不存在的情况下，会抛出异常
			fp.delete();
		}
	}

	/**
	 * 传入一个文件地址，返回其文件夹
	 * 
	 * @param path
	 * @return
	 */
	public String getDir(String path) {
		if (!ValidateUtil.isValid(path)) {
			return "";
		}
		int a = path.lastIndexOf("/");

		return path.substring(0, a);
	}

	/**
	 * 使用线程调用程序脱离主程序
	 * 
	 * @param methodName
	 *            方法名称
	 * @param objects
	 *            参数 要和类型对应，一个类型对应一个参数
	 * @return
	 */

	public Smb getSmb(String methodName, Object[] objects) {
		return new Smb(methodName, objects);
	}

	/**
	 * 线程调用上面的方法,这个模式数据是可以共享的
	 * 
	 * @author suzhen
	 * 
	 */
	public class Smb implements Runnable {

		private SmbUtils smb;
		private String methodName;
		private Class<?>[] classs = new Class[] {};
		private Object[] objects = new Object[] {};
		private String result;

		public Smb(String methodName, Object[] objects) {
			super();
			this.smb = SmbUtils.getInstance();
			this.methodName = methodName;
			this.objects = objects;
			this.classs = new Class[objects.length];
			for (int i = 0; i < objects.length; i++) {
				this.classs[i] = objects[i].getClass();
			}
		}

		public String getResult() {
			return result;
		}

		@Override
		public void run() {
			try {
				Method method = smb.getClass().getDeclaredMethod(methodName,
						classs);
				method.setAccessible(true);
				if (method.getReturnType() == String.class) {
					result = (String) method.invoke(smb, objects);
				} else {
					method.invoke(smb, objects);
				}
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				e.printStackTrace();
			}
		}
	}
}
