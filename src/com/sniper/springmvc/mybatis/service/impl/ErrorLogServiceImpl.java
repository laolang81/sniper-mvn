package com.sniper.springmvc.mybatis.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sniper.springmvc.model.ErrorLog;
import com.sniper.springmvc.mybatis.dao.BaseDao;

@Service("errorLogService")
public class ErrorLogServiceImpl extends BaseServiceImpl<ErrorLog> implements
		ErrorLogService {

	@Resource(name = "errorLogDao")
	@Override
	public void setDao(BaseDao<ErrorLog> dao) {
		super.setDao(dao);
	}

	@Override
	public void writeLog(Exception e) {

		ErrorLog errorLog = new ErrorLog();
		errorLog.setError(e.getMessage());
		errorLog.setErrorClass(e.getClass().getName());
		StackTraceElement[] elements = e.getStackTrace();
		for (StackTraceElement stackTraceElement : elements) {
			errorLog.setMessage(stackTraceElement.toString());
			this.insert("insert", errorLog);
		}
	}

	public static void main(String[] args) {
		List<Integer> pids = new ArrayList<>();
		try {
			int a = 1 / 0;
			System.out.println(a);
		} catch (ArithmeticException e) {
			ErrorLogService errorLogService = new ErrorLogServiceImpl();
			errorLogService.writeLog(e);
			// 错点类型
			StackTraceElement[] elements = e.getStackTrace();
			for (StackTraceElement stackTraceElement : elements) {
				System.out.println(stackTraceElement.getClassName());
				System.out.println(stackTraceElement.getFileName());
				System.out.println(stackTraceElement.getLineNumber());
				System.out.println(stackTraceElement.getMethodName());
				System.out.println(stackTraceElement.toString());
			}
			System.out.println(e.getMessage());
			System.out.println(e.getLocalizedMessage());
		}
		System.out.println(pids instanceof Collection);
	}
}
