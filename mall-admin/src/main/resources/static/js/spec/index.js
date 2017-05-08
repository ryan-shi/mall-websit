var cur_selected = null;
var spec_tb = null;
$(function() {
	$('#catalog_tree').jstree(
			{
				"core" : {
					"themes" : {
						"responsive" : false
					},
					"check_callback" : false,
					'data' : {
						'url' : function(node) {
							return node.id == '#' ? '/admin/catalog/treeRoot'
									: '/admin/catalog/treeChildren';
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

	$('#catalog_tree').on("ready.jstree", function(e, data) {
		// console.log("页面刷新，选择跟节点！");
		$('#catalog_tree').jstree(true).select_node(1);
	});

	$('#catalog_tree')
			.on(
					"select_node.jstree",
					function(e, data) {
						$('#catalog_tree')
								.jstree(true)
								.open_node(
										data.node,
										function(openedData) {
											cur_selected = openedData;
											if (spec_tb != null) {
												spec_tb.settings()[0].ajax.data.id = cur_selected.id;
												spec_tb.ajax
														.reload(null, false);
											} else {
												spec_tb = $('#spec_tb')
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
																		url : "/admin/spec/list",
																		'data' : {
																			'id' : cur_selected.id
																		}
																	},
																	columns : [
																			{
																				title : "分类名称",
																				data : "catalogDTO.name",
																			},
																			{
																				title : "规格名称",
																				data : "name",
																			},
																			{
																				title : "规格编码",
																				data : "code",
																			},
																			{
																				title : "规格排序",
																				data : "sort",
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
																				title : "规格值管理",
																				data : "null",
																				render : function(
																						data,
																						type,
																						row,
																						meta) {
																					return '<button class="btn btn-default btn-xs fa fa-cog" onclick="spec_option('
																							+ row.id
																							+ ')" ></button>';
																				},
																				width : "65px",
																				className:"td-center",
																				orderable : false
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
				$("#catalog_id").val(cur_selected.id);
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

	$.get("./delete/" + id, {
		ts : new Date().getTime()
	}, function(data) {
		if (data == 1) {
			if (spec_tb != null) {
				spec_tb.ajax.reload(null, false);
			}
			alert("删除成功");
		} else {
			alert(data);
		}
	});
};

function spec_option(id){
	$.get("/admin/specOption/index/" + id, {
		ts : new Date().getTime()
	}, function(data) {
		art.dialog({
			lock : true,
			opacity : 0.3,
			title : "规格值管理",
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

function closeDialog() {
	artdialog.close();
}