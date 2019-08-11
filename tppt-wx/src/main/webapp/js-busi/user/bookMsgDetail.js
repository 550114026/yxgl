

/**
 * 开始借阅申请
 */
function borrowBook(bid){
	ttp(CONTEXT_PATH + "/book/borrowbookview?bid="+bid);
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
