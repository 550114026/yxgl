package com.yqglpt.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.UserApi;
import com.yqglpt.model.User;

public class UserUtil {
	
	public static String user_Key="honzh_user";
	
	public static User getUser(HttpServletRequest request){
		User user = (User)request.getSession().getAttribute(user_Key);
		if(user==null)
		{
			return null;
//			Date now=new Date();
//			user=new User();
//			user.set("id",String.valueOf(now.getTime()));
//			user.set("nick_name",String.valueOf(now.getTime()));
//			request.getSession().setAttribute("honzh_user",user);
		}
		return user;
	}
	
	public static User saveUserWidthCheck(String openId){
		User user= User.dao.findLoginUser(openId);
		if(user!=null&&user.get("id")!=null){
				if(user.get("status").equals("0"))
					user.delete();
				else
					return user;					
		}
		return getUserInfo(openId);
	}
	
	public static User saveUser(String openId){
		//删除重新插入
		User user= User.dao.findLoginUser(openId);
		if(user!=null&&user.get("id")!=null){
				user.delete();
		}
		return getUserInfo(openId);
	}

	public static User getUserInfo(String openId) {
		User user;
		// 记录用户信息
		ApiResult apiResult = UserApi.getUserInfo(openId);
		user = new User();
		user.set("id", openId);
		user.set("account", apiResult.getStr("nickname"));
		user.set("nick_name", apiResult.getStr("nickname"));
		switch(apiResult.getInt("sex")){
		case 1:
			user.set("sex","男");
			break;
		case 2:
			user.set("sex","女");
			break;
		default:
			user.set("sex","未知");
			break;
		}
		user.set("city", apiResult.getStr("city"));
		user.set("country", apiResult.getStr("country"));
		user.set("province", apiResult.getStr("province"));
		user.set("headimgurl", apiResult.getStr("headimgurl"));
		user.set("user_type", 3);
		user.save();
		return user;
	}

}
