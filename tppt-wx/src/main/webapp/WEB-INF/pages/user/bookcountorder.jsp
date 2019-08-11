<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>藏书排名</title>
    <%@include file="/common/sys_header.jsp" %>
</head>
<body>

 <div class="bookHouse-page">
        <div class="bookHouse-book-list-panel marginList">
           <input type="hidden" id="hdp" value="${p}"/>
        	 <div id="wrapper" style="width:100%; position:absolute; z-index: 1;top: 0px;bottom: 0px;" >
	          <div id="scroller" style="width: 100%">
				<div id="pullDown">
				   <span class="pullDownIcon"></span><span class="pullDownLabel">向下滑动刷新...</span>
				  </div>
        
				           <c:forEach var="user" items="${page.list}" varStatus="status">
				           <ul class="bookHouse-list" page="${p}" onclick="ttp('${ctx}/friend/friendbook?id=${user.id}')">
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
				                        <span class="bookInfo">收藏书籍：${user.bookCount}本</span>
				                        <div class="fr">
				                            <a href="${ctx}/friend/friendbook?id=${user.id}" class="linkMore">详情&gt;&gt;</a>
				                        </div>
				                    </div>
				                </li>
				            </ul>
				           
						    </c:forEach>
           
         			 <div id="pullUp">
				   <span class="pullUpIcon"></span><span class="pullUpLabel">向上滑动加载更多...</span>
				  </div>
	         </div>
	     </div>
        		
        </div>
    </div>


    
    
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>