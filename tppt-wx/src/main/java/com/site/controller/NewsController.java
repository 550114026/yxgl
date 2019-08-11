package com.site.controller;

import com.jfinal.plugin.activerecord.Page;
import com.vote.model.Event;
import com.vote.model.Merchant;
import com.yqglpt.common.BaseController;
import com.yqglpt.controller.WorkController;
import com.yqglpt.utils.PageUtil;
import org.apache.log4j.Logger;

public class NewsController extends BaseController {

    private static Logger logger = Logger.getLogger(NewsController.class);
    public void index() {
        int pagenum = 1;
        String str = getSearchText();
        if (!isParaBlank("p")) {// 页码
            pagenum = getParaToInt("p");
        }
        setAttr("page", new PageUtil(queryPage(pagenum)));
         render("news.html");
    }

    /**
     * 按页取数据
     */
    public void list() {
        int pagenum = 1;
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
        return Merchant.dao.queryHomeShowPage( pageNumber, 10,0);
    }

    public void content(){
        Integer mid=getParaToInt("mid");
        Merchant merchant=Merchant.dao.queryMerchantWidthOrder(mid);
        setAttr("merchant",merchant);
//        if(merchant!=null) {
//            Event event=Event.dao.findById(merchant.getInt("eventid")) ;
//            setAttr("event",event);
//        }
        render("content.html");

    }
}
