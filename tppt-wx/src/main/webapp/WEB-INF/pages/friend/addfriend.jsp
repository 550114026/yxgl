<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>添加好友</title>
    <%@include file="/common/sys_header.jsp" %>
</head>
<body>
    <div class="bookHouse-page">
        <div class="bookHose-search-box">
            <input class="control-input" type="text" id="txtS" value="${s}" placeholder="输入公众号模糊搜索" />
            <c:choose>  
		    <c:when test="${empty s}">  
            <input class="control-btn" type="button" value="搜索" onclick="dosearch(this)" />
		    </c:when>  
		   <c:otherwise>  
            <input class="control-btn" type="button" value="取消" onclick="cancelSearch(this)" />
		    </c:otherwise>  
		</c:choose>  
        </div>
        <div class="bookHouse-book-list-panel paddingList">
            <ul class="bookHouse-list">
                <li class="list-left">
                    <img class="book-img" src="${ctx}/resources/images/2wm.png"/>
                </li>
                <li class="list-right" style="cursor: pointer;" onclick="ttp('${ctx}/friend/qrcode')">
                    <div class="rows-one">
                        <span class="bookName">生成二维码</span>
                    </div>
                    <div class="rows-two">
                        <span class="bookInfo">分享给好友或微信群</span>
                    </div>
                </li>
            </ul>
           
        </div>

    </div>
    <script >
    function dosearch(){
    	var s=$("#txtS").val();
    	var pageNum='${p}';
    	ttp('${ctx}/friend/userlist?p='+pageNum+"&s="+s);
    }
    
    function cancelSearch(){
    	ttp("${ctx}/friend/addfriend");
    }
    </script>
    
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>