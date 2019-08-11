package com.vote.util;

import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.utils.JsSDKUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class WxjsUtil {


    public static WxConfig initWXconfig(HttpServletRequest request) {
        ApiConfigKit.setThreadLocalApiConfig(ApiConfigUtil.getApiConfig());
        WxConfig wc=new WxConfig();
        wc.setAppId(PropKit.get("appId"));
        wc.setDebug(PropKit.getBoolean("jsapi.debugMode",false));

        long timestamp = System.currentTimeMillis()/1000;
        wc.setTimestamp(String.valueOf(timestamp));
        String randomStr = StringUtils.right(String.valueOf(System.currentTimeMillis()), 10);
        wc.setNonceStr(randomStr);

        try {
            String signature=JsSDKUtil.getSignature(wc.getTimestamp(),wc.getNonceStr(),request);
            wc.setSignature(signature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //JsonKit.toJson(object);
        return wc;
    }


}
