function borrowBook(){
	var msg="";
	if($.trim($("#borrow_time").val()).length==0){
		msg+="请选择借阅时间</br>";
	}
	if($.trim($("#expected_return_time").val()).length==0){
		msg+="请选择计划归还时间</br>";
	}
	if($.trim(msg).length>0){
		var opt={msg:msg};
		sysAlert(opt);
		return ;
	}
	
	var data=formToJson($("#borrowform"));
	$.ajax({
		url : CONTEXT_PATH + "/book/borrowbook",
		type : "post",
		dataType : "json",
		data : data
	}).done(function(resultVO) {
		var opt={msg:"请求发送成功"};
		if(resultVO.success){
			opt={msg:"请求发送成功"};
		}
		else{
			opt={msg:"请求发送失败,错误消息："+resultVO.resultMessage};
		}
		opt.callback=function(){
			if($("#mid").val()!=0){
				ttp(CONTEXT_PATH + "/user/msglist");
			}else{
				history.go(-1)
			}
		};
		sysAlert(opt);
	})

}