<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>消息详情</title>
    <%@include file="/common/sys_header.jsp" %>
	<script type="text/javascript" src="${ctx}/js-busi/user/bookMsgDetail.js"></script>
</head>
<body class="inside-page">
    <div class="bookHouse-page">
        <div class="bookHouse-inside-page-panel">
            <ul class="bookHouse-inside-detail">
                <li>
                    <div class="fl">
                        <span class="detailvalue">图书名称</span>
                    </div>
                    <div class="fr">
                        <span class="detailvalue">${book.book_name}</span>
                    </div>
                </li>
                <li>
                    <div class="fl">
                        <span class="detailvalue">阅读次数</span>
                    </div>
                    <div class="fr">
                        <span class="detailvalue">${book.read_count}</span>
                    </div>
                </li>
                <li>
                    <div class="fl">
                        <span class="detailvalue">上架时间</span>
                    </div>
                    <div class="fr">
                        <span class="detailvalue"><fmt:formatDate  value="${book.add_time}"  type="both" pattern="yyyy-MM-dd" /></span>
                    </div>
                </li>
                <li>
                    <div class="fl">
                        <span class="detailvalue">是否可借阅</span>
                    </div>
                    <div class="fr">
                        <span class="detailvalue">
                           <c:choose>
							    <c:when test="${book.borrow_status==0}">
			                   		 是
							    </c:when>
							    <c:otherwise>
			                   		否
							    </c:otherwise>
							</c:choose>
                       	</span>
                    </div>
                </li>
                <li>
                    <div class="fl">
                        <span class="detailvalue">书主</span>
                    </div>
                    <div class="fr">
                        <span class="detailvalue">${book.owner}</span>
                    </div>
                </li>
            </ul>
        </div>
        <div class="bookHouse-footer-btnbox">
       	 
            <div class="footerbtn-left">  <c:choose>
			    <c:when test="${userRel.idFriend==1}">
                   <input id="btnBorrowBook" type="button" class="footer-btn" value="申请借阅" />
			    </c:when>
			    <c:otherwise>
                   <input id="btnAddFriend" type="button" class="footer-btn" value="申请加为好友" />
			    </c:otherwise>
				</c:choose>
                
            </div>
            <div class="footerbtn-right">
                <input id="btnBookDetail" type="button" class="footer-btn btnType2" value="图书详情" />
            </div>
            
        </div>

    </div>
    <div class="alertModal">
        <div class="alertModal-dialog">
            <div class="alertModal-content">
                您还未添加该书的主人为好友，请确认 是否发起好友申请？
            </div>
            <div class="alertModal-footer">
                <span class="alertModal-btn btnCancel">取消</span>
                <span class="alertModal-btn btnOK">确定</span>
            </div>
        </div>
    </div>
      <script>
        $(function() {
            $("#btnBookDetail").bind("click",function(){
               ttp("${ctx}/book/bookdetail?id=${book.id}");
            });
            $("#btnAddFriend").bind("click",function(){
            	sendAddFriendMsg("${book.user}");
             });
            $("#btnBorrowBook").bind("click",function(){
            	borrowBook("${book.id}");
            });
        })

    </script>
    
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>