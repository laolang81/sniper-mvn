<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="get"
		modelAttribute="groupSearch">
		<div class="form-group">
			<label for="" class="sr-only">${ sniperMenu.getKeyValue("theShow")}</label>
			<form:select title="${ sniperMenu.getKeyValue('theShow')}"
				path="isShow" cssClass="form-control">
				<form:option value="">--</form:option>
				<form:options items="${sniperMenu.params.theShow }" />
			</form:select>
		</div>
		<div class="form-group">
			<label for="" class="sr-only">${ sniperMenu.getKeyValue("theMenu")}</label>
			<form:select title="${sniperMenu.getKeyValue('theMenu')}"
				path="isMenu" cssClass="form-control">
				<form:option value="">--</form:option>
				<form:options items="${sniperMenu.params.theMenu }" />
			</form:select>
		</div>

		<div class="form-group">
			<label for="" class="sr-only">${ sniperMenu.getKeyValue("type")}</label>
			<form:select title="${sniperMenu.getKeyValue('type')}" path="type"
				cssClass="form-control">
				<form:option value="">--</form:option>
				<form:options items="${sniperMenu.params.type }" />
			</form:select>
		</div>
		<div class="form-group">
			<label for="" class="sr-only">名称</label>
			<form:input title="名称" path="groupName" cssClass="form-control" />
		</div>

		<div class="form-group">
			<label for="" class="sr-only">url</label>
			<form:input title="url" path="url" cssClass="form-control" />
		</div>
		
		<div class="form-group">
			<label for="fid" class="sr-only">fid</label>
			<form:input title="url" path="fid" cssClass="form-control" />
		</div>

		<div class="form-group">
			<button class="btn btn-success" type="submit" name="submit"
				value="search">查询</button>
		</div>

	</form:form>
</div>
<c:import url="../public/sniper_menu.jsp" />
<table class="table table-hover">
	<thead>
		<tr>
			<th>选择</th>
			<th>名称/地址/权限吗/排序</th>
			<th>菜单</th>
			<th>公共</th>
			<th>显示</th>
			<th>类型</th>
			<th>父级ID</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="l" items="${lists }">
			<tr id="sl_${l.id}">
				<td><input type="checkbox" name="list.id" value="${l.id}" /></td>
				<td data-type="name"><a
					href=".${baseHref.adminPath }/admin-right/update?id=${l.id}" target="_blank">${l.name}</a><br>${l.url}<br>${l.permission}<br>${l.sort}</td>
				<td data-type="theMenu">${l.theMenu}</td>
				<td data-type="thePublic">${l.thePublic}</td>
				<td data-type="theShow">${l.theShow}</td>
				<td data-type="theShow">${sniperMenu.params.type[l.type]}</td>
				<td data-type="fid"><a
					href=".${baseHref.adminPath }/admin-right/update?id=${l.id}" target="_blank">${l.fid}</a></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>