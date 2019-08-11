<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>${siteName}</title>
    <%@include file="/common/sys_jsp_header.jsp" %>
    <script>

    var newWallpaper="wallpapers/blue.jpg";
    </script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/common/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/common/zoom.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${extpath}/desktop/common.js" />
    <link rel="stylesheet" type="text/css" href="${extpath}/desktop/css/desktop.css" />
    <link rel="stylesheet" type="text/css" href="${ctx}/css/websocket.css" />
    <!-- GC -->
    <!-- <x-compile> -->
    <!-- <x-bootstrap> -->
    <script type="text/javascript" src="${extpath}/shared/include-ext.js"></script>
    <script type="text/javascript" src="${extpath}/shared/options-toolbar.js"></script>
    <!-- </x-bootstrap> -->
    <style>
    .closebtn{background-color: #989f5a}
    .x-action-col-icon{
    height: 22px !important;
    width: 22px !important;
    padding: 0px 10px !important;
    margin-left: 10px;}
    </style>
    <script type="text/javascript">
    var loginStatus=${user!=null?1:0};
    var userId="${userId}";
    var account="${account}";
    var userType="${userType}";
    var userName="${userName}";
        Ext.Loader.setPath({
            'Ext.ux.desktop': '${extpath}/desktop/js',
            //'Ext.ux.desktop.Wallpaper': '${extpath}/desktop/wallpapers',
            
            MyDesktop: '${extpath}/desktop'
        });

        Ext.require('MyDesktop.App');

        var myDesktopApp;
        //
        var newWallpaper="${extpath}/desktop/wallpapers/Blue-Sencha.jpg";
        var deskPath="${extpath}/desktop/";
        

        Ext.onReady(function () {
            myDesktopApp = new MyDesktop.App();

        	//
        	
            
        });

        function dateFormat(value,fm){
        	if(!value)
        		return null;
        	if((typeof value)=="string"){
            	if(!fm){
            		return value.substr(0,10);
            	}
            	else{
            		return value.substr(0,19);
            	}
        	}
        	else{
        		if(!fm)
            		fm='Y-m-d';
                if(null != value){ 
                    return Ext.Date.format(new Date(value),fm); 
                }else{ 
                    return null; 
                }
        	}
        } 
        
        function checkPassWord(password) {//必须为字母加数字且长度不小于8位
        	   var str = password;
        	    if (str == null || str.length < 6 || str.length > 10) {
        	        return false;
        	    }
        	    var reg1 = new RegExp(/^[0-9A-Za-z]+$/);
        	    if (!reg1.test(str)) {
        	        return false;
        	    }
        	    var reg = new RegExp(/[A-Za-z].*[0-9]|[0-9].*[A-Za-z]/);
        	    if (reg.test(str)) {
        	        return true;
        	    } else {
        	        return false;
        	    }
        	}
       
        function play(){
        	var ua = navigator.userAgent.toLowerCase(); 
        	if(ua.match(/msie ([\d.]+)/)){ 
        	jQuery('#__alert_sound').html('<object classid="clsid:22D6F312-B0F6-11D0-94AB-0080C74C7E95"><param name="AutoStart" value="1" /><param name="Src" value="'+src+'" /></object>'); 
        	} 
        	else if(ua.match(/firefox\/([\d.]+)/)){ 
        	jQuery('#__alert_sound').html('<embed src="'+src+'" type="audio/mp3" hidden="true" loop="false" mastersound></embed>'); 
        	} 
        	else if(ua.match(/chrome\/([\d.]+)/)){ 
        	jQuery('#__alert_sound').html('<audio src="'+src+'" type="audio/mp3" autoplay=”autoplay” hidden="true"></audio>'); 
        	} 
        	else if(ua.match(/opera.([\d.]+)/)){ 
        	jQuery('#__alert_sound').html('<embed src="'+src+'" hidden="true" loop="false"><noembed><bgsounds src="/sounds/alert/1.mp3"></noembed>'); 
        	} 
        	else if(ua.match(/version\/([\d.]+).*safari/)){ 
        	jQuery('#__alert_sound').html('<audio src="'+src+'" type="audio/mp3" autoplay=”autoplay” hidden="true"></audio>'); 
        	} 
        	else { 
        	jQuery('#__alert_sound').html('<embed src="'+src+'" type="audio/mp3" hidden="true" loop="false" mastersound></embed>'); 
        	} 
        }
    	var src="${pageContext.request.contextPath}/mp3/msg.mp3";
        $(function(){ 
        
        	
       }); 
    </script>
    <!-- </x-compile> -->
    <script type="text/javascript" src="${extpath}/desktop/Msg.js"></script> 
    <%-- <script type="text/javascript" src="${extpath}/desktop/Login.js"></script> --%>
</head>

<body>
	<div id="__alert_sound"></div>
    <a href="javascript:void(0)" target="_blank" alt="Powered by Ext JS"
       id="poweredby"><div></div></a>

</body>
</html>