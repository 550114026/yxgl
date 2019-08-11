package com.jfinal.weixin.sdk.utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.kit.HashKit;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.JsTicketApi;
import com.jfinal.weixin.sdk.api.JsTicketApi.JsApiType;

public class JsSDKUtil {
	
	public static String getSignature(String timestamp,String nonce_str,HttpServletRequest request) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		String signature="";
		String jsapi_ticket=JsTicketApi.getTicket(JsApiType.jsapi).getTicket();
		String url=PropKit.get("webSite.Url")+PropKit.get("webSite.basePath")+request.getServletPath();
		if(StringUtils.isNoneEmpty(request.getQueryString())){
			url=url+"?"+request.getQueryString();
		}
		
		//特别注意的是调用微信js，url必须是当前页面(转发的不行)  
	    String str ="jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + nonce_str +
                "&timestamp=" + timestamp +
                "&url=" + url;
	  
	     signature = HashKit.sha1(str); 
	     
	    /* MessageDigest crypt = MessageDigest.getInstance("SHA-1");
         crypt.reset();
         crypt.update(str.getBytes("UTF-8"));
         signature = byteToHex(crypt.digest());*/
		
		return signature;
	}
}
