<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<style type="text/css">
.menuContent {
	background: none repeat scroll 0 0 #fff;
	position: absolute;
	left: 0;
	top: 0;
	width: auto;
	z-index: 1000;
}
</style>

<c:if test="${errors!= null}">
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
	role="form" modelAttribute="infoclass">
	<form:hidden path="id" />
	<div class="form-group">
		<label for="keyName" class="col-sm-2 control-label">名称</label>
		<div class="col-sm-8">
			<form:input path="name" cssClass="form-control" placeholder="name" />
			<div class="help-block">
				<form:errors path="name" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="deprtid" class="col-sm-2 control-label">栏目处室</label>
		<div class="col-sm-8">
			<select id="siteidSelect" name="deprtid">
				<option value="0">选择</option>
				<c:forEach var="d" items="${departments }">
					<option value="${d.key }">${d.value }</option>
				</c:forEach>
			</select>
		</div>
	</div>

	<div class="form-group">
		<label for="enabled" class="col-sm-2 control-label">启用</label>
		<div class="col-sm-2">
			<form:select items="${yes_no }" path="enabled"
				cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="sort" class="col-sm-2 control-label">排序</label>
		<div class="col-sm-2">
			<form:input path="sort" cssClass="form-control" />
		</div>
	</div>


	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
</form:form>

<script src="myfiles/Plugin/layer/layer/layer.js"></script>
<script
	src="myfiles/Plugin/jQuery-File-Upload-9.12.1/js/vendor/jquery.ui.widget.js"></script>
<script
	src="myfiles/Plugin/jQuery-File-Upload-9.12.1/js/jquery.iframe-transport.js"></script>
<script
	src="myfiles/Plugin/jQuery-File-Upload-9.12.1/js/jquery.fileupload.js"></script>
<script type="text/javascript"
	src="myfiles/js/jquery.file.path.upload.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		filesBox.band();
	})
</script>
<script type="text/javascript" src="myfiles/js/jquery.items.select.js"></script>
<script type="text/javascript">
	//处室栏目操作
	var deprtid = '${parentItems.departments.id}';
	var itemup = '${infoclass.itemid}';
	$().siteid({
		post : deprtid,
		selected : itemup,
		name : 'itemid',
		gettrue : 1

	});
</script>
