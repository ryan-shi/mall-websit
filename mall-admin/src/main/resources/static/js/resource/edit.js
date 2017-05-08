$(function() {
	$('#saveForm').validate({
		rules : {
			name : {
				required : true
			},
			value:{
				required : true
			}
		},
		messages : {
			name : {
				required : "必填 *"
			},
			value:{
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
												$('#resource_tree').jstree(true).refresh();
												if(resource_tb!=null){
													resource_tb.ajax.reload(null,
															false);
												}
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
