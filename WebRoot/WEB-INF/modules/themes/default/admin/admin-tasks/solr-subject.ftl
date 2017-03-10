<form id="sniperForm" name="sniperForm" action="" method="post" class="form-horizontal" role="form">
	<div class="form-group">
		<label for="id" class="col-sm-2 control-label">新闻总数量</label>
		<div class="col-sm-8">
			<p class="form-control-static">${count!'0'}</p>
		</div>
	</div>
	<div class="form-group">
		<label for="id" class="col-sm-2 control-label">Solr拥有文档数</label>
		<div class="col-sm-8">
			<p class="form-control-static">${solrCount!'0'}</p>
		</div>
	</div>
	<div class="form-group">
		<label for="id" class="col-sm-2 control-label">最大线程数</label>
		<div class="col-sm-8">
			<input type="text" name="maxThreadCount" value="${search.maxThreadCount!''}" class="form-control"/>
			<div class="help-block">规定线程最大数量。</div>
		</div>
	</div>
	
	<div class="form-group">
		<label for="id" class="col-sm-2 control-label">单线程处理新闻数</label>
		<div class="col-sm-8">
			<input type="text" name="pageSize" value="${search.pageSize!''}" class="form-control"/>
		</div>
	</div>
	
	<div class="form-group">
		<label for="pageOffset" class="col-sm-2 control-label">新闻起始ID</label>
		<div class="col-sm-8">
			<input type="text" name="greaterThenId" value="${search.greaterThenId!''}" class="form-control"/>
			<div class="help-block">
				<p>设置新闻读取开始ID,不包含当前ID。</p>
				<p><#if lastModel??>当前Solr最后新闻ID:${lastModel.sid!''}</#if><p>
				<p><#if lastViewSubject??>当前最后新闻ID:${lastViewSubject.id!''}</#if></p>
			</div>
		</div>
	</div>
	
	
	<div class="form-group">
		<div class="col-sm-10 col-md-offset-2">
			<button type="submit" name="submit" value="<#if job??>
				<#switch job.jobStatus>
				<#case "None">
				Start
				<#break>
				<#case "NORMAL">
				Pause
				<#break>
				<#case "PAUSED">
				Continue
				<#break>
				<#case "COMPLETE">
				Stop
				<#break>
				<#case "BLOCKED">
				Blocking
				<#break>
				<#case "ERROR">
				ERROR
				<#break>
				<#default>
				Start
				</#switch>
			<#else>
			Start
			</#if>" class="btn btn-success">
			<#if job??>
				<#switch job.jobStatus>
				<#case "None">
				Start
				<#break>
				<#case "NORMAL">
				Pause
				<#break>
				<#case "PAUSED">
				Continue
				<#break>
				<#case "COMPLETE">
				Stop
				<#break>
				<#case "BLOCKED">
				Blocking
				<#break>
				<#case "ERROR">
				ERROR
				<#break>
				<#default>
				Start
				</#switch>
			<#else>
			Start
			</#if>
			</button>
			<#if job??>
			<#if job.jobStatus=="NORMAL" || job.jobStatus=="PAUSED">
			<button type="submit" name="submit" value="Stop" class="btn btn-success">Stop</button>
			</#if>
			</#if>
		</div>
	</div>
</form>

<div class="alert alert-success" role="alert">
	<p>本调度只有在Solr初始化的时候使用，新闻的添加删除Solr会自动处理.</p>
	<p>根据新闻开始位置和结束位置每次操作多少新闻。</p>
</div>
