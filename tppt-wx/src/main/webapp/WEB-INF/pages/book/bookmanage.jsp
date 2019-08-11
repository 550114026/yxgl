<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>图书管理</title>
    <%@include file="/common/sys_header.jsp" %>
	<script type="text/javascript" src="${ctx}/js-busi/book/bookmanage.js"></script>
</head>
<body>
	 <div class="bookHouse-page">
        <div class="bookHose-search-box1">
            <input class="control-input"  type="text" id="txtSearch" value="${s}" placeholder="输入图书名称模糊搜索" />
            <input class="control-btn" type="button" id="btnSearch" value="搜索" />
            <span class="addFriend-btn" onclick="ttp('${ctx}/book/updatebook')" ></span>
        </div>
        <div class="bookHouse-tabs-box">
            <ul class="bookHouse-tabs">
                <li <c:if test="${t=='1'}">class="curr"</c:if>  onclick="ttp('${ctx}/book/mybook?t=1&s=')">
                    <a href="${ctx}/book/mybook?t=1&s=">
                    <i class="icon icon1"></i>
                    <div class="name">少儿</div>
                    </a>
                </li>
                <li <c:if test="${t=='2'}">class="curr"</c:if> onclick="ttp('${ctx}/book/mybook?t=2&s=')">
                    <a href="${ctx}/book/mybook?t=2&s=">
                    <i class="icon icon2"></i>
                    <div class="name">文学</div>
                    </a>
                </li>
                <li <c:if test="${t=='3'}">class="curr"</c:if> onclick="ttp('${ctx}/book/mybook?t=3&s=')">
                    <a href="${ctx}/book/mybook?t=3&s=">
                    <i class="icon icon3"></i>
                    <div class="name">艺术</div>
                    </a>
                </li>
                <li <c:if test="${t=='s'}">class="curr"</c:if> onclick="ttp('${ctx}/book/mybook?t=s&s=')">
                    <a href="${ctx}/book/mybook?t=s&s=">
                    <i class="icon icon4"></i>
                    <div class="name">其他</div>
                    </a>
                </li>
            </ul>
        </div>
        <div class="bookHouse-book-list-panel paddingList">
        <input type="hidden" id="hdp" value="${p}"/>
          <div id="wrapper" style="width:100%; position:absolute;z-index: 1;top: 100px;bottom: 10px;" >
	          <div id="scroller" style="width: 100%">
				<div id="pullDown">
				   <span class="pullDownIcon"></span><span class="pullDownLabel">向下滑动刷新...</span>
				</div>
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
		                    <a type="button" class="footer-btn-a footerBtn-a-2" onclick="$('a[target=collapseBookHouse${book.id}]').click();" value="取消">取消</a>
		                    <a type="button" class="footer-btn-a"  onclick="listSave(this)" value="确定">确定</a>
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
    	$("#btnSearch").bind("click",function(){
    		var s=$.trim($("#txtSearch").val());
    		ttp(formmatSearchUrl(s));
    	});
    	
        $(".bookHouse-tabs>li").bind("click", function() {
            $(this).addClass("curr");
            $(this).siblings("li.curr").removeClass("curr");
        })
        
        $(".input-checkbox").bind("click",function(){
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
        
        $(".linkMore").bind("click",function(){

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

</script>

  
<%@include file="/common/sys_footer.jsp" %>
</body>
</html>