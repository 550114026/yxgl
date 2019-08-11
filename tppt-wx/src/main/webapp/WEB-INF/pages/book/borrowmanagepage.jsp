<%@include file="/common/sys_jsp_header.jsp" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
        
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
			<c:forEach var="book" items="${page.list}">
			
			<ul class="bookHouse-list" page="${p}">
                <li class="list-left">
                <c:choose>
				    <c:when test="${empty book.cover}">
                    <img class="book-img" src="${ctx}/resources/images/book.png" />
				    </c:when>
				    <c:otherwise>
                    <img class="book-img" src="${ctx}/upload/images/${book.cover}" />
				    </c:otherwise>
				</c:choose>
                </li>
                <li class="list-right">
                    <div class="rows-one">
                        <span class="bookName">${book.book_name}</span>
                         <div class="fr">
                               <span id="fr_${book.id}" class="borrowing ${book.breturn_status==0?'isred':'isyes'} ">${book.breturn_status==0?'未还':'已还'}</span>
			             </div>
                    </div>
                    <div class="rows-two">
                        <span class="bookInfo">借阅者：${book.nick_name==null?'无&nbsp;&nbsp;&nbsp;&nbsp;':book.nick_name}
                        <span class="bookInfo">借阅时间：<fmt:formatDate value="${book.borrow_time}" pattern="yyyy-MM-dd" /></span>
                        <div class="fr">
                        <c:choose>
   							 <c:when test="${empty book.bid or book.breturn_status==1}">
                        	  <a href="${ctx}/book/borrowdetail?id=${book.id}"  id="dt_${book.id}" class="linkMore">详情&gt;&gt;</a>
                            </c:when>
   							 <c:when test="${ book.breturn_status==0}">
                        	   	<a href="${ctx}/book/borrowdetail?id=${book.id}" style="display:none;"  id="dt_${book.id}" class="linkMore">详情&gt;&gt;</a>
                            	<a href="javascript:void(0)" target="collapseBookHouse_${book.id}" id="mo_${book.id}"  class="linkMore">更多&gt;&gt;</a>
                              </c:when>
   							
						</c:choose>
                        </div>
                    </div>
                </li>
            </ul>           
            <c:if test="${not empty book.bid}">
            <div id="collapseBookHouse_${book.id}" class="bookHose-collapse">
                <ul class="bookHouse-inside-detail">
                    <li>
                    <div class="fl">
                        <span class="detailvalue">预计归还时间</span>
                    </div>
                    <div class="detailvalue" style="margin-left:2rem;">
                       <%-- <input type="date" class="bookHouse-input" name="book.book_name" id="book_name" value="${book.book_name}"/> --%>
                       <div class="bookHouse-Wdate" ><fmt:formatDate value="${book.expected_return_time}" pattern="yyyy-MM-dd" /></div> 
                    </div>
                </li>

                    <li>
                        <div class="control-input-group">
                            <div class="name">归还</div>
                            <div class="fr">
                                <span class="input-checkbox ifFalse" onclick="bookReturnCheck('${book.bid}','${book.id}')"></span>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="control-input-group">
                            <div class="name">归还催办</div>
                            <div class="fr">
                             <c:choose>
   							 <c:when test="${book.ycb==1}">
                                <span class="input-checkbox ifTrue" enable="false" ></span>
                            </c:when>
   							 <c:otherwise>
                                <span class="input-checkbox ifFalse" onclick="callbook('${book.bid}',this)"></span>   
                            </c:otherwise>
   							
							</c:choose>
                            </div>
                        </div>
                    </li>                    
                </ul>
                <div class="bookHose-detail-footer-btn-box">
                    <input type="button" class="footer-btn" onclick="ttp('${ctx}/book/borrowdetail?id=${book.id}')" value="详情">
                </div>
            </div>
            </c:if>
		    </c:forEach>

        </div>

    </div>
  
    <script>
            $(".bookHouse-tabs>li").unbind("click").bind("click",function(){
                $(this).addClass("curr");
                $(this).siblings("li.curr").removeClass("curr");
            })
            
            
            $(".input-checkbox").unbind("click").bind("click",function(){
            	if($(this).attr("enable")=="false")
            		return;
                if($(this).hasClass("ifTrue")){
                    $(this).addClass("ifFalse");
                    $(this).removeClass("ifTrue");
                    $(this).next().val("0");
                }else{
                    $(this).removeClass("ifFalse");
                    $(this).addClass("ifTrue");
                    $(this).next().val("1");
                }
            })
            
            $(".linkMore").unbind("click").bind("click",function(){

            	if(!$(this).attr("target"))
            		return ;
            	var id="#"+$(this).attr("target");
                if($(this).hasClass("ifOpen")){
                    $(id).slideUp(200);
                    $(id).removeClass("ifOpen");
                    $(this).removeClass("ifOpen");
                    $(this).html("更多&gt;&gt;");
                }else{
                    $(id).slideDown(200);
                    $(id).siblings(".bookHose-collapse.ifOpen").slideUp(200);
                    $(id).siblings(".bookHose-collapse.ifOpen").removeClass("ifOpen");
                    $(id).addClass("ifOpen");
                    $(".bookHouse-list .linkMore.ifOpen").html("更多&gt;&gt;");
                    $(".bookHouse-list .linkMore.ifOpen").removeClass("ifOpen");
                    $(this).siblings(".ifOpen").removeClass("ifOpen");
                    $(this).addClass("ifOpen");
                    $(this).html("更多&dArr;");
                }
            })


    </script>