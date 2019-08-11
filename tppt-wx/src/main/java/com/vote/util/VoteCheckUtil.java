package com.vote.util;


import com.vote.model.Event;
import com.vote.model.VoteList;

import java.util.Calendar;
import java.util.Date;

public class VoteCheckUtil {

    /**
     * 检查是否可以投票
     *
     * @param eventId
     * @param openId
     * @param mId
     * @return 0 可投票，-1 可VIP投票，-2 不可投票
     */
    public Integer voteCheck(Integer eventId, String openId, Integer mId) {
        Event event = Event.dao.findById(eventId);

        Date voteEndTime=event.getDate("voteendtime");
        //判断时间延迟59秒
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(voteEndTime);
        calendar.add(Calendar.SECOND,59);
        voteEndTime=calendar.getTime();
        //超过投票时间限制
        if(voteEndTime.compareTo(new Date())<0){
            return -9;
        }

        Integer votelimit = event.getInt("votelimit");
        Integer viplimit = event.getInt("viplimit");
        Integer vote = VoteList.dao.voteCount(openId, eventId, mId);
        //有配置，有投票，投票大于限制数
        if (votelimit != null && vote != null && votelimit <= vote) {
            Integer vipvote = VoteList.dao.voteVipCount(openId, eventId, mId);
            //超过VIP限制
            if ((viplimit != null&& viplimit <=0) && vipvote != null && viplimit <= vipvote) {
                return -2;
            }
            return -1;
        }
        return 0;
    }

}
