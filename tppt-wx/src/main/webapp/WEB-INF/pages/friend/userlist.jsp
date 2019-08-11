<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>书友列表</title>
    <%@include file="/common/sys_header.jsp" %>
	<script type="text/javascript" src="${ctx}/js-busi/user/msg.js"></script>
</head>
<body>
      <div class="bookHouse-page">
        <div class="bookHose-search-box">
            <input class="control-input" type="text" id="txtSearch" value="${s}" placeholder="输入公众号模糊搜索" />
            <input class="control-btn" type="button" id="btnSearch" value="搜索"  />
        </div>
        <div class="bookHouse-book-list-panel marginList">
           <input type="hidden" id="hdp" value="${p}"/>
          <div id="wrapper" style="width:100%; position:absolute; z-index: 1;top: 55px;bottom: 0px;" >
	          <div id="scroller" style="width: 100%">
				<div id="pullDown">
				   <span class="pullDownIcon"></span><span class="pullDownLabel">向下滑动刷新...</span>
				  </div>
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
		             		无数据
				    </c:if>  
				   <div id="pullUp">
				   <span class="pullUpIcon"></span><span class="pullUpLabel">向上滑动加载更多...</span>
				  </div>
	         </div>
	     </div>
        </div>

    </div>
    <script>  
    $(function() {
    	$("#btnSearch").bind("click",function(){
    		var s=$.trim($("#txtSearch").val());
    		ttp(formmatSearchUrl(s));
    	});
    });
     function sendMsg(userid){
    	 var url="${ctx}/friend/sendAddfriend";
    	 var data={"message.recive_user":userid};
    		$.ajax({
    			url : url,
    			cache : false,
    			type : "post",
    			data : data,
    			success : function(result) {
    				if(result.success)
    					sysAlert({msg:"消息发送成功"});
    				else
    					sysAlert({msg:"消息发送失败"});
    					
    			}
    		});
     }
    </script>
    
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>