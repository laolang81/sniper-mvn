<#list networks as n>
<h2>${n['name']}</h2>
<h3>${n['ip']}</h3>
<div id="network_${n['name']}"  style="height:400px; width: 95%;"></div>
</#list>


<script src="${baseHref.baseHref }myfiles/Plugin/echarts/dist/echarts.js"></script>
<script type="text/javascript">
    var timeNow = new Date().Format("hh:mm:ss");
    var maxLength = 60;
	var networks = [];
	//初始化基本数据
	var inDate = [timeNow];
	var inData = [0];
	var outData = [0];
	
	<#assign i = 0>
	<#list networks as n>
	networks[${i}] = echarts.init(document.getElementById('network_${n['name']}'));
	<#assign i = i+1>
	</#list>
	//定义二维数组
	for(var k = 0; k < ${i}; k++){
		inData[k] = [];
		outData[k]  = []
	}
    
	var option = {
	   	title : {
		    text: '网络运行情况',
		    subtext: '单位: KB'
		},
		tooltip : {
		    trigger: 'axis'
		},
		legend: {
		    data:[
		    	{name:'接收情况',icon:'circle',textStyle:{color:'red'}},
		    	{name:'发送情况',icon:'circle',textStyle:{color:'block'}}
		    ]
		},
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType : {show: true, type: ['line', 'bar']},
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
	        	"name":"接收情况",
	        	"type":"line",
	        	"smooth": true,
	        	"data":inData
	        },
	        {
	        	"name":"发送情况",
	        	"type":"line",
	        	"smooth": true,
	        	"data":outData
	        }
	    ]
	};
	
	var options = [];
	for (var i = 0; i < networks.length; i++){
		options[i] = clone(option);
		networks[i].setOption(options[i], true);
	}
	
	timeTicket = null;
	clearInterval(timeTicket);
	timeTicket = setInterval(function (){
		$.post(".${baseHref.adminPath}/admin-monitor/network",{},function(data){
		    // 动态数据接口 addData
		    
		    var timeAdd = new Date().Format("hh:mm:ss");
		    if(inDate.length >= maxLength){
	   			inDate.shift();
		   	}
		   	inDate.push(timeAdd);
		   	for(var i = 0; i < data.length; i++){
		   		if(inDate.length >= maxLength){
		   			inData[i].shift();
		   			outData[i].shift();
		   		}
			  
			   inData[i].push(data[i].tx);
			   outData[i].push(data[i].rx);
			   
			   networks[i].setOption({
			   		xAxis: {
			   			data : inDate
			   		},
			   		 series : [
				        {
				        	"data": inData[i]
				        },
				        {
				        	"data": outData[i]
				        }
				    ]
			   });
			   //end for
		   	}
	    },'json')
	    //end setInterval
	}, 1000);
 
 
 	function clone(obj) {
		var o;
		if (typeof obj == "object") {
			if (obj === null) {
				o = null;
			} else {
				if (obj instanceof Array) {
					o = [];
					for (var i = 0, len = obj.length; i < len; i++) {
						o.push(clone(obj[i]));
					}
				} else {
					o = {};
					for (var j in obj) {
						o[j] = clone(obj[j]);
					}
				}
			}
		} else {
			o = obj;
		}
		return o;
	}
                
</script>