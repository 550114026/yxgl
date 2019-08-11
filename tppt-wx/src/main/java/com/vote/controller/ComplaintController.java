package com.vote.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.vote.interceptor.UserInterceptor;
import com.vote.model.*;
import com.vote.util.VoteCheckUtil;
import com.vote.util.WxConfig;
import com.vote.util.WxjsUtil;
import com.yqglpt.common.BaseController;
import com.yqglpt.common.ResultObj;
import com.yqglpt.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Date;

@Before(UserInterceptor.class)
public class ComplaintController extends BaseController {
    private static Logger logger = Logger.getLogger(ComplaintController.class);

    public void index() {
        render("index.html");
    }


    public void next() {
        //投诉类型
        setSessionAttr("ctype", getParaToInt("t", 0));
        render("submit.html");
    }

    public void notice() {
        render("notice.html");
    }

    public void submit() {
        VoteUser user = getUser();
        if (user != null) {
            user = VoteUser.dao.findUserByOpenid(user.getStr("openid"));
            setSessionAttr("vote_user",user);
            Complaint complaint = new Complaint();
            complaint.set("eventid", user.getInt("eventid"));
            if (user.get("merchantid") != null) {
                complaint.set("merchantid", user.get("merchantid"));
            }
            complaint.set("openid", user.get("openid"));
            complaint.set("ctype", getSessionAttr("ctype"));
            complaint.set("createtime", new Date());
            complaint.save();
        }
        render("result.html");
    }


}
