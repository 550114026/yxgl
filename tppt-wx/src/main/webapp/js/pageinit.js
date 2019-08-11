$(function() {
	initViewImg($);
	
	//设置不使用缓存
	$.ajaxSetup({
		cache : false
	});
	//$("#sys_cover").fadeIn(200);
	$(document).ajaxSend(function(evt, request, settings) {
		if (settings.showCover==null || settings.showCover != false)
			$("#sys_cover").fadeIn(200);
	});
	$(document).ajaxStop(function() {
		$("#sys_cover").fadeOut("hide");
	});
	$(document).ajaxError(
			function(event, jqXHR, ajaxSettings, thrownError) {
				sysAlert({msg:thrownError});
			})

})
    function sysAlert(opts){
    	var sys_alert=$("#sys_alert");
    	if(opts.msg)
    		sys_alert.find(".alertModal-content").html(opts.msg);
    	else
    		sys_alert.find(".alertModal-content").html("操作提示");
    	if(opts.callback){
    		var date=opts.data?opts.data:null;
    		sys_alert.find("span.btnOK").unbind("click");
    		sys_alert.find("span.btnOK").bind("click", function() {
    			$("#sys_alert").fadeOut(200);
    			window.setTimeout( function() {
    				opts.callback(true,opts.data)
    			}, 100)
    		})
    	}
    	else
    		sys_alert.find("span.btnOK").bind("click", function() {
    			$("#sys_alert").fadeOut(200);
    		})
    		
    	sys_alert.fadeIn(200);
    }
    function sysConfirm(opts){
    	var sys_confirm=$("#sys_confirm");
    	if(opts.msg)
    		sys_confirm.find(".alertModal-content").html(opts.msg);
    	else
    		sys_confirm.find(".alertModal-content").html("操作确认");
    		
    	if(opts.okText)
    		sys_confirm.find(".btnOK").text(opts.okText);
    	else
    		sys_confirm.find(".btnOK").text("确定");
    		
    	if(opts.cancelText)
    		sys_confirm.find(".btnCancel").text(opts.cancelText);
    	else
    		sys_confirm.find(".btnCancel").text("取消");
    	if(opts.callbackOk){
    		var date=opts.data?opts.data:null;
    		sys_confirm.find("span.btnOK").unbind("click");
    		sys_confirm.find("span.btnOK").bind("click", function() {
    			$("#sys_confirm").fadeOut(200);
    			window.setTimeout( function() {
    				opts.callbackOk(opts.data)
    			}, 100)
    		})
    	}else{
    		sys_confirm.find("span.btnOK").unbind("click");
    		sys_confirm.find("span.btnOK").bind("click", function() {
    			$("#sys_confirm").fadeOut(200);
    		})
    	}
    	
    	if(opts.callbackCancel){
    		var date=opts.data?opts.data:null;
    		sys_confirm.find("span.btnCancel").unbind("click");
    		sys_confirm.find("span.btnCancel").bind("click", function() {
    			$("#sys_confirm").fadeOut(200);
    			window.setTimeout( function() {
    				opts.callbackCancel(opts.data)
    			}, 100)
    		})
    	}
    	else {
    		sys_confirm.find("span.btnCancel").unbind("click");
    		sys_confirm.find("span.btnCancel").bind("click", function() {
    			$("#sys_confirm").fadeOut(200);
    		})
    	}
    		
    	sys_confirm.fadeIn(200);
    }