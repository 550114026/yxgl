<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>借阅详情</title>
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
                        <span class="detailvalue">借阅次数</span>
                    </div>
                    <div class="fr">
                        <span class="detailvalue">${book.borrow_count} </span>
                    </div>
                </li>
                <li>
                    <div class="fl">
                        <span class="detailvalue">上架时间</span>
                    </div>
                    <div class="fr">
                        <span class="detailvalue"><fmt:formatDate value="${book.add_time}" pattern="yyyy-MM-dd" /></span>
                    </div>
                </li>
                <li>
                    <div class="fl">
                        <span class="detailvalue">是否可借阅</span>
                    </div>
                    <div class="fr">
                        <span class="detailvalue">${book.borrow_status==1?'否':'是'}</span>
                    </div>
                </li>
            </ul>
        </div>
        <div class="bookHouse-title">借阅历史</div>
        <div class="bookHouse-inside-page-panel" style="margin-top:0;">
            <ul class="bookHouse-inside-detail">
             <c:choose>
				    <c:when test="${empty page.list}">
                    	无记录
				    </c:when>
				    <c:otherwise>
				            <c:forEach var="br" items="${page.list}">
				            	<li>
				                    <div class="fl">
				                        <span class="detailvalue">借阅者：${br.nick_name} </span>
				                    </div>
				                    <div class="fr">
				                        <span class="borrowing wg ${br.borrow_status=='1'?'isnot':'isyes'}">${br.borrow_status=='1'?'未归还':'已归还'} </span>
				                    </div>
				                    <div class="clear"></div>
				                    <div class="fl">
				                        <span class="detailvalue co_1">借阅时间：<fmt:formatDate value="${br.borrow_time}" pattern="yyyy-MM-dd" /></span>
				                    </div>
				                    <div class="fr">
				                        <span class="detailvalue co_1">计划归还时间：<fmt:formatDate value="${br.expected_return_time}" pattern="yyyy-MM-dd" /></span>
				                    </div>
				                    <div class="clear"></div>
				                    <div class="fl">
				                        <span class="detailvalue co_1">归还时间：<fmt:formatDate value="${br.real_return_time}" pattern="yyyy-MM-dd" /></span>
				                    </div>
				                    <div class="clear"></div>
				                </li>
				            </c:forEach>
				    </c:otherwise>
				</c:choose>
                
            </ul>
        </div>


    </div>

</body>
</html>