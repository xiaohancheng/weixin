<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'login.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
<!-- <link href="./css/bootstrap.min.css" rel="stylesheet"> -->
	<link rel="stylesheet" type="text/css" href="./css/login.css">
	<script type="text/javascript" src="./js/jquery.js"></script>
	<script type="text/javascript" src="./js/md5.js"></script>
  </head>
  
  <body>
  <div id="loginDiv" align="center">
  <form>
  <p>微客服</p>
  <br>
  	<a>账&nbsp;&nbsp;号：</a>
    <input type="text" id="username" name="username"   value=""/> <br><br><br><br>
    <a>密&nbsp;&nbsp;码：</a>
    <input type="password" id="password" name="password" value=""/><br><br><br>
    <input type="radio" name="identity" value="admin"/> 管理员
    <input type="radio" name="identity" value="user" checked="checked"/>客服人员<br><br><br>
    <input class="button" onclick="login()" type="button" name="登录" value="登录"/>
    </form>
    </div>
    <script type="text/javascript">
    function login(){
    	var username=$("#username").val();
    	if(username==""){ alert("账号不能为空");return true;}
    	var password=$("#password").val();
    	if(password==""||password.length<6){
    		alert("密码长度不能小于6");
    		return true;
    	}
    	var identity=$('#loginDiv input[name="identity"]:checked ').val(); 
    	 $.ajax({
             url:"login",
             type:'post',
             dataType:'json',
             data:{"username":username,"password":hex_md5(password),"identity":identity},
             success:function(data){
                 if(data.result=="login"){
                	 alert("账号或密码输入错误");
                 }else if(data.result=="KFClient"){
                     window.location.href =  "KFClient?emNo="+data.emNo;
                 }else if(data.result=="KFManage"){
                	 window.location.href =  "KFManage?emNo="+data.emNo;
                 }
             }
         });
    }
    </script>
  </body>
</html>
