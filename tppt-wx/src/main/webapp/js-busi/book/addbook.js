function uploadBase64Str(base64Str){
	$.ajax({
		url : CONTEXT_PATH + "/file/base64Upload",
		type : "post",
		dataType : "json",
		data : {"base64Str":base64Str}
	}).done(function(backdata) {
		// 服务器成功响应处理函数
		if (backdata.success) {
			$("#filename").val(backdata.object);
			//$("#"+id).parents("span.addImg").find("img").attr("src",CONTEXT_PATH+'/upload/images/'+backdata.object);
			
		}
	})
}

function saveBook(){

	var msg="";
	if($.trim($("#book_name").val()).length==0){
		msg+="请输入图书的名称</br>";
	}
	if($.trim($("#author").val()).length==0){
		msg+="请输入作者姓名</br>";
	}
	if($.trim(msg).length>0){
		var opt={msg:msg,callback:function(){window.close();
		}};
		sysAlert(opt);
		return ;
	}
	$("#btnSave").unbind("click");
	
	var data=formToJson($("#addform"));
	$.ajax({
		url : CONTEXT_PATH + "/book/savebook",
		type : "post",
		dataType : "json",
		data : data
	}).done(function(resultVO) {
		$("#btnSave").bind("click",saveBook);
		var opt={msg:"图书保存成功"};
		if(resultVO.success){
			opt={msg:"图书保存成功",callback:function(){
				if($("#book_id").val()=="0"){
					$("#book_name").val("");
					$("#author").val("");
					$("#filename").val("");
					$("#imgcover").attr("src",CONTEXT_PATH+"/resources/images/addImg.png");

	        		  setSelected("bigType",1);
	        		  setSelected("borrowStatus",0);
	        		  setSelected("returnStatus",0);
				}
					//window.location.href=window.location.href+"?time="+(new Date()).getMilliseconds();
				else
					ttp(CONTEXT_PATH + "/book/mybook");
			}};
		}
		else{
			opt={msg:"保存失败,错误消息："+resultVO.resultMessage};
		}
		sysAlert(opt);
	})

}