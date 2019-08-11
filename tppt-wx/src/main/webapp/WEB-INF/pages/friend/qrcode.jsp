<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>瓜藤图书，您的个人图书管理工具，与您好友一起分享图书</title>
    <%@include file="/common/sys_header.jsp" %>
</head>
<body>
        
        <div class="bookHouse-book-list-panel paddingList">
            <div class="qrcode" align="center" >
                <img class="img" width="80%"  src="${ctx}/resources/images/qrcode.png">
                <p class="sub_title">扫描二维码关注微信</p>
                <p class="sub_title">或者将二维码分享给朋友</p>
            </div>
        </div>
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>