	
<div id="heapMemory" style="height:300px; width: 90%;"></div>

<script src="${baseHref.baseHref }myfiles/Plugin/echarts/dist/echarts.js"></script>
<script type="text/javascript">
    // 使用
    var heapMemory = echarts.init(document.getElementById('heapMemory'));
    var maxLength = 60;
    var jmData = [];
    var smData = [];
    var inDate = [];
    
	optionGrauge = {
		title : {
		    text: '内存使用',
		    subtext: '百分比'
		},
	    tooltip : {
	        formatter: "axis"
	    },
	    legend: {
		    data: ["Heap内存","系统内存"]
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
	    series : [
	        {
	        	"name": "Heap内存",
	        	"type": "line",
	        	"smooth": true,
	        	"data": jmData
	        },
	        {
	        	"name": "系统内存",
	        	"type": "line",
	        	"smooth": true,
	        	"data": smData
	        }
	    ]
	};
	
	//初始化
	heapMemory.setOption(optionGrauge, true);
	
	timeTicket = null;
	clearInterval(timeTicket);
	timeTicket = setInterval(function (){
   		
		$.get(".${baseHref.adminPath}/os/memory",function(data){
			//操作数据
			// 动态数据接口 addData
		    var timeAdd = new Date().Format("hh:mm:ss");
		    if(inDate.length >= maxLength){
		   		inDate.shift();
		   		jmData.shift();
	   			smData.shift();
		   	}
		   	//添加日期
			inDate.push(timeAdd);
		   		
		   //添加内存值
		   jmData.push(data.heap);
		   smData.push(data.sys);
		   heapMemory
			
		   heapMemory.setOption({
		   		xAxis: {
			   			data : inDate
			   	},
			   	series : [
				        {
				        	"data": jmData
				        },
				        {
				        	"data": smData
				        }
				    ]
		    });
		});
		
	    
	},1000)
    
</script>