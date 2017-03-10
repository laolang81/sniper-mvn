
	
<div class="row">
	<div class="col-md-6"><div id="javaMemory" style="height:300px; width: 300px;"></div></div>
	<div class="col-md-6"><div id="sysMemory" style="height:300px; width: 300px;"></div></div>
</div>
<script src="${baseHref.baseHref }myfiles/Plugin/echarts/dist/echarts.js"></script>
<script type="text/javascript">
    // 使用
    var javaMemory = echarts.init(document.getElementById('javaMemory'));
    var sysMemory = echarts.init(document.getElementById('sysMemory'));
    
	optionGrauge = {
		title : {
		    text: '内存使用',
		    subtext: '百分比'
		},
	    tooltip : {
	        formatter: "{a} <br/>{b} : {c}%"
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    series : [
	        {
	            name:'内存使用%比',
	            type:'gauge',
	            splitNumber: 10,       // 分割段数，默认为5
	            axisLine: {            // 坐标轴线
	                lineStyle: {       // 属性lineStyle控制线条样式
	                    color: [[0.2, '#228b22'],[0.8, '#48b'],[1, '#ff4500']], 
	                    width: 8
	                }
	            },
	            axisTick: {            // 坐标轴小标记
	                splitNumber: 10,   // 每份split细分多少段
	                length :12,        // 属性length控制线长
	                lineStyle: {       // 属性lineStyle控制线条样式
	                    color: 'auto'
	                }
	            },
	            axisLabel: {           // 坐标轴文本标签，详见axis.axisLabel
	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                    color: 'auto'
	                }
	            },
	            splitLine: {           // 分隔线
	                show: true,        // 默认显示，属性show控制显示与否
	                length :30,         // 属性length控制线长
	                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
	                    color: 'auto'
	                }
	            },
	            pointer : {
	                width : 5
	            },
	            title : {
	                show : true,
	                offsetCenter: [0, '-40%'],       // x, y，单位px
	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                    fontWeight: 'bolder'
	                }
	            },
	            detail : {
	                formatter:'{value}%',
	                textStyle: {       // 其余属性默认使用全局文本样式，详见TEXTSTYLE
	                    color: 'auto',
	                    fontWeight: 'bolder'
	                }
	            },
	            data:[{value: 0, name: '虚拟内存'}]
	        }
	    ]
	};
	
	
	var javaGrauge = clone(optionGrauge);
	var memGrauge = clone(optionGrauge);
	
	javaGrauge.series[0].data[0].name = '虚拟内存';
	memGrauge.series[0].data[0].name = '系统内存';
	
	javaMemory.setOption(javaGrauge,true);
	sysMemory.setOption(memGrauge,true);
	
	timeTicket = null;
	clearInterval(timeTicket);
	timeTicket = setInterval(function (){
		$.get(".${baseHref.adminPath}/os/javaMemory",function(data){
		    javaMemory.setOption({
		    	series: [ {
		    		 data:[{value: data, name: '虚拟内存'}]
		    	}]
		    });
		});
		
		$.get(".${baseHref.adminPath}/os/sysMemory",function(data){
		    sysMemory.setOption({
		   		 series: [ {
		    		 data:[{value: data, name: '系统内存'}]
		    	}]
		    });
		});
	    
	},1000)
    
    
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