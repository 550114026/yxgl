<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>借出管理</title>
    <%@include file="/common/sys_header.jsp" %>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js-busi/book/borrowmanage.js"></script>
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
          <div id="wrapper" style="width:100%; position:absolute;z-index: 1;top: 55px;bottom: 0px;" >
	          <div id="scroller" style="width: 100%">
				<div id="pullDown">
				   <span class="pullDownIcon"></span><span class="pullDownLabel">向下滑动刷新...</span>
				</div>
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
				 
				 
				    <c:if test="${page.list.size()==0}">  
		             	<div class="noData">无数据</div>
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

			<c:if test="${t!=''}">
				setSelected("typeSelect","${t}");
			</c:if>
  		  
  		  $("#typeSelect").find("a[role='menuitem']").bind("click",function(){typeChange();})
  		
        	
        	$("#btnSearch").bind("click",function(){
        		var s=$.trim($("#txtSearch").val());
        		ttp(formmatSearchUrl(s));
        	});
            $(".bookHouse-tabs>li").bind("click",function(){
                $(this).addClass("curr");
                $(this).siblings("li.curr").removeClass("curr");
            })
            
            
            $(".input-checkbox").bind("click",function(){
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