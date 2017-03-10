package com.sniper.springmvc.datasource;

/**
 * 数据源切换 在数据操作中切换数据源代码
 * DataSourceSwitch.setDataSource(DataSourceSwitch.DATA_SOURCE_MASTER);
 * 
 * @author sniper
 * 
 */
public class DataSourceSwitch {

	public static final ThreadLocal<DataSourceValue> THREAD_LOCAL = new ThreadLocal<>();
	public static final ThreadLocal<String> THREAD_CLASS = new ThreadLocal<>();

	public static void setDataClass(String dataClass) {
		THREAD_CLASS.set(dataClass);
	}

	public static String getDataClass() {
		return THREAD_CLASS.get();
	}

	public static void clearDataClass() {
		THREAD_CLASS.remove();
	}

	public static void setDataSource(DataSourceValue dataSourceType) {
		THREAD_LOCAL.set(dataSourceType);
	}

	public static void setDefault() {
		THREAD_LOCAL.set(DataSourceValue.MASTER);
	}

	public static DataSourceValue getDataSource() {
		return THREAD_LOCAL.get();
	}

	public static void clearDataSource() {
		THREAD_LOCAL.remove();
	}

}
