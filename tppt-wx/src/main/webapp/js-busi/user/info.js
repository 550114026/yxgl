function saveInfo(){

//	var msg="";
//	if($.trim($("#book_name").val()).length==0){
//		msg+="请输入图书的名称</br>";
//	}
//	if($.trim($("#author").val()).length==0){
//		msg+="请输入作者姓名</br>";
//	}
//	if($.trim(msg).length>0){
//		var opt={msg:msg,callback:function(){window.close();
//		}};
//		sysAlert(opt);
//		return ;
//	}
	var myreg = /^(1+\d{10})$/; 
	if(!myreg.test($("#telephone").val())) 
	{ 
		var opt={msg:'请输入有效的手机号码！'};
		sysAlert(opt);
	    return false; 
	} 
	
	var data=formToJson($("#infoform"));
	$.ajax({
		url : CONTEXT_PATH + "/user/saveinfo",
		type : "post",
		dataType : "json",
		data : data
	}).done(function(resultVO) {
		var opt={msg:"保存成功"};
		if(resultVO.success){
			opt={msg:"保存成功",callback:function(){
				window.location.href=window.location.href;
			}};
		}
		else{
			opt={msg:"保存失败,错误消息："+resultVO.resultMessage};
		}
		sysAlert(opt);
	})

}