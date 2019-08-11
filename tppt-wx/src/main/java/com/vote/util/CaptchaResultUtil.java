package com.vote.util;

import com.alibaba.fastjson.JSON;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.weixin.sdk.api.ApiConfigKit;

/**
 * @ProjectName: tppt-parent
 * @Package: com.vote.util
 * @ClassName: CaptchaResultUtil
 * @Author: linjinbiao@bwton.com
 * @Description: ${description}
 * @Date: 2019/6/12 15:57
 * @Version: 1.0
 */
public class CaptchaResultUtil {
    static String captchaResultCacheName = "captchaResult";
    private static Integer faultLimit;

    static {
        if (faultLimit == null) {
            ApiConfigKit.setThreadLocalApiConfig(ApiConfigUtil.getApiConfig());
            faultLimit = PropKit.getInt("tencentCaptcha.faultLimit", 10);
        }
    }

    /**
     * 是否超出失败次数限制
     *
     * @param mid
     * @return
     */
    public static boolean overFaultLimit(String mid) {
        Integer faultTimes = CaptchaResultUtil.getCaptchaFaultTimes(mid);
        if (faultTimes != null && faultTimes > faultLimit) {
            return true;
        }
        return false;
    }


    /**
     * 记录验证结果，分商户处理
     *
     * @param mid
     * @param result
     */
    public static void saveCaptchaResult(String mid, boolean result) {
        if (result) {
           // CacheKit.remove(captchaResultCacheName, mid);
        } else {
            Integer count = CacheKit.get(captchaResultCacheName, mid);
            if(count==null){
                count=0;
            }
            CacheKit.put(captchaResultCacheName, mid, ++count);
        }
    }

    /**
     * 获取验证码失败次数
     *
     * @param mid
     * @return
     */
    public static Integer getCaptchaFaultTimes(String mid) {
        Integer count = CacheKit.get(captchaResultCacheName, mid);
        if (count == null) {
            count = 0;
        }
        return count;
    }


}
