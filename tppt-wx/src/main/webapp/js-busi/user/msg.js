/**
 * 修改图书状态
 * @param btn
 */
function dealMsg(id,t){
	$.ajax({
		url : CONTEXT_PATH + "/user/dealmsg",
		type : "post",
		dataType : "json",
		data : {"message.id":id}
	}).done(function(resultVO) {
		var opt={msg:"处理成功"};
		if(resultVO.success){
			opt={msg:"处理成功",callback:function(){
				ttp(CONTEXT_PATH + "/user/msglist?t="+t);
			}};
		}
		else{
			opt={msg:"处理失败,错误消息："+resultVO.resultMessage};
		}
		sysAlert(opt);
	})
}

/**
 * 开始借阅申请
 */
function borrowBook(mid){
	ttp(CONTEXT_PATH + "/book/borrowbookview?mid="+mid);
}


/**
 * 发送好友请求
 * @param id
 */
function sendAddFriendMsg(id){
	
	$.ajax({
		url : CONTEXT_PATH + "/friend/sendAddfriend",
		type : "post",
		dataType : "json",
		data : {"message.busi_id":id}
	}).done(function(resultVO) {
		var opt={msg:"请求已发送"};
		if(resultVO.success){
		}
		else{
			opt={msg:"请求发送失败,错误消息："+resultVO.resultMessage};
		}
		sysAlert(opt);
	})
}
