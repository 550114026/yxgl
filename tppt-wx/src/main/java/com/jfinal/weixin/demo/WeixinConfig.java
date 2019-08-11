/**
 * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */

package com.jfinal.weixin.demo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import com.site.controller.*;
import com.vote.controller.*;
import com.vote.controller.EventController;
import com.vote.model.*;
import org.beetl.core.GroupTemplate;

import com.alibaba.druid.filter.stat.MergeStatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.beetl.BeetlRenderFactory;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.template.Engine;
import com.jfinal.weixin.ServletUrlSkipHanlder;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.yqglpt.utils.ApiConfigUtils;


public class WeixinConfig extends JFinalConfig {

	public static final Map<String, String> map = new HashMap<String, String>();
	static String pro="a_little_config_pro.txt";
	static String dev="config.properties";
	static String wechart="wechat_emotion.properties";
	
	/**
	 * 如果生产环境配置文件存在，则优先加载该配置，否则加载开发环境配置文件
	 */
	public static void loadProp() {
		try {
			PropKit.use(pro);
		}
		catch (Exception e) {
			PropKit.use(dev);
		}
		try {
		//	WechatKit.use(wechart);
		}
		catch (Exception e) {
		}
		
	}
	
	
	public static void reloadProp() {
		PropKit.useless(pro);
		PropKit.useless(dev);
		//WechatKit.useless(wechart);
		loadProp();
		
	}
	
	
	public void configConstant(Constants me) {
		try {
			loadPropertyFile(pro);
		}
		catch (Exception e) {
			loadPropertyFile(dev);
		}
		loadProp();
		
		me.setRenderFactory(new BeetlRenderFactory());
		GroupTemplate groupTemplate = BeetlRenderFactory.groupTemplate ;
		Map<String, Object> sharedVars = new HashMap<String, Object>();
		sharedVars.put("startTime", new Date());
		sharedVars.put("basePath", PropKit.get("webSite.basePath", "/"));
		sharedVars.put("mapAk", PropKit.get("baidu.map.ak"));
		//图片服务器地址
		sharedVars.put("imageSite", PropKit.get("imageSite.Url", "/"));
		//图片服务器地址
		sharedVars.put("siteName", PropKit.get("webSite.name", "指动传媒"));
		sharedVars.put("siteUrl", PropKit.get("webSite.Url"));
		sharedVars.put("siteBasePath",PropKit.get("webSite.basePath"));

		groupTemplate.setSharedVars(sharedVars);
//		me.setBaseViewPath("/WEB-INF/pages");
//		me.setViewType(ViewType.JSP);
		me.setEncoding("UTF-8");
		
		me.setDevMode(PropKit.getBoolean("devMode", false));
		
		// ApiConfigKit 设为开发模式可以在开发阶段输出请求交互的 xml 与 json 数据
		ApiConfigKit.setDevMode(me.getDevMode());
		
		
		ApiConfigUtils.setThreadLocalApiConfig();
		
//		ApiConfig apiConfig = new ApiConfig();
//		// 配置微信 API 相关常量
//		apiConfig.setToken(PropKit.get("token"));
//		apiConfig.setAppId(PropKit.get("appId"));
//		apiConfig.setAppSecret(PropKit.get("appSecret"));
//		
//		/**
//		 *  是否对消息进行加密，对应于微信平台的消息加解密方式：
//		 *  1：true进行加密且必须配置 encodingAesKey
//		 *  2：false采用明文模式，同时也支持混合模式
//		 */
//		apiConfig.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
//		apiConfig.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
//		ApiConfigKit.setThreadLocalApiConfig(apiConfig);
	}
	
	public void configRoute(Routes me) {
//		me.add("/binduser", BindUserController.class);
//		me.add("/notice", NoticeController.class);
		//me.add("/msg", WeixinMsgController.class);
		me.add("/api", WeixinApiController.class);
		me.add("/index", IndexController.class,"/site/index");
		me.add("/about", AboutController.class,"/site");
		me.add("/news", NewsController.class,"/site/news");
		me.add("/link", LinkController.class,"/site");
		me.add("/service", ServiceController.class,"/site");
		me.add("/event", EventController.class,"/vote/index");
		me.add("/entry", EntryController.class,"/vote/entry");
		me.add("/mer", MerchantController.class,"/vote/merchant");
		me.add("/merc", MerchantController.class,"/vote/merchant");
		me.add("/merch", MerchantController.class,"/vote/merchant");
		me.add("/mercha", MerchantController.class,"/vote/merchant");
		me.add("/merchan", MerchantController.class,"/vote/merchant");
		me.add("/merchant", MerchantController.class,"/vote/merchant");
		me.add("/complaint", ComplaintController.class,"/vote/complaint");
		me.add("/order", OrderController.class,"/vote/order");
		me.add("/pay", PayController.class,"/vote/index");
		me.add("/file", FileUpload.class);

		

		
		
	}
	
	public void configPlugin(Plugins me) {
		// 增加Druid连接池插件配置
				DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password")
						.trim());
				druidPlugin.addFilter(new MergeStatFilter());
				druidPlugin.setMaxActive(150);
				WallFilter wall = new WallFilter();
				wall.setDbType(PropKit.get("dbType"));
				druidPlugin.addFilter(wall);
				me.add(druidPlugin);

				// 配置ActiveRecord插件
				ActiveRecordPlugin arp = new ActiveRecordPlugin(druidPlugin);
				me.add(arp);

				// 添加映射关系

//				arp.addMapping("tfw_user", User.class);// 用户信息
//				arp.addMapping("tb_tfw_tzgg", Notice.class);//
//				arp.addMapping("glpt_work", Work.class);//
//				arp.addMapping("glpt_signin", Signin.class);//
//				arp.addMapping("glpt_work_feedback", WorkFeedBack.class);//
		arp.addMapping("t_event", Event.class);//
		arp.addMapping("t_merchant", Merchant.class);//
		arp.addMapping("t_vote_user", VoteUser.class);//
		arp.addMapping("t_vote_list", VoteList.class);//
		arp.addMapping("orders", Order.class);//
		arp.addMapping("t_complaint", Complaint.class);//

				

				// 增加spring支持
				//me.add(new SpringPlugin("classpath*:/spring/applicationContext.xml"));
		
		// C3p0Plugin c3p0Plugin = new C3p0Plugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password").trim());
		// me.add(c3p0Plugin);
		
		 EhCachePlugin ecp = new EhCachePlugin();
		 me.add(ecp);
		
		// RedisPlugin redisPlugin = new RedisPlugin("weixin", "127.0.0.1");
		// me.add(redisPlugin);
				
				
		//定时任务
     /*   QuartzPlugin quartz = new QuartzPlugin();
        quartz.setJobs("quartzJob.properties");
        me.add(quartz);*/
	}
	
	public void configInterceptor(Interceptors me) {
		
	}
	
	public void configHandler(Handlers me) {
		//添加排除拦截处理
		me.add(new ServletUrlSkipHanlder());
	}
	
	public void afterJFinalStart() {
		ServletContext sc = JFinal.me().getServletContext();
		// 项目路径
		map.put("realPath", sc.getRealPath("/").replaceFirst("/", ""));
		map.put("contextPath", sc.getContextPath());
		for (Object name : PropKit.getProp().getProperties().keySet()) {
			map.put(name.toString(), prop.getProperties().get(name).toString());
		}
		// 1.5 之后支持redis存储access_token、js_ticket，需要先启动RedisPlugin
//		ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache());
		// 1.6新增的2种初始化
//		ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache(Redis.use("weixin")));
//		ApiConfigKit.setAccessTokenCache(new RedisAccessTokenCache("weixin"));
	}

	public static void main(String[] args) {
		JFinal.start("src/main/webapp/views", 80, "/", 5);
	}


	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub
		
	}
}
