package com.sniper.springmvc.model;


/**
 * 用户级别表
 * 
 * @author sniper
 * 
 */
public class MembersLevel extends BaseEntity {

	private static final long serialVersionUID = 1L;

	
	private String id;
	private String name;
	// 拥挤积分低于多少分,或者用户积分高于多少分就是这个级别
	private int lower;
	private int higher;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLower() {
		return lower;
	}

	public void setLower(int lower) {
		this.lower = lower;
	}

	public int getHigher() {
		return higher;
	}

	public void setHigher(int higher) {
		this.higher = higher;
	}

}
