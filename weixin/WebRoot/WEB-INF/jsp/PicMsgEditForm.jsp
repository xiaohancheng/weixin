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
#PicMsgEditDiv {
	padding: 30px;
	padding-bottom: 10px;
	font-size: 22px;
}

#edit_plUrl {
	max-width: 100%;
}
</style>
</head>

<body>
	<div id="PicMsgEditDiv">
		<form:form id="form5" name="form5" commandName="picModel"
			action="editPicMessage" method="post" enctype="multipart/form-data">
			<fieldset>
				<legend>修改图片素材</legend>
				<div class="input-group">
					<span class="input-group-addon" id="basic-addon1"><label
						for="name">标&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;题：</label></span>
					<div>
						<div class="input-text">
							<form:input class="form-control" placeholder="PicTitle"
								aria-describedby="basic-addon1" id="edit_plTitle" path="picTitle"
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
							<input type="file" id="edit_plUrl" aria-describedby="basic-addon2"
								name="images[0]" />
						</div>
					</div>
				</div>
				<div style="display: none">
					<form:input path="picId" id="edit_plId" ></form:input>
				</div>
				<br>
				<div style="text-align: center;">
					<input id="submit4" type="button" tabindex="5" value="保存"
						class="btn btn-default my-button" onclick="CheckPic1()">
					&nbsp;&nbsp; <input id="picMsgEditCancel" type="button"
						tabindex="4" onclick="picMsgEditFormDivHide()" value="取消"
						class="btn btn-default my-button">
				</div>
			</fieldset>
		</form:form>
	</div>
	<script type="text/javascript">
		function CheckPic1() {
			var obj = document.getElementById('edit_plUrl');
			if (obj.value == '') {
				$("#form5").submit();
				return true;
			}
			var stuff;
			try {
				stuff = obj.value.match(/^(.*)(\.)(.{1,8})$/)[3];
			} catch (e) {
				alert('文件类型不正确，请选择.png或.jpg或.gif或.jpeg格式的图片');
				return false;
			}
			stuff = stuff.toLocaleLowerCase();
			if (stuff != 'png' && stuff != 'jpg' && stuff != 'gif'
					&& stuff != 'jpeg') {
				alert('文件类型不正确，请选择.png或.jpg或.gif或.jpeg格式的图片');
				return false;
			}
			$("#form5").submit();
			return true;
		}
	</script>
</body>
</html>
