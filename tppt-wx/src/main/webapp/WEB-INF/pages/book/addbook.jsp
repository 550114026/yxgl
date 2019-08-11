<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head lang="en">
	<title>图书管理</title>
    <%@include file="/common/sys_header.jsp" %>
	<script type="text/javascript" src="${ctx}/js-busi/book/addbook.js"></script>
	<script type="text/javascript" src="${ctx}/js/imageutils/mobileBUGFix.mini.js"></script>
	<script type="text/javascript" src="${ctx}/js/imageutils/localResizeIMG.js"></script>
</head>
<body class="inside-page">
    <div class="bookHouse-page">
    
		<form method="post" action="${ctx}/book/savebook" id="addform">
        <div class="bookHouse-inside-page-panel">
            <ul class="bookHouse-inside-detail">
                <li>
                	
                  <input type="hidden"  name="book.id" id="book_id" value="${book.id}"/>
                    <div class="control-input-group dropdown-group">
                        <div class="name">请选择图书的大类别</div>
                       <div class="arrowbox">
                            <span class="h_arrow"></span>
                        </div>
                        <input type="text" class="bookHouse-input"  readonly="readonly" data-toggle="dropdown" />
                        <input type="hidden" name="book.big_type" id="big_type" >
                        <ul class="dropdown-menu" role="menu" id="bigType">
                        
						<c:forEach var="bt" items="${bigType}">
										<li role="presentation">
			                                <a role="menuitem" tabindex="-1" data-value="${bt.dict_code}" data-text="${bt.dict_name}" onclick="javascript:selected(this)">${bt.dict_name}</a>
			                            </li>
						 </c:forEach>
                           
                        </ul>
                    </div> 
                </li>
                <li>
                    <div class="control-input-group">
                        <div class="name">请输入图书的名称</div>
                        <input type="text" maxlength="10" class="bookHouse-input" name="book.book_name" id="book_name" value="${book.book_name}"/>
                    </div>
                </li>
                <li>
                    <div class="control-input-group">
                        <div class="name">请输入作者姓名</div>
                        <input type="text" maxlength="10" class="bookHouse-input" name="book.author" id="author" value="${book.author}"/>
                    </div>
                </li>
                <li>
                    <div class="control-input-group">
                        <div class="name">请选择封面照片</div>
                        <span class="addImg">
                         <input type="file" id="file" name="file"  style="display:block;height:60px;width:60px;opacity:0;" id="cover"/>
	                        
                         <c:choose>
						    <c:when test="${empty book.cover}">
		                     <img id="imgcover" style="margin-top:-70px;" src="${ctx}/resources/images/addImg.png"/>
	                         <input type="hidden" id="filename" name="book.cover">
						    </c:when>
						    <c:otherwise>
						      <img id="imgcover" style="margin-top:-70px;" src="${ctx}/upload/images/${book.cover}"/>
	                         <input type="hidden" id="filename" name="book.cover" value="${book.cover}">
						 
						    </c:otherwise>
						</c:choose>
                        
                       	   </span>
                    </div>
                </li>
            </ul>
        </div>
        <div class="bookHouse-inside-page-panel">
            <ul class="bookHouse-inside-detail">
               <!--  <li>
                    <div class="control-input-group">
                        <div class="name">请设置是否允许借阅</div>
                        <div class="fr">
                            <span class="input-checkbox ifTrue"></span>
                            <input type="hidden" name="book.can_borrow" id="can_borrow" value="1">
                        </div>
                    </div>
                </li> -->
                <li>
                    <div class="control-input-group dropdown-group">
                        <div class="name">借阅状态</div>
                        <div class="arrowbox">
                            <span class="h_arrow"></span>
                        </div>
                        <input type="text" class="bookHouse-input"  readonly="readonly" data-toggle="dropdown" />
                        <input type="hidden" name="book.borrow_status" id="borrow_status" >
                        <ul class="dropdown-menu" role="menu"  id="borrowStatus">
                        
						<c:forEach var="bd" items="${borrowStatus}">
										<li role="presentation">
			                                <a role="menuitem" tabindex="-1" data-value="${bd.dict_code}" data-text="${bd.dict_name}" onclick="javascript:selected(this)">${bd.dict_name}</a>
			                            </li>
						 </c:forEach>
                           
                        </ul>
                    </div>
                </li>
                <li>
                    <div class="control-input-group dropdown-group">
                        <div class="name">阅读状态</div>
                        <div class="arrowbox">
                            <span class="h_arrow"></span>
                        </div>
                        <input type="text" class="bookHouse-input"  readonly="readonly" data-toggle="dropdown" />
                        <input type="hidden" name="book.read_status" id="return_status" >
                        <ul class="dropdown-menu" role="menu"  id="returnStatus">
                        
						<c:forEach var="rs" items="${read_status}">
										<li role="presentation">
			                                <a role="menuitem" tabindex="-1" data-value="${rs.dict_code}" data-text="${rs.dict_name}" onclick="javascript:selected(this)">${rs.dict_name}</a>
			                            </li>
						 </c:forEach>
                           
                        </ul>
                    </div>
                </li>
                

            </ul>
        </div>
        <div class="bookHouse-footer-btnbox">
            <input type="button" id="btnSave" onclick="saveBook()" class="footer-btn" value="提交" />
        </div>
        </form>
    </div>
    <script>
        $(function() {
        		  setSelected("bigType",${book.id!='0'?book.big_type:1});
        		  setSelected("borrowStatus",${book.id!='0'?book.borrow_status:0});
        		  setSelected("returnStatus",${book.id!='0'?book.read_status:0});
            
        
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
            
            $('#file').localResizeIMG({
                width: 500,
                quality: 0.8,
                success: function (result) {
                $('#imgcover').attr("src",result.base64);
                	uploadBase64Str(result.clearBase64);
                }
            });
        })

    </script>
    
    <%@include file="/common/sys_footer.jsp" %>
</body>
</html>