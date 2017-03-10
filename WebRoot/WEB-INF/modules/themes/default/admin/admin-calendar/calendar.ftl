<head>
	<!-- page specific plugin styles -->
	<link rel="stylesheet" href="myfiles/temple/assets/css/jquery-ui.custom.css" />
	<link rel="stylesheet" href="myfiles/temple/assets/css/fullcalendar.css" />
</head>
<!-- PAGE CONTENT BEGINS -->
<div class="row">
	<div class="col-sm-9">
		<div class="space"></div>
		<!-- #section:plugins/data-time.calendar -->
		<div id="calendar"></div>
		<!-- /section:plugins/data-time.calendar -->
	</div>

	<div class="col-sm-3">
		<div class="widget-box transparent">
			<div class="widget-header">
				<h4>颜色事件</h4>
			</div>

			<div class="widget-body">
				<div class="widget-main no-padding">
					<div id="external-events">
						<div class="external-event label-grey" data-class="label-grey">
							<i class="ace-icon fa fa-arrows"></i>
							label-grey
						</div>

						<div class="external-event label-success" data-class="label-success">
							<i class="ace-icon fa fa-arrows"></i>
							label-success
						</div>

						<div class="external-event label-danger" data-class="label-danger">
							<i class="ace-icon fa fa-arrows"></i>
							label-danger
						</div>

						<div class="external-event label-purple" data-class="label-purple">
							<i class="ace-icon fa fa-arrows"></i>
							label-purple
						</div>

						<div class="external-event label-yellow" data-class="label-yellow">
							<i class="ace-icon fa fa-arrows"></i>
							label-yellow
						</div>

						<div class="external-event label-pink" data-class="label-pink">
							<i class="ace-icon fa fa-arrows"></i>
							label-pink
						</div>

						<div class="external-event label-info" data-class="label-info">
							<i class="ace-icon fa fa-arrows"></i>
							label-info
						</div>

						<label>
							<input type="checkbox" class="ace ace-checkbox" id="drop-remove" />
							<span class="lbl"> Remove after drop</span>
						</label>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- PAGE CONTENT ENDS -->
<div id="tempdata" style="display: none;"></div>
<script type="text/template" id="eventInsert">
<form role="form" class="form-horizontal" style="width:500px; padding: 20px;" id="eventInsert">
	<input type="hidden" name="id" value="{$T.id}">
	<div class="form-group">
		<label for="form-title" class="col-sm-3 control-label no-padding-right">事件名称</label>
		<div class="col-sm-9">
			<textarea rows="3" class="form-control" name="title" id="form-title">{$T.title}</textarea>
		</div>
	</div>
	
	<div class="form-group">
		<label for="form-start" class="col-sm-3 control-label no-padding-right">时间</label>
		<div class="col-sm-9">
			<div class="input-daterange input-group">
				<input type="text" id="form-start-1" name="start" value="{$T.start}" class="input-sm form-control AllWdate">
				<span class="input-group-addon">
					<i class="fa fa-exchange"></i>
				</span>
				<input type="text" name="end" value="{$T.end}" class="input-sm  form-control AllWdate">
			</div>
		</div>
	</div>
	
	<div class="form-group">
		<label for="form-className" class="col-sm-3 control-label no-padding-right">颜色选择</label>
		<div class="col-sm-9">
			<select id="form-className" class="form-control" name="className">
				{#foreach $T.classNames as record} 
				<option value="{$T.record}" {#if $T.record == $T.className }selected{#/if}>{$T.record}</option>
				{#/for}
			</select>
		</div>
	</div>
	
	<div class="form-group">
		<label for="form-allDay" class="col-sm-3 control-label no-padding-right"></label>
		<div class="checkbox">
			<label>
				<input type="checkbox" class="ace" name="allDay" {#if $T.allDay == true }checked="checked"{#/if}>
				<span class="lbl"> 全天(全天只保留日期)</span>
			</label>
		</div>
	</div>
	
</form>
</script>

<script type="text/template" id="eventUpdateAndDelete">
</script>

		
<!-- basic scripts -->
<!-- page specific plugin scripts -->
<script type="text/javascript" src="myfiles/js/jquery-jtemplates.js"></script>
<script src="myfiles/temple/assets/js/jquery-ui.custom.js"></script>
<script src="myfiles/temple/assets/js/jquery.ui.touch-punch.js"></script>
<script src="myfiles/temple/assets/js/date-time/moment.js"></script>
<script src="myfiles/temple/assets/js/fullcalendar.js"></script>
<script src="myfiles/temple/assets/js/bootbox.js"></script>
<script src="myfiles/Plugin/layer/layer/layer.js"></script>
<script type="text/javascript" src="${baseHref.baseHref }myfiles/Plugin/My97DatePicker/WdatePicker.js"></script>
<!-- inline scripts related to this page -->
<script type="text/javascript">

	var classNames = [];
	classNames[0] = 'label-grey';
	classNames[1] = 'label-success';
	classNames[2] = 'label-danger';
	classNames[3] = 'label-purple';
	classNames[4] = 'label-yellow';
	classNames[5] = 'label-pink';
	classNames[6] = 'label-info';
	
	jQuery(function($) {
			
		$(document).delegate('.Wdate', 'click', function() {
			WdatePicker({
				dateFmt : 'yyyy-MM-dd'
			})
		});
		
		$(document).delegate('.AllWdate', 'click', function() {
			WdatePicker({
				dateFmt : 'yyyy-MM-dd HH:mm:ss'
			})
		});
		
		
		/* initialize the external events */
	
		$('#external-events div.external-event').each(function() {
	
			// create an Event Object (http://arshaw.com/fullcalendar/docs/event_data/Event_Object/)
			// it doesn't need to have a start or end
			var eventObject = {
				title: $.trim($(this).text()) // use the element's text as the event title
			};
	
			// store the Event Object in the DOM element so we can get to it later
			$(this).data('eventObject', eventObject);
	
			// make the event draggable using jQuery UI
			$(this).draggable({
				zIndex: 999,
				revert: true,      // will cause the event to go back to its
				revertDuration: 0  //  original position after the drag
			});
			
		});


		Date.prototype.Format = function(fmt)   
		{ //author: meizz   
		  var o = {   
		    "M+" : this.getMonth()+1,                 //月份   
		    "d+" : this.getDate(),                    //日   
		    "h+" : this.getHours(),                   //小时   
		    "m+" : this.getMinutes(),                 //分   
		    "s+" : this.getSeconds(),                 //秒   
		    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
		    "S"  : this.getMilliseconds()             //毫秒   
		  };   
		  if(/(y+)/.test(fmt)){
		  	fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
		  }
		   
		  for(var k in o){
		  	if(new RegExp("("+ k +")").test(fmt)){
		  		fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
		  	}
		  }
		  return fmt;   
		}  
	
	
		/* initialize the calendar */
	
		var date = new Date();
		var d = date.getDate();
		var m = date.getMonth();
		var y = date.getFullYear();
	
		var calendar = $('#calendar').fullCalendar({
			isRTL: true,
			buttonHtml: {
				prev: '<i class="ace-icon fa fa-chevron-left"></i>',
				next: '<i class="ace-icon fa fa-chevron-right"></i>',
			},
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'month,agendaWeek,agendaDay'
			},
			monthNames: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
	        monthNamesShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
	        dayNames: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
	        dayNamesShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六"],
	        today: ["今天"],
	        firstDay: 1,
	        buttonText: {
	                  today: '今天',
	                  month: '月',
	                  week: '周',
	                  day: '日',
	                  prev: '<<',
	                  next: '>>'
	         },
	         // allDayText:'今天的任务',
	         // 切换视图的时候要执行的操作
	         // 动态把数据查出，按照月份动态查询
	         viewDisplay: function (view) {
	         	
	            var viewStart = $.fullCalendar.formatDate(view.start, "yyyy-MM-dd HH:mm:ss");
	            var viewEnd = $.fullCalendar.formatDate(view.end, "yyyy-MM-dd HH:mm:ss");
	            $("#calendar").fullCalendar('removeEvents');
	            $.post(".${baseHref.adminPath}/admin-calendar/events", { start: viewStart, end: viewEnd }, function (data) {
	              if(data.code == 200){
		              var resultCollection = jQuery.parseJSON(data.result);
		              $.each(resultCollection, function (index, term) {
		                   $("#calendar").fullCalendar('renderEvent', term, true);
		              });
	              }
	            }); //把从后台取出的数据进行封装以后在页面上以fullCalendar的方式进行显示
	        },
	        // axisFormat: 'H(:mm)',
	        events: {
	        	url: '.${baseHref.adminPath}/admin-calendar/events',
	        	type: 'POST',
	        	data: {
		            dateStart: 'something',
		            dateEnd: 'somethingelse'
		        },
		        error: function() {
		            alert('there was an error while fetching events!');
		        },
		        color: 'yellow',   // a non-ajax option
	        	textColor: 'black' // a non-ajax option
		    },
		    // 判断该日程能否拖动
			editable: true,
			// 拖拽事件引发的操作
			// 拖拽开始
			eventDragStart:function(event, jsEvent, ui, view) {
				console.log(event);
				console.log(event.start);
				console.log(event.end);
			},
			// 拖拽结束
			eventDragStop:function(event, jsEvent, ui, view ) {
				console.log(event);
				console.log(event.start);
				console.log(event.end);
			},
			// 拖拽完成
			// dayDelta 保存日程向前或者向后移动了多少天
			// inuteDelta 这个值只有在agenda视图有效，移动的时间
			// allDay 如果是月视图，或者是agenda视图的全天日程，此值为true,否则为false
			eventDrop:function( event, dayDelta, minuteDelta, allDay, revertFunc, jsEvent, ui, view ) {
				console.log("结束");
				console.log(event);
				console.log(event.start);
				console.log(event.end);
			
			},
			// 事件改编大小
			eventResizeStart:function( event, jsEvent, ui, view ) { },
			eventResizeStop:function( event, jsEvent, ui, view ) { },
			eventResize:function( event, dayDelta, minuteDelta, revertFunc, jsEvent, ui, view ) { },
			droppable: true, // this allows things to be dropped onto the calendar !!!
			drop: function(date, allDay) { // this function is called when something is dropped
				console.log("事件移动");
				// retrieve the dropped element's stored Event Object
				var originalEventObject = $(this).data('eventObject');
				var $extraEventClass = $(this).attr('data-class');
				
				
				// we need to copy it, so that multiple events don't have a reference to the same object
				var copiedEventObject = $.extend({}, originalEventObject);
				
				// assign it the date that was reported
				copiedEventObject.start = date;
				copiedEventObject.allDay = allDay;
				if($extraEventClass) copiedEventObject['className'] = [$extraEventClass];
				
				// render the event on the calendar
				// the last `true` argument determines if the event "sticks" (http://arshaw.com/fullcalendar/docs/event_rendering/renderEvent/)
				$('#calendar').fullCalendar('renderEvent', copiedEventObject, true);
				
				// is the "remove after drop" checkbox checked?
				if ($('#drop-remove').is(':checked')) {
					// if so, remove the element from the "Draggable Events" list
					$(this).remove();
				}
			},
			selectable: true,
			selectHelper: true,
			select: function(start, end, allDay) {
			
				var temp = {};
					temp.classNames = classNames;
				$('#tempdata').setTemplate($('#eventInsert').html()).processTemplate(temp);
		        var html = $('#tempdata').html();
		        $('#tempdata').empty();
						        
		        layer.open({
					    type: 1,
					    shade: false,
					    title: "事件", //不显示标题
					    content: html, //捕获的元素
					    area: ['600px', '400px'], //宽高
					    btn: ['取消', '保存'],
					    yes: function(index, layero){
					        layer.close(index);
					    },
					    cancel: function(index, layero){ 
					       // layer.close(index);
					       delete temp.classNames;
					       temp.title =  $("form").find('textarea[name="title"]').val();
					       temp.className =  $("form").find('select[name="className"]').val();
					       temp.startDate =  $("form").find('input[name="start"]').val();
					       temp.endDate =  $("form").find('inout[name="end"]').val();
					       temp.allDay =  $("form").find('inout[name="allDay"]').prop("checked");
					       
					       var startDate = new Date(Date.parse(start)).Format("yyyy-MM-dd hh:mm:ss");
						   var endlDate = new Date(Date.parse(end)).Format("yyyy-MM-dd hh:mm:ss");
					       if(!temp.startDate){
					       		temp.startDate = startDate;
					       }
					       
					       if(!temp.endDate){
					       		temp.endDate = endlDate;
					       }
					       
					       if(temp.title == ''){
					       	return true;
					       }
					       
					       var url = '.${baseHref.adminPath}/admin-calendar/eventInsert';
							$.post(url, temp , function (data, textStatus){
							
							if(data.id > 0){
								startDate = new Date(data.startDate).Format("yyyy-MM-dd hh:mm:ss");
								endDate = new Date(data.endDate).Format("yyyy-MM-dd hh:mm:ss");
								calendar.fullCalendar('renderEvent',
								{
									id: data.id,
									title: data.title,
									start: startDate,
									end: endDate,
									allDay: data.allDay,
									className: data.className
								}, false);
							}
						},'json');
					}
				});
				
				// 取消选择
				calendar.fullCalendar('unselect');
			},
			// 点击某个事件
			eventClick: function(calEvent, jsEvent, view) {
				var temp = {};
				temp.id = calEvent.id;
				$.post('.${baseHref.adminPath}/admin-calendar/eventGet',temp, function(data, status){
					temp.allDay = data.allDay;
					var newDate = new Date();
					if(data.startDate){
						newDate.setTime(data.startDate);
						temp.start = new Date(newDate).Format("yyyy-MM-dd hh:mm:ss");
					}
					if(data.endDate){
						newDate.setTime(data.endDate);
						temp.end = new Date(newDate).Format("yyyy-MM-dd hh:mm:ss");
					}
					
					temp.title = calEvent.title;
					temp.className = calEvent.className[0];
					temp.classNames = classNames;
					$('#tempdata').setTemplate($('#eventInsert').html()).processTemplate(temp);
		            var html = $('#tempdata').html();
		            $('#tempdata').empty();
	            
	            	console.log(temp);
					layer.open({
					    type: 1,
					    shade: false,
					    title: "事件", //不显示标题
					    content: html, //捕获的元素
					    area: ['600px', '400px'], //宽高
					    cancel: function(index){
					        layer.close(index);
					    },
					    btn: ['删除', '取消', '保存']
					    ,yes: function(index, layero){ 
					        var url = '.${baseHref.adminPath}/admin-calendar/eventDelete';
							$.post(url, temp , function (data, textStatus){
								if(data.code == 200){
									layer.close(index);
									calendar.fullCalendar('removeEvents' , function(ev){
										return (ev._id == calEvent._id);
									})
								}
							},'json');
					    },btn2: function(index){ //或者使用btn2
					         layer.close(index);
					    },btn3: function(index, layero){
					       delete temp.classNames;
					       temp.title =  $("form").find('textarea[name="title"]').val();
					       temp.className =  $("form").find('select[name="className"]').val();
					       temp.startDate =  $("form").find('input[name="start"]').val();
					       temp.endDate =  $("form").find('inout[name="end"]').val();
					       temp.allDay =  $("form").find('inout[name="allDay"]').prop("checked");
					       
					       calEvent.title = temp.title;
					       calEvent.className = temp.className;
					       var url = '.${baseHref.adminPath}/admin-calendar/eventUpdate';
							$.post(url, temp , function (data, textStatus){
								if(data.code == 200){
									calendar.fullCalendar('updateEvent', calEvent);
									layer.close(index); 
								}
							},'json');
					    }
					});
				});
			}
			
		});
	})
	
	
	
	
</script>

<!-- the following scripts are used in demo only for onpage help and you don't need them -->
<link rel="stylesheet" href="myfiles/temple/assets/css/ace.onpage-help.css" />
<link rel="stylesheet" href="myfiles/temple/docs/assets/js/themes/sunburst.css" />