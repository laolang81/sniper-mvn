<table class="table table-hover">
	<thead>
		<tr>
			<th>任务名称</th>
			<th>任务组</th>
			<th>时间表达式</th>
			<th>状态</th>
			<th>备注</th>
		</tr>
	</thead>
	<tbody>
			<#if jobs??>
			<#list jobs as j>
			<tr>
				<td>${j.jobName!''}</td>
				<td>${j.jobGroup!''}</td>
				<td>${j.cronExpression!''}</td>
				<td>${j.jobStatus!''}</td>
				<td>${j.desc!''}</td>
			</tr>
			</#list>
			</#if>
	</tbody>
</table>