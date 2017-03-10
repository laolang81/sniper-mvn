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

<h4>授予身份给其他人</h4>
<table class="table table-hover">
	<thead>
		<tr>
			<th>用户名</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
		<#list adminUsers?keys as f>
		<tr>
			<td>${adminUsers[f]}</td>
			<td><a href="admin/admin-center/grant?id=${f}">授权</a></td>
		</tr>
		</#list>
	</tbody>
</table>