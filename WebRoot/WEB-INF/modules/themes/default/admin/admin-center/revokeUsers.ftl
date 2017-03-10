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


<h4>收回权限</h4>
<table class="table table-hover">
	<thead>
		<tr>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<#list toUserIds as f>
		<tr>
			<td><a href="admin/admin-center/revoke?id=${f.toUserId}">插销对 <mark>${adminUsers[f.toUserId?c]}</mark> 授权</a></td>
		</tr>
		</#list>
	</tbody>
</table>