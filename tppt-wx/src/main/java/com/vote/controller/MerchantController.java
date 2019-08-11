package com.vote.controller;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.weixin.sdk.kit.IpKit;
import com.vote.interceptor.UserInterceptor;
import com.vote.model.Event;
import com.vote.model.Merchant;
import com.vote.model.VoteList;
import com.vote.model.VoteUser;
import com.vote.util.TencentCaptchaUtil;
import com.vote.util.VoteCheckUtil;
import com.vote.util.WxConfig;
import com.vote.util.WxjsUtil;
import com.yqglpt.common.BaseController;
import com.yqglpt.common.ResultObj;
import com.yqglpt.utils.PageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.util.Date;

/**
 * 选手页
 */

@Before(UserInterceptor.class)
public class MerchantController extends BaseController {

    private static Logger logger = Logger.getLogger(MerchantController.class);

    public void index() {
        Merchant model = null;
        Event eModel = null;
        Integer mid = getParaToInt("mid");
        Integer eid = null;
        if (eid == null) {
            model = Merchant.dao.findById(mid);
            if (model != null) {
                eModel = Event.dao.findFirstById(model.getInt("eventid"));
                if (eModel != null) {
                    eid = eModel.getInt("id");
                    setSessionAttr("event", eModel);
                    setEventId(eid);
                }
            }
        }
        model = Merchant.dao.findFirstByIdWithOrder(eid, mid);
        if (model != null && eModel == null) {
            eModel = Event.dao.findFirstById(model.getInt("eventid"));
            if (eModel != null) {
                setSessionAttr("event", eModel);
            }
        }

        if (model != null && StringUtils.isNotBlank(model.getStr("pictures"))) {
            String[] pics = model.getStr("pictures").split(",");
            setAttr("sharePic", pics[0]);
        }

        //人气加一
        Merchant.dao.addHits(model.getInt("id"));

        setAttr("eModel", eModel);
        if (eModel.getLong("ended").equals(0L)) {
            VoteCheckUtil voteCheckUtil = new VoteCheckUtil();
            Integer checkResult = voteCheckUtil.voteCheck(eid, getOpenId(), mid);
            setAttr("checkResult", checkResult);
        } else {
            setAttr("checkResult", -2);
        }

        WxConfig wxConfig = WxjsUtil.initWXconfig(getRequest());

        setAttr("wxConfig", wxConfig);

        String captchaAppId= TencentCaptchaUtil.getAppId();
        setAttr("captchaAppId", captchaAppId);
        setAttr("captchaEnabled", TencentCaptchaUtil.enabled);
        setAttr("mModel", model);
        render("index.html");
    }

    /**
     * 按页取数据
     */
    public void list() {
        int pagenum = 1;
        String str = getSearchText();
        if (!isParaBlank("p")) {// 页码
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
        Integer number = 1;
        if (!isParaBlank("no")) {// 选手号
            number = getParaToInt("no");
        }
        Page<Merchant> page = Merchant.dao.queryPage(getEventId(), 1, number);
        setAttr("page", new PageUtil(page));
        render("search.html");
    }


    public void order() {
//        int pagenum = 1;
////        String str = getSearchText();
////        if (!isParaBlank("p")) {// 页码
////            pagenum = getParaToInt("p");
////        }
////        /**
////         * 反馈状态
////         */
////        Integer status = getParaToInt("status");
////
////        Page<Work> page = Work.dao.queryPage(getUserId(),pagenum,str,status );
////        setAttr("page", new PageUtil(page));
////        setAttr("s", str);
//////        setAttr("p", pagenum);
//        if(pagenum==1)
//            render("work.html");
//        else
//            render("workpage.html");
        render("order.html");
    }


    public void prize() {
//        int pagenum = 1;
////        String str = getSearchText();
////        if (!isParaBlank("p")) {// 页码
////            pagenum = getParaToInt("p");
////        }
////        /**
////         * 反馈状态
////         */
////        Integer status = getParaToInt("status");
////
////        Page<Work> page = Work.dao.queryPage(getUserId(),pagenum,str,status );
////        setAttr("page", new PageUtil(page));
////        setAttr("s", str);
//////        setAttr("p", pagenum);
//        if(pagenum==1)
//            render("work.html");
//        else
//            render("workpage.html");
        render("prize.html");
    }

    public void entry() {
//        int pagenum = 1;
////        String str = getSearchText();
////        if (!isParaBlank("p")) {// 页码
////            pagenum = getParaToInt("p");
////        }
////        /**
////         * 反馈状态
////         */
////        Integer status = getParaToInt("status");
////
////        Page<Work> page = Work.dao.queryPage(getUserId(),pagenum,str,status );
////        setAttr("page", new PageUtil(page));
////        setAttr("s", str);
//////        setAttr("p", pagenum);
//        if(pagenum==1)
//            render("work.html");
//        else
//            render("workpage.html");
        render("entry.html");
    }

    public void extend() {
        render("extend.html");
    }


    public void tencentCaptcha() {
        render("tencentCaptcha.html");
    }


    public void captchacheck() {

        ResultObj<String> result = new ResultObj<String>();
        String ip = IpKit.getRealIp(this.getRequest());
        String appId = getPara("appId");
        String ticket = getPara("ticket");
        String randstr = getPara("randstr");
        boolean checkResult = true;
        try {
            checkResult = TencentCaptchaUtil.verifyCaptcha(appId,ticket, randstr, ip);
        } catch (Exception ex) {
            logger.info("验证码错误");
            logger.error(ex);
        }

        renderJson(result);

    }
}
