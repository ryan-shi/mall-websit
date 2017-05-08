$(function() {
	$('#saveForm').validate({
		rules : {
			name : {
				required : true
			},
		},
		messages : {
			name : {
				required : "必填 *"
			},
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
												$('#catalog_tree').jstree(true).refresh();
												if(catalog_tb!=null){
													catalog_tb.ajax.reload(null,
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
