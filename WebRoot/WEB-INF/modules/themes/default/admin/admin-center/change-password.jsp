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
		<label for="password_old" class="col-sm-2 control-label">旧密码</label>
		<div class="col-sm-4">
			<input type="password" name="password_old" class="form-control">
		</div>
	</div>

	<div class="form-group">
		<label for="password_n" class="col-sm-2 control-label">密码</label>
		<div class="col-sm-4">
			<input name="password_n" class="form-control" type="password">
			<div class="help-block">密码最少六位</div>
		</div>
	</div>

	<div class="form-group">
		<label for="password_c" class="col-sm-2 control-label">确认密码</label>
		<div class="col-sm-4">
			<input name="password_c" class="form-control" type="password">
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">更改密码</button>
		</div>
	</div>
</form:form>