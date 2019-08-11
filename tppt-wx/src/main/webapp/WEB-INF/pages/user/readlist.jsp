<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>阅读列表</title>
    <%@include file="/common/sys_header.jsp" %>
</head>
<body>

 <div class="bookHouse-page">
        <div class="bookHouse-book-list-panel marginList">
        
        
           <c:forEach var="bookRead" items="${page.list}" varStatus="status">
           <ul class="bookHouse-list"  onclick="ttp('${ctx}/book/bookdetail?id=${bookRead.id}')">
                <li class="list-left">
	               <c:choose>
					    <c:when test="${empty bookRead.cover}">
	                    <img class="book-img" src="${ctx}/resources/images/book.png" />
					    </c:when>
					    <c:otherwise>
	                    <img class="book-img" src="${ctx}/upload/images/${bookRead.cover}" />
					    </c:otherwise>
					</c:choose>
                </li>
                <li class="list-right">
                 <div class="rows-one">
                        <span class="bookName">${bookRead.book_name}</span>
                        <div class="fr">
                            <span class="bookClassify type1">${bookRead.dict_name}</span>
                        </div>
                    </div>
                    <div class="rows-two">
                        <span class="bookInfo">阅读时间：<fmt:formatDate value="${bookRead.start_time}" pattern="yyyy-MM-dd" /></span>
                        <div class="fr">
                            <a href="${ctx}/book/bookdetail?id=${bookRead.id}" class="linkMore">详情&gt;&gt;</a>
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