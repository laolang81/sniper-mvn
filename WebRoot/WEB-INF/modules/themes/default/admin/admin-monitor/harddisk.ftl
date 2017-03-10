<div class="panel panel-default">
  <!-- Default panel contents -->
  <div class="panel-heading">硬盘信息</div>
  <div class="panel-body">
    <p>介绍硬盘每个分区的信息</p>
  </div>

  <!-- Table -->
  <table class="table">
      <thead>
        <tr>
          <th>名称</th>
          <th>路径</th>
          <th>标示</th>
          <th>类型(名称)</th>
          <th>文件系统类型</th>
          <th>详情</th>
        </tr>
      </thead>
      <tbody>
      	<#if harddrisk??>
		<#list harddrisk as h>
		   <tr>
	          <th scope="row">${h.name}</th>
	          <td>${h.dir}</td>
	          <td>${h.flag}</td>
	          <td>${h.systypename}(${h.typename})</td>
	          <td>${h.systypename}</td>
	          <td>
			  	<dl class="dl-horizontal">
	          	<#if h.osHardDisk??>	
	          	  <dt>总大小</dt>
	          	  <dd>${h.osHardDisk.total}</dd>
	          	  <dt>剩余大小</dt>
	          	  <dd>${h.osHardDisk.free}</dd>
	          	  <dt>可用大小</dt>
	          	  <dd>${h.osHardDisk.avail}</dd>
	          	  <dt>已使用大小</dt>
	          	  <dd>${h.osHardDisk.used}</dd>
	          	  <dt>资源利用量</dt>
	          	  <dd>${h.osHardDisk.percent}</dd>
	          	  <dt>读出</dt>
	          	  <dd>${h.osHardDisk.diskReads} Bytes</dd>
	          	  <dt>写入</dt>
	          	  <dd>${h.osHardDisk.diskWrites} Bytes</dd>
	          	</#if>
	          </dl>
	          </td>
	        </tr>
		 
		</#list>
		</#if>
       
        
      </tbody>
    </table>
  
</div>