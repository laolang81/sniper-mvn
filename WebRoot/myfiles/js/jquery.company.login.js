function onlogin() {
	var url = $('#login').attr('action');
	var dataurl = $("input[name='dataurl']").val();
	var u = $('#account').val();
	var p = $('#password').val();
	var v = '0';
	if ($('#verify').length > 0) {
		v = $('#verify').val()
	}
	$.ajax({
		type : "post",
		dataType : 'json',
		url : url,
		data : {
			username : u,
			password : p,
			verifyCode : v
		},
		beforeSend : function(XMLHttpRequest) {
		},
		success : function(data, textStatus) {
			if (data.message == 1)
				window.location = dataurl;
			else {
				alert(data.message);
				$('#' + data.id).focus();
				fleshVerify();
			}
		},
		complete : function(XMLHttpRequest, textStatus) {
		},
		error : function() {
		}
	})
}
function fleshVerify() {
	var timenow = new Date().getTime();
	var verifyImg = $("input[name='verifyImg']").val();
	$('#verifyImg').attr("src", verifyImg + '?' + timenow)
}
function list(a) {
	var companyurl = $("input[name='companyurl']").val();
	if (a) {
		$('#account').val($(a).html());
		$('.list').empty().remove()
	} else {
		value = $('#account').val();
		if (!value)
			return false;
		$.post(companyurl, {
			value : value
		}, function(data, textStatus) {
			if (data) {
				str = '<ul class="list">';
				for ( var index in data) {
					str += '<li onclick="list(this);">'
							+ data[index] + '</li>'
				}
				str += '</ul>';
				$('.list').empty().remove();
				$('.listMain').append(str)
			}
		}, 'json')
	}
}
$().ready(function() {
	$("#login").keydown(function(e) {
		if (e.keyCode == 13) {
			onlogin();
			return false
		}
	});
	$('#account').keyup(function() {
		list()
	})
});