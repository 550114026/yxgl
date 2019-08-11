<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>个人中心</title>
    <%@include file="/common/sys_header.jsp" %>
</head>
<body class="inside-page">
    <div class="bookHouse-page">
        <div class="personal-center-header">
            <img class="center-bg" src="${ctx}/resources/images/centerbanner.png" />
            <div class="center-user" onclick="ttp('${ctx}/user/info?id=${user.id}')">
                <div class="user-imgbox">
                    <%-- <img class="user-img" src="${ctx}/resources/images/bookHouse-img.png" /> --%>
                    
                    	<c:choose>  
						    <c:when test="${fn:substring(user.headimgurl,0,4)=='http'}">  
                        		<img class="user-img" src="${user.headimgurl}" />
						    </c:when>  
						   <c:otherwise>  
                        		<img class="user-img" src="${ctx}/upload/images/${user.headimgurl}" />
						    </c:otherwise>  
						</c:choose>  
                    
                </div>
                <span class="user-name">${user.nick_name}</span>
            </div>
        </div>
        <div class="bookHouse-inside-page-panel">
            <ul class="bookHouse-inside-detail">
               <%--  <li>
                    <div class="personal-center" onclick="ttp('${ctx}/user/msglist')">
                        <img class="centerimg" src="${ctx}/resources/images/gricon1.png" />
                        <span class="name">我的消息</span>
                       <div class="fr">
                            <span class="infonum">${message.count}条信息未读</span>
                            <span class="h_arrow"></span>
                        </div>
                    </div>
                </li> --%>
                <li>
                    <div class="personal-center" onclick="ttp('${ctx}/user/myborrow')">
                        <img class="centerimg" src="${ctx}/resources/images/gricon2.png" />
                        <span class="name">我的借阅</span>
                        <div class="fr">
                            <span class="infonum">${borrowRecord.count}条借阅记录</span>
                            <span class="h_arrow"></span>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="personal-center" onclick="ttp('${ctx}/user/info')">
                        <img class="centerimg" src="${ctx}/resources/images/gricon3.png" />
                        <span class="name">个人信息</span>
                    </div>
                </li>

            </ul>
        </div>

    </div>
    
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>