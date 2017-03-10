package com.sniper.springmvc.advice;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;

import com.sniper.springmvc.model.Log;
import com.sniper.springmvc.mybatis.service.impl.LogService;
import com.sniper.springmvc.utils.StringUtil;

/**
 * 日志记录 pojos + xml 开发环绕通知 logger
 * 
 * @author laolang
 * 
 */
public class Logger {

	@Resource
	private LogService logService;

	/**
	 * 处理连接点 ProceedingJoinPoint 只允许在环绕通知
	 * 
	 * @param point
	 * @return
	 */
	public Object record(ProceedingJoinPoint point) {

		Log log = new Log();
		try {
			// 设置操作人
			
			log.setUser("");
			// 操作名称
			String mname = point.getSignature().getName();
			log.setName(mname);
			// 操作参数
			Object[] params = point.getArgs();

			log.setParams(StringUtil.arr2Str(params));
			// 调用操作目标
			Object ret = point.proceed();
			log.setResult("success");
			// 设置结果消息
			if (ret != null) {
				log.setResultMsg(ret.toString());
			}
			// 返回结果
			return ret;
		} catch (Throwable e) {
			// 保存错误信息
			log.setResult("failure");
			String mess = e.getMessage();
			if (mess.length() > 1000) {
				mess = mess.substring(0, 1000);
			}
			log.setResultMsg(mess);
			System.out.println(e.getMessage());
		} finally {
			logService.insert(log);
		}
		return null;
	}
}