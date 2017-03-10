<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="/ImageHelpTags" prefix="imageHelp" %>
<style>
.fileList {
	list-style: none outside none;
	overflow: auto;
	margin: 0;
	padding: 0;
}

.fileList .li {
	font-weight: bolder;
	position: relative;
	float: left;
	margin: 5px;
	text-align: center;
}

.fileList img {
	max-height: 200px;
}

.fileList .li .info {
	FILTER: alpha(opacity = 50);
	opacity: 0.5;
	height: 20px;
	color: #ffffff;
	line-height: 20px;
	text-align: left;
	bottom: 10px;
	background: none repeat scroll 0 0 #000000;
	left: 3px;
	position: absolute;
	z-index: 100;
	right: 3px;
	padding: 2px;
}

.fileList .li .del {
	right: -3px;
	top: -8px;
	font-size: 18px;
	position: absolute;
	z-index: 100;
	color: black;
	position: absolute;
}

.fileList .li .alertpath {
	left: 3px;
	position: absolute;
	z-index: 100;
	color: #ffffff;
}

.menuContent {
	background: none repeat scroll 0 0 #fff;
	z-index: 1000;
	overflow: auto;
}
.img{max-width: 100%}
</style>

<form:form method="POST" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="files">
	<form:hidden path="id" />
	<div class="row">
		<div class="col-md-9">
			<div class="form-group">
				<label for="name" class="col-sm-2 sr-only control-label text-danger">名称</label>
				<div class="col-sm-12">
					<form:input path="oldName" cssClass="form-control" placeholder="请填写标题" />
				</div>
			</div>


			<div class="form-group">
				<label for="tags" class="col-sm-1 sr-only control-label">标签</label>
				<div class="col-sm-12">
					<form:input path="tags" cssClass="form-control"
						placeholder="多个标签请用英文逗号（,）分开" />
					<div class="help-block">
						<p class="text-warning"><form:errors path="tags" /></p>
					</div>
				</div>
			</div>

			<div class="form-group">
				<label for="tags" class="col-sm-1 sr-only control-label">图片</label>
				<div class="col-sm-12">
					<img class="img" src="<imageHelp:imageHelp height="10950" width="1024" path="${files.newPath }"/>">
					<form:hidden path="newPath" cssClass="img"/>
				</div>
			</div>

			<div class="form-group">
				<ol class="fileList mt10" id="filesList"></ol>
			</div>
			<input type="hidden" name="filesId" id="filesId" >

		</div>
		<div class="col-md-3">
			<div class="form-group">
				<div class="col-sm-8 col-md-offset-0">
					<button type="submit" class="btn btn-danger">保存</button>
					<a href="images/download/${files.id}" class="btn btn-default" role="button">下载</a>
					<c:if test="${files.postUrl != '' &&  files.postUrl != null}"><a href="${files.postUrl }" target="_blank">来源</a></c:if>
				</div>
			</div>
			
			<div class="form-group">
				<label for="target" class="col-sm-3 control-label">事件时间</label>
				<div class="col-sm-9">
					<form:input path="eventTimeDate" cssClass="form-control Wdate"/>
				</div>
			</div>
		</div>
	</div>
</form:form>

<link
	href="myfiles/Plugin/jquery-ui-1.10.3.custom/css/ui-lightness/jquery-ui-1.10.3.custom.min.css"
	media="screen" rel="stylesheet" type="text/css">
<link href="myfiles/Plugin/jQuery-Tags-Input/jquery.tagsinput.css"
	media="screen" rel="stylesheet" type="text/css">
<script type="text/javascript"
	src="myfiles/Plugin/jquery-ui-1.10.3.custom/js/jquery-ui-1.10.3.custom.min.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/jQuery-Tags-Input/jquery.tagsinput.min.js"></script>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	
	$(document).ready(function() {
		$('.Wdate').bind('click', function() {
			WdatePicker({
				dateFmt : 'yyyy-MM-dd'
			})
		});
		
		$("input[name='tags']").tagsInput({
			width : '100%',
			height : '60px',
			defaultText : '添加标签', //默认文字
			autocomplete_url : 'public/sendAjaxTags'
		});
		
			
	});

</script>

