<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>技术事项评议表</title>
<base href="${baseHref.webUrl }/">
<link rel="stylesheet" type="text/css" href="myfiles/score/css/style.css">
<link rel="stylesheet" type="text/css" href="myfiles/score/css/style.css" media="print">
</head>

<body>
<div class="table_box" id="table_box">
	<h1 class="tproject_title">技术事项评议表</h1>
    <span class="project_number">竞争性评审项目编号：${project.type!''}</span>
    <span class="project_name">购买服务项目名称：${project.name!''}</span>
    <div class="table_main">
    	<table width="280" border="1">
            <thead>
            <tr>
              <td height="60" colspan="2" align="left">响应人/省（市）</td>
              </tr>
       		</thead>
          <tbody>
            <tr>
              <td height="30" colspan="2">代理服务商/省（市）</td>
            </tr>
            <tr>
              <td height="30" colspan="2">完成时限</td>
            </tr>
            <tr>
              <td height="30" colspan="2">数量</td>
            </tr>
            <tr>
              <td height="30" colspan="2" align="center">响应文件要求</td>
            </tr>
            <#if standardNames?? && standardNames['main']??>
            <#assign index = 0>
            <#list standardNames['main'] as sn>
            <#if sn??>
            <#if index == 0>
            <tr>
              <td width="33" height="30" rowspan="${standardNames['main']?size}">主要标准</td>
              <td width="451" height="30">${sn}</td>
            </tr>
            <#else>
            <tr>
              <td height="30">${sn}</td>
            </tr>
            </#if>
            <#assign index =index+1>
            </#if>
            </#list>
            </#if>
            <#if standardNames?? && standardNames['general']??>
            <#assign index = 0>
            <#list standardNames['general'] as sn>
            <#if index == 0>
            <tr>
              <td width="33" height="-2" rowspan="${standardNames['main']?size}">一般标准</td>
              <td width="451" height="30">${sn}</td>
            </tr>
           <#else>
            <tr>
              <td height="30">${sn}</td>
            </tr>
            </#if>
            <#assign index =index+1>
            </#list>
            </#if>
            <tr>
              <td height="30" colspan="2">结论</td>
            </tr>
          </tbody>
        </table>
        <#if scores??>
        <#list scores as  s>
        <table width="" border="1">
           <thead>
            <tr>
              <td height="60" colspan="2">${s.enterprise.name}</td>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td height="30" colspan="2">${s.address!''}</td>
            </tr>
            <tr>
              <td height="30" colspan="2">${s.endDate!''}</td>
            </tr>
            <tr>
              <td height="30" colspan="2">${s.techNum!''}</td>
            </tr>
            <tr>
              <td width="" height="30" align="center">技术标准</td>
              <td width="" height="30" align="center">评议</td>
            </tr>
            <#if pssMain?? && pssMain[s.enterprise.id]??>
            <#list pssMain[s.enterprise.id] as pm>
            <tr>
              <td height="30">${pm.standard!''}</td>
              <td height="30">${pm.review!''}</td>
            </tr>
            </#list>
            </#if>
            <#if pssGeneral?? && pssGeneral[s.enterprise.id]??>
            <#list pssGeneral[s.enterprise.id] as pm>
            <tr>
              <td height="30">${pm.standard!''}</td>
              <td height="30">${pm.review!''}</td>
            </tr>
            </#list>
            </#if>
            <tr>
             <td height="30" colspan="2">${s.techConclusion!''}</td>
            </tr>
          </tbody>
        </table>
        </#list>
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