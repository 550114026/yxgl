<%@ page language="java" pageEncoding="UTF-8" import="webSocket.WebSocketMessageServlet,com.bookhouse.utils.UserUtil,com.bookhouse.model.User"%>
<%
	String user = ((User)com.bookhouse.utils.UserUtil.getUser(request)).getStr("id");
	//if(user == null){
		//为用户生成昵称
		//user = "游客" + UserUtil.WebSocketMessageServlet.ONLINE_USER_COUNT;
		//WebSocketMessageServlet.ONLINE_USER_COUNT ++;
		//session.setAttribute("user", user);
	//}
	pageContext.setAttribute("user", user);
%>
<html>
<head>
	<title>WebSocket 聊天室</title>
	<!-- 引入CSS文件 -->
	<link rel="stylesheet" type="text/css" href="ext4/resources/css/ext-all.css">
	<link rel="stylesheet" type="text/css" href="ext4/shared/example.css" />
	<link rel="stylesheet" type="text/css" href="css/websocket.css" />
	
	<!-- 映入Ext的JS开发包，及自己实现的webscoket. -->
	<script type="text/javascript" src="ext4/ext-all-debug.js"></script>
	<script type="text/javascript" src="websocket.js"></script>
	<script type="text/javascript" src="ext4/desktop/common.js"></script>
	<script type="text/javascript">
		var user = "${user}";
	</script>
</head>

<body>
	<h1>WebSocket聊天室</h1>
	<p>通过HTML5标准提供的API与Ext富客户端框架相结合起来，实现聊天室，有以下特点：</p>
	<ul class="feature-list" style="padding-left: 10px;">
		<li>实时获取数据，由服务器推送，实现即时通讯</li>
		<li>利用WebSocket完成数据通讯，区别于轮询，长连接等技术，节省服务器资源</li>
		<li>结合Ext进行页面展示</li>
		<li>用户上线下线通知</li>
	</ul>
	<div id="websocket_button"></div>
</body>
</html>
