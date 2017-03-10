<table class="table table-hover">
	<thead>
		<tr>
			<th>任务名称</th>
			<th>任务组</th>
			<th>时间表达式</th>
			<th>状态</th>
			<th>备注</th>
			<th>操作</th>
		</tr>
	</thead>
	<tbody>
			<#if jobs??>
			<#list jobs as j>
			<tr>
				<td>${j.jobName}</td>
				<td>${j.jobGroup}</td>
				<td>${j.cronExpression}</td>
				<td>${j.jobStatus}</td>
				<td>${j.desc}</td>
				<td>
				<form action="" method="post" class="form-horizontal" role="form">
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
				<div class="btn-group">
				<input type="hidden" name="searchString.jobName" value="${j.jobName}" />
				<#switch j.jobStatus>
				<#case "None">
				<button type="submit" name="searchString.submit" value="start" class="btn btn-default">开始</button>
				<#break>
				<#case "NORMAL">
				<button type="submit" name="searchString.submit" value="paused" class="btn btn-default">暂停</button>
				<#break>
				<#case "PAUSED">
				<button type="submit" name="searchString.submit" value="resume" class="btn btn-default">继续</button>
				<#break>
				</#switch>
				<#if j.jobStatus != "None">
				<button type="submit" name="searchString.submit" value="delete" class="btn btn-default">删除</button>
				</#if>
			</div>
			</form></td>
			</tr>
			</#list>
			</#if>
	</tbody>
</table>