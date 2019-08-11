package com.vote.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

public class VoteList extends Model<VoteList> {

    public static final VoteList dao = new VoteList();

    public VoteList findUserByOpenid(String openid) {
        return findFirst("select * from t_vote_user where openid=? ", openid);
    }

    /**
     * 查询普通投票
     *
     * @param openid
     * @param eId
     * @param mId ,
     * @return
     */
    public Integer voteCount(String openid, Integer eId, Integer mId) {

        String sql="select CAST(sum(ifnull(tickets,0))  as SIGNED)as vote " +
                "from t_vote_list vl " +
                "where vl.eventid=? " +
                "and merchantid = ? " +
                "and openid = ? " +
                "and TO_DAYS(vote_time) = TO_DAYS(now()) ";
        Long result= Db.queryLong(sql, new Object[]{eId,mId,openid});
        if(result!=null){
            return result.intValue();
        }else{
            return 0;
        }
    }

    /**
     * 查询VIP投票
     *
     * @param openid
     * @param eId
     * @param mId
     * @return
     */
    public Integer voteVipCount(String openid, Integer eId, Integer mId) {

        String sql="select CAST(sum(ifnull(tickets,0))  as SIGNED)as vipvote \n" +
                "from t_vote_list vl \n" +
                "where vl.eventid=? \n" +
                "and vl.votetype=2 \n" +
                "and openid = ?  ";
        Long result= Db.queryLong(sql, new Object[]{eId,openid});
        if(result!=null){
            return result.intValue();
        }else{
            return 0;
        }
    }


}
