package com.site.controller;

import com.yqglpt.common.BaseController;
import com.yqglpt.controller.WorkController;
import org.apache.log4j.Logger;

public class LinkController extends BaseController {

    private static Logger logger = Logger.getLogger(LinkController.class);
    public void index() {
         render("link.html");
    }
}
