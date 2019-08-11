function save(){

	var msg="";
	
	if($("#group_id").length==1&&$.trim($("#group_id").val()).length==0){
		msg+="请选择分组</br>";
	}
	if($("#group_name").length==1&&$.trim($("#group_name").val()).length==0){
		msg+="请输入分组名称</br>";
	}
	if($.trim(msg).length>0){
		var opt={msg:msg};
		sysAlert(opt);
		return ;
	}
	var data=formToJson($("#groupform"));
	$.ajax({
		url : CONTEXT_PATH + "/friend/savegroup",
		type : "post",
		dataType : "json",
		data : data
	}).done(function(resultVO) {
		var opt={msg:"保存成功"};
		if(resultVO.success){
			opt={msg:"保存成功",callback:function(){
				window.location.href=CONTEXT_PATH+"/friend/friendlist";
			}};
		}
		else{
			opt={msg:"保存失败,错误消息："+resultVO.resultMessage};
		}
		sysAlert(opt);
	})

}