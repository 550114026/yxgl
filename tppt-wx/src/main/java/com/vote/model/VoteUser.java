package com.vote.model;

import com.jfinal.plugin.activerecord.Model;

public class VoteUser extends Model<VoteUser> {

    public static final VoteUser dao = new VoteUser();

    public VoteUser findUserByOpenid(String openid) {
        return findFirst("select * from t_vote_user where openid=? ", openid);
    }


}
