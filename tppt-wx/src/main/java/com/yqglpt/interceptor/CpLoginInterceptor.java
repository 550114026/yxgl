package com.yqglpt.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.yqglpt.utils.UserUtil;

public class CpLoginInterceptor implements Interceptor{

	private static Logger logger = Logger.getLogger(CpLoginInterceptor.class);
	@Override
	public void intercept(Invocation ai) {

		HttpServletRequest req = ai.getController().getRequest();
		if(req.getSession().getAttribute(UserUtil.user_Key)==null){
			//跳转到输出未登录地址
			ai.getController().redirect("/index");
			
		}
		
	}
}
