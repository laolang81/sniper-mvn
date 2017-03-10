<ul class="list-group">
  <li class="list-group-item">
    <span class="badge">${threadMXBean.threadCount}</span>
   	线程总数
  </li>
  <li class="list-group-item">
    <span class="badge">${threadMXBean.daemonThreadCount}</span>
   	守护线程
  </li>
  <li class="list-group-item">
    <span class="badge">${threadMXBean.peakThreadCount}</span>
   	线程峰值
  </li>
  <li class="list-group-item">
    <span class="badge">${threadMXBean.totalStartedThreadCount}</span>
   	启动以来线程总数目
  </li>
  <li class="list-group-item">
    <span class="badge">${threadMXBean.currentThreadCpuTime}</span>
   	线程CPU时间
  </li>
  <li class="list-group-item">
    <span class="badge">${threadMXBean.currentThreadUserTime}</span>
   	线程用户时间
  </li>
</ul>
<p>线程数： ${lists?size}</p>
<table class="table table-hover">
	<thead>
		<tr>
			<th>线程名称</th>
			<th>CPU时间</th>
			<th>USER时间</th>
			<th>STATE</th>
			<th>线程组</th>
			<th>优先级</th>
			<th>活动</th>
			<th>守护进程</th>
			<td>阻塞</td>
		</tr>
	</thead>
	<tbody>
			<#if lists??>
			<#list lists as j>
			<tr title="${j.name}">
				<td>${freeMarkerUtils.substr(j.name,50)}</td>
				<td>${j.cpuTime}</td>
				<td>${j.userTime}</td>
				<td>${j.state}</td>
				<td>${threadMap[j.id?c]['threadGroupName']}</td>
				<td>${threadMap[j.id?c]['priority']}</td>
				<td>${threadMap[j.id?c]['alive']}</td>
				<td>${threadMap[j.id?c]['daemon']}</td>
				<td>${threadMap[j.id?c]['interrupted']}</td>
				
			</tr>
			</#list>
			</#if>
	</tbody>
</table>