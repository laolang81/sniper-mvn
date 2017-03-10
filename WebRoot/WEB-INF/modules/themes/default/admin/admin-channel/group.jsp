<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="get"
		modelAttribute="search">
		<div class="form-group">
			<label for="" class="sr-only">名称</label>
			<form:input title="名称" path="name" cssClass="form-control" />
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
			<th>编辑</th>
			<th>名称</th>
			<th>栏目</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="l" items="${lists }">
			<tr id="sl_${l.id}">
				<td><input type="checkbox" name="list.id" value="${l.id}" /><a
					href=".${baseHref.adminPath }/admin-channel/groupupdate?id=${l.id}"
					target="_blank">${l.id}</a></td>
				<td data-type="name"><a
					href="http://sdcom.gov.cn/public/html/groups/${l.id }.html"
					target="_blank">${l.name}</a></td>
				<td><c:set var="itemids" value="${fn:split(l.itemid,',')}" />
					<c:forEach items="${itemids }" var="i" varStatus="index">
					${itemsMap[i] }
					<c:choose>
							<c:when test="${index.index % 3 == 0 }">
								<br>
							</c:when>
							<c:otherwise> | </c:otherwise>
						</c:choose>
					</c:forEach></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>