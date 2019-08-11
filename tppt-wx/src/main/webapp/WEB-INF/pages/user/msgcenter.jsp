<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>消息中心</title>
    <%@include file="/common/sys_header.jsp" %>
</head>
<body class="inside-page">
    <div class="bookHouse-page">
        <div class="bookHouse-inside-page-panel">
            <ul class="bookHouse-inside-detail">
            
	         <c:forEach var="message" items="${messageTypeList}" varStatus="status">
                <li>
                    <div class="personal-center" onclick="ttp('${ctx}/user/msglist?t=${message.dict_code}')">
                        <img class="centerimg" src="${ctx}/resources/images/gricon1.png" />
                        <span class="name">${message.dict_name}</span>
                       <div class="fr msg">
                          <span <c:if test="${message.num!=0}">class="num"</c:if> >${message.num}</span> <span class="infonum">条未读信息</span>
                            <span class="h_arrow"></span>
                        </div>
                    </div>
                </li>
                
			</c:forEach>
            </ul>
        </div>

    </div>
    <script>
    </script>
    
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>