<#if msg?? >
<div class="alert alert-warning alert-dismissible" role="alert">
  <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
  <strong>Warning!</strong> ${msg}
</div>
</#if>
<#if isRunas>
<h2>上一个身份是： ${previousUsername} | <a href="admin/admin-center/switchBack">返回上一个身份</a></h2>
</#if>
<h3>当前身份: ${currentUserame}</h3>

<h4>切换到其他身份</h4>
<table class="table table-hover">
	<thead>
		<tr>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<#list fromUserIds as f>
		<tr>
			<td><a href="admin/admin-center/switchTo?id=${f.fromUserId}">切换到 <strong>${adminUsers[f.fromUserId?c]}</strong></a></td>
		</tr>
		</#list>
	</tbody>
</table>

