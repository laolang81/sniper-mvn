package com.sniper.springmvc.scheduler;

public enum TaskStatus {
	START, PAUSE, CONTINUE, STOP, RUNONCE, BLOCKING, ERROR, DELETE;

	private TaskStatus() {
	}

	public static TaskStatus toTaskStatus(String str) {
		try {
			return valueOf(str.toUpperCase());
		} catch (Exception e) {
			// e.printStackTrace();
			return START;
		}
	}

}
