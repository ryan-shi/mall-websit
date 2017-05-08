$(function() {
	$('#saveForm').validate({
		rules : {
			name : {
				required : true
			},
			code:{
				required : true
			}
		},
		messages : {
			name : {
				required : "必填 *"
			},
			code:{
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
										url : "/admin/specOption/new",
										data : $("#saveForm").serialize(),
										headers : {
											"Content-type" : "application/x-www-form-urlencoded;charset=UTF-8"
										},
										success : function(data) {
											if (data == 1) {
												alert("保存成功");
												if(specOption_tb!=null){
													specOption_tb.ajax.reload(null,
															false);
												}
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
