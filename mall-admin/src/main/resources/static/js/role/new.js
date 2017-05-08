$(function() {
	$('#saveForm').validate({
		rules : {
			name : {
				required : true
			}
		},
		messages : {
			name : {
				required : "必填 *"
			}
		}
	});
	$('.saveBtn')
			.click(
					function() {
						if ($('#saveForm').valid()) {
							$
									.ajax({
										type : "POST",
										url : "./new",
										data : $("#saveForm").serialize(),
										headers : {
											"Content-type" : "application/x-www-form-urlencoded;charset=UTF-8"
										},
										success : function(data) {
											if (data == 1) {
												alert("保存成功");
												var roleTb = $(
														'#role_tb')
														.DataTable();
												roleTb.ajax.reload(null,
														false);
												closeDialog();
											} else {
												alert(data);
											}
										}
									});
						} else {
							return;
						}
					});
});
