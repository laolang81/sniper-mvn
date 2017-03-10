<div class="seach">
	<form method="get" action="" role="form" class="form-inline" id="search">

		<div class="form-group">
			<label for="" class="sr-only">名称</label>
			<form:input title="名称" path="name" cssClass="form-control" />
		</div>
		<div class="form-group">
			<button class="btn btn-success" type="submit" name="submit"
				value="search">查询</button>
		</div>
	</form>
</div>
<table class="table table-hover">
	<thead>
		<tr>
			<th>本地文件</th>
			<th>远程文件</th>
			<th>状态</th>
			<th>开始时间</th>
			<td>备注</td>
		</tr>
	</thead>
	<tbody>
		<#if lists??>
		<#list lists as l>
			<tr id="sl_${l.id}">
				<td data-type="name">${l.localFile }</td>
				<td data-type="name">${l.remoteFile}</td>
				<td data-type="name">${l.enabled}</td>
				<td data-type="status">${l.ctime?string("yyyy-MM-dd")}</td>
				<td data-type="status">${l.note}</td>
			</tr>
		</#list>
		</#if>
	</tbody>
</table>
<div class="meneame">${pageHtml }</div>