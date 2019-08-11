<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>阅读排名</title>
    <%@include file="/common/sys_header.jsp" %>
</head>
<body>

 <div class="bookHouse-page">
        <div class="bookHouse-book-list-panel marginList">
       
           <c:forEach var="user" items="${page.list}" varStatus="status">
           <ul class="bookHouse-list" onclick="ttp('${ctx}/user/readlist?id=${user.id}')">
                <li class="list-left">
                <c:if test="${status.index<3}">
                    <span class="num">${status.index+1}</span>
				</c:if>
                    <c:choose>  
						    <c:when test="${fn:substring(user.headimgurl,0,4)=='http'}">  
                        		<img class="book-img" src="${user.headimgurl}" />
						    </c:when>  
						   <c:otherwise>  
                        		<img class="book-img" src="${ctx}/upload/images/${user.headimgurl}" />
						    </c:otherwise>  
						</c:choose>  
                </li>
                <li class="list-right">
                    <div class="rows-one">
                        <span class="bookName">${user.nick_name}</span>
                    </div>
                    <div class="rows-two">
                        <span class="bookInfo">阅读书籍：${user.readCount}本</span>
                        <div class="fr">
                            <a href="${ctx}/user/readlist?id=${user.id}" class="linkMore">详情&gt;&gt;</a>
                        </div>
                    </div>
                </li>
            </ul>
           
		    </c:forEach>
          
        </div>
    </div>


    
    
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>