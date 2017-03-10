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
	role="form" modelAttribute="items">
	<form:hidden path="itemid" />
	<div class="form-group">
		<label for="keyName" class="col-sm-2 control-label text-danger">名称</label>
		<div class="col-sm-8">
			<form:input path="name" cssClass="form-control" placeholder="栏目名称" />
			<div class="help-block">
				<form:errors path="name" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="deprtid" class="col-sm-2 control-label text-danger">栏目处室</label>
		<div class="col-sm-8">
			<form:select path="deprtid" id="siteidSelect">
				<form:option value="0">选择</form:option>
				<form:options items="${deps }" />
			</form:select>
		</div>
	</div>

	<div class="form-group">
		<label for="deShortName" class="col-sm-2 control-label">位置设置</label> <label
			for="pro3" class="col-sm-1 control-label">左侧</label>
		<div class="col-sm-1">
			<form:input path="pro3" cssClass="form-control" placeholder="" />
		</div>
		<label for="pro1" class="col-sm-1 control-label">中侧</label>
		<div class="col-sm-1">
			<form:input path="pro1" cssClass="form-control" placeholder="" />
		</div>
		<label for="pro2" class="col-sm-1 control-label">右侧</label>
		<div class="col-sm-1">
			<form:input path="pro2" cssClass="form-control" placeholder="" />
		</div>
		<label for="pro4" class="col-sm-1 control-label">下级</label>
		<div class="col-sm-1">
			<form:input path="pro4" cssClass="form-control" placeholder="" />
		</div>
	</div>

	<div class="form-group">
		<label for="subjectnum" class="col-sm-2 control-label">新闻数量</label>
		<div class="col-sm-2">
			<form:input path="subjectnum" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="displayorder" class="col-sm-2 control-label">排序</label>
		<div class="col-sm-2">
			<form:input path="displayorder" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="status" class="col-sm-2 control-label">启用</label>
		<div class="col-sm-2">
			<form:select items="${yes_no }" path="status" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="blink" class="col-sm-2 control-label">链接</label>
		<div class="col-sm-2">
			<form:select items="${yes_no }" path="blink" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="linkUrl" class="col-sm-2 control-label">链接地址</label>
		<div class="col-sm-8">
			<form:input path="linkUrl" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="banner" class="col-sm-2 control-label">图片</label>
		<div class="col-sm-8">
			<form:hidden path="linkImg" />
			<input type="file" class="fileupload" name="imgFile"
				data-url="upload">
			<div class="progress" style="margin-top:10px;">
				<div class="progress-bar" role="progressbar" aria-valuenow="0"
					aria-valuemin="0" aria-valuemax="100" style="width: 0%;">0%</div>
			</div>
			<div class="help-block">
				<c:if test="${itemid.linkImg != null && itemid.linkImg != '' }">
					<img src="${systemConfig.imagePrefix }${itemid.linkImg}" class="img-thumbnail">
				</c:if>
			</div>
		</div>

	</div>
	<div class="form-group">
		<label for="style" class="col-sm-2 control-label">样式</label>
		<div class="col-sm-4">
			<form:select path="style" items="${styles }" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="hasmore" class="col-sm-2 control-label">显示更多</label>
		<div class="col-sm-2">
			<form:select items="${yes_no }" path="hasmore"
				cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
</form:form>

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
	var deprtid = '${items.deprtid}';
	var itemup = '${items.itemup}';
	$().siteid({
		post : deprtid,
		selected : itemup,
		name : 'itemup',
		gettrue : 1

	});
</script>