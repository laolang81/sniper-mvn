package com.sniper.springmvc.enums;

public enum UserPostValue {

	START_POST("start_post"), READ_POST("read_post"), MOVE_POST("move_post"), AUDIT_RESULT(
			"audit_result"), AUDIT("audit"), S("sniper");
	private String value;

	private UserPostValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public static UserPostValue toValue(String value) {
		try {
			return valueOf(value.toUpperCase());
		} catch (Exception e) {
			return null;
		}

	}

	public static UserPostValue toTaskStatus(String str) {
		try {
			return valueOf(str.toUpperCase());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		System.out.println(UserPostValue.MOVE_POST.toString());
		System.out.println(UserPostValue.toTaskStatus("s"));
	}

}
