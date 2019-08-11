<%@include file="/common/sys_jsp_header.jsp" %>

         
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
         <c:forEach var="message" items="${page.list}">
         <ul class="bookHouse-list" onclick="ttp('${ctx}/user/msg?id=${message.id}')"  page="${p}" >
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
	                        <a href="${ctx}/user/msg?id=${message.id}" class="linkMore">${message.deal_status==1?"处理":"详情"}&gt;&gt;</a>
	                    </div>
                    </div>
                </li>
            </ul>
		    </c:forEach>