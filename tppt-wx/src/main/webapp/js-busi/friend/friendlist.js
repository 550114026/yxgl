

function selectGroup(li){
	$("li.cur").removeClass("cur");
	$(li).addClass("cur");
	var groupId=$(li).data("group");
	$("ul.user").each(function(){
		if(typeof groupId == "undefined" ||groupId==$(this).data("group")){
			$(this).show();
		}
		else
			$(this).hide();
		
	});
}

function deleteGroup(span){
	
	var opt={msg:"确定要删除分组["+$(span).next().text()+"]",callbackOk:function(){
		
		deleteG($(span).data("id"));
	},
	
	};
	sysConfirm(opt);
	
	return false;
}

function deleteG(id){
	
	$.ajax({
		url : CONTEXT_PATH + "/friend/deletegroup",
		type : "post",
		dataType : "json",
		data : {"group.id":id}
	}).done(function(resultVO) {
		var opt={msg:"删除成功"};
		if(resultVO.success){
			opt={msg:"删除成功",callback:function(){
				window.location.href=window.location.href+"?time="+(new Date()).getMilliseconds();
			}};
		}
		else{
			opt={msg:"删除失败,错误消息："+resultVO.resultMessage};
		}
		sysAlert(opt);
	})
}


function editGroup(li){
	if($(li).data("m")==1){
		$(li).data("m",0);
		$(".manage").hide()
		$(li).text("编辑");
		
	}else{
		$(li).data("m",1);
		$(".manage").show()
		$(li).text("取消");
	}
	
	
}
function movetogroup(){
	var ids=getCheckedId();
	if(ids.length==0){
		var opt={msg:"请选择要移动到其他组的好友"};
		sysAlert(opt);
		return ;
	}
	ttp(CONTEXT_PATH+"/friend/movetogroup?ids="+ids);
}
function addgroup(){
	
	if($("ul.ul-group").find(".pg").length>=3){
		var opt={msg:"好友分组不能超过3个"};
		sysAlert(opt);
		return ;
	}
	
	
	
	var ids=getCheckedId();
	if(ids.length==0){
		var opt={msg:"请选择要加入到新分组的好友"};
		sysAlert(opt);
		return ;
	}
	ttp(CONTEXT_PATH+"/friend/addgroup?ids="+ids);
}

function getCheckedId(){
	var ids = new Array();
	$(".ckb").each(function(){
		if(this.checked){
			ids.push("'"+$(this).attr("value")+"'");
		}
	});
	return ids.join(",");
}


/**
 * 显示设置昵称界面
 * @param userId
 * @param btn
 */
function setNickName(userId,btn){
	var div=$("#divsetNickname");
	div.data("userId",userId);
	var nickName=$(btn).parents("li.list-right").find("span.bookName").text();
	div.find("#nickName").val(nickName);
	div.fadeIn(200);
}

/**
 * 保存昵称设置
 */
function saveNickName(){
	
	var div=$("#divsetNickname");
	var userId=div.data("userId");
	var nickName=div.find("#nickName").val();
	
	if($.trim(nickName).length==0){
		var opt={msg:"请输入好友昵称"};
		sysAlert(opt);
		return;
	}
	
	$.ajax({
		url : CONTEXT_PATH + "/friend/setfriendnickname",
		type : "post",
		dataType : "json",
		data : {"uerRel.nick_name":nickName,"uerRel.friend_user":userId}
	}).done(function(resultVO) {
		var opt={msg:"设置成功"};
		if(resultVO.success){
			$("li.list-right[userId="+userId+"]").find("span.bookName").html(nickName);
		}
		else{
			opt={msg:"设置失败,错误消息："+resultVO.resultMessage};
		}
		$('#divsetNickname').fadeOut(200)
		sysAlert(opt);
	})
}

