	<head>
		<title>Full Calendar - Ace Admin</title>

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
												<h4>Draggable events</h4>
											</div>

											<div class="widget-body">
												<div class="widget-main no-padding">
													<div id="external-events">
														<div class="external-event label-grey" data-class="label-grey">
															<i class="ace-icon fa fa-arrows"></i>
															My Event 1
														</div>

														<div class="external-event label-success" data-class="label-success">
															<i class="ace-icon fa fa-arrows"></i>
															My Event 2
														</div>

														<div class="external-event label-danger" data-class="label-danger">
															<i class="ace-icon fa fa-arrows"></i>
															My Event 3
														</div>

														<div class="external-event label-purple" data-class="label-purple">
															<i class="ace-icon fa fa-arrows"></i>
															My Event 4
														</div>

														<div class="external-event label-yellow" data-class="label-yellow">
															<i class="ace-icon fa fa-arrows"></i>
															My Event 5
														</div>

														<div class="external-event label-pink" data-class="label-pink">
															<i class="ace-icon fa fa-arrows"></i>
															My Event 6
														</div>

														<div class="external-event label-info" data-class="label-info">
															<i class="ace-icon fa fa-arrows"></i>
															My Event 7
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
							</div><!-- /.col -->
						</div><!-- /.row -->
				
			
			

		<!-- basic scripts -->

		<!-- page specific plugin scripts -->
		<script src="myfiles/temple/assets/js/jquery-ui.custom.js"></script>
		<script src="myfiles/temple/assets/js/jquery.ui.touch-punch.js"></script>
		<script src="myfiles/temple/assets/js/date-time/moment.js"></script>
		<script src="myfiles/temple/assets/js/fullcalendar.js"></script>
		<script src="myfiles/temple/assets/js/bootbox.js"></script>

		<!-- inline scripts related to this page -->
		<script type="text/javascript">
			jQuery(function($) {

/* initialize the external events
	-----------------------------------------------------------------*/

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




	/* initialize the calendar
	-----------------------------------------------------------------*/

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
                  today: '本月',
                  month: '月',
                  week: '周',
                  day: '日',
                  prev: '上一月',
                  next: '下一月'
         },
         allDayText:'今天的任务',
         // 切换视图的时候要执行的操作
         viewDisplay: function (view) {//动态把数据查出，按照月份动态查询
         	alert('切换视图的时候要执行的操作');
            var viewStart = $.fullCalendar.formatDate(view.start, "yyyy-MM-dd HH:mm:ss");
            var viewEnd = $.fullCalendar.formatDate(view.end, "yyyy-MM-dd HH:mm:ss");
            $("#calendar").fullCalendar('removeEvents');
            $.post("http://www.cnblogs.com/sr/AccessDate.ashx", { start: viewStart, end: viewEnd }, function (data) {
              var resultCollection = jQuery.parseJSON(data);
              $.each(resultCollection, function (index, term) {
                   $("#calendar").fullCalendar('renderEvent', term, true);
              });
            }); //把从后台取出的数据进行封装以后在页面上以fullCalendar的方式进行显示
        },
        
        events: function(start, end, callback) {
	        $.ajax({
	            url: 'admin/admin-calendar/events',
	            dataType: 'json',
	            data: {
	                // our hypothetical feed requires UNIX timestamps
	                start: Math.round(start.getTime() / 1000),
	                end: Math.round(end.getTime() / 1000)
	            },
	            success: function(doc) {
	
	                var events = [];
	
	                $(doc).find('event').each(function() {
	                    event.push({
	                        title: $(this).attr('title'),
	                        start: $(this).attr('start') // will be parsed
	                    });
	                });
					
	                callback(events);
	            }
	        });
	    },
	    
	    /*
		events: [
		  {
			title: 'All Day Event',
			start: new Date(y, m, 1),
			className: 'label-important'
		  },
		  {
		  	id: 11111,
			title: 'Long Event',
			start: moment().subtract(5, 'days').format('YYYY-MM-DD'),
			end: moment().subtract(1, 'days').format('YYYY-MM-DD'),
			className: 'label-success'
		  },
		  {
			title: 'Some Event',
			start: new Date(y, m, d-3, 16, 0),
			allDay: false, // 指定是否是全天事件
			className: 'label-info'
		  }
		],
		*/
		editable: true, //判断该日程能否拖动
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
			
			bootbox.prompt("新日程:", function(title) {
				if (title !== null) {
					console.log(start);
					console.log(end);
					console.log(allDay);
					calendar.fullCalendar('renderEvent',
						{
							title: title,
							start: start,
							end: end,
							allDay: allDay,
							className: 'label-info'
						},
						true // make the event "stick"
					);
				}
			});
			

			calendar.fullCalendar('unselect');
		},
		// 点击某个事件
		eventClick: function(calEvent, jsEvent, view) {

			//display a modal
			var modal = 
			'<div class="modal fade">\
			  <div class="modal-dialog">\
			   <div class="modal-content">\
				 <div class="modal-body">\
				   <button type="button" class="close" data-dismiss="modal" style="margin-top:-10px;">&times;</button>\
				   <form class="no-margin">\
					  <label>更改日程：</label>\
					  <input class="middle" autocomplete="off" type="text" value="' + calEvent.title + '" />\
					 <button type="submit" class="btn btn-sm btn-success"><i class="ace-icon fa fa-check"></i> 保存</button>\
				   </form>\
				 </div>\
				 <div class="modal-footer">\
					<button type="button" class="btn btn-sm btn-danger" data-action="delete"><i class="ace-icon fa fa-trash-o"></i> 删除日程</button>\
					<button type="button" class="btn btn-sm" data-dismiss="modal"><i class="ace-icon fa fa-times"></i> 取消</button>\
				 </div>\
			  </div>\
			 </div>\
			</div>';
		
		
			var modal = $(modal).appendTo('body');
			modal.find('form').on('submit', function(ev){
				ev.preventDefault();

				calEvent.title = $(this).find("input[type=text]").val();
				calendar.fullCalendar('updateEvent', calEvent);
				modal.modal("hide");
			});
			modal.find('button[data-action=delete]').on('click', function() {
				calendar.fullCalendar('removeEvents' , function(ev){
					return (ev._id == calEvent._id);
				})
				modal.modal("hide");
			});
			
			modal.modal('show').on('hidden', function(){
				modal.remove();
			});

			console.log(calEvent);
			//console.log(calEvent.id);
			//console.log(jsEvent);
			//console.log(view);

			// change the border color just for fun
			//$(this).css('border-color', 'red');

		}
		
	});


})
		</script>

		<!-- the following scripts are used in demo only for onpage help and you don't need them -->
		<link rel="stylesheet" href="myfiles/temple/assets/css/ace.onpage-help.css" />
		<link rel="stylesheet" href="myfiles/temple/docs/assets/js/themes/sunburst.css" />

		
