package com.sniper.springmvc.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 用户组装hql
 * 
 * @author laolang
 * 
 */
public class HqlUtils implements Serializable {

	private static final long serialVersionUID = 1L;
	private Class<?> clazz;
	private String entityName;
	private String entityAsName = "ean";
	private List<String> where = new ArrayList<>();
	private List<String> join = new ArrayList<>();
	private String having;
	private String group;
	private ArrayList<String> order = new ArrayList<>();
	private boolean distinct = false;
	private String hql = "";
	private String hqlCount = "";
	private boolean debug = false;
	private Map<String, Object> params = new LinkedHashMap<>();

	public HqlUtils() {
	}

	public HqlUtils(Class<?> clazz2) {
		super();
		this.clazz = clazz2;
		this.entityName = this.clazz.getSimpleName();
	}

	public String getEntityName() {
		return entityName;
	}

	public String getEntityAsName() {
		return entityAsName;
	}

	public void setEntityAsName(String entityAsName) {
		this.entityAsName = entityAsName;
	}

	public String getWhere() {
		if (this.where.size() > 0) {
			String whereHql = "";
			whereHql = StringUtils.join(where, " ").trim();
			if (whereHql.trim().startsWith("and")
					|| whereHql.trim().startsWith("or")) {
				// 送第一个空格开始截取
				whereHql = whereHql.substring(whereHql.indexOf(" "));
			}
			return " WHERE " + whereHql;
		}
		return "";
	}

	public HqlUtils where(String where) {
		if (!"".equalsIgnoreCase(where)) {
			this.where.add(where.trim());
		}
		return this;
	}

	public HqlUtils orWhere(String where) {
		if (!"".equalsIgnoreCase(where)) {
			this.where.add(" or " + where.trim());
		}
		return this;
	}

	public HqlUtils and() {
		this.where.add(" and ");
		return this;
	}

	public HqlUtils or() {
		this.where.add(" or ");
		return this;
	}

	public HqlUtils andWhere(String where) {
		if (!"".equalsIgnoreCase(where)) {
			this.where.add(" and " + where.trim());
		}
		return this;
	}

	public HqlUtils startBracket() {
		this.where.add("(");
		return this;
	}

	public HqlUtils endBracket() {
		this.where.add(")");
		return this;
	}

	public HqlUtils setWhere(String where) {
		if (!"".equalsIgnoreCase(where)) {
			this.where.add(where.trim());
		}
		return this;
	}

	public void clearWhere() {
		if (this.where.size() > 0) {
			this.where.clear();
		}
	}

	public String getJoin() {
		if (join.size() > 0) {
			return " " + StringUtils.join(join, " ");
		}
		return "";
	}

	/**
	 * 
	 * @param clazz
	 * @param asName
	 * @param joinType
	 *            inner join left join right join
	 * @param where
	 */
	public void join(String joinName, String asName, String joinType,
			String where) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" ");
		buffer.append(joinType);
		buffer.append(" ");
		buffer.append(joinName);
		buffer.append(" ");
		buffer.append(asName);
		buffer.append(" ");
		buffer.append("ON");
		buffer.append(" ");
		buffer.append(where);
		buffer.append(" ");
		this.join.add(buffer.toString());

	}

	public void join(String joinName, String asName, String joinType) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(" ");
		buffer.append(joinType);
		buffer.append(" ");
		buffer.append(joinName);
		buffer.append(" ");
		buffer.append(asName);
		this.join.add(buffer.toString());
	}

	public void setJoin(String join) {
		this.join.add(join.trim());
	}

	public String getHaving() {
		if (ValidateUtil.isValid(having)) {
			return " HAVING " + having;
		}
		return "";
	}

	public void setHaving(String having) {
		this.having = having;
	}

	public String getGroup() {
		if (ValidateUtil.isValid(group)) {
			return " GROUP By " + group;
		}
		return "";
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getOrder() {

		if (this.order.size() > 0) {
			String orderHql = "";
			orderHql = StringUtils.join(order, ",");
			return " ORDER BY " + orderHql;
		}

		return "";
	}

	public HqlUtils order(String order) {
		if (!"".equalsIgnoreCase(order)) {
			this.order.add(order.trim());
		}
		return this;
	}

	public HqlUtils setOrder(String order) {
		if (!"".equalsIgnoreCase(order)) {
			this.order.add(order.trim());
		}
		return this;
	}

	public void clearOrder() {
		if (this.order.size() > 0) {
			this.order.clear();
		}
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public String getHql() {

		if (!"".equals(this.hql)) {
			return this.hql;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		if (isDistinct()) {
			buffer.append(" DISTINCT ");
		}
		buffer.append(getEntityAsName());
		buffer.append(" FROM ");
		buffer.append(getEntityName());
		buffer.append(" ");
		buffer.append(getEntityAsName());
		buffer.append(getJoin());
		buffer.append(getWhere());
		buffer.append(getHaving());
		buffer.append(getGroup());
		buffer.append(getOrder());

		this.hql = buffer.toString();

		if (isDebug()) {
			System.out.println(this.hql);
		}
		return hql;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public String getHqlCount() {

		if (!"".equals(this.hqlCount)) {
			return this.hqlCount;
		}

		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		buffer.append(" count(");
		if (isDistinct()) {
			buffer.append("DISTINCT ");
		}
		buffer.append(getEntityAsName());
		buffer.append(")");
		buffer.append(" FROM ");
		buffer.append(getEntityName());
		buffer.append(" ");
		buffer.append(getEntityAsName());
		buffer.append(getJoin());
		buffer.append(getWhere());
		buffer.append(getHaving());
		buffer.append(getGroup());
		buffer.append(getOrder());

		this.hqlCount = buffer.toString();
		if (isDebug()) {
			System.out.println(this.hqlCount);
		}
		return this.hqlCount;
	}

	public void setHqlCount(String hqlCount) {
		this.hqlCount = hqlCount;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	@Override
	public String toString() {
		return this.hqlCount + "\t" + this.hql;
	}

}
