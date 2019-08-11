package com.site.controller;

import com.yqglpt.common.BaseController;
import com.yqglpt.controller.WorkController;
import org.apache.log4j.Logger;

public class AboutController extends BaseController {

    private static Logger logger = Logger.getLogger(WorkController.class);
    public void index() {
         render("about.html");
    }

}
