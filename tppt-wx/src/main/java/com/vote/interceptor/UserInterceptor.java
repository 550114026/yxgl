package com.vote.interceptor;

import com.alibaba.druid.util.Base64;
import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.PropKit;
import com.jfinal.weixin.sdk.api.*;
import com.jfinal.weixin.sdk.kit.IpKit;
import com.vote.model.VoteUser;
import com.vote.util.EmojiFilter;
import com.yqglpt.model.User;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * LoginInterceptor.
 */
public class UserInterceptor implements Interceptor {

	private static Logger logger = Logger.getLogger(UserInterceptor.class);
	private static ApiConfig apiConfig=null;
	
	/**
	 * 如果要支持多公众账号，只需要在此返回各个公众号对应的  ApiConfig 对象即可
	 * 可以通过在请求 url 中挂参数来动态从数据库中获取 ApiConfig 属性值
	 */
	public ApiConfig getApiConfig() {
		ApiConfig ac = new ApiConfig();
		
		// 配置微信 API 相关常量
		ac.setToken(PropKit.get("token"));
		ac.setAppId(PropKit.get("appId"));
		ac.setAppSecret(PropKit.get("appSecret"));
		
		/**
		 *  是否对消息进行加密，对应于微信平台的消息加解密方式：
		 *  1：true进行加密且必须配置 encodingAesKey
		 *  2：false采用明文模式，同时也支持混合模式
		 */
		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
		return ac;
	}

	/**
	 * 1.判断是是否存在session 2.如果1.不成立，判断受否存在code 2.1
	 * 有code，则到数据库查询code是否存在，如果不存在则新增用户，并作登录
	 * 2.2不存在code，则跳转到微信授权页面，并将当前的请求地址设置为跳转地址
	 */
	@Override
	public void intercept(Invocation ai) {
		VoteUser user = ai.getController().getSessionAttr("vote_user");

		HttpServletRequest req = ai.getController().getRequest();

		
		if(PropKit.getBoolean("initMode")){
			user = VoteUser.dao.findUserByOpenid("oIQFv1XR-n6fB_DqHv-qr2jhxzIc");
			//user = User.dao.findLoginUser("ogCfAwdenjG2AHKOKFvTzlfCtXog");
			//user = User.dao.findLoginUser("ogCfAwTZmWL0geAomZA-sxqU_a7Y");
			//user = User.dao.findLoginUser("ogCfAwajyKU-pf7n_r7Awcf7HiUU");
			if (user != null) {
				ai.getController().setSessionAttr("vote_user", user);
				ai.invoke();
				return ;
			}
		}
		ApiConfigKit.setThreadLocalApiConfig(getApiConfig()) ;
		
		apiConfig=ApiConfigKit.getApiConfig();
		if (user == null|| org.apache.commons.lang3.StringUtils.isEmpty(user.getStr("openid"))) {
			logger.debug("用户未登录，获取code");
			String code = ai.getController().getPara("code");
			logger.debug("获取code:" + code);
			// 请求是否有openid
			if (!StringUtils.isEmpty(code)) {
				SnsAccessToken accessToken=SnsAccessTokenApi.getSnsAccessToken(apiConfig.getAppId(), apiConfig.getAppSecret(), code);
				String openId=accessToken.getOpenid();
				logger.debug("accessToken:" + JSON.toJSONString(accessToken));
				logger.debug("获取openId:" + openId);
				// 根据openId到数据库取用户信息
				user = VoteUser.dao.findUserByOpenid(openId);
				if (user != null) {
					ai.getController().setSessionAttr("vote_user", user);
					ai.invoke();
				} else {
					user=new VoteUser();
					user.set("openid",openId);
					logger.debug("根据openid获取用户信息openId："+openId);
					// 第一次登录用户，保存用户信息
					ApiResult apiResult=SnsApi.getUserInfo(accessToken.getAccessToken(),accessToken.getOpenid());
					if(apiResult!=null){
						logger.debug("根据openid获取用户信息apiResult："+JSON.toJSONString(apiResult));
						user.set("nickname",EmojiFilter.filterEmoji2(apiResult.getStr("nickname")));
						user.set("headimgurl",apiResult.getStr("headimgurl"));
						Integer sex=apiResult.getInt("sex");
						user.set("sex",sex);
						user.set("province",apiResult.getStr("province"));
						user.set("city",apiResult.getStr("city"));
						user.set("country",apiResult.getStr("country"));
						//补充活动ID
						int eventid = ai.getController().getParaToInt("eid",0);
						if(eventid!=0){
							user.set("eventid",eventid);
						}
					}
					user.set("ip",IpKit.getRealIp(req));
					user.save();
					ai.getController().setSessionAttr("vote_user", user);
					ai.invoke();
				}
			} else {
				logger.debug("OpenId未注册，跳转到微信服务器获取用户信息");
				//String Url = PropKit.get("weChart.oauth2");
				// 本网站前缀
				String siteUrl = PropKit.get("webSite.Url")+req.getRequestURI().toString();
				if (!StringUtils.isEmpty(req.getQueryString()))
					siteUrl = siteUrl + "?" + req.getQueryString();
				try {
					siteUrl = URLEncoder.encode(siteUrl, "utf-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					logger.debug("UnsupportedEncodingException:"+e.getMessage());
				}
				// 微信接口地址
				String Url=SnsAccessTokenApi.getAuthorizeURL(apiConfig.getAppId(),siteUrl,false);
				
				logger.debug("跳转地址：" + Url);
				ai.getController().getResponse();
				ai.getController().redirect(Url);
			}
		} else {
			ai.invoke();
		}
	}
}
