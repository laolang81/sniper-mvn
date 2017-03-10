<style type="text/css">
.gbook{ overflow: hidden; padding: 10px;width: auto;}
.gbook li{ position: relative;margin-top:10px;border: 1px solid #E5E5E5;border-radius: 3px 3px 3px 3px;box-shadow: 0 4px 10px -1px rgba(200, 200, 200, 0.7);line-height: 25px;overflow: hidden;padding: 5px; }
.gbook li:first-child{margin-top:0}
.gbook span {padding: 0 10px 0 0;font-size: 13px;}
.gbook a {padding: 0 5px 0 0;display: block;position: absolute;top: 0px;}
.gbook .title { border-bottom: 1px solid #f1f1f1; margin-bottom: 5px;position: relative;text-indent: 5px;height:30px;line-height: 30px;}

.gbook .bulb {right: 5px; }
.gbook .bulb label{margin-top: 5px; }
.gbook .del{ right: 100px; }
.gbook .retweet{ right: 0px; }
.gbook .reply{ right: 0px;}
.gbook .print{ right: 80px;}
.gbook .dep{  position: absolute;right: 25px; }

.gbook .title b{padding-left: 10px;}
.gbook .content{border: 1px solid #E5E5E5;border-radius: 3px 3px 3px 3px;box-shadow: 0 4px 10px -1px rgba(200, 200, 200, 0.7); padding: 5px;}
.gbook .content-title{ padding: 5px;color: rgb(33, 117, 155);}
.gbook .rel_content{text-align: center;overflow: hidden;margin: 5px;}
.gbook .textarea{padding: 5px;width: 97%;}
.gbook .replyLi{color: #666;}
.gbook .replyLi .content-title{color: #666;}
.gbook .replyLi .textarea{color: #666;}
.gbook .openapply{}
.gbook .openapply span{ display: inline-block; width: 33%; padding:0px;}
</style>

<div class="seach">
	<form method="get" action="" role="form" class="form-inline" id="groupSearch">
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
	<li id="${l.id}" data-url=".${baseHref.adminPath!''}/admin-comment/openapplyhandle" data-type="${l.state!'0'}">
		<div class="openapply">
			<span title="姓名">姓名:${l.username}</span>
			<span title="工作单位">工作单位:${l.workunit!''}</span>
			<span title="电话">电话:${l.tel!''}</span>
			<span title="邮箱">邮箱:${l.email!''}</span>
			<span title="证件">证件:${l.idname!''}</span>
			<span title="地址">地址:${l.address!''}</span>
			<span title="邮政">邮政:${l.postcode!''}</span>
			<span title="申请时间">申请时间:${l.stime?string("yyyy-MM-dd")}</span>
			
			<span title="法人或组织名称">法人或组织名称:${l.frname!''}</span>
			<span title="法人或组织组织结构代码">机构代码：${l.frcode!''}</span>
			<span title="法人或组织营业执照">营业执照：${l.frzhizhao!''}</span>
			<span title="法人或组织法人代表">法人代表：${l.frdaibiao!''}</span>
			<span title="法人或组织联系人">联系人：${l.frmaster!''}</span>
			<span title="法人或组织电话">电话：${l.frtel!''}</span>
			<span title="法人或组织邮箱">邮箱：${l.fremail!''}</span>
			
			<span title="所需信息索取号">索取号：${l.xtnum!''}</span>
			<span title="所需信息的用途">信息用途：${l.xtuse!''}</span>
			<span title="是否减免费用">减免费用：${l.xtmoney!''}</span>
			<span title="信息的指定提供方式">信息提供方式：${l.xttype!''}</span>
			<span title="获取信息的方式">信息获取方式：${l.xtinfo!''}</span>
			
			<a href="javascript:void(0)" class="print oper" data-oper="1" data-url=".${baseHref.adminPath}/admin-print/openapplyprint?id=${l.id}" title="打印当前留言" data-action="Print" >
			<i class="fa fa-print"></i></a>
			<span>${l.ip!''}</span>
			<a href="javascript:void(0)" class="del oper" data-oper="2" data-url=".${baseHref.adminPath}/admin-comment/openapplyhandle" title="删除" data-action="DeleteTrue">
			<i class="fa fa-trash-o"></i></a>
			<a class="bulb" data-oper="2"  data-url=".${baseHref.adminPath}/admin-comment/openapplyhandle" title="是否处理-${l.enabled!''}" data-action="Enable">
				<label>
					<input type="checkbox" class="ace ace-switch" name="enabled" <#if l.enabled?? && l.enabled  == 1>checked</#if>>
					<span class="lbl oper" data-oper="3" title="是否处理-${l.enabled!''}" data-action="Enable" data-lbl="已&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;未"></span>
				</label>
			</a>
			
		</div>
		<div class="title">
			<span title="">(<#if l.xtothertype?? && l.xtothertype == 'true'>是<#else>否</#if>)若本机无法按照指定方式提供所需信息,也可以接受其他方式</span>
		</div>
		<div class="content-title">${l.content!''}</div>
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
<div class="alert alert-success alert-dismissible" role="alert">
	<button type="button" class="close" data-dismiss="alert">
		<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
	</button>
	<ul>
		<li><i class="fa fa-trash-o ml2"></i> 删除</li>
		<li><i class="fa fa-print ml2" ></i> 打印</li>
	</ul>
</div>