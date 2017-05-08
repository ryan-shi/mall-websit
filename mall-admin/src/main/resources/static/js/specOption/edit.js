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
										url : "/admin/specOption/update",
										data : $("#saveForm").serialize(),
										headers : {
											"Content-type" : "application/x-www-form-urlencoded;charset=UTF-8"
										},
										success : function(data) {
											if (data == 1) {
												alert("编辑成功");
												var specOptionTb=$('#specOption_tb').DataTable();
												specOptionTb.ajax.reload(null, false);
												closeDialogSpecOp();
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
