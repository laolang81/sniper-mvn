package com.sniper.springmvc.action.junit;

import org.junit.Test;

import com.sniper.springmvc.mybatis.service.impl.LogSmbServiceImpl;
import com.sniper.springmvc.utils.SmbUtils;

public class SmbTest {

	@Test
	public void putDirTest() throws Exception {
		// 有2种方式，一种默认调用方式，一种线程方法
		// 线程方式
		SmbUtils smb = SmbUtils.getInstance();
		smb.setLogSmbService(new LogSmbServiceImpl());
		smb.setEnbaleFileConfig(false);
		SmbUtils.Smb smb2 = smb.new Smb(SmbUtils.SMB_PUT_DIR,
				new Object[] { "aa/" });
		smb2.run();
		// 默认方式
		smb.smbPutDir("aas");

		smb.smbDelDir("aas/");
	}

	// 远程url smb://192.168.0.77/test
	// 如果需要用户名密码就这样：
	// smb://username:password@192.168.0.77/test

	@Test
	public void test() throws Exception {
		SmbUtils smb = SmbUtils.getInstance();
		System.out.println(smb.getBaseUrl());
		smb.setLogSmbService(new LogSmbServiceImpl());
		smb.setEnbaleFileConfig(false);
		SmbUtils.Smb smb2 = smb.new Smb(SmbUtils.SMB_PUT_FILE, new Object[] {
				"/Users/suzhen/Downloads/joda-time-2.9.7.jar", "aa/" });
		smb2.run();
		System.out.println(smb2.getResult());

	}
}
