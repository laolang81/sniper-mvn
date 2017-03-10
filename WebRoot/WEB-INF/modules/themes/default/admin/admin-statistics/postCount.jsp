<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="seach">
	<form:form action="" role="form" cssClass="form-inline" method="post"
		modelAttribute="search">

		<div class="form-group">
			<label for="startDate" class="sr-only">栏目开始时间</label>
			<form:input path="itemDateStart" cssClass="form-control Wdate"
				title="栏目开始时间" />
		</div>
		<div class="form-group">
			<label for="startDate" class="sr-only">开始时间</label>
			<form:input path="startDate" cssClass="form-control Wdate"
				title="新闻搜索开始" />
			- <label for="endDate" class="sr-only">结束时间</label>
			<form:input path="endDate" cssClass="form-control Wdate"
				title="新闻搜索结束时间" />
		</div>

		<div class="form-group">
			<label for="siteidSelect" class="sr-only">处室</label>
			<form:select path="siteid" cssClass="form-control" id="siteidSelect"
				title="处室选择">
				<form:option value="">选择</form:option>
				<form:options items="${deps }" />
			</form:select>
		</div>

		<div class="form-group">
			<button class="btn btn-success" type="submit" name="submit"
				value="search">查询</button>
			<button class="btn btn-success" type="submit" name="submit"
				value="export">导出</button>
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
			<th>留言量/回复量</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach var="dep" items="${searchDeps }">
			<tr>
				<td><strong>${dep.value }</strong></td>
				<td>${siteidCount[dep.key]['view'] }</td>
				<td>${siteidCount[dep.key]['sid'] }</td>
				<td></td>
				<td>${siteidCount[dep.key]['worldCount'] }/${siteidCount[dep.key]['worldAnswerCount'] }</td>
			</tr>
			<c:choose>
				<c:when test="${itemsMap[dep.key] != null }">
					<!-- 获取栏目信息 -->
					<c:forEach var="itemd" items="${itemsMap[dep.key] }">
						<c:choose>
							<c:when test="${siteidItemidCount[dep.key][itemd.key] != null }">
								<tr>
									<td title="${itemd.key}"><a target="_blank"
										href="http://sdcom.gov.cn/Office-${dep.key }-${itemd.key }.html">${itemd.value }</a></td>
									<td>${siteidItemidCount[dep.key][itemd.key]['view'] }</td>
									<td>${siteidItemidCount[dep.key][itemd.key]['sid'] }</td>
									<td><fmt:formatDate
											value="${siteidItemidCount[dep.key][itemd.key]['date'] }"
											pattern="yyyy-MM-dd" /></td>
									<td></td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr>
									<td title="${itemd.key}"><a target="_blank"
										href="http://sdcom.gov.cn/Office-${dep.key }-${itemd.key }.html">${itemd.value }</a></td>
									<td></td>
									<td></td>
									<td></td>
									<td></td>
								</tr>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</c:when>
			</c:choose>
		</c:forEach>
	</tbody>
</table>
<div class="alert alert-success alert-dismissible" role="alert">
	<button type="button" class="close" data-dismiss="alert">
		<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
	</button>
	<ul>
		<li>第一个日期选择: 限制栏目最后新闻发布日期，新闻最后发布日期高于选择日期不显示。</li>
		<li>第二个日期和第三个: 新闻和留言的起止时间。</li>
	</ul>
</div>