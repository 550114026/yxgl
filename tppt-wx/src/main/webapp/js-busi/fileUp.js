
function ajaxFileUpload(file) {
	// 图片格式的验证
	var filepath = $(file).val();
	var extStart = filepath.lastIndexOf(".");
	var ext = filepath.substring(extStart, filepath.length).toUpperCase();
	if (ext != ".BMP" && ext != ".PNG" && ext != ".GIF" && ext != ".JPG"
			&& ext != ".JPEG") {
		bootAlert("图片限于bmp,png,gif,jpeg,jpg格式");
		$(file).val("");
		return false;
	}
var id=$(file).attr("id");
	$.ajaxFileUpload({
		url : CONTEXT_PATH + "/file/fileUpload", // 用于文件上传的服务器端请求地址
		secureuri : false, // 一般设置为false
		fileElementId : $(file).attr("id"), // 文件上传空间的id属性 <input type="file"
		// id="imageFile" name="imageFile" />
		type : "POST", // get 或 post
		dataType : "json", // 返回值类型
		success : function(backdata, status) {
			// 服务器成功响应处理函数
			if (backdata.success) {
				$("#"+id).parents("span.addImg").find("input#filename").val(backdata.object);
				$("#"+id).parents("span.addImg").find("img").attr("src",CONTEXT_PATH+'/upload/images/'+backdata.object);
				
			}
		},
		error : function(data, status, e) // 服务器响应失败处理函数
		{
			sysAlert({msg:"图片上传失败，请重新上传！"});
		}
	});

}