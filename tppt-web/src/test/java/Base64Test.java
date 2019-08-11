import com.ikkong.core.toolbox.kit.CharsetKit;
import org.springframework.http.HttpEntity;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class Base64Test {

    /**
     * Shiro 记住密码采用的是AES加密，AES key length 需要是16位，该方法生成16位的key
     */
    public static void main(String[] args) {

        try {
            String imageUrl = "https://fuss10.elemecdn.com/f/0e/6ddd0933acd041d02c495e7e016bajpeg.jpeg";
            String filePath = "D:\\images\\upload\\20181130\\6ddd0933acd041d02c495e7e016bajpeg.jpg";
            download(imageUrl,filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }


        String keyStr = "JFinalBlade";

        byte[] keys;
        try {
            keys = keyStr.getBytes(CharsetKit.UTF_8);
            System.out.println(new BASE64Encoder().encode(Arrays.copyOf(keys, 16)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public static void download(String urlString, String savePath) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();//con.connect();
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
        OutputStream os = new FileOutputStream(savePath);
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
    }



    public static void downloadQianURL(String fileUrl,String savePath) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            // 设置字符编码
            connection.setRequestProperty("Charset", "UTF-8");
            connection.connect();

            InputStream is = connection.getInputStream();
            // 创建文件
            File file = new File(savePath);
            FileOutputStream out = new FileOutputStream(file);
            int i = 0;
            while ((i = is.read()) != -1) {
                out.write(i);
            }
            is.close();
            out.close();

        } catch (Exception e) {
            System.out.println(e + fileUrl + savePath);
        }
    }

}
