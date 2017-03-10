package com.sniper.springmvc.model;

/**
 * 文章表和栏目表对应的关系
 * 
 * @author suzhen
 * 
 */
public class SdItemsSubjects extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private int itemid;
	private int subjectid;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public int getItemid() {
		return itemid;
	}

	public void setItemid(int itemid) {
		this.itemid = itemid;
	}

	public int getSubjectid() {
		return subjectid;
	}

	public void setSubjectid(int subjectid) {
		this.subjectid = subjectid;
	}

}
