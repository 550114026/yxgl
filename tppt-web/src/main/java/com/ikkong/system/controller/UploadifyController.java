package com.ikkong.system.controller;

import com.ikkong.core.base.BaseController;
import com.ikkong.core.constant.Cst;
import com.ikkong.core.dao.Db;
import com.ikkong.core.toolbox.Func;
import com.ikkong.core.toolbox.Record;
import com.ikkong.core.toolbox.file.BladeFile;
import com.ikkong.core.toolbox.file.UploadFileUtils;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

import java.io.File;
import java.util.Map;

public class UploadifyController extends BaseController {
	
	public Record upload() {
		UploadFile file = getFile(getPara("imgFile"));
		Record maps = Record.create();
		if (null == file) {
			maps.set("error", 1);
			maps.set("message", "请选择要上传的图片");
			return maps;
		}
		String originalFileName = file.getOriginalFileName();
		String dir = getPara("dir", "image");
		// 测试后缀
		boolean ok = UploadFileUtils.testExt(dir, originalFileName);
		if (!ok) {
			maps.set("error", 1);
			maps.set("message", "上传文件的类型不允许");
			return maps;
		}
		
		UploadFileUtils.transfer(file);
		BladeFile bf = new BladeFile(file);
		bf.transfer();
		Object fileId = bf.getFileId();	
		String url = "/uploadify/renderFile/" + fileId;
		maps.set("error", 0);
		maps.set("fileId", fileId);
		maps.set("url", Cst.me().getContextPath() + url);
		maps.set("fileName", originalFileName);
		return maps;	
	}
	
	public void renderFile() {
		String id = getPara(0);
		Map<String, Object> file = Db.init().findById("TFW_ATTACH", id);
		if (Func.isEmpty(file)) {
			redirect("/error/error404");
			return;
		} else {
			String url = file.get("URL").toString();
			File f = new File((Cst.me().isRemoteMode() ? "" : PathKit.getWebRootPath()) + url);
			renderFile(f);
		}
	}
	
}
