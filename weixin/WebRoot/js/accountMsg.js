function resetAccountMsg(){
	$("#ch_emName").attr("disabled","disabled");
	$("#ch_emSex").attr("disabled","disabled");
	$("#ch_emEmail").attr("disabled","disabled");
	$("#ch_emPhone").attr("disabled","disabled");
	$("#accChangePass").show();
	$("#accEdit").show();
	$("#accMsgSave").hide();
	$("#cancelEdit").hide();
    $("#chPassDiv").hide();
	$("#passSave").hide();
	$("#chanMsgDiv").show();
}
function accountMessageClick(event){
	 if ( event && event.stopPropagation ) { 
	        // this code is for Mozilla and Opera 
	    event.stopPropagation(); 
	} 
	else if (window.event) { 
	        // this code is for IE 
	    window.event.cancelBubble = true; 
	}
}
function accountEditCilck(){
	$("#ch_emName").removeAttr("disabled");
	$("#ch_emSex").removeAttr("disabled");
	$("#ch_emEmail").removeAttr("disabled");
	$("#ch_emPhone").removeAttr("disabled");
	$("#accChangePass").hide();
	$("#accEdit").hide();
	$("#accMsgSave").show();
	$("#cancelEdit").show();
	 return true;
}
function updateUser(){
	var emNo=$("#ch_emNo").val();
	var emName=$("#ch_emName").val();
	if(emName==""){alert("名字不能为空");return false;}
	var emSex=$("#ch_emSex").val();
	var emEmail=$("#ch_emEmail").val();
	 var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	 if (!filter.test(emEmail)){
	 alert('您的电子邮件格式不正确');
	 return false;}
	var emPhone=$("#ch_emPhone").val();
	 var reg = /^0?1[3|4|5|8][0-9]\d{8}$/;
	 if (!reg.test(emPhone)){
	      alert("号码有误");
	      return false;
	 }
	var message={"emNo":emNo,"emName":emName,"emSex":emSex,"emEmail":emEmail,"emPhone":emPhone};
	$.ajax({
        type : 'POST',  
        data :"jsonStr="+JSON.stringify(message),    
        url : 'updateUser.do', 
        success : function(data, status) {  
        	alert("修改成功");
        	resetAccountMsg();
        },  
        error : function(error, exception) {  
            alert("修改失败");  
        }  
    });  
	return true;
}
function updatePassword(){
	var emNo=$("#ch_emNo").val();
	var oldPassword=hex_md5($("#oldPassword").val());
	if($("#oldPassword").val()==""||$("#oldPassword").val().length<6){alert("旧密码长度不能小于6");return false;}
	if($("#newPassword").val()==""||$("#newPassword").val().length<6) {alert("新密码长度不能小于6");return false;}
	if($("#newPassword").val()!=$("#ensurePassword").val()){alert("两次输入密码不同");return false}
	var newPassword=hex_md5($("#newPassword").val());
	var ensurePassword=hex_md5($("#ensurePassword").val());
	var message={"emNo":emNo,"oldPassword":oldPassword,"newPassword":newPassword,"ensurePassword":ensurePassword};
	$.ajax({
        type : 'POST',  
        data :"jsonStr="+JSON.stringify(message),    
        url : 'changePassword.do', 
        success : function(data, status) {  
        	if(data=="error1") alert("两次输入的密码不同");
        	else if(data=="error2") alert("旧密码输入错误");
        	else {
        		alert("密码修改成功");
        		resetAccountMsg();
        	}
        },  
        error : function(error, exception) {  
            alert("密码修改失败");  
        }  
    });  
	return true;
}
function accChanpass(){
	$("#chanMsgDiv").hide();
	$("#chPassDiv").show();
	$("#accChangePass").hide();
	$("#accEdit").hide();
	$("#passSave").show();
	$("#cancelEdit").show();
	return true;
}