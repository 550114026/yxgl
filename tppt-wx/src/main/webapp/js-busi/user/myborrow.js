function typeChange(){
	var s=$.trim($("#txtSearch").val());
	var t=$.trim($("#borrowRecordStatus").val());
	if(t.length==0)
		t=null;
	ttp(formmatSearchUrl(s,t));
}
function returnBookCheck(id,div){
	var opt={msg:"是否确认归还图书？",
			callbackOk:function(){
				returnBook(id,div)
	}}
	sysConfirm(opt);
}

function returnBook(id,div){
	$(div).attr("enable",false);
	
	$.ajax({
		url : CONTEXT_PATH + "/user/returnbook",
		type : "post",
		dataType : "json",
		data : {"borrowRecord.id":id}
	}).done(function(resultVO) {
		var opt={msg:"归还成功"};
		if(resultVO.success){
			
			$("#fr_"+id).removeClass("isred").addClass("isyes");
			
			opt={msg:"归还成功",callback:function(){
				//ttp(CONTEXT_PATH + "/user/myborrow");
			}};
		}
		else{
			opt={msg:"处理失败,错误消息："+resultVO.resultMessage};
		}
		sysAlert(opt);
	})
}


function likeBook(id,div){
	if($(div).data("like")=="1"){
		opt={msg:"已经点赞过此图书，不可重复点赞"};
		sysAlert(opt);
		return ;
	}
	$.ajax({
		url : CONTEXT_PATH + "/user/likesbook",
		type : "post",
		dataType : "json",
		data : {"borrowRecord.id":id}
	}).done(function(resultVO) {
		var opt={msg:"点赞成功"};
		if(resultVO.success){
			$(div).data("like",1);
			opt={msg:"点赞成功",callback:function(){
				//ttp(CONTEXT_PATH + "/user/myborrow");
			}};
		}
		else{
			opt={msg:"处理失败,错误消息："+resultVO.resultMessage};
		}
		sysAlert(opt);
	})
}
