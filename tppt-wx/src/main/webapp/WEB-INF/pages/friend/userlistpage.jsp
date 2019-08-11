<%@include file="/common/sys_jsp_header.jsp" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
        
	         <c:forEach var="user" items="${page.list}">
	            <ul class="bookHouse-list" page="${p}">
	                <li class="list-left">
	                <c:choose>  
						    <c:when test="${fn:substring(user.headimgurl,0,4)=='http'}">  
                        		<img class="head-img" src="${user.headimgurl}" />
						    </c:when>  
						   <c:otherwise>  
                        		<img class="head-img" src="${ctx}/upload/images/${user.headimgurl}" />
						    </c:otherwise>  
						</c:choose>  
	                </li>
	                <li class="list-right">
	                    <div class="search-friend-list">
	                        <span class="friend-name">${user.nick_name}</span>
	                        <div class="fr">
	                        <c:choose>  
							    <c:when test="${ytj==1}">  
	                            <span class="friend-add isfalse">已添加</span
							    </c:when>  
							   <c:otherwise>  
		                            <span class="friend-add" onclick="sendAddFriendMsg('${user.id}')">添加</span>
							    </c:otherwise>  
							</c:choose>  
	                        </div>
	                    </div>
	                </li>
	            </ul>
		    </c:forEach>
		   <c:if test="${page.list.size()==0}">  
		             	<div class="noData">无数据</div>
				    </c:if>  

        </div>

    </div>
    <script>  