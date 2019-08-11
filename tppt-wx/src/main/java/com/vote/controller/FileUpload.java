package com.vote.controller;

import com.alibaba.druid.util.Base64;
import com.jfinal.aop.Before;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;
import com.vote.interceptor.UserInterceptor;
import com.yqglpt.common.ResultObj;
import com.yqglpt.interceptor.LoginInterceptor;
import com.yqglpt.utils.StrUtils;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Before(UserInterceptor.class)
public class FileUpload extends Controller {

	private static Logger logger = Logger.getLogger(FileUpload.class);


	public  String getFilePath() {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String path =File.separator+"upload"+File.separator +sdf.format(new Date())+File.separator;

		return path;
	}

	public void base64Upload() {

		File smallImg = null;
		ResultObj<String> result = new ResultObj<String>();
		try {
			String extension=".jpg";
			String savePathDir=getFilePath();
			//String savePath=PathKit.getWebRootPath()+savePathDir;
			String savePath=PropKit.get("images.savePath")+savePathDir;

			String newId = StrUtils.uuid();
			String bigFileName = newId + "_b"+extension ;
			String smallFileName =  newId+ "_s"  + extension;
			Base64 base64 = new Base64();

			File currentPathFile = new File(savePath);
			// 未上传时upload目录不一定存在
			if (!currentPathFile.exists()) {
				currentPathFile.mkdirs();
			}

			String base64Str = getPara("base64Str");
			byte[] b = base64.base64ToByteArray(base64Str);
			 for(int i=0;i<b.length;++i)   {  
                if(b[i]<0)  
                {//调整异常数据  
                    b[i]+=256;  
                }  
            }  
		    //生成jpeg图片  
			OutputStream out = new FileOutputStream(savePath+bigFileName);      
            out.write(b);  
            out.flush();  
            out.close();  
            
            //缩略图
            InputStream in = new ByteArrayInputStream(b);             
			smallImg = new File(savePath + smallFileName);
			Thumbnails.of(in).size(160, 160).toFile(smallImg);// 变为400*300,遵循原图比例缩或放到400*某个高度
			
			result = ResultObj.newInstance(savePathDir+smallFileName);

		} catch (Exception e) {
			e.printStackTrace();
			result = ResultObj.newFailInstance("", "文件写入服务器出现错误，请稍后再上传");
		}

		renderJson(result);

	}

	public void fileUpload() {
		File source = null;
		File smallImg = null;
		File bigImg = null;
		ResultObj<String> result = new ResultObj<String>();
		try {
			String tempPath = PathKit.getWebRootPath() + "/temp";

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

			String savePath = PathKit.getWebRootPath() + "/upload/images/";
			String newId = StrUtils.uuid();
			String bigFileName = newId + "_b" + extension;
			String smallFileName = newId + extension;

			if (".png".equals(extension) || ".jpg".equals(extension)
					|| ".gif".equals(extension) || "jpeg".equals(extension)
					|| "bmp".equals(extension)) {

				// 按指定大小把图片进行缩和放（会遵循原图高宽比例）
				// 此处把图片压成400×500的缩略图
				smallImg = new File(savePath + smallFileName);
				Thumbnails.of(source).size(80, 80).toFile(smallImg);// 变为400*300,遵循原图比例缩或放到400*某个高度

				// 缩略图
				// file.getFile().renameTo(new File(savePath + smallFileName));
				// 大图
				bigImg = new File(savePath + bigFileName);
				Thumbnails.of(source).size(800, 800).toFile(bigImg);// 变为400*300,遵循原图比例缩或放到400*某个高度
				// source.renameTo(new File(savePath + bigFileName));

				result = ResultObj.newInstance(smallFileName);
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
