/**
 * 问卷动态添加
 */
(function ($) {
   $.fn.extend({
        "sniperAnswer": function (options) {
            //设置默认值
            options = $.extend({
            	form		: 'form',
            }, options);
            
            /**
             * 挡点击同一个提的其他时候取消非自己所选
             */
            function bindText(){
            	$(options.form).delegate( "input[type='text']","click", function () {
            		// 获取同列答案其他的值
            		var parent = $(this).parents("li");
            		// 查找其他单选按钮并取消
            		$(parent).find("input[type='radio']").prop("checked", false);
            	})
            }
            /**
             * 挡点击飞其他的时候把其他清空
             */
            function bindRadio(){
            	$(options.form).delegate( "input[type='radio']","click", function () {
            		// 获取同列答案其他的值
            		var parent = $(this).parents("li");
            		$(parent).find("input[type='text']").val('');
            	})
            }
           

            $(document).ready(function(){
            	bindText();
            	bindRadio();
            });


           
            
        }            
    });
})(jQuery);
