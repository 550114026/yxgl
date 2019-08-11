package com.vote.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.utils.HttpUtils;
import com.jfinal.weixin.sdk.utils.JsonUtils;
import lombok.Data;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * @ProjectName: tppt-parent
 * @Package: com.vote.util
 * @ClassName: TencentCaptchaUtil
 * @Author: linjinbiao@bwton.com
 * @Description: ${description}
 * @Date: 2019/6/11 16:09
 * @Version: 1.0
 */
public class TencentCaptchaUtil {
    private static Logger logger = Logger.getLogger(TencentCaptchaUtil.class);

    public static Boolean enabled = false;
    private static ApiConfig apiConfig = null;
    private static TencentCaptchaConfig tcc = null;
    private static String verifyUrl;

    static {
        if (apiConfig == null) {
            ApiConfigKit.setThreadLocalApiConfig(ApiConfigUtil.getApiConfig());
            apiConfig = ApiConfigKit.getApiConfig();
            enabled = PropKit.getBoolean("tencentCaptcha.enabled", false);
            verifyUrl = PropKit.get("tencentCaptcha.URL");
            String json = PropKit.get("tencentCaptcha.config");
            tcc=JSON.parseObject(json, TencentCaptchaConfig.class);

            if (tcc != null) {
                tcc.init();
            }
        }
    }


    /**
     * 调用接口验证
     * @param appId
     * @param ticket
     * @param randstr
     * @param userIP
     * @return
     * @throws IOException
     */
    public static boolean verifyCaptcha(String appId,String ticket, String randstr, String userIP) throws IOException {
        if(!tcc.appIds.contains(appId)){
            logger.info("验证码提交的APPID错误！");
            return false;
        }

        String appSecretKey=tcc.appSecretKeys.get(appId);
        Map<String, String> queryParas = new HashMap<>();
        queryParas.put("aid", appId);
        queryParas.put("AppSecretKey", appSecretKey);
        queryParas.put("Ticket", ticket);
        queryParas.put("Randstr", randstr);
        queryParas.put("UserIP", userIP);
        String result = HttpUtils.get(verifyUrl, queryParas);
        VerifyResult vr = JsonUtils.parse(result, VerifyResult.class);
        if (vr.getResponse().equals("1")) {
            return true;
        } else {
            logger.info("验证码错误！");
        }
        return false;
    }

    /**
     * 随机获取一个appid
     * @return
     */
    public static String getAppId() {
        if (tcc != null&&tcc.appIds.size()>0) {
            Integer count = tcc.appIds.size();
            Random R = new Random();
            Integer index = R.nextInt(count);
            return tcc.appIds.get(index);
        }
        return null;
    }

    @Data
    public static class VerifyResult {

        @JSONField(name="response")
        private String response;

        @JSONField(name="evil_level")
        private String evil_level;


        @JSONField(name="err_msg")
        private String err_msg;

    }


    /**
     * 验证码参数配置
     */
    public static class TencentCaptchaConfig {

        @JSONField(name="appList")
        List<TencentCaptchaApp> appList = new ArrayList<>();
        List<String> appIds = new ArrayList<>();
        Map<String, String> appSecretKeys = new HashMap();

        public void init() {
            appIds = new ArrayList<>();
            appSecretKeys = new HashMap();
            for (TencentCaptchaApp tca : appList) {
                appIds.add(tca.appId);
                appSecretKeys.put(tca.appId, tca.appSecretKey);
            }
        }

        public List<TencentCaptchaApp> getAppList() {
            return appList;
        }

        public void setAppList(List<TencentCaptchaApp> appList) {
            this.appList = appList;
        }

    }


    @Data
    public static  class TencentCaptchaApp {

        String appId;
        String appSecretKey;

    }

//    public void test(){
//
//        TencentCaptchaConfig tcc=new TencentCaptchaConfig();
//
//        TencentCaptchaApp tca=new TencentCaptchaApp();
//        tca.setAppId("1");
//        tca.setAppSecretKey("2");
//        tcc.getAppList().add(tca);
//     String json=   JSON.toJSONString(tcc);
//
//     json="{\"appList\":[{\"appId\":\"2086779568\",\"appSecretKey\":\"0rHaS4Za49G0uQY7kscVEAg**\"},{\"appId\":\"2076264959\",\"appSecretKey\":\"0kR682dgvVA90CDE7ZToy7A**\"}]}";
//
//        tcc=JSON.parseObject(json,TencentCaptchaConfig.class);
//        if (tcc != null) {
//            tcc.init();
//            Integer count = tcc.appIds.size();
//            Random R = new Random();
//            Integer index = R.nextInt(count);
//        String id=     tcc.appIds.get(index);
//        }
//    }
//
//    public static void main(String[] args){
//
//        TencentCaptchaUtil TCU=new TencentCaptchaUtil();
//        TCU.test();
//    }

}
