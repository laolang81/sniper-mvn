
<table class="table table-hover">
	<thead>
		<tr>
			<th>ID</th>
			<th>名称</th>
			<th>浏览器</th>
			<th>系统</th>
			<th>语言</th>
			<th>IP</th>
			<th>填写时间</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<#list lists as l>
			<tr id="sl_${l.id}">
				<td><input type="checkbox" name="list.id" value="${l.id}" />${l.id}</td>
				<td><a href=".${baseHref.adminPath }/admin-survey/answer?sdid=${l.id}&id=${l.survey.id}" target="_blank">${l.sessionid}</a></td>
				<td>${l.navigator}</td>
				<td>${l.os}</td>
				<td>${l.locale}</td>
				<td>${l.ip}</td>
				<td>${l.cTime?string("yyyy-MM-dd")}</td>
			</tr>
			</#list>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>
