package com.sniper.springmvc.model;

import java.util.Date;

public class Lottery extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String date;
	private String type;
	private Date ctime = new Date();
	private String num;
	private int numOne;
	private int numTwo;
	private int numThree;
	private int numFour;
	private int numFive;
	private int numSix;
	private int numSeven;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public int getNumOne() {
		return numOne;
	}

	public void setNumOne(int numOne) {
		this.numOne = numOne;
	}

	public int getNumTwo() {
		return numTwo;
	}

	public void setNumTwo(int numTwo) {
		this.numTwo = numTwo;
	}

	public int getNumThree() {
		return numThree;
	}

	public void setNumThree(int numThree) {
		this.numThree = numThree;
	}

	public int getNumFour() {
		return numFour;
	}

	public void setNumFour(int numFour) {
		this.numFour = numFour;
	}

	public int getNumFive() {
		return numFive;
	}

	public void setNumFive(int numFive) {
		this.numFive = numFive;
	}

	public int getNumSix() {
		return numSix;
	}

	public void setNumSix(int numSix) {
		this.numSix = numSix;
	}

	public int getNumSeven() {
		return numSeven;
	}

	public void setNumSeven(int numSeven) {
		this.numSeven = numSeven;
	}

}
