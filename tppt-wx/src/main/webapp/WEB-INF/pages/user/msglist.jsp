<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>消息列表</title>
    <%@include file="/common/sys_header.jsp" %>
</head>
<body>
    <div class="bookHouse-page">
        <div class="bookHose-search-box">
            <input class="control-input" type="text" id="txtSearch" value="${s}" placeholder="输入关键字模糊搜索" />
            <input class="control-btn" type="button" id="btnSearch" value="搜索" />
        </div>
        <div class="bookHouse-book-list-panel paddingList">
             <input type="hidden" id="hdp" value="${p}"/>
          <div id="wrapper" style="width:100%; position:absolute; z-index: 1;top:55px;bottom: 0px;" >
	          <div id="scroller" style="width: 100%">
				<div id="pullDown">
				   <span class="pullDownIcon"></span><span class="pullDownLabel">向下滑动刷新...</span>
				  </div>
		         <c:forEach var="message" items="${page.list}">
		         
		         <ul class="bookHouse-list" onclick="ttp('${ctx}/user/msg?id=${message.id}&t=${t}')">
		                <li class="list-left">
		             		<c:if test="${message.read_status==0}"><span class="num"></span></c:if>
		                	<img class="book-img" src="${ctx}/upload/images/${message.headimgurl}" />
		                    <%-- <img class="book-img" src="${ctx}/resources/images/msg${message.busi_type}.png" /> --%>
		                </li>
		                <li class="list-right typetwo">
		                    <div class="rows-one">
		                     		
		                        <span class="bookName " <c:if test="${message.read_status==0}">style=" font-weight: bold;" </c:if>>${message.title}</span>
		                        <div class="fr">
		                            <%-- <span class="infoicon icon1" >${message.msgTypeName}</span> --%>
		                        </div>
		                    </div>
		                    <div class="rows-middle">${message.content}</div>
		                    <div class="rows-two">
		                        <span class="bookInfo">发送时间：<fmt:formatDate value="${message.send_time}" pattern="yyyy-MM-dd" />  </span>
		                        <div class="fr">
			                        <a href="${ctx}/user/msg?id=${message.id}&t=${t}" class="linkMore">${message.deal_status==1?"处理":"详情"}&gt;&gt;</a>
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

    </div> <script>
    $(function() {
    	$("#btnSearch").bind("click",function(){
    		var s=$.trim($("#txtSearch").val());
    		ttp(formmatSearchUrl(s));
    	});

    })

</script>
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>