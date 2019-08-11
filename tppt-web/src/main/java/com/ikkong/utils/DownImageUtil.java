package com.ikkong.utils;

import com.ikkong.common.vo.ResultObj;
import com.jfinal.kit.PropKit;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * 下载图片
 */
public class DownImageUtil {


    private static Logger logger = Logger.getLogger(DownImageUtil.class);

    public static String getFilePath(String eid) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String path = File.separator + "upload" + File.separator + sdf.format(new Date()) + File.separator + eid + File.separator;

        return path;
    }

    public static String downLoadImage(String urlStr, String eid) {

        String savePathDir = getFilePath(eid);
        String savePath = PropKit.get("images.savePath") + savePathDir;
        String fileName = null;
        if (urlStr.indexOf("/") > 0) {
            // 去掉路径
            fileName = urlStr.substring(urlStr.lastIndexOf("/") + 1);
        } else if (urlStr.indexOf("\\") > 0) {
            // 去掉路径
            fileName = urlStr.substring(urlStr.lastIndexOf("\\") + 1);
        }
        File sf = new File(savePath);
        if (!sf.exists()) {
            sf.mkdirs();
        }


        String extension = ".jpg";
        String newId = StrUtils.uuid();
        String bigFileName = newId + "_b" + extension;

        String sourcePath = savePath + bigFileName;

        if (!downImages(urlStr, sourcePath)) {
            return null;
        }
        logger.debug("图片路径下载地址：" + urlStr);
        logger.debug("图片路径本地：" + sourcePath);


        //图片对象
        File originalImg = new File(sourcePath);
        Thumbnails.Builder<File> thumbnails = Thumbnails.of(originalImg);
        if (thumbnails != null) {
            File smallImg = null;
            ResultObj<String> result = new ResultObj<String>();
            String smallFileName = null;
            try {
                smallFileName = newId + "_s" + extension;
                // 按指定大小把图片进行缩和放（会遵循原图高宽比例）
                // 此处把图片压成400×500的缩略图
                smallImg = new File(savePath + smallFileName);
                // 变为400*300,遵循原图比例缩或放到400*某个高度
                thumbnails.size(300, 300).toFile(smallImg);
                result = ResultObj.newInstance(savePathDir + smallFileName);
            } catch (Exception e) {
                logger.error("下载图片失败！地址：" + urlStr);
                e.printStackTrace();
                return null;
            }
            return savePathDir + smallFileName;
        }
        return null;
    }


    /**
     * 根据图片的URL下载的图片到本地的filePath
     *
     * @param imageUrl 图片的网址
     * @param filePath 文件夹
     */
    public static boolean downImages(String imageUrl, String filePath) {

        try {
            URL url = new URL(imageUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            System.out.println(con.getResponseCode());
            System.out.println(con.getContentLength());
            Map map = con.getHeaderFields();
            Iterator it = map.keySet().iterator();
            boolean gzip = false;
            while (it.hasNext()) {
                Object type = map.get(it.next());
                if (type.toString().indexOf("gzip") != -1) {
                    gzip = true;
                    break;
                }
            }
            InputStream is = con.getInputStream();
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(filePath);
            if (!gzip) {
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
            } else {
                GZIPInputStream gis = new GZIPInputStream(is);
                while ((len = gis.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
            }
            os.flush();
            os.close();
            is.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }


    //链接url下载图片
    private static boolean downloadPicture(String urlStr, String path) {
        URL url = null;
        try {
            url = new URL(urlStr);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());
            File sf = new File(path);

            FileOutputStream fileOutputStream = new FileOutputStream(sf);
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            fileOutputStream.flush();
            dataInputStream.close();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
