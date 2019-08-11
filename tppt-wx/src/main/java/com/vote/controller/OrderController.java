package com.vote.controller;

import com.vote.model.Event;
import com.vote.model.Merchant;
import com.yqglpt.common.BaseController;
import org.apache.log4j.Logger;

import java.util.List;

public class OrderController extends BaseController {

    private static Logger logger = Logger.getLogger(OrderController.class);

    public void index() {
        Integer id = getParaToInt("eid");
        Event eModel = Event.dao.findFirstById(id);
        setSessionAttr("event",eModel);
        List<Merchant> list = Merchant.dao.queryByOrder(id);
        setAttr("eModel", eModel);
        setAttr("list", list);
        render("index.html");
    }

}
