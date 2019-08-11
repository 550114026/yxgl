package com.vote.controller;

import com.vote.model.Event;
import com.vote.model.Merchant;
import com.yqglpt.common.BaseController;
import com.yqglpt.common.ResultObj;
import org.apache.log4j.Logger;

import java.util.Date;

public class EntryController extends BaseController {

    private static Logger logger = Logger.getLogger(EntryController.class);

    public void index() {
        Integer id = getParaToInt("eid");
        Event eModel = Event.dao.findFirstById(id);
        setAttr("eModel", eModel);
        render("entry.html");
    }


    /**
     * 保存
     */
    public void save() {
        Merchant merchant = getModel(Merchant.class, "merchant");
        ResultObj<String> result = new ResultObj<String>();
        Integer canEntry = Merchant.dao.canEntry(getOpenId(), merchant.getInt("eventid"));
        if (canEntry == 0) {
            result = ResultObj.newFailInstance("", "超过提交限制");
        } else {
            merchant.set("create_time", new Date());
            merchant.set("source", "2");
            merchant.set("status", 1);
            merchant.set("audit_status", 0);
            merchant.set("version", 0);
            merchant.set("totlevotes", 0);
            merchant.set("vipvotes", 0);
            merchant.set("hits", 0);
            merchant.set("openid", getOpenId());
            try {
                merchant.save();
                result = ResultObj.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                result = ResultObj.newFailInstance("", "系统异常");
            }
        }
        renderJson(result);
    }

}
