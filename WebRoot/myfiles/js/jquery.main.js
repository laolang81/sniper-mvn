/**
 * 用户列表也的所有操作
 */
jQuery.ajaxSettings.traditional = true;
(function($) {
	$.fn.extend({
				"SniperMain" : function(options) {
					// 设置默认值
					options = $.extend({
						baseurl : 'doftec',
					}, options);

					
					/**
					 * 事件获取
					 */
					var mainCalendar = function() {
						var $sniperCaldenar = $(".sniperCaldenar");
						var url = options.baseurl + "/admin-calendar/events";
						var startTime = new Date();
						startTime.setDate(startTime.getDate() - 1);

						var entTime = new Date();
						entTime.setDate(entTime.getDate() + 1);

						$.post(url,
						{
							start : startTime
									.Format("yyyy-M-d")
									+ ' 08:00:00',
							end : entTime.Format("yyyy-M-d")
									+ ' 08:00:00'
						},
						function(data, status) {

							if (data.code != 200) {
								return;
							}

							$sniperCaldenar.find('a span')
									.html(data.result.length);
							html = '';
							for (var i = 0; i < data.result.length; i++) {
								html += '<li>';
								// html += '<span>';
								html += '<span class="msg-title">'
										+ data.result[i].title
										+ '</span>';
								html += '<span class="msg-time"><i class="ace-icon fa fa-clock-o"></i><span>'
										+ data.result[i].start
										+ '</span></span>';
								html += '<span class="msg-time"><i class="ace-icon fa fa-clock-o"></i><span>'
										+ data.result[i].end
										+ '</span></span>';
								// html += '</span>';
								html += '</li>';
							}
							$sniperCaldenar.find(
									"ul li:eq(1) ul").append(
									html);
						})
					}
					//请求日程数据
					mainCalendar();

					// 未审核新闻数量获取
					function sniperPost() {
						var $sniperPost = $(".sniperPost");
						var $sniperComment = $(".sniperComment");
						var url = options.baseurl + "/topMenuValue";
						//获取数据
						$.post(url,{},function(data, status){
							if(data){
								if(data['postAuditCount']){
									$sniperPost.html(data['postAuditCount']);
									$sniperComment.html(data['commentCount']);
								}
							}
						})
					}
					
					//定义10秒一次请求
					function autoPost(){
						//先执行一次
						sniperPost()
						//10秒执行一次
						sp  = setInterval(function(){
							sniperPost()
						},10000);
					}
					
					//请求文章数据
					autoPost();
					
					//用户浏览记录
					function autoCollect(){
						var data ={};
						data['title'] = document.title;
						data['url'] = window.location.href;
						
						var url = options.baseurl + "/collect";
						$.post(url,data,function(data, status){
							if(data){
								$('#tempdata').setTemplate($('#collectTemplate').html()).processTemplate(data);
								html = $('#tempdata').html();
								//console.log(html)
								$("#collectLi").html(html);
							}
						})
					}
					
					autoCollect();
					// end
				}
			});
})(jQuery);
