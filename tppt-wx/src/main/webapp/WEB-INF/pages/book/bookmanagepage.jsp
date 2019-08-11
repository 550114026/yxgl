<%@include file="/common/sys_jsp_header.jsp" %>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
        
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
 <c:if test="${page.list.size()>0}">
			<c:forEach var="book" items="${page.list}">
            <ul class="bookHouse-list" page="${p}" id="items_${book.id}" onclick="$(this).find('.linkMore').click();">
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
                            <span class="zanicon"></span>
                            <span class="zannum">${book.likes}</span>
                            <span class="ifreadBook ${book.read_status==0?'isyes':'isnot'} ">${book.read_status==0?'未读':'已读'}</span>
                        </div>
                    </div>
                    <div class="rows-two">
                        <span class="bookInfo">作者：${book.author}</span>
                        <span id="spborrow_status_${book.id}" class="borrowing ${book.borrow_status=='0'?'isyes':'isnot'}">${book.borrow_status=='0'?'闲置':'借出'}</span>
                         <div class="fr">
                            <a href="javascript:void(0);" target="collapseBookHouse${book.id}" class="linkMore">更多&gt;&gt;</a>
                        </div>
                    </div>
                </li>
            </ul>
          	 <div id="collapseBookHouse${book.id}" class="bookHose-collapse">
                <div class="bookHose-detail-btn-box">
                    <span><a href="${ctx}/book/updatebook?id=${book.id}">修改</a></span>
                    <span><a href="javascript:deletebook('${book.id}')">删除</a></span>
                    <span><a href="${ctx}/book/borrowdetail?id=${book.id}">详情</a></span>
                </div>
                <form id="setform${book.id}">
                <input type="hidden" name="book.id" id="book_id" value="${book.id}">
                <ul class="bookHouse-inside-detail">
                   <%--  <li>
                        <div class="control-input-group">
                            <div class="name">是否允许借阅</div>
                            <div class="fr">
                                <span class="input-checkbox  ${book.can_borrow==1?"ifTrue":"ifFalse"}""></span>
                            <input type="hidden" name="book.can_borrow" id="can_borrow" value="${book.can_borrow}">
                            </div>
                        </div>
                    </li> --%>
                    <li>
                        <div class="control-input-group">
                            <div class="name">是否已读</div>
                            <div class="fr">
                                <span class="input-checkbox ${book.read_status==1?"ifTrue":"ifFalse"}" ></span>
                            <input type="hidden" name="book.read_status" id="read_status" value="${book.read_status}">
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="control-input-group dropdown-group">
                            <div class="name">借阅状态</div>
                            <div class="arrowbox">
                                <span class="h_arrow"></span>
                            </div>
                            <input type="text" class="bookHouse-input" readonly="readonly" data-toggle="dropdown" />
                       		<input type="hidden" name="book.borrow_status" id="borrow_status" >
                            <ul class="dropdown-menu" role="menu" id="borrowStatus_${book.id}">
                                <c:forEach var="bd" items="${borrowStatus}">
										<li role="presentation">
			                                <a role="menuitem" tabindex="-1" data-value="${bd.dict_code}" data-text="${bd.dict_name}" onclick="javascript:selected(this)">${bd.dict_name}</a>
			                            </li>
						 		</c:forEach>
                            </ul>
                        </div>
                    </li>
                </ul>                
                  <script>
                  $(function() {
                  		  setSelected("borrowStatus_${book.id}",${book.borrow_status});
                  });
                  </script>                
                </form>
                <div class="bookHose-detail-footer-btn-box">
                    <input type="button" class="footer-btn footerBtn-2" onclick="$('a[target=collapseBookHouse${book.id}]').click();" value="取消">
                    <input type="button" class="footer-btn"  onclick="listSave(this)" value="确定">
                </div>
            </div>
		    </c:forEach>

    <script>
    	
        $(".bookHouse-tabs>li").unbind("click").bind("click", function() {
            $(this).addClass("curr");
            $(this).siblings("li.curr").removeClass("curr");
        })
        
        $(".input-checkbox").unbind("click").bind("click",function(){
                if($(this).hasClass("ifTrue")){
                    $(this).addClass("ifFalse");
                    $(this).removeClass("ifTrue");
                    $(this).next().val("0");
                }else{
                    $(this).removeClass("ifFalse");
                    $(this).addClass("ifTrue");
                    $(this).next().val("1");
                }
                return false;
            })
        
        $(".linkMore").unbind("click").bind("click",function(){

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