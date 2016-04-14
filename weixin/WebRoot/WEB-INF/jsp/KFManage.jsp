<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>客服后台管理系统</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="./css/bootstrap.min.css" rel="stylesheet">
<link href="./css/bootstrap-table.css" rel="stylesheet">
<link href="./css/KFManage.css" rel="stylesheet">
<script src="./js/jquery.js"></script>
<script src="./js/bootstrap.min.js"></script>
<script src="./js/bootstrap-table.js"></script>
<script src="./js/bootstrap-table-multiple-search.js"></script>
<script src="./js/bootstrap-table-zh-CN.js"></script>
<script type="text/javascript" src="./js/md5.js"></script>
<script type="text/javascript" src="./js/emoji.js"></script>
 <script type="text/javascript" src="./js/highcharts.js"></script> 
 <script type="text/javascript" src="./js/exporting.js"></script> 
</head>

<body onload="init('<%=request.getAttribute("flag")%>')">
	<div class="navbar navbar-duomi navbar-static-top" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="KFManage?emNo=<%=session.getAttribute("emNo")%>" id="logo">客服后台管理平台 </a>
			</div>
			<div id="manageaccount">
				欢迎： <a onclick="accountClick(this,event)"><%=session.getAttribute("emNo")%></a>
			</div>
			<div id="managelogout">
				<a onclick="quit()">退出</a>
			</div>
		</div>

	</div>
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-2">
				<ul id="main-nav" class="nav nav-tabs nav-stacked" style="">
					<li class="active"><a> 导航 </a></li>
					<li><a id="menu1" href="#kefuManage"
						class="nav-header collapsed" data-toggle="collapse"> 客服人员管理 </a>
						<ul id="kefuManage" class="nav nav-list collapse secondmenu"
							style="height: 0px;">
							<li><a onclick="showKFMsg()">查看客服人员</a></li>
							<li><a id="addKF" onclick="showAddKF()">添加客服人员</a></li>
						</ul></li>
					<li><a id="menu2" href="#textSucaiManage"
						class="nav-header collapsed" data-toggle="collapse"> 文本素材管理 </a>
						<ul id="textSucaiManage" class="nav nav-list collapse secondmenu"
							style="height: 0px;">
							<li><a onclick="showTextSucai()">查看文本素材</a></li>
							<li><a id="addTextSucai" onclick="showAddTextSucai()">添加文本素材</a></li>
						</ul></li>

					<li><a id="menu3" href="#picSucaiManage"
						class="nav-header collapsed" data-toggle="collapse"> 图片素材管理 </a>
						<ul id="picSucaiManage" class="nav nav-list collapse secondmenu"
							style="height: 0px;">
							<li><a onclick="showPicSucai()">查看图片素材</a></li>
							<li><a id="addPicSucai" onclick="showAddPicSucai()">添加图片素材</a></li>
						</ul></li>

					<li><a id="menu4" href="#infoManage"
						class="nav-header collapsed" data-toggle="collapse"> 聊天记录管理 </a>
						<ul id="infoManage" class="nav nav-list collapse secondmenu"
							style="height: 0px;">
							<li><a onclick="showChatInfo()">查看聊天记录</a></li>
							<li><a id="countChatInfo" onclick="countChatInfo()">聊天记录统计</a></li>
						</ul></li>
					<li><a id="menu5" class="nav-header collapsed" data-toggle="collapse" onclick="showSystemInfo()">
							关于系统
					</a></li>

				</ul>
			</div>
			<div class="col-md-10">
				<!-- 客服列表 -->
				<div id="user_List_Div">
					<div id="userList_div">
						<table id="userList_table"></table>
					</div>
				</div>
				<!-- 客服信息表单 -->
				<div id="user_Form_Div"><jsp:include page="UserForm.jsp"></jsp:include></div>
				<!-- 文本消息表单 -->
				<div id="text_Msg_Form_Div"><jsp:include
						page="TextMsgForm.jsp"></jsp:include></div>
				<!-- 文本消息编辑表单 -->
				<div id="text_Msg_Edit_Form_Div"
					class="alert alert-warning alert-dismissible" role="alert">
					<button type="button" class="close" aria-label="Close"
						onclick="textMsgEditFormDivHide()">
						<span aria-hidden="true">&times;</span>
					</button>
					<jsp:include page="TextMsgEditForm.jsp"></jsp:include>
				</div>
				<!-- 文本列表 -->
				<div id="text_Msg_List_Div">
					<div id="textMsgList_div">
						<table id="textMsgList_table"></table>
					</div>
				</div>
				<!-- 图片消息表单 -->
				<div id="pic_Msg_Form_Div">
				<jsp:include page="PicMsgForm.jsp"></jsp:include>
				</div>
				<!-- 图片消息列表 -->
				<div id="pic_Msg_List_Div">
					<div id="picMsgList_div">
					<table id="picMsgList_table"></table>
					</div>
				</div>
				<!-- 图片消息编辑表单 -->
				<div id="pic_Msg_Edit_Form_Div"
					class="alert alert-warning alert-dismissible" role="alert">
					<button type="button" class="close" aria-label="Close"
						onclick="picMsgEditFormDivHide()">
						<span aria-hidden="true">&times;</span>
					</button>
					<jsp:include page="PicMsgEditForm.jsp"></jsp:include>
				</div>
				<!-- 聊天信息列表 -->
				<div id="last_Chat_Info_List_Div">
				<div id="lastChatInfoList_div">
				<table id="lastChatInfoList_table"></table>
				</div>
				</div>
				<!-- 聊天信息统计 -->
				<div id="count_Chat_Info_Div">
				<div id="countChatInfo_Div">
					<div>
					<br>
					<select id="countChatSelect" onchange="inithighChart() " class="btn btn-default dropdown-toggle " data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
					<option selected="selected" value="本日">本日服务人数</option>
					<option value="本周">本周服务人数</option>
					<option value="本月">本月服务人数</option>
					<option value="本年">本年服务人数</option>
					<option value="全部">总共服务人数</option>
					</select></div><br>
					 <div id="CountChatInfocontainer" style="min-width:700px;height:400px"></div>
				</div>
				</div>
				<!-- 关于系统 -->
				<div id="system_Info_Div">
				<fieldset>
				<legend>关于我们</legend>
				<div><h4>什么是微信客服管理平台</h4>
					微信在线客服管理平台提供对微信在线客服系统的管理功能，包括对客服人员的管理、素材的管理以及聊天记录的管理。
				</div><br>
				<div><h4>什么是微信在线客服系统</h4>
					微信在线客服系统是一套客服人员与客户通过微信公众号进行在线服务的系统，客户可以在微信公众号中点击在线客服
					<br>按钮与客服建立连接进行在线咨询。目前支持的消息类型为文本类型和图片类型。
				</div><br>
				<div>
				<h4>版本</h4>
				微信客服管理平台：V1.1.1.20160414_beta
				</div><br>
				<div>
				<h4>联系方式</h4>
				移动电话：15913126585<br><br>
				QQ邮箱：1242805755@qq.com<br><br>
				<div style="display:inline"><div style="float:left">微信：</div><div style="float:left"><img style="width: 120px" src="./image/wechatErWeiMa.png"></div>
				</div>
				</div>
				</fieldset>
				</div>
			</div>
		</div>
	</div>
	<jsp:include page="AccountMsg.jsp"></jsp:include>
	<script type="text/javascript">
		var account =
	<%=request.getAttribute("emNo")%>
		;
		function showKFMsg() {
			$("#user_Form_Div").hide();
			$("#text_Msg_Form_Div").hide();
			$("#text_Msg_List_Div").hide();
			$("#pic_Msg_Form_Div").hide();
			$("#pic_Msg_List_Div").hide();
			$("#last_Chat_Info_List_Div").hide();
			$("#count_Chat_Info_Div").hide();
			$("#system_Info_Div").hide();
			$("#user_List_Div").show();
		}
		function showTextSucai() {
			$("#user_Form_Div").hide();
			$("#text_Msg_Form_Div").hide();
			$("#user_List_Div").hide();
			$("#pic_Msg_Form_Div").hide();
			$("#pic_Msg_List_Div").hide();
			$("#last_Chat_Info_List_Div").hide();
			$("#count_Chat_Info_Div").hide();
			$("#system_Info_Div").hide();
			$("#text_Msg_List_Div").show();
		}
		function showAddKF() {
			$("#text_Msg_Form_Div").hide();
			$("#user_List_Div").hide();
			$("#text_Msg_List_Div").hide();
			$("#pic_Msg_Form_Div").hide();
			$("#pic_Msg_List_Div").hide();
			$("#last_Chat_Info_List_Div").hide();
			$("#count_Chat_Info_Div").hide();
			$("#system_Info_Div").hide();
			$("#user_Form_Div").show();
		}
		function showAddTextSucai() {
			$("#user_List_Div").hide();
			$("#user_Form_Div").hide();
			$("#text_Msg_List_Div").hide();
			$("#pic_Msg_Form_Div").hide();
			$("#pic_Msg_List_Div").hide();
			$("#last_Chat_Info_List_Div").hide();
			$("#count_Chat_Info_Div").hide();
			$("#system_Info_Div").hide();
			$("#text_Msg_Form_Div").show();
		}
		function showAddPicSucai(){
			$("#user_List_Div").hide();
			$("#user_Form_Div").hide();
			$("#text_Msg_List_Div").hide();
			$("#text_Msg_Form_Div").hide();
			$("#pic_Msg_List_Div").hide();
			$("#last_Chat_Info_List_Div").hide();
			$("#count_Chat_Info_Div").hide();
			$("#system_Info_Div").hide();
			$("#pic_Msg_Form_Div").show();
		}
		function showPicSucai(){
			$("#user_List_Div").hide();
			$("#user_Form_Div").hide();
			$("#text_Msg_List_Div").hide();
			$("#text_Msg_Form_Div").hide();
			$("#pic_Msg_Form_Div").hide();
			$("#last_Chat_Info_List_Div").hide();
			$("#count_Chat_Info_Div").hide();
			$("#system_Info_Div").hide();
			$("#pic_Msg_List_Div").show();
		}
		function showChatInfo(){
			$("#user_List_Div").hide();
			$("#user_Form_Div").hide();
			$("#text_Msg_List_Div").hide();
			$("#text_Msg_Form_Div").hide();
			$("#pic_Msg_Form_Div").hide();
			$("#pic_Msg_List_Div").hide();
			$("#count_Chat_Info_Div").hide();
			$("#system_Info_Div").hide();
			$("#last_Chat_Info_List_Div").show();
		}
		function countChatInfo(){
			$("#user_List_Div").hide();
			$("#user_Form_Div").hide();
			$("#text_Msg_List_Div").hide();
			$("#text_Msg_Form_Div").hide();
			$("#pic_Msg_Form_Div").hide();
			$("#pic_Msg_List_Div").hide();
			$("#last_Chat_Info_List_Div").hide();
			$("#system_Info_Div").hide();
			$("#count_Chat_Info_Div").show();
			inithighChart();
		}
		function showSystemInfo(){
			$("#user_List_Div").hide();
			$("#user_Form_Div").hide();
			$("#text_Msg_List_Div").hide();
			$("#text_Msg_Form_Div").hide();
			$("#pic_Msg_Form_Div").hide();
			$("#pic_Msg_List_Div").hide();
			$("#last_Chat_Info_List_Div").hide();
			$("#count_Chat_Info_Div").hide();
			$("#system_Info_Div").show();
		}
		function inithighChart(){
			var time=$("#countChatSelect").val();
			$.get("countChatInfo?time=" +time, function(
					data, status) {
				$('#CountChatInfocontainer').highcharts(JSON.parse(data));
			});
		}
	
		function init(flag) {
			if (flag == "AddUserSuccess") {
				$("#addKF").click();
				$("#menu1").click();
				alert("添加成功");
				showKFMsg();
			} else if (flag == "AddUserFail") {
				$("#addKF").click();
				$("#menu1").click();
			} else if (flag == "AddTextMsgFail") {
				$("#addTextSucai").click();
				$("#menu2").click();
			} else if (flag == "AddTextMsgSuccess") {
				$("#addTextSucai").click();
				$("#menu2").click();
				alert("添加成功");
				showTextSucai();
			} else if (flag == "editTextMsgFail") {
				$("#menu2").click();
				showTextSucai();
				$("#text_Msg_Edit_Form_Div").show();
			} else if (flag == "editTextMsgSuccess") {
				$("#menu2").click();
				showTextSucai();
				alert("修改成功");
			}else if(flag=="AddPicMsgSuccess"){
				$("#addPicSucai").click();
				$("#menu3").click();
				alert("添加成功");
				showPicSucai();
			}else if(flag=="AddPicMsgFail"){
				$("#addPicSucai").click();
				$("#menu3").click();
			}else if(flag=="editPicMsgFail"){
				$("#menu3").click();
				showPicSucai();
				$("#pic_Msg_Edit_Form_Div").show();
			}else if(flag=="editPicMsgSuccess"){
				$("#menu3").click();
				showPicSucai();
				alert("修改成功");
			}
		}
		function quit() {
			$.get("logout?username=" + account, function(data, status) {
				location.href = "welcome";
			});
		}
		$(document).click(function(event){
		    resetAccountMsg();
		    $('#accountMessage').hide();
		});

		function accountClick(e,event){
			 if ( event && event.stopPropagation ) { 
			        // this code is for Mozilla and Opera 
			    event.stopPropagation(); 
			} 
			else if (window.event) { 
			        // this code is for IE 
			    window.event.cancelBubble = true; 
			}
			$('#accountMessage').show();
		}
	</script>
	<script src="./js/KFManage.js"></script>
</body>
</html>
