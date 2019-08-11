package com.yqglpt.controller;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Formatter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.utils.JsSDKUtil;
import com.yqglpt.common.BaseController;
import com.yqglpt.common.ResultObj;
import com.yqglpt.interceptor.LoginInterceptor;
import com.yqglpt.model.Notice;
import com.yqglpt.model.Signin;
import com.yqglpt.utils.ApiConfigUtils;
import org.apache.log4j.Logger;

@Before(LoginInterceptor.class)
public class SignInController extends BaseController{

	private static Logger logger = Logger.getLogger(SignInController.class);
	
	public void index() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		
		ApiConfigUtils.setThreadLocalApiConfig();
		
		long timestamp = System.currentTimeMillis()/1000;  
	    String randomStr = StringUtils.right(String.valueOf(System.currentTimeMillis()), 10);  
	    String signature=JsSDKUtil.getSignature(String.valueOf(timestamp),randomStr,getRequest());

		setAttr("appId", PropKit.get("appId"));
		setAttr("signature", signature);
		setAttr("timestamp", timestamp);
		setAttr("randomStr", randomStr);
		setAttr("today", new Date());
		render("signin.html");
	}
	
	
	public void list(){
		String date=getPara("date");
		List<Signin> list=Signin.dao.querySiginList(getUserId(), date);
		renderJson(list);
	}
	
	
	
	public void add() throws NoSuchAlgorithmException, UnsupportedEncodingException{
			ApiConfigUtils.setThreadLocalApiConfig();
		long timestamp = System.currentTimeMillis()/1000;  
	    String randomStr = StringUtils.right(String.valueOf(System.currentTimeMillis()), 10);  
	    String signature=JsSDKUtil.getSignature(String.valueOf(timestamp),randomStr,getRequest());

		setAttr("appId", PropKit.get("appId"));
		setAttr("signature", signature);
		setAttr("timestamp", timestamp);
		setAttr("randomStr", randomStr);
		render("signinadd.html");
	}
	

	public void save(){
		Signin signin = getModel(Signin.class, "signin");
		signin.set("userid", Integer.valueOf(getUserId()));
		signin.set("username", getNickName());
		signin.set("signdate", new Date());
		signin.set("signtime", new Date());
		signin.set("version",0);
		ResultObj<String> result = new ResultObj<String>();
		try {
			signin.save();
			result = ResultObj.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			result = ResultObj.newFailInstance("", "系统异常");
		}
		renderJson(result);
	}
}
