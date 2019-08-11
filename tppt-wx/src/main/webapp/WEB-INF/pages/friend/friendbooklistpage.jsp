<%@include file="/common/sys_jsp_header.jsp" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
      
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
      <c:if test="${page.list.size()>0}">
         <c:forEach var="book" items="${page.list}">
            <ul class="bookHouse-list" page="${p}" >
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
						<li class="list-right" onclick="$(this).find('.linkMore').click()"> 
					</c:when>  
				   <c:otherwise>  
						<li class="list-right" onclick="ttp('${ctx}/book/borrowdetail?id=${book.id }')"> 
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
						    	<a href="javascript:void(0);" target="collapseBookHouse${book.id}" class="linkMore">更多&gt;&gt;</a>
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
                    <input type="button" class="footer-btn" onclick="ttp('${ctx}/book/borrowbookview?bid=${book.id }')" value="借阅申请">
                    </c:if>
                    <input type="button" class="footer-btn footerBtn-2" onclick="ttp('${ctx}/book/borrowdetail?id=${book.id }')"  value="详情">
                </div>
            </div>
		    </c:forEach>
            
     <script>
        
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
            return false;
        })

</script>
    </c:if>