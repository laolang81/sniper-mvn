package com.sniper.springmvc.data;

import java.util.ArrayList;
import java.util.List;

/**
 * 记载系统的时间段的时间
 * 
 * @author suzhen
 * 
 */
public class SystemRunTimeData {

	private long start = 0l;
	private long end = 0l;
	// 记录每隔时间段的时间差
	private List<ModelNode> models = new ArrayList<>();

	public SystemRunTimeData() {
		start = System.currentTimeMillis();
	}

	public long getStart() {
		return start;
	}

	/**
	 * 获取总共执行的时间
	 * 
	 * @return
	 */
	public long getEnd() {
		end = System.currentTimeMillis() - start;
		return end;
	}

	public List<ModelNode> getModels() {
		return models;
	}

	/**
	 * 添加节点
	 * 
	 * @param msg
	 *            消息
	 * @param section
	 *            是否是区间值
	 */
	public void addModelNode(String msg) {
		// 获取使用时间
		long useTime = System.currentTimeMillis() - start;
		ModelNode modelNode = new ModelNode(msg, useTime);
		// 获取区间时间
		if (models.size() > 0) {
			int length = models.size();
			long diffTime = System.currentTimeMillis()
					- models.get(length - 1).getNowTime();
			modelNode.setDiffTime(diffTime);
		} else {
			modelNode.setDiffTime(0);
		}
		models.add(modelNode);
	}

	/**
	 * 时间模板
	 * 
	 * @author suzhen
	 * 
	 */
	public class ModelNode {
		private String msg;
		// 使用的时间
		private long useTime;
		// 和上个的时间差
		private long diffTime;
		// 执行是的时间
		private long nowTime;

		public ModelNode() {
			super();
			this.nowTime = System.currentTimeMillis();
		}

		public ModelNode(String msg, long useTime) {
			super();
			this.msg = msg;
			this.useTime = useTime;
			this.nowTime = System.currentTimeMillis();
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public long getUseTime() {
			return useTime;
		}

		public void setUseTime(long useTime) {
			this.useTime = useTime;
		}

		public long getDiffTime() {
			return diffTime;
		}

		public void setDiffTime(long diffTime) {
			this.diffTime = diffTime;
		}

		public long getNowTime() {
			return nowTime;
		}

		public void setNowTime(long nowTime) {
			this.nowTime = nowTime;
		}

		@Override
		public String toString() {
			return "ModelNode [msg=" + msg + ", useTime=" + useTime
					+ ", diffTime=" + diffTime + ", nowTime=" + nowTime + "]";
		}
	}
}
