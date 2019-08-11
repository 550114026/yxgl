<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>个人信息</title>
    <%@include file="/common/sys_header.jsp" %>
	<script type="text/javascript" src="${ctx}/js-busi/user/info.js"></script>
</head>
<body class="inside-page">
    <div class="bookHouse-page">
        <div class="bookHouse-inside-page-panel">
        
		<form method="post" id="infoform">
		
                  <input type="hidden"  name="user.id" id="userid" value="${user.id}"/>
            <ul class="bookHouse-inside-detail">
                <li>
                <div class="control-input-group">
                        <div class="name">头像</div>
                        <span class="addImg">
                         <input type="file" id="file" name="file" onchange="ajaxFileUpload(this)"  style="display:block;height:60px;width:60px;opacity:0;" id="cover"/>
	                      <c:choose>  
						    <c:when test="${fn:substring(user.headimgurl,0,4)=='http'}">  
                        		<img  style="margin-top:-70px;" class="head-img" src="${user.headimgurl}" />
						    </c:when>  
						   <c:otherwise>  
                        		<img  style="margin-top:-70px;" class="head-img" src="${ctx}/upload/images/${user.headimgurl}" />
						    </c:otherwise>  
						</c:choose>  
	                    <input type="hidden" id="filename" name="user.headimgurl" value="${user.headimgurl}">
                        
                       	   </span>
                    </div>
                
                   
                </li>
                <li>
                    <div class="fl">
                        <span class="detailvalue">昵称</span>
                    </div>
                    <div class="fr">    
                    	<input type="text" readonly="readonly" class="bookHouse-input" name="user.nick_name" id="nick_name" value="${user.nick_name}"/>
                   </div>
                </li>
                <li>
                <div class="control-input-group dropdown-group">
                        <div class="name">性别</div>
                       <div class="arrowbox">
                            <span class="h_arrow"></span>
                        </div>
                        <input type="text" class="bookHouse-input"  readonly="readonly" data-toggle="dropdown" />
                        <input type="hidden" name="user.sex" id="sex" >
                        <ul class="dropdown-menu" role="menu" id="slsex">                        
							<li role="presentation">
				               <a role="menuitem" tabindex="-1" data-value="男" data-text="男" onclick="javascript:selected(this)">男</a>
				            </li>                                             
							<li role="presentation">
				               <a role="menuitem" tabindex="-1" data-value="女" data-text="女" onclick="javascript:selected(this)">女</a>
				            </li>                               
                        </ul>
                    </div> 
                </li>
                <li>
                    <div class="fl">
                        <span class="detailvalue">手机号</span>
                    </div>
                    <div class="fr">  	
                    	<input type="text" class="bookHouse-input" name="user.telephone" id="telephone" value="${user.telephone}"/>
	                </div>
                </li>
            </ul>
           
        </form>
        </div>
        <div class="bookHouse-footer-btnbox">
            <input id="footerBtn" type="button" class="footer-btn" onclick="return saveInfo();" value="保存" />
        </div>

    </div>
    <script>
        $(function() {
        		  setSelected("slsex",'${user.sex}');
        })

    </script>
    
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>