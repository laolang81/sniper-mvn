package com.sniper.springmvc.advice;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;

import com.sniper.springmvc.data.SystemRunTimeData;

/**
 * controller环绕方法
 * 
 * @author suzhen
 * 
 */
public class RunTimeAdvice {

	ThreadLocal<SystemRunTimeData> threadLocal = new ThreadLocal<>();

	public void doBefore(JoinPoint point) {
		Object target = point.getTarget();
		SystemRunTimeData data = new SystemRunTimeData();
		StringBuffer msg = new StringBuffer();
		msg.append("before:");
		msg.append(target.getClass().getName());
		msg.append(":");
		msg.append(point.getSignature().getName());
		msg.append(";");
		data.addModelNode(msg.toString());
		threadLocal.set(data);
	}

	public void doAfter(JoinPoint point) {
		SystemRunTimeData data = threadLocal.get();
		if (data != null) {
			Object target = point.getTarget();
			StringBuffer msg = new StringBuffer();
			msg.append("after:");
			msg.append(target.getClass().getName());
			msg.append(":");
			msg.append(point.getSignature().getName());
			msg.append(";");
			data.addModelNode(msg.toString());
		}

		System.out.println(data.getModels());

	}

	/**
	 * ProceedingJoinPoint 是JoinPoint的子类
	 * http://www.cnblogs.com/toSeeMyDream/p/6254000.html
	 * @param point
	 * @return
	 */
	public Object record(ProceedingJoinPoint point) {
		return point;
	}
}
