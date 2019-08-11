<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>借入管理</title>
    <%@include file="/common/sys_header.jsp" %>
	<script type="text/javascript" src="${ctx}/js-busi/user/myborrow.js"></script>
</head>
<body>
    <div class="bookHouse-page">
        <div class="bookHose-search-box1">
           <div> 
           	<input class="control-input"  type="text" id="txtSearch" value="${s}" placeholder="输入图书名称模糊搜索" />
            <input class="control-btn" type="button" id="btnSearch" value="搜索" />
           </div>  
           <div class="right dropdown-group" id="typeSelect">
           	<input type="text" class="bookHouse-input type" value="所有" readonly="readonly" data-toggle="dropdown"><span class="h_arrow"></span>
           	<input type="hidden"  id="borrowRecordStatus" value="">
            <ul class="dropdown-menu" role="menu" id="bigType">
				<li role="presentation">
                      <a role="menuitem" tabindex="-1" data-value="" data-text="所有" onclick="javascript:selected(this);">所有</a>
                </li>
                <li role="presentation">
                      <a role="menuitem" tabindex="-1" data-value="1" data-text="已还" onclick="javascript:selected(this);">已还</a>
                </li>
                <li role="presentation">
                      <a role="menuitem" tabindex="-1" data-value="0" data-text="未还" onclick="javascript:selected(this);">未还</a>
                </li>
               </ul>
           </div>
        </div>
        <div class="bookHouse-book-list-panel paddingList">
        
             <input type="hidden" id="hdp" value="${p}"/>
          <div id="wrapper" style="width:100%; position:absolute; z-index: 1;top:55px;bottom: 0px;" >
	          <div id="scroller" style="width: 100%">
				<div id="pullDown">
				   <span class="pullDownIcon"></span><span class="pullDownLabel">向下滑动刷新...</span>
				  </div>
			         <c:forEach var="book" items="${page.list}">
			            <ul class="bookHouse-list" >
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
			                           <span id="fr_${book.id}" class="borrowing wg  ${book.return_status==0?'isred':'isyes'} ">${book.return_status==0?'未还':'已还'}</span>
			                        </div>
			                    </div>
			                    <div class="rows-two">
			                        <span class="bookInfo">书主：${book.book_name}</span>
			                        <span class="bookInfo">预计归还时间：<fmt:formatDate  value="${book.expected_return_time}"  type="both" pattern="yyyy-MM-dd" /></span>
			                        <div class="fr">
			                            <a href="javascript:void(0)" target="collapseBookHouse_${book.id}" class="linkMore">更多&gt;&gt;</a>
			                        </div>
			                    </div>
			                </li>
			            </ul>
			            
			            <div id="collapseBookHouse_${book.id}" class="bookHose-collapse">
			                <ul class="bookHouse-inside-detail">
			                    <li>
			                        <div class="fl">
			                            <span class="detailvalue">借阅时间</span>
			                        </div>
			                        <div class="detailvalue" style="margin-left:2rem;">
			                            <fmt:formatDate  value="${book.borrow_time}"  type="both" pattern="yyyy-MM-dd" />
			                        </div>
			                    </li>
							    <c:if test="${book.return_status==1}"> 
							    <li>
			                        <div class="fl">
			                            <span class="detailvalue">归还时间</span>
			                        </div>
			                        <div class="detailvalue" style="margin-left:2rem;">
			                            <fmt:formatDate  value="${book.real_return_time}"  type="both" pattern="yyyy-MM-dd" />
			                       </div>
			                    </li>
							    </c:if>
			
							    <c:if test="${book.return_status==0}"> 
			                    <li>
			                        <div class="control-input-group">
			                            <div class="name">归还</div>
			                            <div class="fr">
			                                <span data-id="${book.id}" class="input-checkbox ifFalse"></span>
			                            </div>
			                        </div>
			                    </li>
							    </c:if>
			                    <li onclick="likeBook('${book.id}',this)" data-like="${book.like_status}">
			                        <div class="control-input-group" >
			                            <div class="name">${book.like_status==1?"已":""}点赞</div>
			                            <div class="fr">
				                        	 <span class="zanicon"></span>
			                            </div>
			                        </div>
			                    </li>
			
			                </ul>
			            </div>
					    </c:forEach>
				<div id="pullUp">
				      <span class="pullUpIcon"></span><span class="pullUpLabel">向上滑动加载更多...</span>
				  	</div>
		         </div>
		     </div>
        </div>

    </div>
     <script>
        $(function() {
    		<c:if test="${t!=''}">
				setSelected("typeSelect","${t}");
			</c:if>
			
			 $("#typeSelect").find("a[role='menuitem']").bind("click",function(){typeChange();})
			 
        	$("#btnSearch").bind("click",function(){
        		var s=$.trim($("#txtSearch").val());
        		ttp(formmatSearchUrl(s));
        	});
        	
        	 $(".input-checkbox").bind("click",function(){
             	if($(this).attr("enable")=="false")
             		return;
             	returnBookCheck($(this).data("id"),this);
                 if($(this).hasClass("ifFalse")){
                     $(this).removeClass("ifFalse");
                     $(this).addClass("ifTrue");
                     $(this).next().val("1");
                 }
             })
             
             $(".linkMore").bind("click",function(){
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
        })

    </script>
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>