<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <meta name="viewport" content="width=device-width, initial-scale=1">
<title>${siteName}</title>
<%@include file="/common/sys_jsp_header.jsp" %>
    <script type="text/javascript" src="${ctx}/resources/js/common/jquery-1.11.1.min.js"></script>
     <link href="${ctx}/resources/css/style.css" rel="stylesheet" type="text/css">
    <link href="${ctx}/resources/css/login.css" rel="stylesheet" type="text/css">
  </head>
  <body>
	<div class="shangfq-header-two">
		<div class="container-two">
			<div class="logo"></div>
		</div>
	</div>
	<div class="shangfq-body login-page">
		<div class="container-two">
			<div class="floatleft">
				<div class="login-banner"><img src="${ctx}/resources/images/pc/login/banner.jpg"/></div>
			</div>
			<div class="floatright">
				<div class="login-submit-box">
					<div class="title">登录</div>
					<div style="display:none;" class="errorInfo">请输入正确的用户名密码</div>
					<div class="login-inputBox">
						<div class="login-input">
							<div class="inputName urername"></div>
							<input class="input-control" placeholder="用户名" id="userName" name="userName" maxlength="10" type="text" value=""/>
						</div>
						<div class="login-input">
							<div class="inputName pwdname"></div>
							<input class="input-control" type="password"  id="passWord" name="passWord" maxlength="10" placeholder="密码" value=""/>
						</div>
						<div class="login-btnBox">
							<input class="login-btn" type="button" onclick="dologin()" value="立即登录" />
						</div>
					</div>
				</div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	<div class="shangfq-footer-two">
		<p>闽ICP备140****号-1&nbsp;&nbsp;增值电信业务经营许可证***-20150324</p>
		<p>Copyright &copy; 2017 Fenqibao All Rughts Reserved<br/>八维通 版权所有</p>
	</div>
	
	<script>
		function dologin(){
			var userName=$.trim($("#userName").val());
			var passWord=$.trim($("#passWord").val());
			if(userName.length==0){
				$(".errorInfo").html("请输入用户名！").show();
				return ;
			}
			if(passWord.length==0){
				$(".errorInfo").html("请输入密码！").show();
				return ;
			}
			
			$.ajax({
				url : path+'/dologin',
				type : "post",
				dataType : "json",
				data : { "userName": userName, "passWord": passWord }
			}).done(function(backdata) {
				if(backdata.result==1){
	           	  window.location.href="${ctx}/index";
				}
	           	 else{
	 				$(".errorInfo").html(backdata.msg).show();
	           	 }
			})
			
		}

		// 按回车键登录
		$(document).keydown(function(event) {
			if (event.keyCode == 13) {
				dologin();
			}
		})
	</script>
	




</body>
</html>