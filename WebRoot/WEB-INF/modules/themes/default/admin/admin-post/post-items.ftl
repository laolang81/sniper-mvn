<#assign depsSize = deps?size>
<link rel="stylesheet" href="myfiles/Plugin/zTree_v3/css/metroStyle/metroSniperStyle.css" type="text/css">
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/zTree_v3/js/jquery.ztree.core-3.5.min.js"></script>
<script type="text/javascript"
	src="${baseHref.baseHref }myfiles/Plugin/zTree_v3/js/jquery.ztree.exhide-3.5.js"></script>
    <SCRIPT type="text/javascript">
        <!--
        var setting = {
          view: {
    			showLine: true,
    			showIcon: true,
    			dblClickExpand: false
    		},
    		data: {
    			simpleData: {
    				enable: true
    			}
    		},
    		callback: {
    			onClick: onClick
    		}
          
        };

		<#assign open='false'>
		<#if deps?size lt 3>
		<#assign open='true'>
		</#if>
		
		<#assign menu>
		 <#if deps??>
        <#list deps?keys as dk>
        	{id:'${dk + 10000}',pId:'0',name:"${deps[dk]}",open:${open},isParent:true},
        	<#if items[dk]??>
        	<#list items[dk]?keys as ik>
        	{id:'${ik}',pId:'${dk + 10000}',name:"${items[dk][ik]}",url:".${baseHref.adminPath!''}/admin-post/insert?id=${ik}"},
        	</#list>
        	</#if>
        </#list>
        </#if>
		</#assign>
		
        var zNodes =[
       		${menu?substring(0, menu?length - 2)}
        ];
		
		function onClick(e, treeId, treeNode) {
			if(treeNode.url != ''){
				window.open(treeNode.url);
			}
		}
		
		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		});
        //-->
    </SCRIPT>
<h3>处室-栏目选择</h3>
<div class="zTreeDemoBackground left">
	<ul id="treeDemo" class="ztreeSniper"></ul>
</div>
