var cur_selected = null;
var resource_tb = null;
$(function() {
	$('#resource_tree').jstree(
			{
				"core" : {
					"themes" : {
						"responsive" : false
					},
					"check_callback" : false,
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
				"plugins" : [ "types" ]
			});

	var resource_table_view = $("#resource_table_view");
	var resource_detail_view = $("#resource_detail_view");
	$('#resource_tree').on("ready.jstree", function(e, data) {
		// console.log("页面刷新，选择跟节点！");
		$('#resource_tree').jstree(true).select_node(1);
	});

	$('#resource_tree')
			.on(
					"select_node.jstree",
					function(e, data) {
						$('#resource_tree')
								.jstree(true)
								.open_node(
										data.node,
										function(openedData) {
											// console.log("选择节点响应，打开当前节点！");
											// console.log("打开当前节点回调！");
											// console.log(openedData);
											cur_selected = openedData;
											if (cur_selected.children.length > 0) {
												resource_table_view
														.removeClass("remove");
												resource_detail_view.html("");
												if (resource_tb != null) {
													resource_tb.settings()[0].ajax.data.id = cur_selected.id;
													resource_tb.ajax.reload(
															null, false);
												} else {
													resource_tb = $(
															'#resource_tb')
															.DataTable(
																	{
																		"language" : {
																			"lengthMenu" : "每页  _MENU_ 条记录",
																			"emptyTable" : "当前数据为空",
																			"zeroRecords" : "没有找到记录",
																			"info" : "显示  _START_ 到  _END_ 条记录，总共  _TOTAL_ 条记录",
																			"infoEmpty" : "显示  0  到  0  条记录，总共  0  条记录",
																			"infoFiltered" : "(从 _MAX_ 条记录过滤)",
																			"processing" : "加载中...",
																			"paginate" : {
																				"first" : "首页",
																				"previous" : "上一页",
																				"next" : "下一页",
																				"last" : "尾页",
																			}
																		},
																		dom : '<"toolbar">rgt<"row"<"col-lg-4"l><"col-lg-4"i><"col-lg-4"p>>',
																		ordering : false,
																		paging : false,
																		processing : true,
																		serverSide : true,
																		stateSave : true,
																		autoWidth : false,
																		ajax : {
																			url : "/admin/resource/list",
																			'data' : {
																				'id' : cur_selected.id
																			}
																		},
																		columns : [
																				{
																					title : "资源名称",
																					data : "name",
																				},
																				{
																					title : "资源描述",
																					data : "description",
																				},
																				{
																					title : "资源值",
																					data : "value",
																				},
																				{
																					title : "创建时间",
																					data : "createTime",
																				},
																				{
																					title : "更新时间",
																					data : "updateTime",
																				},
																				{
																					title : "操作",
																					data : "null",
																					render : function(
																							data,
																							type,
																							row,
																							meta) {
																						return '<button class="btn btn-warning btn-xs fa fa-edit" onclick="edit('
																								+ row.id
																								+ ')" ></button>'
																								+ '<button class="btn btn-primary btn-xs fa fa-eye" onclick="view('
																								+ row.id
																								+ ')"></button>'
																								+ '<button class="btn btn-danger btn-xs fa fa-trash" onclick="del('
																								+ row.id
																								+ ')"></button>';
																					},
																					width : "80px",
																					orderable : false
																				} ],
																	});
												}
											} else {
												resource_table_view
														.addClass("remove");
												$
														.get(
																"./"
																		+ cur_selected.id,
																{
																	ts : new Date()
																			.getTime()
																},
																function(data) {
																	var form = $(
																			data)
																			.find(
																					"form");
																	var resource_id = form
																			.find(
																					"#id")
																			.val();
																	var insertDiv = $('<div class="col-lg-offset-3 col-lg-9">'
																			+ '<button class="btn btn-sm btn-danger" onclick="edit('
																			+ resource_id
																			+ ')">修改</button>'
																			+ '<button class="btn btn-sm btn-warning" style="margin-left:10px;" onclick="del('
																			+ resource_id
																			+ ')">删除</button>'
																			+ '</div>');
																	form
																			.find(
																					"#returnDiv")
																			.empty();
																	form
																			.find(
																					"#returnDiv")
																			.append(
																					insertDiv);
																	$(
																			"#resource_detail_view")
																			.html(
																					form[0].innerHTML);
																});
											}
										});
					});
});

var artdialog;
function edit(id) {
	$.get("./update/" + id, {
		ts : new Date().getTime()
	}, function(data) {
		art.dialog({
			lock : true,
			opacity : 0.3,
			title : "修改",
			width : 'auto',
			height : 'auto',
			left : '50%',
			top : '50%',
			content : data,
			esc : true,
			init : function() {
				artdialog = this;
			},
			close : function() {
				artdialog = null;
			}
		});
	});
}

function create() {
	$.get("./new", function(data) {
		art.dialog({
			lock : true,
			opacity : 0.3,
			title : "新增",
			width : 'auto',
			height : 'auto',
			left : '50%',
			top : '50%',
			content : data,
			esc : true,
			init : function() {
				artdialog = this;
				$("#parentId").val(cur_selected.id);
			},
			close : function() {
				artdialog = null;
			}
		});
	});
}

function view(id) {
	$.get("./" + id, {
		ts : new Date().getTime()
	}, function(data) {
		art.dialog({
			lock : true,
			opacity : 0.3,
			title : "查看",
			width : 'auto',
			height : 'auto',
			left : '50%',
			top : '50%',
			content : data,
			esc : true,
			init : function() {
				artdialog = this;
			},
			close : function() {
				artdialog = null;
			}
		});
	});
}

function del(id) {
	if (!confirm("您确定删除此记录吗？")) {
		return false;
	}

	if (id == "1") {
		alert("根节点不能删除")
		return;
	}

	$.get("./treeChildren", {
		parent : id
	}, function(data) {
		if (data.length > 0) {
			alert("该菜单下还有子菜单不能删除！")
			return;
		}else{
			$.get("./delete/" + id, {
				ts : new Date().getTime()
			}, function(data) {
				if (data == 1) {
					if (resource_tb != null) {
						resource_tb.ajax.reload(null, false);
					}
					if (cur_selected.id == id)
						$('#resource_tree').jstree(true).select_node(
								cur_selected.parent);
					$('#resource_tree').jstree(true).refresh();
					alert("删除成功");
				} else {
					alert(data);
				}
			});
		}
	});
};

function closeDialog() {
	artdialog.close();
}