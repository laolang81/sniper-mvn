<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<form:form method="post" cssClass="form-horizontal" id="sniperForm"
	role="form" modelAttribute="files">

	<div class="form-group">
		<label for="oldName" class="col-sm-2 control-label">名称</label>
		<div class="col-sm-10">
			<form:input path="oldName" cssClass="form-control" placeholder="" />
		</div>
	</div>

	<div class="form-group">
		<label for="tags" class="col-sm-2 control-label">标签</label>
		<div class="col-sm-10">
			<form:input path="tags" cssClass="form-control" placeholder="" />
		</div>
	</div>

	<div class="form-group">
		<label for="eventTimeDate" class="col-sm-2 control-label">结束时间</label>
		<div class="col-sm-3">
			<form:input path="eventTimeDate" cssClass="form-control Wdate" />
		</div>
	</div>

	<div class="form-group">
		<label for="eventTimeDate" class="col-sm-2 control-label">附件预览</label>
		<div class="col-sm-3">
			<c:choose>
				<c:when test="${type == 'video' }">
					<div id="a1"></div>
				</c:when>
				<c:when test="${type == 'image' }">
					<a href="file-info/download?id=${files.id }" target="_blank"><img alt="" src="${baseHref.webUrl }${files.newPath}"></a>
				</c:when>
				<c:otherwise>
					<a href="file-info/download?id=${files.id }" target="_blank">下载[${files.oldName }]</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
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
	src="myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript"
	src="myfiles/Plugin/ckplayer6.7/ckplayer/ckplayer.js" charset="utf-8"></script>
<script type="text/javascript">
	$('.Wdate').bind('click', function() {
		WdatePicker({
			dateFmt : 'yyyy-MM-dd'
		});
	});

	$(function() {
		$("input[name='tags']").tagsInput({
			width : '100%',
			height : '60px',
			autocomplete_url : 'public/sendAjaxTags'
		});
	});
	<c:if test="${type == 'video'}">
	var flashvars = {
		f : '${baseHref.webUrl }${files.newPath}',
		c : 0,
		b : 1
	};
	var video = [ '${baseHref.webUrl}${files.newPath}->video/mp4' ];
	CKobject.embed('myfiles/Plugin/ckplayer6.7/ckplayer/ckplayer.swf', 'a1',
			'ckplayer_a1', '720', '480', false, flashvars, video);
	</c:if>
</script>