package com.yqglpt.controller;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.yqglpt.common.ResultObj;
import com.yqglpt.model.User;
import com.yqglpt.utils.ShiroKit;

/**
 * 绑定用户
 * @author Administrator
 *
 */
public class BindUserController extends Controller{
	private static Logger logger = Logger.getLogger(BindUserController.class);

	public void index() {
		logger.info("openid:"+getSessionAttr("openid"));
		logger.info("nickname:"+getSessionAttr("nickname"));
		logger.info("headimgurl:"+getSessionAttr("headimgurl"));
		setAttr("openid", getSessionAttr("openid"));
		setAttr("nickname", getSessionAttr("nickname"));
		setAttr("headimgurl", getSessionAttr("headimgurl"));
		setAttr("url", getPara("url"));
		render("bind.html");
	}
	
	public void savebind(){
		ResultObj<String> result = ResultObj.newInstance();
		String account=getPara("account").trim();
		String password=getPara("password").trim();
		User user=User.dao.findUserByAccount(account);
		if(user!=null){
			String salt=user.getStr("SALT");
			if(ShiroKit.md5(password, salt).equals(user.getStr("PASSWORD"))){
				user.set("openid", getSessionAttr("openid").toString());
				user.set("nickname", getSessionAttr("nickname").toString());
				user.set("headimgurl", getSessionAttr("headimgurl").toString());
				User.dao.bindUser(user.getInt("ID"), getSessionAttr("openid").toString(), getSessionAttr("nickname").toString(), getSessionAttr("headimgurl").toString());
				setSessionAttr("honzh_user", user);
			}
			else{
				result=ResultObj.newFailInstance("-1","帐号/密码错误");
			}
		}else{
			result=ResultObj.newFailInstance("-2","帐号/密码错误");
		}
		
		renderJson(result);
	}
}
