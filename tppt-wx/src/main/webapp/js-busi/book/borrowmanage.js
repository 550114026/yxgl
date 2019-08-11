function typeChange(){
	var s=$.trim($("#txtSearch").val());
	var t=$.trim($("#borrowRecordStatus").val());
	if(t.length==0)
		t=null;
	ttp(formmatSearchUrl(s,t));
}
    		
function bookReturnCheck(bid,id){
	var opt={msg:"是否确认将图书设置为已归还？",
			callbackOk:function(){
		bookreturned(bid,id)
	}}
	sysConfirm(opt);
}


/**
 * 图书归还
 * @param bid
 */
function bookreturned(bid,id){
	$.ajax({
		url : CONTEXT_PATH + "/book/returnbook",
		type : "post",
		dataType : "json",
		data : {"borrowRecord.id":bid}
	}).done(function(resultVO) {
		var opt={msg:"图书设置归还成功"};
		if(resultVO.success){
			opt={msg:"图书设置归还成功"};
			$("#dt_"+id).show();
			$("#mo_"+id).remove();
			$("#collapseBookHouse_"+id).remove();
			$("#fr_"+id).removeClass("isnot").addClass("isyes").html("闲置");
		}
		else{
			opt={msg:"保存失败,错误消息："+resultVO.resultMessage};
		}
		sysAlert(opt);
	})
}

/**
 * 图书催归
 * @param bid
 */
function callbook(bid,sp){
	if($(sp).attr("enable")=="false")
		return;
	$.ajax({
		url : CONTEXT_PATH + "/book/callbook",
		type : "post",
		dataType : "json",
		data : {"borrowRecord.id":bid}
	}).done(function(resultVO) {
		var opt={};
		if(resultVO.success){
			$(sp).attr("enable","false");
			opt={msg:"操作成功，信息已发出"};
			$(sp).attr("enable","false");
			$(sp).removeAttr("onclick");
		}
		else{
			opt={msg:"操作失败,错误消息："+resultVO.resultMessage};
		}
		sysAlert(opt);
	})
}