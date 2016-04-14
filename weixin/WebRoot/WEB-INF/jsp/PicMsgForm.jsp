<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%
	String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'PicMsgForm.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
#PicMsgDiv {
	width: 70%;
	border: 1px;
	border-style: solid;
	border-color: #d1cece;
	padding: 30px;
	padding-bottom: 10px;
	border-radius: 10px;
	font-size: 22px;
}
#pl_pic{
max-width: 100%;
}
</style>
</head>

<body>
	<div id="PicMsgDiv">
		<form:form id="form4" name="form4" commandName="picModel"
			action="addPicMessage" method="post" enctype="multipart/form-data">
			<fieldset>
				<legend>添加图片素材</legend>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"><label
						for="name">标&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;题：</label></span>
					<div>
						<div class="input-text">
							<form:input class="form-control" placeholder="PicTitle"
								aria-describedby="basic-addon1" id="picTitle" path="picTitle"
								tabindex="1" />
						</div>
						<form:errors path="picTitle" cssClass="error" />
					</div>
				</div>
				<br>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"><label
						for="name">图&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;片：</label></span>
					<div>
						<div class="input-text">
							<input type="file" id="pl_pic" aria-describedby="basic-addon2"
								name="images[0]" />
						</div>
					</div>
				</div>
				<br>
				<div style="text-align: center;">
					<input id="reset" type="reset" tabindex="4"
						class="btn btn-default my-button"> &nbsp;&nbsp; <input
						id="submit4" type="button" tabindex="5" value="保存"
						class="btn btn-default my-button" onclick="CheckPic()">
				</div>
			</fieldset>
		</form:form>
	</div>
	<script type="text/javascript">
		function CheckPic() {
			var obj = document.getElementById('pl_pic');
			if (obj.value == '') {
				alert('请选择要上传的图片');
				return false;
			}
			var stuff;
			try{
			 stuff = obj.value.match(/^(.*)(\.)(.{1,8})$/)[3];
			}catch(e){
				alert('文件类型不正确，请选择.png或.jpg或.gif或.jpeg格式的图片');
				return false;
			}
			stuff=stuff.toLocaleLowerCase();
			if (stuff != 'png'&&stuff!='jpg'&&stuff!='gif'&&stuff!='jpeg') {
				alert('文件类型不正确，请选择.png或.jpg或.gif或.jpeg格式的图片');
				return false;
			}
			$("#form4").submit();
			return true;
		}
	</script>
</body>
</html>
