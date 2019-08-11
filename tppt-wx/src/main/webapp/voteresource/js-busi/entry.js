jm(document).ready(function(){  jm('#file').localResizeIMG({
    width: 500,
    quality: 0.8,
    success: function (result) {
        jm('#imgcover').attr("src", result.base64);
        uploadBase64Str(result.clearBase64);
    }
});


});

function bindclick(){
    jm("#img-line").find(".roundedCorner").bind("click",function () {
        var div=jm(this).parent();

        var picturs=jm("#picturs").val().replace(div.data("val")+",","");
        jm("#picturs").val(picturs);
        div.remove();
        if(jm("#img-line").find(".fl").length<4){
            jm("#img-add").show();
        }
        // Mobi.prompt({
        //     textClass: "confirm",
        //     content: "是否删除图片",
        //     callback: function() {
        //         var picturs=jm("#picturs").val().replace(div.data("val")+",","");
        //         jm("#picturs").val(picturs);
        //         div.remove();
        //         if(jm("#img-line").find(".fl").length<4){
        //             jm("#img-add").show();
        //         }
        //     },
        //     cancelCallback: function() {
        //
        //     }
        // });
    })
}


function uploadBase64Str(base64Str){
    jm.ajax({
        url : CONTEXT_PATH + "/file/base64Upload",
        type : "post",
        dataType : "json",
        data : {"base64Str":base64Str},
        success:function(backdata){
            jm('#imgcover').attr("src", CONTEXT_PATH+"/voteresource/image/addImg.png");
            // 服务器成功响应处理函数
            if (backdata.success) {
                var picturs=jm("#picturs").val()+backdata.object+",";
                jm("#picturs").val(picturs);
                var html=" <div class=\"fl\" data-val=\""+backdata.object+"\">\n" +
                    "      <img  src=\""+IMAGE_PATH+backdata.object.replace(/\\/g,"/")+"\"/>\n" +
                    "       <div class=\"roundedCorner\">X</div>\n" +
                    "     </div>"
                jm("#img-line").append(html);
                if(jm("#img-line").find(".fl").length==4){
                    jm("#img-add").hide();
                }
                bindclick();
            }
        },
        error:function(){
            Mobi.ing("提交失败！", true);
        }
    });
}

function save(){

    var msg="";
    if(jm.trim(jm("#name").val()).length==0){
        Mobi.ing("请输入选手名称", true);
        return ;
    }
    var regPhone=/^1\d{10}$/;
    var phone=jm.trim(jm("#phone").val());
    if(phone.length==0){
        Mobi.ing("请输入手机号码", true);
        return ;
    }else if(phone.match(regPhone)==null){
        Mobi.ing("手机号码格式错误", true);
        return ;
    }
    if(jm.trim(jm("#address").val()).length==0){
        Mobi.ing("请输入地址", true);
        return ;
    }

    jm("#btnSave").unbind("click");

    var data=formToJson(jm("#addForm"));
    jm.ajax({
        url : CONTEXT_PATH + "/entry/save",
        type : "post",
        dataType : "json",
        data : data,
        success:function(resultVO){
            jm("#btnSave").bind("click",save);
            if(resultVO.success){
                Mobi.ing("报名提交成功,等待审核！", true);
                window.setTimeout(function() {
                        window.location.href=CONTEXT_PATH+"/event?eid="+jm("#eventid").val();
                    },
                    500);
            }
            else{
                Mobi.ing("提交失败,消息:"+resultVO.resultMessage, false);
            }
        },
        error:function(){
            Mobi.ing("提交失败！", true);
        }
    });

}


/**
 *
 * @param formObj
 * @returns {___anonymous555_556}
 */
function formToJson(formObj){
    var a=formObj.serialize();
    return urlArgs(a);
};


function urlArgs(query){
    query=decodeURIComponent(query);
    var args={};
    var pairs = query.split('&');
    for  (var i = 0; i < pairs.length; i++) {
        var pos = pairs[i].indexOf('=');
        if(pos == -1){
            continue;
        } // 如果没找到，就跳过
        var name = pairs[i].substr(0,pos);  // 获得名称
        var value = pairs[i].substr(pos+1); // 提取value
        //value = decodeURIComponent(value); // 解码value ,如果地址栏的参数没有转码的话则不需要这一步
        args[name] = value;
    }
    return args
}
