<form id="sniperForm" name="sniperForm" action="" method="post" class="form-horizontal" role="form">
	
	<div class="form-group">
		<label for="id" class="col-sm-2 control-label">新闻ID</label>
		<div class="col-sm-8">
			<input type="text" name="id" value="${search.id!''}" class="form-control"/>
			<div class="help-block">单独拉去某一篇新闻图片(对RunOnce有效,当新闻ID大于0,下面2个参数无效)</div>
		</div>
	</div>
	
	<div class="form-group">
		<label for="sort" class="col-sm-2 control-label">开始时间</label>
		<div class="col-sm-8">
			<input type="text" name="startDate" value="${search.startDate!''}" class="form-control Wdate"/>
			<div class="help-block">新闻图片拉取的开始时间(对RunOnce有效)</div>
		</div>
	</div>
	
	<div class="form-group">
		<label for="sort" class="col-sm-2 control-label">结束时间</label>
		<div class="col-sm-8">
			<input type="text" name="endDate" value="${search.endDate!''}" class="form-control Wdate"/>
			<div class="help-block">新闻图片拉取的结束时间(对RunOnce有效)</div>
		</div>
	</div>
	
	<div class="form-group">
		<label for="sort" class="col-sm-2 control-label">运行时间表达式</label>
		<div class="col-sm-8">
			<input type="text" name="cron" value="${search.cron!'0 0 23 * * ?'}" class="form-control" placeholder=""/>
			<div class="help-block">举例(每日23点)0 0 23 * * ?(对RunOnce无效),当执行任务后不要立马查看图片请等待后台操作完成.</div>
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
			<button type="submit" name="submit" value="RunOnce" class="btn btn-success">RunOnce</button>
			<div class="help-block">RunOnce 运行一次程序,上面的条件有效</div>
		</div>
	</div>
</form>

<div class="alert alert-success" role="alert">表达式备忘：<br />
<br />
字段 允许值 允许的特殊字符<br />
<br />
秒 0-59 , – * /<br />
<br />
分 0-59 , – * /<br />
<br />
小时 0-23 , – * /<br />
<br />
日期 1-31 , – * ? / L W C<br />
<br />
月份 1-12 或者 JAN-DEC , – * /<br />
<br />
星期 1-7 或者 SUN-SAT , – * ? / L C #<br />
<br />
年（可选） 留空, 1970-2099 , – * /<br />
<br />
表达式意义<br />
<br />
"0 0 12 * * ?" 每天中午12点触发<br />
<br />
"0 15 10 ? * *" 每天上午10:15触发<br />
<br />
"0 15 10 * * ?" 每天上午10:15触发<br />
<br />
"0 15 10 * * ? *" 每天上午10:15触发<br />
<br />
"0 15 10 * * ? 2005" 2005年的每天上午10:15触发<br />
<br />
"0 * 14 * * ?" 在每天下午2点到下午2:59期间的每1分钟触发<br />
<br />
"0 0/5 14 * * ?" 在每天下午2点到下午2:55期间的每5分钟触发<br />
<br />
"0 0/5 14,18 * * ?" 在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发<br />
<br />
"0 0-5 14 * * ?" 在每天下午2点到下午2:05期间的每1分钟触发<br />
<br />
"0 10,44 14 ? 3 WED" 每年三月的星期三的下午2:10和2:44触发<br />
<br />
"0 15 10 ? * MON-FRI" 周一至周五的上午10:15触发<br />
<br />
"0 15 10 15 * ?" 每月15日上午10:15触发<br />
<br />
"0 15 10 L * ?" 每月最后一日的上午10:15触发<br />
<br />
"0 15 10 ? * 6L" 每月的最后一个星期五上午10:15触发<br />
<br />
"0 15 10 ? * 6L 2002-2005" 2002年至2005年的每月的最后一个星期五上午10:15触发<br />
<br />
"0 15 10 ? * 6#3" 每月的第三个星期五上午10:15触发<br />
<br />
每天早上6点<br />
<br />
0 6 * * *<br />
<br />
每两个小时<br />
<br />
0 */2 * * *<br />
<br />
晚上11点到早上8点之间每两个小时，早上八点<br />
<br />
0 23-7/2，8 * * *<br />
<br />
每个月的4号和每个礼拜的礼拜一到礼拜三的早上11点<br />
<br />
0 11 4 * 1-3<br />
<br />
1月1日早上4点<br />
<br />
0 4 1 1 *<br />
</div>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
	
<script type="text/javascript">
	$(function() {
		$('.Wdate').bind('click', function() {
			WdatePicker({
				dateFmt : 'yyyy-MM-dd',
				lang : 'zh-cn'
			})
		});
	});
</script>	
