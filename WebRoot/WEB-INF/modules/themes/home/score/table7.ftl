<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>${project.name!''}打分表</title>
<base href="${baseHref.webUrl }/">
<link rel="stylesheet" type="text/css" href="myfiles/score/css/style.css">
<link rel="stylesheet" type="text/css" href="myfiles/score/css/style.css" media="print">
<style type="text/css">
.table_main table tr td {border: 1px solid #ccc;padding: 5px;  text-align: center;}
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
	<h1 class="tproject_title">${project.name!''}打分表(${expert.name!''})</h1>
    <div class="table_main">
    <table width="600" border="1" bordercolor="#000000" cellpadding="0" cellspacing="0">
		<tbody>
			<tr>
				<td width="50" height="100">序号</td>
				<td width="100">商务部分</td>
				<td width="50">分值</td>
				<td width="400">评分依据</td>
			</tr>
			<#if standardNames['score_buss']??>
			<#assign index = 0>
			<#list standardNames['score_buss'] as s>
			<#if index == 0>
			<tr>
				<td rowspan="${standardNames['score_buss']?size}">1</td>
				<td rowspan="${standardNames['score_buss']?size}">商务部分</td>
				<td>${s.question!''}</td>
				<td>${s.standard!''}</td>
			</tr>
			<#else>
			<tr>
				<td>${s.question!''}</td>
				<td>${s.standard!''}</td>
			</tr>
			</#if>
			<#assign index = index +1>
			</#list>
			</#if>
			<#if standardNames['score_tech']??>
			<#assign index = 0>
			<#list standardNames['score_tech'] as s>
			<#if index == 0>
			<tr>
				<td rowspan="${standardNames['score_tech']?size}">2</td>
				<td rowspan="${standardNames['score_tech']?size}">技术部分</td>
				<td>${s.question!''}</td>
				<td>${s.standard!''}</td>
			</tr>
			<#else>
			<tr>
				<td>${s.question!''}</td>
				<td>${s.standard!''}</td>
			</tr>
			</#if>
			<#assign index = index +1>
			</#list>
			</#if>
			<#if standardNames['score_price']??>
			<#assign index = 0>
			<#list standardNames['score_price'] as s>
			<#if index == 0>
			<tr>
				<td rowspan="${standardNames['score_price']?size}">3</td>
				<td rowspan="${standardNames['score_price']?size}">报价部分</td>
				<td>${s.question!''}</td>
				<td>${s.standard!''}</td>
			</tr>
			<#else>
			<tr>
				<td>${s.question!''}</td>
				<td>${s.standard!''}</td>
			</tr>
			</#if>
			<#assign index = index +1>
			</#list>
			</#if>
			<tr>
				<td colspan="4">得分汇总</td>
			</tr>
		</tbody>
	</table>
	<table border="1" bordercolor="#000000" cellpadding="0" cellspacing="0" >
		<tbody>
			<tr>
				<td height="44" colspan="${scores?size}">投标单位</td>
			</tr>
			
			<tr>
				<td style="padding:0px">
				<#if scores??>
		        <#list scores as  s>
				<table border="0" bordercolor="#000000" cellpadding="0" cellspacing="0" style="margin-top:0px">
				<tbody>
					<tr>
						<td height="44">${s.enterprise.name}</td>
					</tr>
					<#if pssScoreBuss?? && pssScoreBuss[s.enterprise.id]??>
					<#list pssScoreBuss[s.enterprise.id] as ps>
					<tr>
					<td>${ps.review!''}</td>
					</tr>
					</#list>
					</#if>
					<#if pssScoreTech?? && pssScoreTech[s.enterprise.id]??>
					<#list pssScoreTech[s.enterprise.id] as ps>
					<tr>
					<td>${ps.review!''}</td>
					</tr>
					</#list>
					</#if>
					<#if pssScorePrice?? && pssScorePrice[s.enterprise.id]??>
					<#list pssScorePrice[s.enterprise.id] as ps>
					<tr>
					<td>${ps.review!''}</td>
					</tr>
					</#list>
					</#if>
					<tr>
					<td>${scoreSum[s.enterprise.id]}</td>
					</tr>
				</tbody>
			</table>
			</#list>
			</#if>
			</td>
			</tr>
		</tbody>
	</table>
  </div>
  <div class="meneame">${pageHtml }</div>
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