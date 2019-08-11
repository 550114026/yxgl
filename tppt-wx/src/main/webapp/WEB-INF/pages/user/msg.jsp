<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>${msg.dict_name}</title>
    <%@include file="/common/sys_header.jsp" %>
	<script type="text/javascript" src="${ctx}/js-busi/user/msg.js"></script>
</head>
<body>
    <div class="bookHouse-page">
        <div class="message-to-remind-header">
            <div class="message-to-remind">
                <div class="title1">消息标题：${msg.title}</div>
                <div class="title2">发送人：${msg.nick_name}</div>
            </div>
        </div>
        <div class="message-to-remind-body">
            <div class="title1">消息内容：${msg.content}</div>
             <div class="title2" style="margin-top:0.5rem;">消息类型：${msg.dict_name}</div> 
            <div class="title2" style="margin-top:0.5rem;">发送时间：<fmt:formatDate value="${msg.send_time}" pattern="yyyy-MM-dd" /></div>
            
             <c:if test="${msg.deal_status==2}">
	            <div class="title2" style="margin-top:0.5rem;">处理时间：<fmt:formatDate value="${msg.deal_time}" pattern="yyyy-MM-dd" /></div>
	          
			</c:if>
        </div>
        <div class="bookHouse-footer-btnbox">
	              <c:choose>
				    <c:when test="${msg.busi_type==11 and msg.deal_status==1}">
	                   <input id="footerBtn" type="button" class="footer-btn" onclick="dealMsg(${msg.id},${t})" value="通过验证" />
				    </c:when>
				    <c:when test="${msg.busi_type==21 and msg.deal_status==1}">
	                   <input id="footerBtn" type="button" class="footer-btn" onclick="dealMsg(${msg.id},${t})" value="同意借阅" />
				    </c:when>
				    <c:when test="${msg.busi_type==22 and msg.deal_status==1}">
	                   <input id="footerBtn" type="button" class="footer-btn" onclick="borrowBook(${msg.id},${t})" value="借阅申请" />
				    </c:when>
				    <c:otherwise>
	                   <input id="footerBtn" type="button" class="footer-btn" onclick="history.go(-1)" value="返回" />
				    </c:otherwise>
				</c:choose>
        </div>
        
        

    </div>

    
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>