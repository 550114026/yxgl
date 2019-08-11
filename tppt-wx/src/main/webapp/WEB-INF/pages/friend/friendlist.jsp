<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>我的好友</title>
    <%@include file="/common/sys_header.jsp" %>
	<script type="text/javascript" src="${ctx}/js-busi/friend/friendlist.js"></script>
</head>
<body>
    <div class="bookHouse-page">
        <div class="bookHose-search-box1">
            <input class="control-input" type="text" id="txtSearch" value="${s}"  placeholder="输入好友名称模糊搜索"/>
            <input class="control-btn" type="button" id="btnSearch" value="搜索"/>
            <span class="addFriend-btn" onclick="ttp('${ctx}/friend/userlist')" ></span>
        </div>
        <div class="bookHouse-book-list-panel marginList" >
          <%--   <div class="bookHouse-addfriend-panel"  onclick="ttp('${ctx}/friend/addfriend')">
                <span class="addFriend-btn"></span>
                <span class="addFriend-text">添加好友</span>
            </div> --%>
            <div class="div-group" >
	            <ul class="ul-group ">
	            	<li  class="edit group cur"  >
	            		所有
	            	</li>
	            	<c:forEach var="group" items="${list}">
	            	<li  class="edit group pg" data-group="${group.id}">
	            		<span class="btn manage"  data-id="${group.id}" style="display:none"><div class="remove"></div></span>
	            		<span>${group.group_name}<span>
	            	</li>
	            	 </c:forEach>
	            	<li  class="edit group" data-group="0">
	            		未分组
	            	</li>
	            	<li class="fr" onclick="editGroup(this)" data-m="0" style="margin-right: 10px;">
	            		编辑
	            	</li>
	            	<li class="edit fr manage" style="display:none;font-size: 18px;" onclick="movetogroup()">
	            		>
	            	</li>
	            	<li class="edit fr manage" style="display:none;font-size: 20px;" onclick="addgroup()">
	            		+
	            	</li>
	            </ul>
	            <div style="clear:both;"></div>
            </div>
            <div style="clear:both;"></div>
            
            <!-- <div id="wrapper" style=" width:100%; position:absolute; z-index:1;top:150px; bottom:50px; ">
	            <div id="scroller">
					<div id="pullDown">
					   <span class="pullDownIcon"></span><span class="pullDownLabel">Pull down to refresh...</span>
					  </div> -->
			            <c:forEach var="user" items="${page.list}">
			            <ul class="bookHouse-list user"   data-group="${user.group_id}" >
			            	<li class="list-check manage"  style="display:none">
			            		<input type="checkbox" class="edit ckb" id="ckb${user.id}" value="${user.id}">
			                </li>
			            
			                <li class="list-left" style="display:inline-block;" onclick="ttp('${ctx}/friend/friendbook?id=${user.id}')">
			                    	<c:choose>  
									    <c:when test="${fn:substring(user.headimgurl,0,4)=='http'}">  
			                        		<img class="head-img" src="${user.headimgurl}" />
									    </c:when>  
									   <c:otherwise>  
			                        		<img class="head-img" src="${ctx}/upload/images/${user.headimgurl}" />
									    </c:otherwise>  
									</c:choose>  
			                </li>
			                <li class="list-right"  userId="${user.id}">
			                    <div class="rows-one" onclick="ttp('${ctx}/friend/friendbook?id=${user.id}')">
			                        <span class="bookName">${user.nick_name}</span>
			                    </div>
			                    <div class="rows-two" >
			                        <span class="bookInfo" onclick="ttp('${ctx}/friend/friendbook?id=${user.id}')">藏书：${user.bookCount}本</span>
			                        <span class="bookInfo" onclick="ttp('${ctx}/friend/friendbook?id=${user.id}')">阅读：${user.readCount}本</span>
			                        <div class="fr" onclick="setNickName('${user.id}',this)" >
			                            <a href="javascript:void(0);" onclick="setNickName('${user.id}',this)"  class="linkMore ifOpen">设置昵称</a>
			                        </div>
			                    </div>
			                </li>
			            </ul>
					    </c:forEach>
			    
	            
					 <!--  <div id="pullUp">
					   <span class="pullUpIcon"></span><span class="pullUpLabel">Pull up to refresh...</span>
					  </div>
	            </div> -->
         </div>
        </div>
        
        
<div class="alertModal" id="divsetNickname">
    <div class="alertModal-dialog" style="width: 6rem;">
        <div class="alertModal-content">
            	修改好友昵称
            	<br>
            	<br>
         <div >
                   <div class="name">请输入昵称：<input  style="border: solid 1px #eee;" type="text" maxlength="10"    id="nickName" value=""/></div>
                   
          </div>
            	<br>
        </div>
        <div class="alertModal-footer">
            <span class="alertModal-btn btnCancel" onclick="$('#divsetNickname').fadeOut(200);" >取消</span>
            <span class="alertModal-btn btnOK" onclick="saveNickName()">确定</span>
        </div>
    </div>
</div>
        <ul class="bookHouse-friend-nav"  style="display:none;">
            <li>A</li>
            <li>B</li>
            <li>C</li>
            <li>D</li>
            <li>E</li>
            <li>F</li>
            <li>G</li>
            <li>H</li>
            <li>I</li>
            <li>J</li>
            <li>K</li>
            <li>L</li>
            <li>M</li>
            <li>N</li>
            <li>O</li>
            <li>P</li>
            <li>Q</li>
            <li>R</li>
            <li>S</li>
            <li>T</li>
            <li>U</li>
            <li>V</li>
            <li>W</li>
            <li>X</li>
            <li>Y</li>
            <li>Z</li>
        </ul>
    </div>
     <script>
    $(function() {
    	$("#setNickname").fadeIn(200);
    	$(".ul-group").find("li.group").bind("click",function(){
    		selectGroup(this);
    	});
    	$(".ul-group").find("span.btn").bind("click",function(){
    		deleteGroup(this);
    		return false;
    	});
    	
    	
    	
    	$("#btnSearch").bind("click",function(){
    		var s=$.trim($("#txtSearch").val());
    		ttp(formmatSearchUrl(s));
    	});
    })

    </script>
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>