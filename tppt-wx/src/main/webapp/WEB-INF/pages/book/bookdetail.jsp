<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>图书详情</title>
    <%@include file="/common/sys_header.jsp" %>
</head>
<body>

<body class="inside-page">
    <div class="bookHouse-page">
        <div class="bookHouse-inside-page-panel">
            <ul class="bookHouse-inside-detail">
                <li>
                    <div class="book-detail-img fl">
                     	<c:choose>
						    <c:when test="${empty book.cover}">
		                    <img class="book-img" src="${ctx}/resources/images/book.png" />
						    </c:when>
						    <c:otherwise>
		                    <img class="book-img" src="${ctx}/upload/images/${book.cover}" />
						    </c:otherwise>
						</c:choose>
                    </div>
                    <div class="fl">
                        <div class="bookinfo">图书名称：${book.book_name}</div>
                        <div class="bookinfo">作者：${book.author}</div>
                    </div>
                </li>
                <li>
                    <div class="fl">
                        <span class="detailvalue">图书类别</span>
                    </div>
                    <div class="fr">
                        <span class="detailvalue">少儿</span>
                    </div>
                </li>
                <li>
                    <div class="fl">
                        <span class="detailvalue">是否允许借阅</span>
                    </div>
                    <div class="fr">
                        <span class="detailvalue">${book.borrow_status==0?"是":"否"}</span>
                    </div>
                </li>
                <li>
                    <div class="fl">
                        <span class="detailvalue">借阅状态</span>
                    </div>
                    <div class="fr">
                        <span class="detailvalue">${book.borrow_status_name}
                         <%-- <c:choose>
						    <c:when test="${book.borrow_status==0}">闲置</c:when>
						    <c:when test="${book.borrow_status==1}">借出</c:when>
						    <c:when test="${book.borrow_status==2}">下架</c:when>
						</c:choose> --%>
                    </div>
                </li>
                <li>
                    <div class="fl">
                        <span class="detailvalue">阅读量</span>
                        <span class="detailvalue">${book.read_count}</span>
                        <span class="zanicon isred"></span>
                        <span class="zannum">${book.likes}</span>
                    </div>
                </li>
            </ul>
        </div>


</div>

<%@include file="/common/sys_footer.jsp" %>
</body>
</html>