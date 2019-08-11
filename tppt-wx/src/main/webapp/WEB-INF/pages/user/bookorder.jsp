<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>热门图书</title>
    <%@include file="/common/sys_header.jsp" %>
</head>
<body>
    <div class="bookHouse-page">
        <div class="bookHouse-book-list-panel paddingList">
        <input type="hidden" id="hdp" value="${p}"/>
          <div id="wrapper" style="width:100%; position:absolute; z-index: 1;top: 0px;bottom: 0px;" >
	          <div id="scroller" style="width: 100%">
				<div id="pullDown">
				   <span class="pullDownIcon"></span><span class="pullDownLabel">向下滑动刷新...</span>
				  </div>
	         <c:forEach var="book" items="${page.list}" varStatus="status">
	            <ul class="bookHouse-list"  page="${p}">
	                <li class="list-left"> 
	                <c:if test="${status.index<3}">
	                    <span class="num">${status.index+1}</span>
					</c:if>
	                <c:choose>
					    <c:when test="${empty book.cover}">
	                    <img class="head-img" src="${ctx}/resources/images/book.png" />
					    </c:when>
					    <c:otherwise>
	                    <img class="head-img" src="${ctx}/upload/images/${book.cover}" />
					    </c:otherwise>
					</c:choose>
	                </li>
	                <li class="list-right">
	                    <div class="rows-one">
	                        <span class="bookName">${book.book_name}&nbsp;</span>
	                        <div class="fr">
	                            <span class="zannum" style="width:80px;">热度：${book.hot}</span>
	                            <!-- <span class="ifreadBook isnot">未读</span> -->
	                        </div>
	                    </div>
	                    <div class="rows-two">
	                        <span class="bookInfo">作者：${book.author}</span>
	                        &nbsp;<span class="bookInfo">书友：${book.nick_name}</span>
	                        <div class="fr">
	                       		<a href="javascript:void(0);" target="collapseBookHouse${book.id}" class="linkMore">更多&gt;&gt;</a>
	                      	</div>
	                    </div>
	                </li>
	            </ul>
	            <div id="collapseBookHouse${book.id}" class="bookHose-collapse">
	                <div class="bookHose-detail-footer-btn-box" style="border-top:0;">
	                	 <c:choose>  
						    <c:when test="${book.borrow_status==0&&userId!=book.user}">  
						    	<a type="button" class="footer-btn-a" onclick="ttp('${ctx}/book/borrowbookview?bid=${book.id }')" value="借阅申请">借阅申请</a>
	                   		</c:when>  
						   <c:otherwise>  
	                       	 	<a type="button" class="footer-btn-a gray"  value="借阅申请">借阅申请</a>
	                   		</c:otherwise>  
						</c:choose>  
	                    <a type="button" class="footer-btn-a footerBtn-a-2" onclick="ttp('${ctx}/book/borrowdetail?id=${book.id }')"  value="详情">详情</a>
	                </div>
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

    })

    
    
    
</script>
    <%@include file="/common/sys_footer.jsp" %>
    
</body>
</html>