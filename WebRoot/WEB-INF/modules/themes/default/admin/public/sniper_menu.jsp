<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="s"%>

<div id="sniper_menu" class="btn-group" data-spy="affix"
	data-offset-top="100" style="z-index:100">
	<div class="btn-group">
		<button type="button" class="btn btn-info dropdown-toggle"
			data-toggle="dropdown">
			<i class="fa fa-angle-double-down"></i>
		</button>
		<ul class="dropdown-menu">
			<li><a href="javascript:;" data-value="1" data-type="select">全选</a></li>
			<li><a href="javascript:;" data-value="2" data-type="select">不选</a></li>
			<li><a href="javascript:;" data-value="3" data-type="select">反选</a></li>
		</ul>
	</div>
	<s:if test="${sniperMenu.delBotton == true }">
		<div class="btn-group">
			<button type="button" class="btn btn-danger" data-click="on"
				data-value="delete" data-type="delete">
				<i class="fa fa-trash-o"></i>
			</button>
		</div>
	</s:if>

	<s:if test="${sniperMenu != null }">
		<s:set value="${sniperMenu.params }" var="sniperMenu1" scope="page"></s:set>
		<s:forEach var="myKey" items="${sniperMenu1 }">
			<div class="btn-group">
				<button type="button" class="btn btn-warning dropdown-toggle"
					data-toggle="dropdown">
					${ sniperMenu.getKeyValue(myKey.key)} <span class="caret"></span>
				</button>
				<ul class="dropdown-menu" style="max-height:300px; overflow: auto;">
					<s:forEach var="key" items="${myKey.value }">
						<li><a href="javascript:;" data-value='${key.key}'
							data-type='${myKey.key}'>${key.value }</a>
					</s:forEach>
				</ul>
			</div>
		</s:forEach>
	</s:if>

	<s:if test="${sniperMenu.buttons != null }">
		<s:forEach items="${sniperMenu.buttons }" var="b">
			<div class="btn-group">
				<button type="button" class="btn btn-${b.color }" data-single="on"
					data-url="${b.url }" data-click="on" data-value="${b.value }"
					data-type="${b.type }"
					<s:if test="${b.target != null }">data-toggle="modal-s"
					data-target="${b.target }"</s:if>>
					${b.name }</button>
			</div>
		</s:forEach>
	</s:if>
</div>

<!-- 调用 -->
<script type="text/javascript" src="myfiles/js/jquery.sniper.menu.js"></script>
<script type="text/javascript">
	$(function() {
		$().snipermenu({
			baseurl : 'doftec',
			url : '${sniperUrl}'
		});
	});

	
</script>