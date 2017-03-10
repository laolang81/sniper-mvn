package com.sniper.springmvc.junit;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

public class LoginTest {

	@Test
	public void login() {
		// 最终用户登录必须是新版
		String shaPassword = DigestUtils.sha1Hex("admin");
		UsernamePasswordToken token = new UsernamePasswordToken("admin", shaPassword);
		// 获取当前的Subject
		Subject currentUser = SecurityUtils.getSubject();
		try {
			// 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
			// 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
			// 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
			// System.out.println("对用户[" + username + "]进行登录验证..验证开始");
			currentUser.login(token);
			// System.out.println("对用户[" + username + "]进行登录验证..验证通过");
		} catch (UnknownAccountException uae) {
			// System.out.println("对用户[" + username + "]进行登录验证..验证未通过,未知账户");
		} catch (IncorrectCredentialsException ice) {
			// System.out.println("对用户[" + username + "]进行登录验证..验证未通过,错误的凭证");
		} catch (LockedAccountException lae) {
			// System.out.println("对用户[" + username + "]进行登录验证..验证未通过,账户已锁定");
		} catch (ExcessiveAttemptsException eae) {
			// System.out.println("对用户[" + username + "]进行登录验证..验证未通过,错误次数过多");
		}

		// 验证是否登录成功
		if (currentUser.isAuthenticated()) {

		} else {
			token.clear();

		}
	}
}
