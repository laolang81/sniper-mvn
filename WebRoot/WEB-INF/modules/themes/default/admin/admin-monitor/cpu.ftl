
<div id="cpu" style="height:400px; width: 90%;"></div>


<script src="${baseHref.baseHref }myfiles/Plugin/echarts/dist/echarts.js"></script>
<script type="text/javascript">
   
	//定义时间
	var timeNow = new Date().Format("hh:mm:ss");
	//CPU
	var cpu = echarts.init(document.getElementById('cpu'));  
	var maxLength = 60;
	var inDate = [];
	//处理cpu名称
	var cpuNames = [<#if cpus??><#list 1..cpus?size as t>'CPU${t}'<#if t != cpus?size>,</#if></#list></#if>];
	//定义cpu运行数据
	var cpuData = [];
	//组装每隔cpu的运行情况
	var cpuSeries = [];
	for (var i = 0; i < cpuNames.length; i++){
		cpuData[i] = [];
		cpuSeries[i] =   {
	        	"name": cpuNames[i],
	        	"type": "line",
	        	"smooth": true,
	        	"data": cpuData[i]
	        };
	}

	var option = {
	   	title : {
		    text: 'CPU使用率',
		    subtext: '百分比'
		},
		tooltip : {
		    trigger: 'axis'
		},
		legend: {
		    data: cpuNames
		},
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType : {show: true, type: ['line']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    calculable : true,
	    xAxis : [
	        {
	            type : 'category',
	            data : inDate
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value'
	        }
	    ],
	    series : 
	       cpuSeries
	    
	};
	
	//显示
	cpu.setOption(option, true);
	
	timeTicket = null;
	clearInterval(timeTicket);
	timeTicket = setInterval(function (){
		$.post(".${baseHref.adminPath}/admin-monitor/cpu",{},function(data){
		    // 动态数据接口 addData
		    var timeAdd = new Date().Format("hh:mm:ss");
		    if(inDate.length >= maxLength){
		   		inDate.shift();
		   	}
		   	//添加日期
			inDate.push(timeAdd);
		   	for(var i = 0; i < data.length; i++){
		   		if(inDate.length >= maxLength){
		   			cpuData[i].shift();
		   		}
		   		
			   //添加cpu值
			   cpuData[i].push(data[i].data[5]);
			   //赋值给series
			   cpuSeries[i].data = cpuData[i];
			   cpu.setOption({
			   		xAxis: {
			   			data : inDate
			   		},
			   		series :cpuSeries
			   });
			   //end for
		   	}
	    },'json')
	    //end setInterval
	}, 1000);
 
                
</script>