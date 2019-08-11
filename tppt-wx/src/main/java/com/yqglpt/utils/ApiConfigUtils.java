package com.yqglpt.utils;

import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;

public class ApiConfigUtils {
	
	public static void setThreadLocalApiConfig(){
		
		ApiConfig apiConfig =getApiConfig();
		/**
		 *  是否对消息进行加密，对应于微信平台的消息加解密方式：
		 *  1：true进行加密且必须配置 encodingAesKey
		 *  2：false采用明文模式，同时也支持混合模式
		 */
		apiConfig.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		apiConfig.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
		ApiConfigKit.setThreadLocalApiConfig(apiConfig);
		
	}
	
	
	public static ApiConfig getApiConfig(){
		ApiConfig apiConfig = new ApiConfig();
		// 配置微信 API 相关常量
		apiConfig.setToken(PropKit.get("token"));
		apiConfig.setAppId(PropKit.get("appId"));
		apiConfig.setAppSecret(PropKit.get("appSecret"));
		return apiConfig;
	}

}
