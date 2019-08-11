<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/";
%>
<meta charset="UTF-8">
<c:set var="extpath" value="${pageContext.request.contextPath}/ext4"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script>
var basePath="${basePath}";
var extpath="${extpath}";
var path="${pageContext.request.contextPath}";
</script>