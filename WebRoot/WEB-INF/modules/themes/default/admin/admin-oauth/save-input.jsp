<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<form:form method="post" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="oauthClient" action="">
	
	<div class="form-group">
		<label for="clientSecret" class="col-sm-2 control-label">App
			Id</label>
		<div class="col-sm-10">
			<p class="form-control-static">${oauthClient.id }</p>
			<div class="help-block">新建时为空</div>
		</div>
		
	</div>

	<div class="form-group">
		<label for="clientSecret" class="col-sm-2 control-label">App
			Secret</label>
		<div class="col-sm-10">
			<p class="form-control-static">${oauthClient.clientSecret }</p>
		</div>
	</div>

	<div class="form-group">
		<label for="clientName" class="col-sm-2 control-label">App 名称</label>
		<div class="col-sm-10">
			<form:input path="clientName" cssClass="form-control"
				placeholder="name" />
			<div class="help-block">
				<form:errors path="clientName" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="description" class="col-sm-2 control-label">App 描述</label>
		<div class="col-sm-10">
			<form:textarea path="description" cssClass="form-control" />
		</div>
	</div>

	<div class="form-group">
		<label for="clientUrl" class="col-sm-2 control-label">客户端地址</label>
		<div class="col-sm-10">
			<form:input path="clientUrl" cssClass="form-control" placeholder="" />
			<div class="help-block">
				<form:errors path="clientUrl" />
			</div>
		</div>
	</div>

	<div class="form-group">
		<label for="enabled" class="col-sm-2 control-label">启用</label>
		<div class="col-sm-2">
			<form:checkbox path="enabled" />
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
</form:form>

<link href="myfiles/Plugin/kindeditor/themes/default/default.css"
	media="screen" rel="stylesheet" type="text/css">

<script type="text/javascript"
	src="myfiles/Plugin/kindeditor/kindeditor-min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/kindeditor/lang/zh_CN.js"></script>

<script type="text/javascript">
	KindEditor.ready(function(K) {
		var editor1 = K.editor({
			uploadJson : 'upload?${_csrf.parameterName}=${_csrf.token}',
			fileManagerJson : 'file-upload/htmlmanager',
			allowFileManager : true,
			allowImageUpload : true,
			urlType : 'domain',
			filePostName : 'imgFile',
			afterBlur : function() {
				this.sync();
			}

		});

		K('#attachement').click(function() {
			editor1.loadPlugin('image', function() {
				editor1.plugin.imageDialog({
					showLocal : true,
					showRemote : true,
					imageUrl : K('#attachement').val(),
					clickFn : function(url, title) {
						K('#attachement').val(url);
						editor1.hideDialog();
					},
					clickFns : function(data) {
						$("#attachement_id").val(data.id);
					}
				});
			});
		});
	});
</script>