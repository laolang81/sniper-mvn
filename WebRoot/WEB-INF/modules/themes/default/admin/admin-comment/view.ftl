<style type="text/css">
.gbook{ overflow: hidden; padding: 10px;width: auto;}
.gbook li{ position: relative;margin-top:10px;border: 1px solid #E5E5E5;border-radius: 3px 3px 3px 3px;box-shadow: 0 4px 10px -1px rgba(200, 200, 200, 0.7);line-height: 25px;overflow: hidden;padding: 5px; }
.gbook li:first-child{margin-top:0}
.gbook span {padding: 0 10px 0 0;font-size: 13px;}
.gbook a{padding: 0 5px 0 0;display: block;position: absolute;top: 0px; font-size: 18px;}
.gbook .title {margin-top:5px; border-bottom: 1px solid #f1f1f1; margin-bottom: 5px;position: relative;text-indent: 5px;height:35px;line-height: 30px;}

.gbook .bulb {right: 10px; }
.gbook .bulb label{margin-top: 3px;}
 
.gbook .del{ right: 140px; }
.gbook .retweet{ right: 0px; }
.gbook .replay{ right: 0px;}
.gbook .print{ right: 105px;}
.gbook .dep{  position: absolute;right: 30px; }

.gbook .title b{padding-left: 10px;}
.gbook .content{border: 1px solid #E5E5E5;border-radius: 3px 3px 3px 3px;box-shadow: 0 4px 10px -1px rgba(200, 200, 200, 0.7); padding: 10px;}
.gbook .content-title{ padding: 5px;color: rgb(33, 117, 155);}
.gbook .rel_content{text-align: center;overflow: hidden;margin: 5px;}
.gbook .textarea{padding: 5px;width: 97%;}
.gbook .replyLi{color: #666;}
.gbook .replyLi .content-title{color: #666;}
.gbook .replyLi .textarea{color: #666;}
.gbook .reply { right: 0;}
</style>

<div class="seach">
	<form method="get" action="" role="form" class="form-inline" id="groupSearch">
	
		<div class="form-group">
			<label for="startDate" class="sr-only">开始时间</label>
			<input id="startDate" name="startDate" class="form-control Wdate" type="text" value="">
			- <label for="endDate" class="sr-only">结束时间</label>
			<input id="endDate" name="endDate" class="form-control Wdate" type="text" value="">
		</div>
			
		<div class="form-group">
			<label class="sr-only" for="">状态选择</label>
			<select title="是否是显示" class="form-control" name="flag" id="isShow">
				<#if status??>
				<#list status?keys as f>
				<#if commentSearch.flag??>
				<option <#if commentSearch.flag?c == f >selected="selected"</#if> value="${f}">${status[f]}</option>
				<#else>
				<option value="${f}">${status[f]}</option>
				</#if>
				</#list>
				</#if>
			</select>
		</div>
		<div class="form-group">
			<label class="sr-only" for="siteidSelect">状态选择</label>
			<select id="siteidSelect" class="form-control" name="siteid">
				<#if departments??>
					<#list departments?keys as d>
						<option value="${d}" <#if commentSearch.siteid?? && commentSearch.siteid?c ==  d >selected="selected"</#if>>${departments[d]}</option>
					</#list>
				</#if>
			</select>
		</div>
		
		<div class="form-group">
			<label class="sr-only" for="">名称</label>
			<input type="text" value="${commentSearch.name!''}" title="留言人搜索" placeholder="留言人搜索" class="form-control" name="name" id="name">
		</div>

		<div class="form-group">
			<button value="search" name="submit" type="submit" class="btn btn-success">查询</button>
		</div>

	</form>
</div>

<ul class="gbook" id="gbook">
	<#if lists??>
	<#list lists as l>
	<li id="${l.id}" data-url=".${baseHref.adminPath!''}/admin-comment/handle" data-type="${l.state!'0'}">
		<div class="title">
			<span title="留言ID">${l.id!''}:</span>
			<span title="留言者的名称">${l.user!'名称'}</span>
			<span title="留言者的邮箱">${l.email!'Email'}</span>
			<span title="留言者的电话">${l.tel!'电话'}</span>
			<span title="留言者的时间">${l.time?string("yyyy-MM-dd")}</span>
			<span title="留言者的IP">${l.ip!''}</span>
			<span title="留言类型">${type[l.type?c]!''}</span>
			<a href="javascript:void(0)" class="print oper" data-oper="1" data-url=".${baseHref.adminPath}/admin-print/print?id=${l.id}" title="打印当前留言" data-action="Print">
				<i class="fa fa-print"></i>
			</a>
			<a class="bulb" data-oper="3" title="显示当前留言-${l.bShow!''}" data-action="Enable">
				<label>
					<input type="checkbox" class="ace ace-switch" name="bShow" <#if l.bShow?? && l.bShow  == 1>checked</#if>>
					<span class="lbl oper" data-oper="3" title="是否显示当前留言-${l.bShow!''}" data-action="Enable" data-lbl="显&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;否"></span>
				</label>
			</a>
			<a href="javascript:void(0)" class="del oper" data-oper="2" title="放入回收站" data-action="${delete}">
				<i class="fa fa-trash-o"></i>
			</a>
		</div>
		<div class="content">${l.content!''}</div>
		<div class="title">
			<#if l.answerTime??>
				<span title="回复时间">时间：${l.answerTime?string("yyyy-MM-dd")}</span>
				<span title="回复时间">回复时间差：${l.answerDiff!'0'}天</span>
			</#if>
			<#if l.officeMoveTime??>
				<span title="回复时间">处室时间：${l.officeMoveTime?string("yyyy-MM-dd")}</span>
			</#if>
			
			<select class="dep" name="Departments">
				<#if departments??>
				<#list departments?keys as k>
				<option value="${k}" <#if l.depId?c == k>selected="selected"</#if>>${departments[k]}</option>
				</#list>
				</#if>
			</select>
			<a data-action="Offices" title="变更处室" data-oper="2" class="reply oper" href="javascript:void(0)">
			<i class="fa fa-mail-forward"></i></a>
		</div>
		<div class="back">
			<textarea id="r${l.id}" class="textarea" cols="68" rows="3">${l.answer!'暂未回答'}</textarea>
		</div>
		<div class="title">
			<select onchange="$('#r${l.id}').val($('#r${l.id}').val() + this.value);" class="dep">
			<option value="">---快捷回复---</option>
			<option value="感谢您的留言.">感谢您的留言。</option>
			<option value="欢迎您继续关注我们的工作并提出宝贵的意见。">欢迎您继续关注我们的工作并提出宝贵的意见。</option>
			<option value="我们会认真考虑您的建议。">我们会认真考虑您的建议。</option>
			<option value="欢迎您直接致电我们。电话：">欢迎您直接致电我们。电话：</option>
			</select>
			<a href="javascript:void(0)" class="replay oper" data-oper="2" title="回复当前留言" data-action="Reply">
				<i class="fa fa-comment"></i>
			</a>
		</div>
	</li>
	</#list>
	</#if>
</ul>

<div class="meneame">${pageHtml }</div>
<script type="text/javascript" src="${baseHref.baseHref }myfiles/js/jquery.sniper.comment.js"></script>
<script>
//插件应用
$(function(){
	$().sniperComment();
});
</script>
<script type="text/javascript" src="${baseHref.baseHref }myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(document).delegate('.Wdate', 'click', function() {
		WdatePicker({
			dateFmt : 'yyyy-MM-dd'
		})
	});
</script>
<div class="alert alert-success alert-dismissible" role="alert">
	<button type="button" class="close" data-dismiss="alert">
		<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
	</button>
	<ul>
		<li><i class="fa fa-trash-o ml2"></i> 删除</li>
		<li><i class="fa fa-print ml2" ></i> 打印</li>
		<li><i class="fa fa-mail-forward ml2"></i> 转移处室</li>
		<li><i class="fa fa-comment ml2"></i> 回复留言</li>
	</ul>
</div>
