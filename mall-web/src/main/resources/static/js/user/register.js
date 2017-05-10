$(function() {
	$('#registerBtn').click(function() {
		if ($("#password").val() == $("#confirm_password").val()) {
			$("#registerForm").submit();
		} else {
			alert("两次密码不一致！")
			return;
		}
	});
});
