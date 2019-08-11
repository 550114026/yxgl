package com.yqglpt.interceptor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import com.vote.util.ApiConfigUtil;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.alibaba.druid.support.json.JSONUtils;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.ApiConfig;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.SnsAccessToken;
import com.jfinal.weixin.sdk.api.SnsAccessTokenApi;
import com.jfinal.weixin.sdk.api.UserApi;
import com.yqglpt.model.User;

/**
 * LoginInterceptor.
 */
public class LoginInterceptor implements Interceptor {

	private static Logger logger = Logger.getLogger(LoginInterceptor.class);
	private static ApiConfig apiConfig=null;
	


	/**
	 * 1.判断是是否存在session 2.如果1.不成立，判断受否存在code 2.1
	 * 有code，则到数据库查询code是否存在，如果不存在则新增用户，并作登录
	 * 2.2不存在code，则跳转到微信授权页面，并将当前的请求地址设置为跳转地址
	 */
	@Override
	public void intercept(Invocation ai) {
		User user = ai.getController().getSessionAttr("honzh_user");

		HttpServletRequest req = ai.getController().getRequest();
		/*if(user==null){
			String siteUrl = PropKit.get("webSite.Url")+req.getRequestURI().toString();
			if (!StringUtils.isEmpty(req.getQueryString()))
				siteUrl = siteUrl + "?" + req.getQueryString();
			try {
				siteUrl = URLEncoder.encode(siteUrl, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				logger.info("UnsupportedEncodingException:"+e.getMessage());
			}
			//跳转到账号绑定页面澀
			String Url="/binduser?url="+siteUrl;
			logger.info("跳转到绑定用户地址："+siteUrl);
			ai.getController().redirect(Url);
			return;
		}*/
		
		if(PropKit.getBoolean("initMode")){
			user = User.dao.findUserByAccount("admin");
			//user = User.dao.findLoginUser("ogCfAwdenjG2AHKOKFvTzlfCtXog");
			//user = User.dao.findLoginUser("ogCfAwTZmWL0geAomZA-sxqU_a7Y");
			//user = User.dao.findLoginUser("ogCfAwajyKU-pf7n_r7Awcf7HiUU");
			if (user != null) {
				ai.getController().setSessionAttr("honzh_user", user);
				ai.invoke();
				return ;
			}
		}
		ApiConfigKit.setThreadLocalApiConfig(ApiConfigUtil.getApiConfig()) ;
		
		apiConfig=ApiConfigKit.getApiConfig();
		if (user == null) {
			logger.info("用户未登录，获取code");
			String code = ai.getController().getPara("code");
			logger.info("获取code:" + code);
			// 请求是否有openid
			if (!StringUtils.isEmpty(code)) {
				SnsAccessToken accessToken=SnsAccessTokenApi.getSnsAccessToken(apiConfig.getAppId(), apiConfig.getAppSecret(), code);
				String openId=accessToken.getOpenid();
				logger.info("获取openId:" + openId);
				// 根据openId到数据库取用户信息
				user = User.dao.findLoginUser(openId);
				if (user != null) {
					ai.getController().setSessionAttr("honzh_user", user);
					ai.invoke();
				} else {

					logger.info("根据openid获取用户信息openId："+openId);
					// 第一次登录用户，跳转到账号绑定页面
					ApiResult apiResult = UserApi.getUserInfo(openId);
					ai.getController().setSessionAttr("openid", openId);
					ai.getController().setSessionAttr("nickname", apiResult.getStr("nickname"));
					ai.getController().setSessionAttr("headimgurl", apiResult.getStr("headimgurl"));

					logger.info("根据openid获取用户信息apiResult："+openId+apiResult.getStr("nickname")+apiResult.getStr("headimgurl"));
					
					String siteUrl = PropKit.get("webSite.Url")+req.getRequestURI().toString();
					if (!StringUtils.isEmpty(req.getQueryString())) {
						siteUrl = siteUrl + "?" + req.getQueryString();
					}
					try {
						siteUrl = URLEncoder.encode(siteUrl, "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						logger.info("UnsupportedEncodingException:"+e.getMessage());
					}

					ai.getController().setSessionAttr("url", siteUrl);
					//跳转到账号绑定页面澀
					String Url=PropKit.get("webSite.Url")+PropKit.get("webSite.basePath")+"/binduser/index?url="+siteUrl;
					logger.info("跳转到绑定用户yemi："+Url);
					ai.getController().redirect(Url);
//					if (user.save())
//						ai.getController().setSessionAttr("honzh_user", user);
				}
			} else {
				logger.info("OpenId未注册，跳转到微信服务器获取用户信息");
				//String Url = PropKit.get("weChart.oauth2");
				// 本网站前缀
				String siteUrl = PropKit.get("webSite.Url")+req.getRequestURI().toString();
				if (!StringUtils.isEmpty(req.getQueryString())) {
					siteUrl = siteUrl + "?" + req.getQueryString();
				}
				try {
					siteUrl = URLEncoder.encode(siteUrl, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					logger.info("UnsupportedEncodingException:"+e.getMessage());
				}
				// 微信接口地址
				String Url=SnsAccessTokenApi.getAuthorizeURL(apiConfig.getAppId(),siteUrl,true);
				
				logger.info("跳转地址：" + Url);
				ai.getController().redirect(Url);
			}
		} else {
			ai.invoke();
		}
	}
}
