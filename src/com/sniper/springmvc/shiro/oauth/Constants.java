package com.sniper.springmvc.shiro.oauth;

public enum Constants {
	CURRENT_USER("currentUser"), RESOURCE_SERVER_NAME("SNIPER-SERVER"), INVALID_CLIENT_DESCRIPTION(
			"客户端验证失败，如错误的client_id/client_secret。"), SESSION_FORCE_LOGOUT_KEY(
			"SESSION_FORCE_LOGOUT_KEY");

	private String value;

	private Constants() {
	}

	private Constants(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public static void main(String[] args) {

		System.out.println(Constants.RESOURCE_SERVER_NAME.toString());
		System.out.println(Constants.RESOURCE_SERVER_NAME.name());
	}

}
