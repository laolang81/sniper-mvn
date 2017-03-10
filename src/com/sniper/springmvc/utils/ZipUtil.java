package com.sniper.springmvc.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import com.sniper.springmvc.model.SdAttachments;

public class ZipUtil {

	private List<SdAttachments> files = new ArrayList<>();
	private String rootDir = "";

	public void setFiles(List<SdAttachments> files) {
		this.files = files;
	}

	public ZipUtil addFile(SdAttachments files) {
		this.files.add(files);
		return this;
	}

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}

	/**
	 * 自定义下载名称
	 * 
	 * @param filePath
	 * @return
	 */
	public String zip(String filePath) {
		// 设置储存位置
		String rootPath = this.rootDir + "/attachments/zip/";
		File filedir = new File(rootPath);

		if (!filedir.isDirectory()) {
			filedir.mkdirs();
		}
		// 生成的ZIP文件名为Demo.zip
		String zipFilePath = rootPath + filePath;

		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipFilePath));
			// 需要同时下载的两个文件result.txt ，source.txt
			for (SdAttachments entry : files) {
				// 设置文件名称, 如果带有斜杠zip会认为这是一个文件夹
				String fName = URLEncoder.encode(entry.getPrefilename(),
						"UTF-8");
				// 设置名称
				String suffix = FilesUtil.getFileExt(entry.getFilename());
				if (fName.lastIndexOf(suffix) == -1) {
					fName = fName + "." + suffix;
				}

				out.putNextEntry(new ZipEntry(fName));
				// 插入对应的文件
				File file = new File(this.rootDir + entry.getFilename());
				FileInputStream fis = new FileInputStream(file);
				int len = 0;
				byte[] buffer = new byte[1024];
				// 读入需要下载的文件的内容，打包到zip文件
				while ((len = fis.read(buffer)) > 0) {
					out.write(buffer, 0, len);
				}
				out.setEncoding("utf-8");
				out.closeEntry();
				fis.close();

			}
			out.flush();
			out.close();

		} catch (Exception e) {
			System.out.println("错误: " + e.getMessage());
		}

		return zipFilePath;
	}

	public String zipData(String filePath) {
		// 设置储存位置
		String rootPath = this.rootDir + "/attachments/zip/";
		File filedir = new File(rootPath);

		if (!filedir.isDirectory()) {
			filedir.mkdirs();
		}
		// 生成的ZIP文件名为Demo.zip
		String zipFilePath = rootPath + filePath;

		File oldFile = new File(zipFilePath);
		if (oldFile.isFile()) {
			oldFile.delete();
		}

		try {
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					zipFilePath));

			// String encoding = System.getProperty("sun.jnu.encoding");
			// System.out.println(System.getProperty("sun.jnu.encoding"));
			String encoding = "UTF-8";
			out.setEncoding(encoding);
			// 需要同时下载的两个文件result.txt ，source.txt
			for (SdAttachments entry : files) {
				// 设置文件名称, 如果带有斜杠zip会认为这是一个文件夹
				// String fName = URLEncoder.encode(entry.getOldName(),
				// encoding);
				String fName = entry.getPrefilename();
				// 设置名称
				String suffix = FilesUtil.getFileExt(entry.getFilename());
				if (fName.lastIndexOf(suffix) == -1) {
					fName = fName + "." + suffix;
				}
				out.putNextEntry(new ZipEntry(entry.getAid() + "." + fName));
				// 插入对应的文件
				File file = new File(this.rootDir + entry.getFilename());
				DataInputStream dis = new DataInputStream(
						new BufferedInputStream(new FileInputStream(file)));
				int len = 0;
				while ((len = dis.read()) != -1) {
					out.write(len);
				}
				dis.close();
			}
			out.closeEntry();
			// out.flush();
			out.close();

		} catch (Exception e) {
			System.out.println("错误: " + e.getMessage());
		}

		return zipFilePath;
	}

	public static void main(String[] args) {

		String name = "中文";
		System.out.println(name.lastIndexOf("jpg"));
	}
}