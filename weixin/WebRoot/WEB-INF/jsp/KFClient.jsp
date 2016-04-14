<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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

<title>My JSP 'HelloWorld.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="./css/bootstrap.min.css" rel="stylesheet">
<link href="./css/bootstrap-table.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" href="./css/KFClient.css">
<script type="text/javascript" src="./js/jquery.js"></script>
<script type="text/javascript" src="./js/emoji.js"></script>

<script type="text/javascript" src="./js/md5.js"></script>
<script src="./js/bootstrap-table.js"></script>
<script src="./js/bootstrap-table-multiple-search.js"></script>
<script src="./js/bootstrap-table-zh-CN.js"></script>
<script src="./js/bootstrap.min.js"></script>
</head>

<body onload="init('<%=session.getAttribute("emNo")%>')">
	<div id="container">
		<div id="head">
			<div id="logo_image">
				<img alt="未知错误" src="./image/logo.png">
			</div>
			<div id="logo_name">
				<label>|微客服</label>
			</div>
			<div id="account">
				欢迎： <a onclick="accountClick(this,event)"><%=session.getAttribute("emNo")%></a>
			</div>
			<div id="logout">
				<a onclick="quit()">退出</a>
			</div>
		</div>
		<div id="contact_panel">
			<ul>
				<li  id="wait_li">待接入</li>
				<li id="jieru_li">已接入</li>
			</ul>
			<div id="wait_panel"></div>
			<div id="jieru_panel"></div>
		</div>
		<div id="container1">
			<div id="message_panel" class="message_panel"></div>
			<div id="talk_panel" class="talk_panel">
				<div id="talk" class="talk"></div>
				<br>
				<button type="button" id="send" class="send">发送</button>
			</div>
		</div>
		<div id="chat_sucai">
		<ul>
				<li  id="searchText_li">搜索文本</li>
				<li id="searchPic_li">搜索图片</li>
		</ul>
		<div id="textMsgList_div"><table id="textMsgList_table"></table></div>
		<div id="picMsgList_div"><table id="picMsgList_table"></table></div>
		</div>
	</div>
	<select id="select" class="btn btn-default dropdown-toggle " data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
		<option>手动接入</option>
		<option>自动接入</option>
	</select>
	<div id="kong">不能发送空白信息</div>
	<div id="pictures"></div>
	<div id="emojis"></div>
	<jsp:include page="AccountMsg.jsp"></jsp:include>
	<script type="text/javascript" src="./js/client.js"></script>
</body>
</html>
