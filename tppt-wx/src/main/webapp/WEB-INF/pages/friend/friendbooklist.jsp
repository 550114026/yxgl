<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>好友藏书</title>
    <%@include file="/common/sys_header.jsp" %>
</head>
<body>
    <div class="bookHouse-page">
        <div class="bookHouse-book-list-panel paddingList">
         <input type="hidden" id="hdp" value="${p}"/>
          <div id="wrapper" style="width:100%; position:absolute;z-index: 1;top: 0px;bottom: 0px;" >
	          <div id="scroller" style="width: 100%">
				<div id="pullDown">
				   <span class="pullDownIcon"></span><span class="pullDownLabel">向下滑动刷新...</span>
				  </div>
				         <c:forEach var="book" items="${page.list}">
				            <ul class="bookHouse-list"  page="${p}">
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
				                 <c:choose>  
								    <c:when test="${book.borrow_status==0}">  
										<li class="list-right" > 
									</c:when>  
								   <c:otherwise>  
										<li class="list-right" > 
									</c:otherwise>  
								</c:choose>  
				              
				                    <div class="rows-one">
				                        <span class="bookName">${book.book_name}</span>
				                        <div class="fr">
				                            <span class="zanicon" data-status="${book.likeStatus}" onclick="likesBook('${book.id}',this)"></span>
				                            <span class="zannum">${book.likes}</span>
					                        <c:choose>  
											    <c:when test="${book.readed>0}">  
				                             <span class="ifreadBook isyes">已读</span> 
											    </c:when>  
											   <c:otherwise>
				                             <span class="ifreadBook isnot">未读</span> 
					                        	</c:otherwise>  
											</c:choose>  
				                        </div>
				                    </div>
				                    <div class="rows-two">
				                        <span class="bookInfo">作者：${book.author}</span>
				                        <span class="borrowing ${book.borrow_status==0?'isyes':'isnot'}">${book.borrow_status==0?'闲置':'借出'}</span>
				                        <div class="fr">
				                        <c:choose>  
										    <c:when test="${book.borrow_status==0}">  
										    	<a href="javascript:void(0);" onclick="$(this).find('.linkMore').click()" target="collapseBookHouse${book.id}" class="linkMore">更多&gt;&gt;</a>
				                      		</c:when>  
										   <c:otherwise>  
				                        	 	<a href="javascript:void(0);" onclick="ttp('${ctx}/book/borrowdetail?id=${book.id }'); return false;"  class="linkMore">详情&gt;&gt;</a>
				                      		</c:otherwise>  
										</c:choose>  
				                              </div>
				                    </div>
				                </li>
				            </ul>
				            <div id="collapseBookHouse${book.id}" class="bookHose-collapse">
				                <div class="bookHose-detail-footer-btn-box" style="border-top:0;">
				                    <c:if test="${userId!=book.user}">
				                      	<a  type="button" class="footer-btn-a" onclick="ttp('${ctx}/book/borrowbookview?bid=${book.id }')" value="借阅申请">借阅申请</a>
				                 
				                    </c:if>
				                    <a type="button" class="footer-btn-a footerBtn-a-2" onclick="ttp('${ctx}/book/borrowdetail?id=${book.id }')"  value="详情">详情</a>
				               
								</div>
				            </div>
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
      
        
       /*  $(".input-checkbox").bind("click",function(){
                if($(this).hasClass("ifTrue")){
                    $(this).addClass("ifFalse");
                    $(this).removeClass("ifTrue");
                    $(this).next().val("0");
                }else{
                    $(this).removeClass("ifFalse");
                    $(this).addClass("ifTrue");
                    $(this).next().val("1");
                }
            }) */
        
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
            return false;
        })

    })
    
    
    
function likesBook(id,span){
	if($(span).data("status")&&$(span).data("status")!="0"){
		opt={msg:"已经点赞过此图书，不可重复点赞"};
		sysAlert(opt);
		return ;
	}
	$.ajax({
		url : CONTEXT_PATH + "/friend/likesbook",
		type : "post",
		dataType : "json",
		data : {"book.id":id}
	}).done(function(resultVO) {
		var opt={msg:"点赞成功"};
		if(resultVO.success){
			$(span).next().text(parseInt($(span).next().text())+1);
			$(span).data("status","1");
			opt={msg:"点赞成功",callback:function(){
				//ttp(CONTEXT_PATH + "/user/myborrow");
			}};
		}
		else{
			opt={msg:"处理失败,错误消息："+resultVO.resultMessage};
		}
		sysAlert(opt);
	})
}

</script>
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>