<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<style>
.from label {float: none;padding: 0;text-indent: 10px;}
dl{ border: 1px solid gray; height: 550px;overflow: scroll;}
dt{ border-bottom: 1px solid #DDDDDD;float: left;font-size: 14px; line-height: 30px;  width: 98%; margin: 5px 1%;text-indent: 10px;}
dd{ padding: 1px 0;float: left;width: 46%;line-height: 20px;margin-left: 10px;}
.itemSelect{ background-color: #CC0000;color: #FFFFFF;}
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
	role="form" modelAttribute="pageIndex">
	
	<div class="form-group">
		<label for="name" class="col-sm-2 control-label">名称</label>
		<div class="col-sm-8">
			<form:input path="name" cssClass="form-control" placeholder="" />
		</div>
	</div>
	
	<div class="form-group">
		<div class="col-sm-10 menuContent well well-sm col-md-offset-1">
			<dl>
			<c:forEach items="${deps }" var="d">
				<dt><a name="name${d.key }">${d.value }</a></dt>
				<c:if test="${ itemsMap[d.key] != null}">
					<c:forEach items="${itemsMap[d.key] }" var="tr">
						<dd>
								<label title="${tr.key }" for="itemid${tr.key }"
									<c:if test="${itmidsData[tr.key] != null }">class="itemSelect"</c:if>>
									<input
									<c:if test="${itmidsData[tr.key] != null }">checked="checked"</c:if>
									type="checkbox" value="${tr.key }" id="itemid${tr.key }"
									name="itemid">&nbsp;${tr.value }(${tr.key })
								</label>
							</dd>
					</c:forEach>
				</c:if>
			</c:forEach>
			</dl>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" class="btn btn-danger">保存</button>
		</div>
	</div>
</form:form>
<script type="text/javascript">
//栏目背景颜色改变
$().ready(function(){
	$('dd label').click(function(){
		if($(this).find("input[type='checkbox']").prop("checked")){
			console.log($(this).html());
			$(this).addClass('itemSelect');
		}else{
			$(this).removeClass('itemSelect');
		}
	});
})
</script>