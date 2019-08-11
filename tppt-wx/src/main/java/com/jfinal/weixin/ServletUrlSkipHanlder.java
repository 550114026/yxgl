package com.jfinal.weixin;

import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

public class ServletUrlSkipHanlder extends Handler {
	
	public static final HashSet<String> servletSet = new HashSet<String>(){{
	      add("/message");
	   }
	};
	
	@SuppressWarnings("deprecation")
	public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled){
	    if (servletSet.contains(target)){
	        return;
	    }else{
	        nextHandler.handle(target,request,response,isHandled);
	    }
	}
}
