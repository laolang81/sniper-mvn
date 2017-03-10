<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<form:form method="post" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="ad" action="">

	<div class="form-group">
		<label for="name" class="col-sm-2 control-label">名称</label>
		<div class="col-sm-10">
			<form:input path="name" cssClass="form-control" placeholder="name" />
			
		</div>
	</div>

	<div class="form-group">
		<label for="description" class="col-sm-2 control-label">备注</label>
		<div class="col-sm-10">
			<form:textarea path="note" rows="6" cssClass="form-control"
				cssStyle="height:100px" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
</form:form>

