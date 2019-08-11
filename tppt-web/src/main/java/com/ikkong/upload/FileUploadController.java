package com.ikkong.upload;

import com.ikkong.common.vo.ResultObj;
import com.ikkong.core.base.BaseController;
import com.ikkong.utils.StrUtils;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUploadController extends BaseController {

	private static Logger logger = Logger.getLogger(FileUploadController.class);

	public  String getFilePath() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String path =File.separator+"upload"+File.separator +sdf.format(new Date())+File.separator+"set"+File.separator;

		return path;
	}
	public void upload() {
		File source = null;
		File smallImg = null;
		File originalImg = null;
		ResultObj<String> result = new ResultObj<String>();
        String savePathDir=getFilePath();
		//String savePath=PathKit.getWebRootPath()+savePathDir;
		String savePath=PropKit.get("images.savePath")+savePathDir;

        File currentPathFile = new File(savePath);
        // 未上传时upload目录不一定存在
        if (!currentPathFile.exists()) {
            currentPathFile.mkdirs();
        }
		try {
			//String tempPath = PathKit.getWebRootPath() + "/temp";

			UploadFile file = getFile();// getFile(getPara("name"), tempPath);

			// // 异步上传时，无法通过uploadFile.getFileName()获取文件名
			// String fileName = getPara("filename");
			// fileName = fileName.substring(fileName.lastIndexOf("\\") + 1); //
			// 去掉路径
			// // 异步上传时，无法通过File source = uploadFile.getFile();获取文件
			// source = new File(tempPath + "/" + fileName); // 获取临时文件对象
			source = file.getFile();
			String fileName = file.getFileName();
			fileName = fileName.substring(fileName.lastIndexOf("\\") + 1); // 去掉路径
			String extension = fileName.substring(fileName.lastIndexOf("."));

			String newId = StrUtils.uuid();
			String bigFileName = newId + "_b" + extension;
			String smallFileName = newId+ "_s"  + extension;

			if (".png".equals(extension) || ".jpg".equals(extension)
					|| ".gif".equals(extension) || ".jpeg".equals(extension)
					|| ".bmp".equals(extension)) {

				// 按指定大小把图片进行缩和放（会遵循原图高宽比例）
				// 此处把图片压成400×500的缩略图
				smallImg = new File(savePath + smallFileName);
				Thumbnails.of(source).size(400,400).toFile(smallImg);// 变为400*300,遵循原图比例缩或放到400*某个高度


				// 原图
				originalImg = new File(savePath + bigFileName);
				Thumbnails.of(source).size(600,600).toFile(originalImg);// 变为400*300,遵循原图比例缩或放到400*某个高度
				// source.renameTo(new File(savePath + bigFileName));
				//source.renameTo(originalImg);
				result = ResultObj.newInstance(savePathDir+smallFileName);
			} else {
				source.delete();
				result = ResultObj.newFailInstance("",
						"只允许上传png,jpg,jpeg,gif,bmp类型的图片文件");
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = ResultObj.newFailInstance("", "文件写入服务器出现错误，请稍后再上传");
		}

		renderJson(result);

	}
}
