<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>符合性检查表</title>
<base href="${baseHref.webUrl }/">
<link rel="stylesheet" type="text/css" href="myfiles/score/css/style.css">
<link rel="stylesheet" type="text/css" href="myfiles/score/css/style.css" media="print">
</head>

<body>
<div class="table_box" id="table_box">
	<h1 class="tproject_title">符合性检查表</h1>
    <span class="project_number">竞争性评审项目编号：${project.type!''}</span>
    <span class="project_name">购买服务项目名称：${project.name!''}</span>
    <div class="table_main">
    	<table width="300" border="1">
            <thead>
            <tr>
              <td height="60">序号</td>
              <td height="60"><img src="myfiles/score/images/1.jpg" width="246" height="50" alt=""/></td>
            </tr>
       		</thead>
          <tbody>
            <tr>
              <td>1</td>
              <td height="30">响应文件</td>
            </tr>
            <tr>
              <td>2</td>
              <td height="30">法定代表人授权书</td>
            </tr>
            <tr>
              <td>3</td>
              <td height="30">资格证明文件</td>
            </tr>
            <tr>
              <td>4</td>
              <td height="30">重要商务、技术、价格条款</td>
            </tr>
            <tr>
              <td>5</td>
              <td height="30">投标分享报价表</td>
            </tr>
            <tr>
              <td>6</td>
              <td height="30">结论</td>
            </tr>
          </tbody>
        </table>
        <#if scores??>
        <#assign index = 1>
        <#list scores as  s>
        <ul class="table_list">
   	  	  <li><span>${s.enterprise.name}</span></li>
        	<li>${s.responseFile!''}</li>
        	<li>${s.authLetter!''}</li>
        	<li>${s.certInfo!''}</li>
        	<li>${s.importantPrice!''}</li>
        	<li>${s.quotationSheet	!''}</li>
        	<li>${s.conclusion!''}</li>
        </ul>
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
        	<li>相应文件有法定人代表签署时，无需提供法定代表人授权书。</li>
        	<li>表中1----5项只需要填写“有”或“无”</li>
        	<li>在结论栏中填写“合格”或“不合格”</li>
        	
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