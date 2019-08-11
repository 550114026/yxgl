<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>好友分组</title>
    <%@include file="/common/sys_header.jsp" %>
	<script type="text/javascript" src="${ctx}/js-busi/friend/group.js"></script>
</head>
<body class="inside-page">
    <div class="bookHouse-page">
		<form method="post"  id="groupform">
        <div class="bookHouse-inside-page-panel">
           <input type="hidden"  name="ids" id="user" value="${ids}"/>
            <ul class="bookHouse-inside-detail">
                <c:if test="${optype=='move'}">
                <li>
                    <div class="control-input-group dropdown-group">
                       <div class="name">请选择分组</div>
                       <div class="arrowbox">
                            <span class="h_arrow"></span>
                        </div>
                        <input type="text" class="bookHouse-input"  readonly="readonly" data-toggle="dropdown" />
                        <input type="hidden" name="group.id" id="group_id" >
                        <ul class="dropdown-menu" role="menu" id="groupId">
                        
						<c:forEach var="group" items="${groups}">
										<li role="presentation">
			                                <a role="menuitem" tabindex="-1" data-value="${group.id}" data-text="${group.group_name}" onclick="javascript:selected(this)">${group.group_name}</a>
			                            </li>
						 </c:forEach>
                           
                        </ul>
                    </div> 
                </li>
                </c:if> 
                
                <c:if test="${optype=='add'}">
                <li>
                    <div class="control-input-group">
                        <div class="name">请输入组名</div>
                        <input type="text" placeholder="请输入3个字以下的组名" class="bookHouse-input" name="group.group_name" id="group_name" maxlength="3" value=""/>
                    </div>
                </li>
                </c:if> 
            </ul>
        </div>
        
        </form>
        <div class="bookHouse-inside-page-panel">
         <div class="group-head">已选择好友</div>
             <ul class="group-user" >
         		<c:forEach var="user" items="${list}">
             	  <li  >
                    	<c:choose>  
						    <c:when test="${fn:substring(user.headimgurl,0,4)=='http'}">  
                        		<img class="head-img" src="${user.headimgurl}" />
						    </c:when>  
						   <c:otherwise>  
                        		<img class="head-img" src="${ctx}/upload/images/${user.headimgurl}" />
						    </c:otherwise>  
						</c:choose>  
					</br>
					${user.nick_name}
                </li>
            	 </c:forEach>
            </ul>
        </div>
        <div class="bookHouse-footer-btnbox">
            <input type="button" onclick="save()" class="footer-btn" value="提交" />
        </div>
    </div>
    <script>
    var optype="${optype}";
        $(function() {
            
        
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
            })
        })

    </script>
    
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>