<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>${project.name!''}打分表</title>
<base href="${baseHref.webUrl }/">
<link rel="stylesheet" type="text/css" href="myfiles/score/css/style.css">
<link rel="stylesheet" type="text/css" href="myfiles/score/css/style.css" media="print">
<style type="text/css">
.table_main table tr td {border: 1px solid #ccc;padding: 5px;  text-align: center; height:25px;}
.meneame{border-radius: 4px; box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05); display: inline-block; margin: 10px 0;line-height: 30px;}
.meneame A ,.meneame SPAN.current,.meneame SPAN.disabled{-moz-border-bottom-colors: none; -moz-border-left-colors: none; -moz-border-right-colors: none; -moz-border-top-colors: none; background-color: #FFFFFF; border-color: #DDDDDD; border-image: none; border-style: solid; border-width: 1px 1px 1px 0; float: left; line-height: 20px; padding: 4px 12px; text-decoration: none;}
.meneame A:first-child{border-bottom-left-radius: 4px; border-left-width: 1px;border-top-left-radius: 4px;}
.meneame A:last-child{border-bottom-right-radius: 4px; border-top-right-radius: 4px;}
.meneame span:first-child{border-bottom-left-radius: 4px; border-left-width: 1px;border-top-left-radius: 4px;}
.meneame span:last-child{border-bottom-right-radius: 4px; border-top-right-radius: 4px;}
.meneame A:hover{BACKGROUND-COLOR: #f5f5f5;}
.meneame A:active{BACKGROUND-COLOR: #fff}
.meneame SPAN.current{color: #999999;cursor: pointer; background-color: #F5F5F5;}
.meneame SPAN.disabled{cursor: pointer;}
@media print{
	.meneame{display:none}
}
</style>
</head>

<body>
<div class="table_box" id="table_box">
	<h1 class="tproject_title">${project.name!''}打分表</h1>
    <div class="table_main">
    <#assign expertID = ''>
	<#if experts??>
	<#list experts?keys as k>
	<#assign expertID = k>
	<#break>
	</#list>
	</#if>
    <#if scoreSummaries??>
    <table width="100" border="1" bordercolor="#000000" cellpadding="2" cellspacing="0">
		<tbody>
			<tr>
				<td style="height:61px">投标单位</td>
			</tr>
			<tr>
			</tr>
			<#list scoreSummaries as ss>
		    <#assign sizeBus = 1>
		 	<#assign sizeTech = 1>
		 	<#assign sizePrice = 1>
		 	
		 	<#list ss.buss?keys as sb>
		 	<#if expertID == sb>
		 	<#assign sizeBus = ss.buss[sb]?size>
		 	</#if>
		 	</#list>
		 	<#list ss.tech?keys as sb>
		 	<#if expertID == sb>
		 	<#assign sizeTech = ss.tech[sb]?size>
		 	</#if>
		 	</#list>
		 	<#list ss.price?keys as sb>
		 	<#if expertID == sb>
		 	<#assign sizePrice = ss.price[sb]?size>
		 	</#if>
		 	</#list>
		 	<#assign sizeEn = sizeBus + sizeTech + sizePrice>
		 	<tr>
				<td style="height:${ sizeEn * 35 - 4}px">${ss.enterprise.name}</td>
			</tr>
			
		</#list>
		</tbody>
	</table>
	
    <table width="600" border="1" bordercolor="#000000" cellpadding="2" cellspacing="0">
		<tbody>
			<tr>
				<td style="height:61px">项目</td>
				<td style="height:61px">分值</td>
			</tr>
			<tr>
			<#assign expertID = ''>
			<#if experts??>
			<#list experts?keys as k>
			<#assign expertID = k>
			</#list>
			</#if>
			</tr>
		    <#list scoreSummaries as ss>
		    <#assign sizeBus = 1>
		 	<#assign sizeTech = 1>
		 	<#assign sizePrice = 1>
		 	<#list ss.buss?keys as sb>
		 	<#if expertID == sb>
		 	<#assign sizeBus = ss.buss[sb]?size>
		 	</#if>
		 	</#list>
		 	<#list ss.tech?keys as sb>
		 	<#if expertID == sb>
		 	<#assign sizeTech = ss.tech[sb]?size>
		 	</#if>
		 	</#list>
		 	<#list ss.price?keys as sb>
		 	<#if expertID == sb>
		 	<#assign sizePrice = ss.price[sb]?size>
		 	</#if>
		 	</#list>
		 	<#assign sizeEn = sizeBus + sizeTech + sizePrice>
		 	<#if ss.buss??>
		 	<#assign index = 0>
			 	<#list ss.buss?keys as sb>
			 	<#list ss.buss[sb] as sbs>
				 	<#if index == 0>
				 	<#-- 得分标题 -->
				 	<tr>
						<td rowspan="${ss.buss[sb]?size}">商务部分</td>
						<td>
							${sbs.question!''}
						</td>
					</tr>
					<#else>
					<tr>
						<td>${sbs.question!''}</td>
					</tr>
					</#if>
				<#assign index = index+1>
				</#list>
				<#break>
			 	</#list>
		 	</#if>
		 	<#if ss.tech??>
		 	<#assign index = 0>
			 	<#list ss.tech?keys as sb>
			 	<#list ss.tech[sb] as sbs>
				 	<#if index == 0>
				 	<#-- 得分标题 -->
				 	<tr>
						<td rowspan="${ss.tech[sb]?size}">技术部分</td>
						<td>
							${sbs.question!''}
						</td>
					</tr>
					<#else>
					<tr>
						<td>${sbs.question!''}</td>
					</tr>
					</#if>
				<#assign index = index+1>
				</#list>
				<#break>
			 	</#list>
		 	</#if>
		 	
		 	<#if ss.price??>
		 	<#assign index = 0>
			 	<#list ss.price?keys as sb>
			 	<#list ss.price[sb] as sbs>
				 	<#if index == 0>
				 	<#-- 得分标题 -->
				 	<tr>
						<td rowspan="${ss.price[sb]?size}">报价部分</td>
						<td>
							${sbs.question!''}
						</td>
					</tr>
					<#else>
					<tr>
						<td>${sbs.question!''}</td>
					</tr>
					</#if>
				<#assign index = index+1>
				</#list>
				<#break>
			 	</#list>
		 	</#if>
			</#list>
		</tbody>
	</table>
	<table cellpadding="2" cellspacing="0" border="1" bordercolor="#000000">
		<tbody>
			<tr>
				<td colspan="${experts?size}" >评审专家</td>
			</tr>
			<tr>
			<#list experts?keys as k>
			<td>${experts[k]}</td>
			</#list>
			</tr>
			<#list experts?keys as k>
			<td style="padding:0px; height:auto;">
			<table style="border:0px;width:100%;height: 100%;margin: 0;padding: 0; " cellpadding="2" cellspacing="0" border="0" bordercolor="#000000">
				<tbody>
				<#list scoreSummaries as ss>
				<#list ss.buss[k] as sbs>
					<tr>
						<td style="border-left:0px;border-right:0px;border-top:0px;">${sbs.standard!''}</td>
					</tr>
				</#list>
				<#list ss.tech[k] as sbs>
					<tr>
						<td style="border-left:0px;border-right:0px;border-top:0px;">${sbs.standard!''}</td>
					</tr>
				</#list>
				<#list ss.price[k] as sbs>
					<tr>
						<td style="border-left:0px;border-right:0px;border-top:0px;">${sbs.standard!''}</td>
					</tr>
				</#list>
				
				</#list>
				</tbody>
			</table>
			</td>
			</#list>
			</tr>
		</tbody>
	</table>
	
	<table cellpadding="2" cellspacing="0" border="1" bordercolor="#000000">
		<tbody>
			<tr>
				<td style="height:61px;">总得分</td>
			</tr>
			<#list scoreSummaries as ss>
			<#assign sizeBus = 1>
		 	<#assign sizeTech = 1>
		 	<#assign sizePrice = 1>
		 	<#list ss.buss?keys as sb>
		 	<#if expertID == sb>
		 	<#assign sizeBus = ss.buss[sb]?size>
		 	</#if>
		 	</#list>
		 	<#list ss.tech?keys as sb>
		 	<#if expertID == sb>
		 	<#assign sizeTech = ss.tech[sb]?size>
		 	</#if>
		 	</#list>
		 	<#list ss.price?keys as sb>
		 	<#if expertID == sb>
		 	<#assign sizePrice = ss.price[sb]?size>
		 	</#if>
		 	</#list>
		 	<#assign sizeEn = sizeBus + sizeTech + sizePrice>
			<tr>
			<td style="height:${ sizeEn * 35 - 4}px">${ss.sum!''}<br>(${ss.avg})</td>
			</tr>
			</#list>
		</tbody>
	</table>
	</#if>
  	</div>
    <div class="table_sign">
    	<span>各评委（签字）：</span>
        <strong>机关纪委（签字）：</strong>
    </div>
    <div class="table_note">
    	<h2>注：</h2>
        <ul>
        	<li>仅对“商务事项评议”合格的响应人进行评议；</li>
        	<li>“评议”栏中填写“接受”或“不接受”；</li>
        	<li>“结论”栏中填写“合格”或“不合格”</li>
        	<li>凡有一项主要技能标准不接受者，其结论即为“不合格”</li>
        </ul>
    </div>
</div><!--
<script>
function myPrint(obj){
    var newWindow=window.open("打印窗口","_blank");//打印窗口要换成页面的url
    var docStr = obj.innerHTML;
    newWindow.document.write(docStr);
    newWindow.document.close();
    newWindow.print();
    newWindow.close();
}
</script>
<button onclick="myPrint(document.getElementById('table_box'))">打 印</button>-->
</body>
</html>