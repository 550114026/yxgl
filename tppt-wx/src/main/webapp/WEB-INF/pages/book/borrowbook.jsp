<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>图书借阅</title>
    <%@include file="/common/sys_header.jsp" %>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js-busi/book/borrowbook.js"></script>
</head>
<body class="inside-page">
    <div class="bookHouse-page">
        <div class="bookHouse-inside-page-panel">
        
		<form method="post"  id="borrowform">
            <ul class="bookHouse-inside-detail">
                <li>
                    <div class="fl">
                        <span class="detailvalue">借阅图书</span>
                    </div>
                    <div class="detailvalue" style="margin-left:2rem;">
                       ${book.book_name}
                  </div>
                </li>
                <li>
                    <div class="fl">
                        <span class="detailvalue">借阅时间</span>
                    </div>
                    <div class="detailvalue" style="margin-left:2rem;">
                       <input type="date" class="bookHouse-input" onchange="$('#expected_return_time').attr('min',$(this).val())" name="borrowRecord.borrow_time" min='${today}'  id="borrow_time" />
                  </div>
                </li>
                <li>
                    <div class="fl">
                        <span class="detailvalue">计划归还时间</span>
                    </div>
                    <div class="detailvalue" style="margin-left:2rem;">
                        <input type="date" class="bookHouse-input" name="borrowRecord.expected_return_time" onchange="$('#borrow_time').attr('max',$(this).val())" id="expected_return_time" min='${today}' />
                  </div>
                </li>
            </ul>
            <input type="hidden" name="message.id" id="mid" value="${(empty mid)?'':mid}" />
            <input type="hidden" name="message.busi_id" id="bid" value="${(empty bid)?'':bid}" />
            
        </form>
        </div>
        <div class="bookHouse-footer-btnbox" style="margin-top:2rem;">
            <input type="button" class="footer-btn" onclick="borrowBook()" value="提交" />
        </div>
    </div>
    <script>
        $(function() {
        	
        })

    </script>
    
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>