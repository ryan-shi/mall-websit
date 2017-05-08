$(function() {
	$('#saveForm').validate({
		rules : {
			username : {
				required : true
			},
			department : {
				required : true
			}
		},
		messages : {
			username : {
				required : "必填 *"
			},
			department : {
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
										url : "./update",
										data : $("#saveForm").serialize(),
										headers : {
											"Content-type" : "application/x-www-form-urlencoded;charset=UTF-8"
										},
										success : function(data) {
											if (data == 1) {
												alert("保存成功");
												var userTb = $(
														'#user_tb')
														.DataTable();
												userTb.ajax.reload(null,
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
