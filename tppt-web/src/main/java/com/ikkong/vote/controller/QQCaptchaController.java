package com.ikkong.vote.controller;

import com.ikkong.core.base.BaseController;
import org.apache.log4j.Logger;

/**
 * @ProjectName: tppt-parent
 * @Package: com.ikkong.vote.controller
 * @ClassName: QQCaptchaController
 * @Author: linjinbiao@bwton.com
 * @Description: ${description}
 * @Date: 2019/6/11 15:44
 * @Version: 1.0
 */
public class QQCaptchaController extends BaseController {
    private static Logger logger = Logger.getLogger(QQCaptchaController.class);

    public void index() {
        render("captcha.html");
    }
}
