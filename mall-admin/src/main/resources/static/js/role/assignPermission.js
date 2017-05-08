$(function() {
	$('#resource_tree').jstree(
			{
				"core" : {
					"themes" : {
						"responsive" : false,
					},
					"check_callback" : true,
					'data' : {
						'url' : function(node) {
							return node.id == '#' ? '/admin/resource/treeRoot'
									: '/admin/resource/treeChildren';
						},
						'data' : function(node) {
							return {
								'parent' : node.id
							};
						}
					}
				},
				"types" : {
					"default" : {
						"icon" : "fa fa-folder icon-state-warning icon-lg"
					},
					"file" : {
						"icon" : "fa fa-file icon-state-warning icon-lg"
					}
				},
				"checkbox" : {
					"keep_selected_style" : false
				},
				"plugins" : [ "types", "checkbox" ],
			});

	$('#resource_tree').on('select_node.jstree', function(e, data) {
		var cur = data.node;
		$('#resource_tree').jstree(true).open_all(cur);
	});

	$('#resource_tree').on('ready.jstree', function(e, data) {
		$('#resource_tree').jstree(true).load_all();
	});

	$('#resource_tree').on('load_all.jstree', function(e, data) {
		$.get("./roleResource/" + $("#id").val(), {
			ts : new Date().getTime()
		}, function(data) {
			for (var i = 0; i < data.length; i++) {
				if ($('#resource_tree').jstree(true).is_leaf(data[i].id)) {
					$('#resource_tree').jstree(true).check_node(data[i].id);
				} else {
					$('#resource_tree').jstree(true).open_node(data[i].id);
				}
			}

		});
	});

	function concat_disinct(arr1, arr2) {
		var arr = arr1.concat();
		for (var i = 0; i < arr2.length; i++) {
			arr.indexOf(arr2[i]) === -1 ? arr.push(arr2[i]) : 0;
		}
		return arr;
	}

	function removeByValue(arr, val) {
		for (var i = 0; i < arr.length; i++) {
			if (arr[i] == val) {
				arr.splice(i, 1);
				break;
			}
		}
	}

	$('.saveBtn')
			.click(
					function() {
						var nodes = $('#resource_tree').jstree(true)
								.get_checked(true);
						var result = [];
						for (var i = 0; i < nodes.length; i++) {
							result.push(nodes[i].id);
							result = concat_disinct(result, nodes[i].parents);
							removeByValue(result, "#");
							removeByValue(result, "1");
						}
						if (result.length == 0) {
							alert("没有选择权限！");
							return;
						}
						$
								.ajax({
									type : "POST",
									url : "./assignPermission",
									data : {
										roleId : $("#id").val(),
										resourceIds : result
									},
									headers : {
										"Content-type" : "application/x-www-form-urlencoded;charset=UTF-8"
									},
									traditional : true,
									success : function(data) {
										if (data == 1) {
											alert("权限分配成功");
											closeDialog();
										} else {
											alert(data);
										}
									}
								});
					});
});
