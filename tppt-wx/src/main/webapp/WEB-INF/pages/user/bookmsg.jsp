<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>图书消息</title>
    <%@include file="/common/sys_header.jsp" %>
</head>
<body>
    <div class="bookHouse-page">
        <div class="bookHouse-book-list-panel marginList">
         <c:if test="${page.list.size()==0}">
          <ul >
                <li style="text-align: center;">
                  		 无图书消息
                </li>
            </ul>
         </c:if >
         <c:forEach var="message" items="${page.list}">
          <ul class="bookHouse-list" onclick="ttp('${ctx}/user/bookmsgdetail?id=${message.bid}&mid=${message.id}')">
                <li class="list-left">
                   <c:choose>
					    <c:when test="${empty message.cover}">
	                    <img class="book-img" src="${ctx}/resources/images/book.png" />
					    </c:when>
					    <c:otherwise>
	                    <img class="book-img" src="${ctx}/upload/images/${message.cover}" />
					    </c:otherwise>
					</c:choose>
                </li>
                <li class="list-right">
                    <div class="rows-one">
                        <span class="bookName">${message.title}</span>
                    </div>
                    <div class="rows-two">
                        <span class="bookInfo"><fmt:formatDate  value="${message.send_time}"  type="both" pattern="yyyy-MM-dd" />新书上架，请点击查看详情  </span>
                        <span class="bookInfo fr"><fmt:formatDate value="${message.send_time}" type="both" pattern="yyyy-MM-dd HH:mm" /></span>
                    </div>
                </li>
            </ul>
		    </c:forEach>
	
        </div>

    </div>
    
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>