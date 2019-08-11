package com.site.controller;

import com.yqglpt.common.BaseController;
import com.yqglpt.controller.WorkController;
import org.apache.log4j.Logger;

public class ServiceController extends BaseController {

    private static Logger logger = Logger.getLogger(ServiceController.class);
    public void index() {
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
         render("service.html");
    }
}
