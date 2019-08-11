<%@include file="/common/sys_jsp_header.jsp" %>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
           <c:forEach var="user" items="${page.list}" varStatus="status">
           <ul class="bookHouse-list" page="${p}" onclick="ttp('${ctx}/friend/friendbook?id=${user.id}')" >
                <li class="list-left">
             <%--    <c:if test="${status.index<3}">
                    <span class="num">${status.index+1}</span>
				</c:if> --%>
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
                        <span class="bookInfo">收藏书籍：${user.bookCount}本</span>
                        <div class="fr">
                            <a href="${ctx}/friend/friendbook?id=${user.id}" class="linkMore">详情&gt;&gt;</a>
                        </div>
                    </div>
                </li>
            </ul>
           
		    </c:forEach>
           
         