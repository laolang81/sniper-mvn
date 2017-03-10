package com.sniper.springmvc.datasource;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sniper.springmvc.utils.ValidateUtil;

/**
 * 数据源切换 对于内部调用的不管用 比如你写了一个扩展了一个接口,类在调用的时候,虽然在内部调用父接口的方法,但是注解无法获取
 * 
 * @author sniper
 * 
 */
public class DataSourceAspect {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DataSourceAspect.class);

	/**
	 * 获取本类的接口查询数据切换的值，如果不存在则寻找父级 清空当前数据切换恢复默认 每次执行完毕线程都是空的 主要切入的是接口
	 * 
	 * @param point
	 */
	public void doBefore(JoinPoint point) {

		Object target = point.getTarget();
		StringBuffer buffer = new StringBuffer();
		buffer.append("DataSourceAspect:");
		String className = target.getClass().getName();
		String methodName = point.getSignature().getName();
		buffer.append(className);
		buffer.append(".");
		buffer.append(methodName);

		// 获取父类的接口
		// Class<?>[] superClassz = target.getClass().getSuperclass()
		// .getInterfaces();
		// System.out
		// .println("父类的:" + target.getClass().getSuperclass().getName());
		// 获取类的接口
		Class<?>[] classz = target.getClass().getInterfaces();
		// System.out.println("类:" + target.getClass().getName());
		// System.out.println(superClassz[0].getName());
		// System.out.println("数据源切换");
		// System.out.println(classz[0].getName());
		// System.out.println(method);

		Class<?>[] parameterTypes = ((MethodSignature) point.getSignature())
				.getMethod().getParameterTypes();
		try {
			if (!ValidateUtil.isValid(classz)) {
				// 设置默认
				DataSourceSwitch.setDefault();
				return;
			}
			Method m = classz[0].getMethod(methodName, parameterTypes);
			// 注解是否存在此元素上
			if (m == null || !m.isAnnotationPresent(DataSource.class)) {
				DataSourceSwitch.setDefault();
				return;
			}
			DataSource data = m.getAnnotation(DataSource.class);
			DataSourceSwitch.setDataSource(data.value());
			DataSourceSwitch.setDataClass(buffer.toString());

		} catch (Exception e) {
			LOGGER.error(buffer.toString(), e);
			e.printStackTrace();
		}
	}
}
