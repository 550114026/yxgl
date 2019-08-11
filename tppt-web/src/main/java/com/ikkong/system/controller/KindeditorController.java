/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import java.util.List;
import java.util.Map;

public class KindeditorController extends BaseController {
	
	public void upload_json() {
		UploadFile file = getFile();
		Record maps = Record.create();
		if (null == file) {
			maps.set("error", 1);
			maps.set("message", "请选择要上传的图片");
			renderJson(maps);
			return ;
		}
		String originalFileName = file.getOriginalFileName();
		String dir = getPara("dir", "image");
		// 测试后缀
		boolean ok = UploadFileUtils.testExt(dir, originalFileName);
		if (!ok) {
			maps.set("error", 1);
			maps.set("message", "上传文件的类型不允许");
			renderJson(maps);
			return ;
		}
		BladeFile bf = new BladeFile(file);
		bf.transfer();
		Object fileId = bf.getFileId();	
		String url = "/kindeditor/renderFile/" + fileId;
		maps.set("error", 0);
		maps.set("title", fileId);
		maps.set("url", Cst.me().getContextPath() + url);
		maps.set("name", originalFileName);
		renderJson(maps);
	}
	
	public void file_manager_json() {
		String dir = getPara("dir", "image");
		// 考虑安全问题
		String path = getPara("path", "");
		// 不允许使用..移动到上一级目录
		if (path.indexOf("..") >= 0) {
			renderText("Access is not allowed.");
			return;
		}
		// 最后一个字符不是/
		if (!"".equals(path) && !path.endsWith("/")) {
			renderText("Parameter is not valid.");
			return;
		}
		String order = getPara("order", "name");

		Map<String, Object> result = UploadFileUtils.listFiles(dir, path, order);
		renderJson(result);
	}
	
	public void initimg() { 
		Map<String, Object> img = Db.init().findById("TFW_ATTACH", getPara("id"));
		if (null != img) {
			renderJson(json(img));
		} else {
			renderJson(fail("获取图片失败！"));
		}
	}

	public void initfile() {
		String ids = getPara("ids");
		Db dao = Db.init();
		List<Record> file = dao.selectList("select ID as \"id\",NAME as \"name\",URL as \"url\" from TFW_ATTACH where ID in (#{join(ids)})", Record.create().set("ids", ids.split(",")));
		if (null != file) {
			renderJson(json(file));
		} else {
			renderJson(fail("获取附件失败！"));
		}
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
