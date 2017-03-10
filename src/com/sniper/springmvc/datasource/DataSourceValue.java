package com.sniper.springmvc.datasource;

public enum DataSourceValue {
	MASTER("master"), SLAVE("slave"), LOCAL("local"), STAT("stat");

	private String name;

	DataSourceValue(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
