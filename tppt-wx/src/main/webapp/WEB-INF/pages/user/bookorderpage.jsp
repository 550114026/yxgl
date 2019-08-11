<%@include file="/common/sys_jsp_header.jsp" %>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
      <c:if test="${page.list.size()>0}">
           <c:forEach var="book" items="${page.list}" varStatus="status">
              <ul class="bookHouse-list"  page="${p}" >
                  <li class="list-left"> 
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
                <c:when test="${book.borrow_status==0}">  
                  <input type="button" class="footer-btn" onclick="ttp('${ctx}/book/borrowbookview?bid=${book.id }')" value="借阅申请">
                        </c:when>  
               <c:otherwise>  
                            <input type="button" class="footer-btn gray"  value="借阅申请">
                        </c:otherwise>  
            </c:choose>  
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
        })
 </script> </c:if>