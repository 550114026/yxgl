<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<meta charset="UTF-8">
<base href="<%=basePath%>">
<meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
<link rel="stylesheet" href="css/jquery.mobile-1.4.5.min.css" />
<script type="text/javascript" charset="utf-8" src="js/jquery-1.7.1.min.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery.cookie.js"></script>
<script type="text/javascript" charset="utf-8" src="js/jquery.mobile-1.4.5.min.js"></script>
<link rel="stylesheet" href="validator/jquery.validator.css">
<script type="text/javascript" src="validator/jquery.validator.js"></script>
<script type="text/javascript" src="validator/local/zh_CN.js"></script>
<link rel="stylesheet" href="js/jqm-datebox-1.4.5.min.css">
<script type="text/javascript" src="js/jqm-datebox-1.4.5.core.min.js"></script>
<script type="text/javascript" src="js/jqm-datebox-1.4.5.mode.datebox.min.js"></script>
<script type="text/javascript" src="js/jquery.mobile.datebox.i18n.zh_CN.utf8.js"></script>
<%--<link rel="stylesheet" href="js/jquery.ui.datepicker.mobile.css">--%>
<%--<script type="text/javascript" src="js/jQuery.ui.datepicker.js"></script>--%>
<%--<script type="text/javascript" src="js/jquery.ui.datepicker.mobile.js"></script>--%>
<%--<link rel="stylesheet" href="jqueryui/jquery-ui.min.css">--%>
<%--<script type="text/javascript" src="jqueryui/jquery-ui.min.js"></script>--%>
<%--<script type="text/javascript">--%>
<%--	jQuery(function($){  --%>
<%--        $.datepicker.regional['zh-CN'] = {  --%>
<%--            closeText: '关闭',  --%>
<%--            prevText: '<上月',  --%>
<%--            nextText: '下月>',  --%>
<%--            currentText: '今天',  --%>
<%--            monthNames: ['一月','二月','三月','四月','五月','六月',  --%>
<%--            '七月','八月','九月','十月','十一月','十二月'],  --%>
<%--            monthNamesShort: ['一','二','三','四','五','六',  --%>
<%--            '七','八','九','十','十一','十二'],  --%>
<%--            dayNames: ['星期日','星期一','星期二','星期三','星期四','星期五','星期六'],  --%>
<%--            dayNamesShort: ['周日','周一','周二','周三','周四','周五','周六'],  --%>
<%--            dayNamesMin: ['日','一','二','三','四','五','六'],  --%>
<%--            weekHeader: '周',  --%>
<%--            dateFormat: 'yy-mm-dd',  --%>
<%--            firstDay: 1,  --%>
<%--            isRTL: false,  --%>
<%--            showMonthAfterYear: true,  --%>
<%--            yearSuffix: '年'};  --%>
<%--        $.datepicker.setDefaults($.datepicker.regional['zh-CN']);  --%>
<%--	});--%>
<%--</script>--%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>