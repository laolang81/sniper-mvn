<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:if test="${errors!= null || fn:length(errors) > 0}">
	<div class="alert alert-success alert-dismissible" role="alert">
		<button type="button" class="close" data-dismiss="alert">
			<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
		</button>
		<ul>
			<c:forEach items="${errors }" var="m">
				<li>${m}</li>
			</c:forEach>
		</ul>
	</div>
</c:if>

<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="adminUser">

	<form:hidden path="id" />
	<div class="form-group">
		<label for="name" class="col-sm-2 control-label">登录名称</label>
		<div class="col-sm-4">
			<p class="form-control-static">${adminUser.name }</p>
		</div>
	</div>

	<div class="form-group">
		<label for="nickName" class="col-sm-2 control-label">显示昵称</label>
		<div class="col-sm-4">
			<form:input path="nickName" cssClass="form-control"
				placeholder="nickName" />
		</div>
	</div>

	<div class="form-group">
		<label for="email" class="col-sm-2 control-label">Email</label>
		<div class="col-sm-4">
			<form:input path="email" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">更改信息</button>
		</div>
	</div>
</form:form>