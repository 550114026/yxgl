package com.vote.util;

import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfig;

public class ApiConfigUtil {

    /**
     * 如果要支持多公众账号，只需要在此返回各个公众号对应的  ApiConfig 对象即可
     * 可以通过在请求 url 中挂参数来动态从数据库中获取 ApiConfig 属性值
     */
    public static ApiConfig getApiConfig() {
        ApiConfig ac = new ApiConfig();

        // 配置微信 API 相关常量
        ac.setToken(PropKit.get("token"));
        ac.setAppId(PropKit.get("appId"));
        ac.setAppSecret(PropKit.get("appSecret"));

        ac.setAppId(PropKit.get("appId"));

        /**
         *  是否对消息进行加密，对应于微信平台的消息加解密方式：
         *  1：true进行加密且必须配置 encodingAesKey
         *  2：false采用明文模式，同时也支持混合模式
         */
        ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
        ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
        return ac;
    }
}
