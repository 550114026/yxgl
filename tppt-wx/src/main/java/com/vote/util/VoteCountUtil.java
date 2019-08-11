package com.vote.util;

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
public class VoteCountUtil {
    static String voteCountCacheName = "voteRecord";
    private static Integer voteLimit;

    static {
        if (voteLimit == null) {
            //ApiConfigKit.setThreadLocalApiConfig(ApiConfigUtil.getApiConfig());
            voteLimit = PropKit.getInt("vote.limit", 15);
        }
    }

    /**
     * 是否超出失败次数限制
     *
     * @param mid
     * @return
     */
    public static boolean overVoteLimit(String mid) {
        Integer faultTimes = VoteCountUtil.getVoteCount(mid);
        if (faultTimes != null && faultTimes > voteLimit) {
            return true;
        }
        return false;
    }


    /**
     * @param mid
     */
    public static void saveVoteCount(String mid) {
        Integer count = CacheKit.get(voteCountCacheName, mid);
        if(count==null){
            count=0;
        }
        CacheKit.put(voteCountCacheName, mid, ++count);
    }

    /**
     * @param mid
     * @return
     */
    private static Integer getVoteCount(String mid) {
        Integer count = CacheKit.get(voteCountCacheName, mid);
        if (count == null) {
            count = 0;
        }
        return count;
    }


}
