/*客服列表js代码*/
$('#userList_table').bootstrapTable(
						{
							url : "userList",
							dataType : "json",
							pagination : true, //分页
							pageList:[10,25,50,100],
							pageSize:10,
							singleSelect : false,
							search : true, //显示搜索框
							sidePagination : "server", //服务端处理分页
							columns : [
									{
										title : '用户名',
										field : 'emName',
										align : 'center',
										valign : 'middle'
									},
									{
										title : '工号',
										field : 'emNo',
										align : 'center',
										valign : 'middle',
									},
									{
										title : '性别',
										field : 'emSex',
										align : 'center',
										valign : 'middle',
										formatter : function(value, row, index) {
											var s;
											if (row.emSex == 1)
												s = "男";
											else
												s = "女";
											return s;
										}
									},
									{
										title : '邮箱',
										field : 'emEmail',
										align : 'center',
										valign : 'middle',
									},
									{
										title : '联系电话',
										field : 'emPhone',
										align : 'center',
										valign : 'middle',
									},
									{
										title : '身份',
										field : 'emIdentity',
										align : 'center',
										valign : 'middle',
										formatter : function(value, row, index) {
											var s;
											if (row.emIdentity == "user")
												s = "客服人员";
											else
												s = "管理员";
											return s;
										}
									},
									{
										title : '状态',
										field : 'emStatus',
										align : 'center',
										valign : 'middle',
										formatter : function(value, row, index) {
											var s;
											if (row.emStatus == 1)
												s = "启用";
											else
												s = "禁用";
											return s;
										}
									},
									{
										title : '操作',
										field : 'emId',
										align : 'center',
										valign : 'middle',
										formatter : function(value, row, index) {
											var e;
											if(row.emStatus==0){
												e= '<a class=\"caozuo\" onclick="qiyong(\''
													+ row.emId + '\')">启用</a> ';
											}
											else{ e = '<a class=\"caozuo\" onclick="jinyong(\''
													+ row.emId + '\')">禁用</a> ';
											}
											var d = '<a  class=\"caozuo\" onclick="del(\''
													+ row.emId + '\')">删除</a> ';
											return e + d;
										}
									} ]
						});
		function del(emId) {
			if (confirm("确认要删除？")) {
			$.get("delUser?userId=" + emId, function(data, status) {
				if (data == "fail")
					alert("删除失败！");
				else {
					alert("删除成功！");
					$('#userList_table').bootstrapTable('refresh');
				}
			});
			}
		}
		function qiyong(emId){
			$.get("enableUser?userId=" + emId, function(data, status) {
				if (data == "fail")
					alert("启用失败！");
				else {
					alert("启用成功！");
					$('#userList_table').bootstrapTable('refresh');
				}
			});
		}
		function jinyong(emId){
			$.get("disableUser?userId=" + emId, function(data, status) {
				if (data == "fail")
					alert("禁用失败！");
				else {
					alert("禁用成功！");
					$('#userList_table').bootstrapTable('refresh');
				}
			});
		}
/*文本列表js代码*/
		$('#textMsgList_table')
		.bootstrapTable(
				{
					url : "textMsgList",
					dataType : "json",
					pagination : true, //分页
					pageList:[10,25,50,100],
					pageSize:10,
					singleSelect : false,
					search : true, //显示搜索框
					sidePagination : "server", //服务端处理分页
					columns : [
							{
								title : '标题',
								field : 'tlTitle',
								align : 'center',
								valign : 'middle'
							},
							{
								title : '内容',
								field : 'tlContent',
								align : 'center',
								valign : 'middle',
							},
							{
								title : '创建者',
								field : 'emNo',
								align : 'center',
								valign : 'middle',
							},
							{
								title : '状态',
								field : 'tlStatus',
								align : 'center',
								valign : 'middle',
								formatter : function(value, row, index) {
									var s;
									if (row.tlStatus == 1)
										s = "启用";
									else
										s = "禁用";
									return s;
								}
							},
							{
								title : '操作',
								field : 'tlId',
								align : 'center',
								valign : 'middle',
								formatter : function(value, row, index) {
									var e;
									if(row.tlStatus==0){
										e= '<a class=\"caozuo\" onclick="qiyong1(\''
											+ row.tlId + '\')">启用</a> ';
									}
									else{ e = '<a class=\"caozuo\" onclick="jinyong1(\''
											+ row.tlId + '\')">禁用</a> ';
									}
									var d = '<a  class=\"caozuo\" onclick="del1(\''
											+ row.tlId + '\')">删除</a> ';
									var f= '<a  class=\"caozuo\" onclick="edit1(\''
										+ row.tlTitle+ '\',\''
										+ index+ '\',\''
										+ row.tlId+ '\')">修改</a> ';
									return e + d+f;
								}
							} ]
				});
function del1(tlId) {
	if (confirm("确认要删除？")) {
	$.get("delTextMsg?tlId=" + tlId, function(data, status) {
		if (data == "fail")
			alert("删除失败！");
		else {
			alert("删除成功！");
			$('#textMsgList_table').bootstrapTable('refresh');
		}
	});
	}
}
function qiyong1(tlId){
	$.get("enableTextMsg?tlId=" + tlId, function(data, status) {
		if (data == "fail")
			alert("启用失败！");
		else {
			alert("启用成功！");
			$('#textMsgList_table').bootstrapTable('refresh');
		}
	});
}
function jinyong1(tlId){
	$.get("disableTextMsg?tlId=" + tlId, function(data, status) {
		if (data == "fail")
			alert("禁用失败！");
		else {
			alert("禁用成功！");
			$('#textMsgList_table').bootstrapTable('refresh');
		}
	});
}
function edit1(title,index,tlId){
	var content;
	var i=0;
	$('#textMsgList_table tbody tr').eq(index).find("td").each(function () {
		if(i==1) {content=$(this).text();}
		i++;
	}); 
	$("#edit_tlTitle").attr("value",title);
	$("#edit_tlContent").val(content);
	$("#edit_tlId").attr("value",tlId);
	$("#text_Msg_Edit_Form_Div").show();
}
function textMsgEditFormDivHide(){
	$(".error").hide();
	$("#text_Msg_Edit_Form_Div").hide();
}

/*图片列表js代码*/
$('#picMsgList_table')
.bootstrapTable(
		{
			url : "picMsgList",
			dataType : "json",
			pagination : true, //分页
			pageList:[4,8,16,32],
			pageSize:4,
			singleSelect : false,
			search : true, //显示搜索框
			sidePagination : "server", //服务端处理分页
			columns : [
					{
						title : '标题',
						field : 'plTitle',
						align : 'center',
						valign : 'middle'
					},
					{
						title : '图片',
						field : 'plUrl',
						align : 'center',
						valign : 'middle',
						formatter : function(value, row, index) {
							var s;
							s="<img onclick=\"showBigPic('"+row.plUrl+"')\" class=\"preview_pl_pic\" src=\""+row.plUrl+"\"  alt=\"未知错误\"/>"
							return s;
						}
					},
					{
						title : '创建者',
						field : 'emNo',
						align : 'center',
						valign : 'middle',
					},
					{
						title : '状态',
						field : 'plStatus',
						align : 'center',
						valign : 'middle',
						formatter : function(value, row, index) {
							var s;
							if (row.plStatus == 1)
								s = "启用";
							else
								s = "禁用";
							return s;
						}
					},
					{
						title : '操作',
						field : 'plId',
						align : 'center',
						valign : 'middle',
						formatter : function(value, row, index) {
							var e;
							if(row.plStatus==0){
								e= '<a class=\"caozuo\" onclick="qiyong2(\''
									+ row.plId + '\')">启用</a> ';
							}
							else{ e = '<a class=\"caozuo\" onclick="jinyong2(\''
									+ row.plId + '\')">禁用</a> ';
							}
							var d = '<a  class=\"caozuo\" onclick="del2(\''
									+ row.plId + '\')">删除</a> ';
							var f= '<a  class=\"caozuo\" onclick="edit2(\''
								+ row.plTitle+ '\',\''
								+ row.plId+ '\')">修改</a> ';
							return e + d+f;
						}
					} ]
		});
function del2(plId) {
if (confirm("确认要删除？")) {
$.get("delPicMsg?plId=" + plId, function(data, status) {
if (data == "fail")
	alert("删除失败！");
else {
	alert("删除成功！");
	$('#picMsgList_table').bootstrapTable('refresh');
}
});
}
}
function qiyong2(plId){
$.get("enablePicMsg?plId=" + plId, function(data, status) {
if (data == "fail")
	alert("启用失败！");
else {
	alert("启用成功！");
	$('#picMsgList_table').bootstrapTable('refresh');
}
});
}
function jinyong2(plId){
$.get("disablePicMsg?plId=" + plId, function(data, status) {
if (data == "fail")
	alert("禁用失败！");
else {
	alert("禁用成功！");
	$('#picMsgList_table').bootstrapTable('refresh');
}
});
}
function edit2(plTitle,plId){
$("#edit_plTitle").attr("value",plTitle);
$("#edit_plId").attr("value",plId);
$("#pic_Msg_Edit_Form_Div").show();
}
function picMsgEditFormDivHide(){
$(".error").hide();
$("#pic_Msg_Edit_Form_Div").hide();
}
function showBigPic(url){
	var div=$(".col-md-10");
	var str="";
	str+="<div id=\"big_Pic_div\" class=\"alert alert-warning alert-dismissible\" role=\"alert\">";
	str+="<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>";
	str+="<img src=\""+url+"\" alt=\"未知错误\">";
	str+="</div>";
	div.append(str);
}

/*聊天记录列表js代码*/
$('#lastChatInfoList_table')
.bootstrapTable(
		{
			url : "lastChatList",
			dataType : "json",
			pagination : true, //分页
			pageList:[10,25,50,100],
			pageSize:10,
			singleSelect : false,
			search : true, //显示搜索框
			sidePagination : "server", //服务端处理分页
			columns : [
					{
						title : '发送者',
						field : 'FromUserName',
						align : 'center',
						valign : 'middle'
					},
					{
						title : '接收者',
						field : 'ToUserName',
						align : 'center',
						valign : 'middle'
					},
					{
						title : '内容',
						//field : 'emNo',
						align : 'center',
						valign : 'middle',
						formatter : function(value, row, index) {
							var e="";
							if(row.MsgType=="image"){
								e='<img class=\"preview_pl_pic\" src=\"'+row.picUrl+'\">';
							}else if(row.MsgType=="text"){
								e='<a>'+replaceEmoji(row.Content)+'</a>';
							}
							return e;
						}
					},
					{
						title : '对话时间',
						field : 'CreateTime',
						align : 'center',
						valign : 'middle',
						formatter : function(value, row, index) {
							var timestamp =row.CreateTime ;
							var newDate = new Date();
							newDate.setTime(timestamp * 1000);
							return newDate.toLocaleString();
						}
					},
					{
						title : '操作',
						field : 'MsgId',
						align : 'center',
						valign : 'middle',
						formatter : function(value, row, index) {
							var e='<a class=\"caozuo\" onclick=\"detail(\''+row.MsgId+'\')\">详情</a>';
							return e;
						}
					} ]
		});