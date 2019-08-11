package com.ikkong.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class NetUtils {

	// 静态常量
	private static final Logger log = LoggerFactory
			.getLogger(NetUtils.class);
	
	/** 
     * @Description:使用HttpURLConnection发送get请求 
     * @author:liuyc 
     * @time:2016年5月17日 下午3:27:29 
     */  
    public static String sendGet(String urlParam, Map<String, Object> params, String charset) {  
        StringBuffer resultBuffer = null;  
        // 构建请求参数  
        StringBuffer sbParams = new StringBuffer();  
        if (params != null && params.size() > 0) {  
            for (Entry<String, Object> entry : params.entrySet()) {  
                sbParams.append(entry.getKey());  
                sbParams.append("=");  
                sbParams.append(entry.getValue());  
                sbParams.append("&");  

                log.info("sendGet请求参数："+entry.getKey()+"="+entry.getValue());
            }  
        }  
        HttpURLConnection con = null;  
        BufferedReader br = null;  
        try {  
            URL url = null;  
            if (sbParams != null && sbParams.length() > 0) {  
                url = new URL(urlParam + "?" + sbParams.substring(0, sbParams.length() - 1));  
            } else {  
                url = new URL(urlParam);  
            }  
            log.info("sendGet请求地址："+urlParam+"?"+url.getQuery());
            con = (HttpURLConnection) url.openConnection();  
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");  
            con.connect();  
            resultBuffer = new StringBuffer();  
            br = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));  
            String temp;  
            while ((temp = br.readLine()) != null) {  
                resultBuffer.append(temp);  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        } finally {  
            if (br != null) {  
                try {  
                    br.close();  
                } catch (IOException e) {  
                    br = null;  
                    throw new RuntimeException(e);  
                } finally {  
                    if (con != null) {  
                        con.disconnect();  
                        con = null;  
                    }  
                }  
            }  
        }  
        return resultBuffer.toString();  
    }  
	
	
	/**
	 * http Post请求
	 * 
	 * @param saServiceVO
	 * @return
	 * @throws Exception
	 */
	public static String httpPost(String url) throws  Exception{

		try {
			

			log.info("................执行服务请求执行NetUtil.httpPost..................");
			
			
			/*
			 * //get方式需要将参数拼接到URL ParamsUtil.initParams(saServiceVO);
			 */

			log.info("................执行服务请求执行NetUtil.httpPost,地址："+url);
			URL u = new URL(url);
			HttpURLConnection client = (HttpURLConnection) u.openConnection();
			client.setRequestMethod("get");
			client.setDoOutput(true);
			client.setRequestProperty("Content-length", "0");
			client.setUseCaches(false);
			client.setAllowUserInteraction(false);
			client.setRequestProperty("User-agent", "	Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0"); 
			

			// 参数类型
			
			client.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;");
			// 参数
			String params="";
			

			log.info("................执行服务请求执行NetUtil.httpPost,参数："+params);
			byte[] data = params.getBytes("UTF-8");
			client.setRequestProperty("Content-Length",
					String.valueOf(data.length));

			// 写入参数
			OutputStream outStream = client.getOutputStream();
			outStream.write(data);
			outStream.flush();
			outStream.close();

			StringBuilder sb = new StringBuilder();
			client.connect();
			int status = client.getResponseCode();
			switch (status) {
			case 200:
			case 201:
				String resultCartset = "UTF-8";
				BufferedReader br = new BufferedReader(new InputStreamReader(
						client.getInputStream(), resultCartset));
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line);
				}
				br.close();
				log.info("................执行服务请求执行NetUtil.httpPost,返回："+sb.toString());
				return sb.toString();
			default:
				throw new Exception("执行httpPost请求返回状态出错，状态信息：" + status);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
}
