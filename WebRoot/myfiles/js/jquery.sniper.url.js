/**
 * 检测url
 */
var checkUrl = function(obj) {
	var urlObj = $(obj);
	var url = urlObj.attr("data-url");
	if (!url) {
		return;
	}
	var html = "";
	$.ajax({
		url : "public/checkUrl",
		data : {
			url : url
		},
		type : 'POST',
		dataType : 'json',
		success : function(data) {
			if (data == 200) {
				html = '(<i class="fa fa-check" aria-hidden="true">' + data
						+ '</i>)';
			} else {
				html = '(<i class="fa fa-times" aria-hidden="true">' + data
						+ '</i>)';
			}
			urlObj.append(html);
		},
		complete : function(response) {
		}
	});
}

$().ready(function() {
	$.each($(".checkUrl"), function(index, dom) {
		checkUrl(this);
	})
})
