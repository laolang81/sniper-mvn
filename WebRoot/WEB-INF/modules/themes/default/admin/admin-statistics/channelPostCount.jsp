<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="post"
		modelAttribute="search">
		<div class="form-group">
			<label for="startDate" class="sr-only">开始时间</label>
			<form:input path="startDate" cssClass="form-control Wdate"
				id="startDate" />
			- <label for="endDate" class="sr-only">结束时间</label>
			<form:input path="endDate" cssClass="form-control Wdate" id="endDate" />
		</div>


		<div class="form-group">
			<label for="group" class="sr-only">聚合栏目</label>
			<form:select path="group" cssClass="form-control" id="group">
				<form:option value="">选择</form:option>
				<form:options items="${indexNames }" />
			</form:select>
		</div>
		<div class="form-group">
			<button class="btn btn-success" type="submit" name="submit"
				value="search">查询</button>
		</div>
	</form:form>
</div>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(document).delegate('.Wdate', 'click', function() {
		WdatePicker({
			dateFmt : 'yyyy-MM-dd'
		})
	});
</script>
<table class="table table-hover">
	<thead>
		<tr>
			<th>处室 > 栏目</th>
			<th>新闻访问量</th>
			<th>新闻发布量</th>
			<th>最后新闻发布日期</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="index" items="${indexs }">
			<tr>
				<td><strong>${index.value.name }</strong></td>
				<td></td>
				<td></td>
				<td></td>
			</tr>
			<c:if test="${ indexCounts[index.key] != null}">
				<c:set var="sid">0</c:set>
				<c:set var="view">0</c:set>
				<c:forEach items="${indexCounts[index.key] }" var="itemCount">
					<c:set var="siteid">${ itemCount.value.siteid}</c:set>
					<c:if
						test="${deps[siteid]  != null && items[siteid][itemCount.key] != null }">
						<tr>
							<td>${deps[siteid] }>${items[siteid][itemCount.key] }</td>
							<td>${itemCount.value.sid }</td>
							<td>${itemCount.value.view }</td>
							<td><fmt:formatDate value="${itemCount.value.date }"
									pattern="yyyy-MM-dd" /></td>
						</tr>
						<c:set var="sid">${sid +  itemCount.value.sid}</c:set>
						<c:set var="view">${view + itemCount.value.view }</c:set>
					</c:if>
				</c:forEach>
			</c:if>
			<tr>
				<td><strong>${index.value.name }</strong></td>
				<td>${sid }</td>
				<td>${view }</td>
				<td></td>
			</tr>
		</c:forEach>
	</tbody>
</table>
