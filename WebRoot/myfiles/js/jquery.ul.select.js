(function($) {
	$.fn.extend({
		"ulSelect" : function(options) {
			options = $.extend({
				url : '',
				valueName : '',
				idName : '',
				ulName : ''
			}, options);

			var run = function() {
				$(options.ulName).delegate("li", "click", function() {
					$id = $(this).attr("data");
					$value = $(this).html();
					$("input[name='" + options.idName + "']").val($id);
					$("input[name='" + options.valueName + "']").val($value);
					$(options.ulName).hide();
				})

				$('input[name="' + options.valueName + '"]').bind(
						"keyup",
						function() {
							$ul = $(this).next();
							$value = $(this).val();
							if ($value != '') {
								$.post(options.url, {
									name : $value
								}, function(data) {
									if (data) {
										$(options.ulName).show();
										$ul.empty();
										var li = "";
										for ( var i in data) {
											li += "<li data=\"" + i + "\">"
													+ data[i] + "</li>";
										}
										$ul.append(li);
									} else {
										$(options.ulName).hide();
									}
								}, "json")
							}
						});
			};
			
			
			run();

		}
	});
})(jQuery)