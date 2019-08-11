package com.vote.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.weixin.sdk.kit.IpKit;
import com.vote.interceptor.UserInterceptor;
import com.vote.model.Event;
import com.vote.model.Merchant;
import com.vote.model.VoteList;
import com.vote.model.VoteUser;
import com.vote.util.*;
import com.yqglpt.common.BaseController;
import com.yqglpt.common.ResultObj;
import com.yqglpt.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Date;

@Before(UserInterceptor.class)
public class EventController extends BaseController {
    private static Logger logger = Logger.getLogger(EventController.class);

    public void index() {
        Integer id = getParaToInt("eid");
        setEventId(id);
        Event eModel = Event.dao.findFirstById(id);
        if (eModel != null && StringUtils.isNotBlank(eModel.getStr("pictures"))) {
            String[] pics = eModel.getStr("pictures").split(",");
            setAttr("sharePic", pics[0]);
        }
        //人气加一
        Event.dao.addHits(id);
        setSessionAttr("event", eModel);
        setAttr("eModel", eModel);
        setAttr("page", new PageUtil(queryPage(1)));
        WxConfig wxConfig = WxjsUtil.initWXconfig(getRequest());
        setAttr("wxConfig", wxConfig);

        //补充活动ID
        VoteUser voteUser = getUser();
        if (voteUser.getInt("eventid") == null) {
            voteUser.set("eventid", id);
            setSessionAttr("vote_user", voteUser);
            voteUser.update();
        }


        String captchaAppId = TencentCaptchaUtil.getAppId();
        setAttr("captchaAppId", captchaAppId);
        setAttr("captchaEnabled", TencentCaptchaUtil.enabled);
        render("index.html");
    }

    /**
     * 按页取数据
     */
    public void list() {
        int pagenum = 1;
        String str = getSearchText();
        // 页码
        if (!isParaBlank("p")) {
            pagenum = getParaToInt("p");
        }
        setAttr("page", new PageUtil(queryPage(pagenum)));
        render("list.html");
    }

    /**
     * 数据库取数据
     *
     * @param pageNumber
     * @return
     */
    public Page<Merchant> queryPage(Integer pageNumber) {
        return Merchant.dao.queryPage(getEventId(), pageNumber, null);
    }


    /**
     * 搜索
     */
    public void search() {
        Integer id = getParaToInt("eid");
        if (getEventId() == null) {
            setEventId(id);
        }
        Event eModel = Event.dao.findFirstById(getEventId());
        if (eModel != null && StringUtils.isNotBlank(eModel.getStr("pictures"))) {
            setSessionAttr("event", eModel);
            String[] pics = eModel.getStr("pictures").split(",");
            setAttr("sharePic", pics[0]);
        }
        setSessionAttr("event", eModel);
        setAttr("eModel", eModel);
        Integer number = 1;
        if (!isParaBlank("no")) {// 选手号
            number = getParaToInt("no");
        }
        Page<Merchant> page = Merchant.dao.queryPage(getEventId(), 1, number);
        setAttr("page", new PageUtil(page));
        render("search.html");
    }


    public void vote() {

        ResultObj<String> result = new ResultObj<String>();
        if (StringUtils.isEmpty(getOpenId())) {
            result = ResultObj.newFailInstance("", "请重新登录");
            renderJson(result);
            return;
        }

        //选手ID
        Integer mid = getParaToInt("mid");
        logger.info("普通投票：" + getEventId() + ":" + getOpenId() + ":" + mid);

        //先做投票限制
        if (VoteCountUtil.overVoteLimit(mid.toString())) {
            result = ResultObj.newFailInstance("-5", "投票过于频繁，请稍后重试");
            renderJson(result);
            return;
        } else {
            VoteCountUtil.saveVoteCount(mid.toString());
        }

        if (CaptchaResultUtil.overFaultLimit(mid.toString())) {
            result = ResultObj.newFailInstance("-3", "验证码验证失败次数过多，请稍后重试");
            renderJson(result);
            return;
        }

        //验证码验证
        boolean captchaCheckResult = captchaCheck(mid.toString());
        if (!captchaCheckResult) {
            result = ResultObj.newFailInstance("-4", "验证码错误，请稍后重试");
            renderJson(result);
            return;
        } else {
            CaptchaResultUtil.saveCaptchaResult(mid.toString(), captchaCheckResult);
        }
        //CaptchaResultUtil.saveCaptchaResult(mid.toString(), false);

        Event event = Event.dao.findById(getEventId());

        //检查是否可投票
        VoteCheckUtil voteCheckUtil = new VoteCheckUtil();
        Integer checkResult = voteCheckUtil.voteCheck(getEventId(), getOpenId(), mid);
        if (checkResult != null && checkResult != 0) {
            result = ResultObj.newFailInstance(checkResult.toString(), "");
            renderJson(result);
            return;
        }

        VoteList model = new VoteList();
        model.set("eventid", getEventId());
        model.set("merchantid", mid);
        model.set("openid", getOpenId());
        model.set("votetype", 1);
        model.set("status", 1);
        model.set("vote_time", new Date());
        model.set("version", 1);
        model.set("tickets", 1);
        model.set("amount", 0f);
        try {
            model.save();
            result = ResultObj.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result = ResultObj.newFailInstance("", "系统异常");
        }


        //补充选手ID
        VoteUser voteUser = getUser();
        if (voteUser.getInt("merchantid") == null) {
            voteUser.set("merchantid", mid);
            voteUser.update();
        }

        renderJson(result);

    }


    public void prize() {
        Integer id = getParaToInt("eid");
        Event eModel = Event.dao.findFirstById(id);
        setSessionAttr("event", eModel);
        setAttr("eModel", eModel);
        render("prize.html");
    }

    /**
     * 1.判断验证码是是否启用
     * 2.调用接口验证
     *
     * @return
     */
    private boolean captchaCheck(String mid) {
        if (!TencentCaptchaUtil.enabled) {
            return true;
        }

        String ip = IpKit.getRealIp(this.getRequest());
        String appId = getPara("appId");
        String ticket = getPara("ticket");
        String randstr = getPara("randstr");
        boolean checkResult = true;
        try {
            checkResult = TencentCaptchaUtil.verifyCaptcha(appId, ticket, randstr, ip);
        } catch (Exception ex) {
            logger.info("验证码错误");
            logger.error(ex);
        }
        return checkResult;
    }

}
