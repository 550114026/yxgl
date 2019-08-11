package com.site.controller;

import com.jfinal.plugin.activerecord.Page;
import com.vote.model.Merchant;
import com.yqglpt.common.BaseController;
import com.yqglpt.model.Work;
import com.yqglpt.utils.PageUtil;
import org.apache.log4j.Logger;

public class IndexController extends BaseController {

    private static Logger logger = Logger.getLogger(IndexController.class);
    String viewPath="index/";

    public void index() {
        setAttr("page", new PageUtil(queryPage(1)));
        render("index.html");
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
        return Merchant.dao.queryHomeShowPage( pageNumber, 5,1);
    }

}
