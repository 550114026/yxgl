/**
 * 修改图书状态
 * @param btn
 */
function listSave(btn){
	var form=$(btn).parents("div.bookHose-collapse").find("form");
	var data=formToJson(form);
	$.ajax({
		url : CONTEXT_PATH + "/book/savebook",
		type : "post",
		dataType : "json",
		data : data
	}).done(function(resultVO) {
		var opt={msg:"保存成功"};
		if(resultVO.success){
			opt={msg:"保存成功"};
			var span=$("#spborrow_status_"+data["book.id"]);
			if(data["book.borrow_status"]==1){
				span.removeClass("isyes");
				span.addClass("isnot");
				span.text("借出");
			}
			else{
				span.removeClass("isnot");
				span.addClass("isyes");
				span.text("闲置");
				
			}
			
		}
		else{
			opt={msg:"保存失败,错误消息："+resultVO.resultMessage};
		}
		sysAlert(opt);
	})
}

/**
 * 删除图书
 * @param btn
 */
function deletebook(id){
	var opt={msg:"是否删除图书",okText:"删除",callbackOk:function(){
		$.ajax({
			url : CONTEXT_PATH + "/book/deletbook",
			type : "post",
			dataType : "json",
			data : {"book.id":id}
		}).done(function(resultVO) {
			if(resultVO.success){
				opt={msg:"删除成功"};
				sysAlert(opt);
				$("#items_"+id).remove();
				$("#collapseBookHouse"+id).remove();
			}
			else{
				opt={msg:"删除失败,错误消息："+resultVO.resultMessage};
				sysAlert(opt);
			}
		});
	}};
	sysConfirm(opt);
}


/**
 * 
 * @param id
 */
function viewBook(id){
	
}