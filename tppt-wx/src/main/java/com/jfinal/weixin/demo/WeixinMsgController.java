///**
// * Copyright (c) 2011-2014, James Zhan 詹波 (jfinal@126.com).
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// */
//
//package com.jfinal.weixin.demo;
//
//import java.util.HashMap;
//
//import org.apache.commons.lang.StringUtils;
//
//import com.jfinal.kit.PropKit;
//import com.jfinal.log.Log;
//import com.jfinal.plugin.ehcache.CacheKit;
//import com.jfinal.weixin.sdk.api.ApiConfig;
//import com.jfinal.weixin.sdk.api.ApiResult;
//import com.jfinal.weixin.sdk.api.CustomServiceApi;
//import com.jfinal.weixin.sdk.api.MediaApi;
//import com.jfinal.weixin.sdk.api.MediaFile;
//import com.jfinal.weixin.sdk.api.UserApi;
//import com.jfinal.weixin.sdk.jfinal.MsgControllerAdapter;
//import com.jfinal.weixin.sdk.msg.in.InImageMsg;
//import com.jfinal.weixin.sdk.msg.in.InLinkMsg;
//import com.jfinal.weixin.sdk.msg.in.InLocationMsg;
//import com.jfinal.weixin.sdk.msg.in.InShortVideoMsg;
//import com.jfinal.weixin.sdk.msg.in.InTextMsg;
//import com.jfinal.weixin.sdk.msg.in.InVideoMsg;
//import com.jfinal.weixin.sdk.msg.in.InVoiceMsg;
//import com.jfinal.weixin.sdk.msg.in.event.InCustomEvent;
//import com.jfinal.weixin.sdk.msg.in.event.InFollowEvent;
//import com.jfinal.weixin.sdk.msg.in.event.InLocationEvent;
//import com.jfinal.weixin.sdk.msg.in.event.InMassEvent;
//import com.jfinal.weixin.sdk.msg.in.event.InMenuEvent;
//import com.jfinal.weixin.sdk.msg.in.event.InQrCodeEvent;
//import com.jfinal.weixin.sdk.msg.in.event.InTemplateMsgEvent;
//import com.jfinal.weixin.sdk.msg.in.speech_recognition.InSpeechRecognitionResults;
//import com.jfinal.weixin.sdk.msg.out.OutCustomMsg;
//import com.jfinal.weixin.sdk.msg.out.OutTextMsg;
//import com.yqglpt.model.User;
//import com.yqglpt.utils.StrUtils;
//import com.yqglpt.utils.UserUtil;
//
//import net.sf.json.JSONObject;
//
///**
// * 将此 DemoController 在YourJFinalConfig 中注册路由， 并设置好weixin开发者中心的 URL 与 token ，使
// * URL 指向该 DemoController 继承自父类 WeixinController 的 index
// * 方法即可直接运行看效果，在此基础之上修改相关的方法即可进行实际项目开发
// */
//public class WeixinMsgController extends MsgControllerAdapter {
//
//	/**
//	 * 客户给客服发消息的最后时间缓存， 客户发消息给客服时（第一次分配不算，第二条消息开始计时） 客服回复客户消息后，计时清零
//	 * 下一条客户消息到达是，判断是否有超时，如果超时，则重新分配客服 主键为客户ID+"_"+客服ID
//	 */
//	public static String msgCacheKey = "lastMsgTime";
//	static Log logger = Log.getLog(WeixinMsgController.class);
//	private static final String helpStr = "\t发送 help 可获得帮助，发送\"视频\" 可获取视频教程，发送 \"美女\" 可看美女，发送 music 可听音乐 ，发送新闻可看JFinal新版本消息。公众号功能持续完善中";
//
//	/**
//	 * 如果要支持多公众账号，只需要在此返回各个公众号对应的 ApiConfig 对象即可 可以通过在请求 url 中挂参数来动态从数据库中获取
//	 * ApiConfig 属性值
//	 */
//	public ApiConfig getApiConfig() {
//		ApiConfig ac = new ApiConfig();
//
//		// 配置微信 API 相关常量
//		ac.setToken(PropKit.get("token"));
//		ac.setAppId(PropKit.get("appId"));
//		ac.setAppSecret(PropKit.get("appSecret"));
//
//		/**
//		 * 是否对消息进行加密，对应于微信平台的消息加解密方式： 1：true进行加密且必须配置 encodingAesKey
//		 * 2：false采用明文模式，同时也支持混合模式
//		 */
//		ac.setEncryptMessage(PropKit.getBoolean("encryptMessage", false));
//		ac.setEncodingAesKey(PropKit.get("encodingAesKey", "setting it in config file"));
//		return ac;
//	}
//
//	/**
//	 * 收到文字消息
//	 */
//	protected void processInTextMsg(InTextMsg inTextMsg) {
//		renderText("", "text/xml");
//
//		if (inTextMsg.getContent().length() < 10) {
//			String key = inTextMsg.getContent();
//			logger.info(key);
//			if (WechatKit.containsKey(key)) {
//				logger.info(WechatKit.get(key));
//				inTextMsg.setContent(WechatKit.get(key));
//			}
//		}
//
//		String content = inTextMsg.getContent();
//		for (Object key : WechatKit.getProp().getProperties().keySet()) {
//			if (key != null) {
//				logger.info(key.toString());
//				content = content.replace(key.toString(), WechatKit.get(key.toString()));
//			}
//		}
//		inTextMsg.setContent(content);
//
//		int msgStatus = 1;
//		// 客服人员ID
//		String supporterId = "";
//		String customerId = inTextMsg.getFromUserName();
//
//
//
//		// 转发给多客服PC客户端
//		// OutCustomMsg outCustomMsg = new OutCustomMsg(inTextMsg);
//		// render(outCustomMsg);
//	}
//
//	/**
//	 * 是否超过了最长等待时间
//	 *
//	 * @param key
//	 * @return
//	 */
//	public Boolean overWaitTime(String key) {
//		Object lastTime = CacheKit.get(msgCacheKey, key);
//		if (lastTime != null) {
//			long lt = Long.valueOf(lastTime.toString());
//			long now = System.currentTimeMillis();
//			long min = (now - lt) / 1000 / 60;
//			//long min = (now - lt) / 1000;
//			logger.info("===========缓存时间间隔:lt="+lt+";now="+now+":"+min+",waitMin时间："+PropKit.getInt("waitMin"));
//			if (min >= PropKit.getInt("waitMin")) {
//				return true;
//			}
//		}
//		else{
//			CacheKit.put(msgCacheKey, key, System.currentTimeMillis());
//		}
//		return false;
//	}
//
//	/**
//	 * 收到图片消息
//	 */
//	protected void processInImageMsg(InImageMsg inImageMsg) {
//		renderText("", "text/xml");
//
//
//		// CustomServiceApi.sendImage( inImageMsg.getFromUserName(),
//		// inImageMsg.getMediaId());
//		// 转发给多客服PC客户端
//		// OutCustomMsg outCustomMsg = new OutCustomMsg(inImageMsg);
//		// render(outCustomMsg);
//	}
//
//	@Override
//	protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
//		// 转发给多客服PC客户端
//		// OutCustomMsg outCustomMsg = new OutCustomMsg(inVoiceMsg);
//		// render(outCustomMsg);
//	}
//
//	@Override
//	protected void processInVideoMsg(InVideoMsg inVideoMsg) {
//		// 转发给多客服PC客户端
//		// OutCustomMsg outCustomMsg = new OutCustomMsg(inVideoMsg);
//		// render(outCustomMsg);
//	}
//
//	@Override
//	protected void processInShortVideoMsg(InShortVideoMsg inShortVideoMsg) {
//		// //转发给多客服PC客户端
//		// OutCustomMsg outCustomMsg = new OutCustomMsg(inShortVideoMsg);
//		// render(outCustomMsg);
//	}
//
//	@Override
//	protected void processInLocationMsg(InLocationMsg inLocationMsg) {
//		// 转发给多客服PC客户端
//		// OutCustomMsg outCustomMsg = new OutCustomMsg(inLocationMsg);
//		// render(outCustomMsg);
//	}
//
//	@Override
//	protected void processInLinkMsg(InLinkMsg inLinkMsg) {
//		// 转发给多客服PC客户端
//		// OutCustomMsg outCustomMsg = new OutCustomMsg(inLinkMsg);
//		// render(outCustomMsg);
//	}
//
//	@Override
//	protected void processInCustomEvent(InCustomEvent inCustomEvent) {
//		logger.debug("测试方法：processInCustomEvent()");
//		renderNull();
//	}
//
//	/**
//	 * 实现父类抽方法，处理关注/取消关注消息
//	 */
//	protected void processInFollowEvent(InFollowEvent inFollowEvent) {
//		if (InFollowEvent.EVENT_INFOLLOW_SUBSCRIBE.equals(inFollowEvent.getEvent())) {
//
//			String openId = inFollowEvent.getFromUserName();
//			UserUtil.saveUser(openId);
//
//			logger.debug("关注：" + inFollowEvent.getFromUserName());
//			OutTextMsg outMsg = new OutTextMsg(inFollowEvent);
//			outMsg.setContent(PropKit.get("subscribe.msg"));
//			render(outMsg);
//		}
//		// 如果为取消关注事件，将无法接收到传回的信息
//		if (InFollowEvent.EVENT_INFOLLOW_UNSUBSCRIBE.equals(inFollowEvent.getEvent())) {
//			logger.debug("取消关注：" + inFollowEvent.getFromUserName());
//			String openId = inFollowEvent.getFromUserName();
//			User user = new User();
//			user.set("id", openId);
//			user.set("status", 0);
//			user.update();
//		}
//	}
//
//	@Override
//	protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {
//		if (InQrCodeEvent.EVENT_INQRCODE_SUBSCRIBE.equals(inQrCodeEvent.getEvent())) {
//			logger.debug("扫码未关注：" + inQrCodeEvent.getFromUserName());
//			OutTextMsg outMsg = new OutTextMsg(inQrCodeEvent);
//			outMsg.setContent(PropKit.get("subscribe.msg"));
//			render(outMsg);
//		}
//		if (InQrCodeEvent.EVENT_INQRCODE_SCAN.equals(inQrCodeEvent.getEvent())) {
//			logger.debug("扫码已关注：" + inQrCodeEvent.getFromUserName());
//			String openId = inQrCodeEvent.getFromUserName();
//			UserUtil.saveUser(openId);
//		}
//	}
//
//	@Override
//	protected void processInLocationEvent(InLocationEvent inLocationEvent) {
//		logger.debug("发送地理位置事件：" + inLocationEvent.getFromUserName());
//		// OutTextMsg outMsg = new OutTextMsg(inLocationEvent);
//		// outMsg.setContent("地理位置是：" + inLocationEvent.getLatitude());
//		// render(outMsg);
//		renderNull();
//	}
//
//	@Override
//	protected void processInMassEvent(InMassEvent inMassEvent) {
//		logger.debug("测试方法：processInMassEvent()");
//		renderNull();
//	}
//
//	/**
//	 * 实现父类抽方法，处理自定义菜单事件
//	 */
//	protected void processInMenuEvent(InMenuEvent inMenuEvent) {
//		logger.debug("菜单事件：" + inMenuEvent.getFromUserName());
//		// OutTextMsg outMsg = new OutTextMsg(inMenuEvent);
//		// outMsg.setContent("菜单事件内容是：" + inMenuEvent.getEventKey());
//		// render(outMsg);
//		renderNull();
//	}
//
//	@Override
//	protected void processInSpeechRecognitionResults(InSpeechRecognitionResults inSpeechRecognitionResults) {
//		logger.debug("语音识别事件：" + inSpeechRecognitionResults.getFromUserName());
//		// OutTextMsg outMsg = new OutTextMsg(inSpeechRecognitionResults);
//		// outMsg.setContent("语音识别内容是：" +
//		// inSpeechRecognitionResults.getRecognition());
//		// render(outMsg);
//		renderNull();
//	}
//
//	@Override
//	protected void processInTemplateMsgEvent(InTemplateMsgEvent inTemplateMsgEvent) {
//		logger.debug("测试方法：processInTemplateMsgEvent()");
//		renderNull();
//	}
//
//	// /**
//	// * 实现父类抽方法，处理文本消息
//	// * 本例子中根据消息中的不同文本内容分别做出了不同的响应，同时也是为了测试 jfinal weixin sdk的基本功能：
//	// * 本方法仅测试了 OutTextMsg、OutNewsMsg、OutMusicMsg 三种类型的OutMsg，
//	// * 其它类型的消息会在随后的方法中进行测试
//	// */
//	// protected void processInTextMsg(InTextMsg inTextMsg) {
//	// String msgContent = inTextMsg.getContent().trim();
//	// // 帮助提示
//	// if ("help".equalsIgnoreCase(msgContent) || "帮助".equals(msgContent)) {
//	// OutTextMsg outMsg = new OutTextMsg(inTextMsg);
//	// outMsg.setContent(helpStr);
//	// render(outMsg);
//	// }
//	// // 图文消息测试
//	// else if ("news".equalsIgnoreCase(msgContent) ||
//	// "新闻".equalsIgnoreCase(msgContent)) {
//	// OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);
//	// outMsg.addNews("JFinal 2.0 发布,JAVA 极速 WEB+ORM 框架", "本星球第一个极速开发框架",
//	// "https://mmbiz.qlogo.cn/mmbiz/KJoUl0sqZFS0fRW68poHoU3v9ulTWV8MgKIduxmzHiamkb3yHia8pCicWVMCaFRuGGMnVOPrrj2qM13u9oTahfQ9A/0?wx_fmt=png",
//	// "http://mp.weixin.qq.com/s?__biz=MzA4NjM4Mjk2Mw==&mid=211063163&idx=1&sn=87d54e2992237a3f791f08b5cdab7990#rd");
//	// outMsg.addNews("JFinal 1.8 发布,JAVA 极速 WEB+ORM 框架", "现在就加入 JFinal
//	// 极速开发世界，节省更多时间去跟女友游山玩水 ^_^",
//	// "http://mmbiz.qpic.cn/mmbiz/zz3Q6WSrzq1ibBkhSA1BibMuMxLuHIvUfiaGsK7CC4kIzeh178IYSHbYQ5eg9tVxgEcbegAu22Qhwgl5IhZFWWXUw/0",
//	// "http://mp.weixin.qq.com/s?__biz=MjM5ODAwOTU3Mg==&mid=200313981&idx=1&sn=3bc5547ba4beae12a3e8762ababc8175#rd");
//	// outMsg.addNews("JFinal 1.6 发布,JAVA 极速 WEB+ORM 框架", "JFinal 1.6 主要升级了
//	// ActiveRecord 插件，本次升级全面支持多数源、多方言、多缓",
//	// "http://mmbiz.qpic.cn/mmbiz/zz3Q6WSrzq0fcR8VmNCgugHXv7gVlxI6w95RBlKLdKUTjhOZIHGSWsGvjvHqnBnjIWHsicfcXmXlwOWE6sb39kA/0",
//	// "http://mp.weixin.qq.com/s?__biz=MjM5ODAwOTU3Mg==&mid=200121522&idx=1&sn=ee24f352e299b2859673b26ffa4a81f6#rd");
//	// render(outMsg);
//	// }
//	// // 音乐消息测试
//	// else if ("music".equalsIgnoreCase(msgContent) || "音乐".equals(msgContent))
//	// {
//	// OutMusicMsg outMsg = new OutMusicMsg(inTextMsg);
//	// outMsg.setTitle("When The Stars Go Blue-Venke Knutson");
//	// outMsg.setDescription("建议在 WIFI 环境下流畅欣赏此音乐");
//	// outMsg.setMusicUrl("http://www.jfinal.com/When_The_Stars_Go_Blue-Venke_Knutson.mp3");
//	// outMsg.setHqMusicUrl("http://www.jfinal.com/When_The_Stars_Go_Blue-Venke_Knutson.mp3");
//	// outMsg.setFuncFlag(true);
//	// render(outMsg);
//	// }
//	// else if ("美女".equalsIgnoreCase(msgContent)) {
//	// OutNewsMsg outMsg = new OutNewsMsg(inTextMsg);
//	// outMsg.addNews(
//	// "JFinal 宝贝更新喽",
//	// "jfinal 宝贝更新喽，我们只看美女 ^_^",
//	// "https://mmbiz.qlogo.cn/mmbiz/KJoUl0sqZFRHa3VrmibqAXRfYPNdiamFnpPTOvXoxsFlRoOHbVibGhmHOEUQiboD3qXWszKuzWpibFxsVW1RmNB9hPw/0?wx_fmt=jpeg",
//	// "http://mp.weixin.qq.com/s?__biz=MzA4NjM4Mjk2Mw==&mid=211356950&idx=1&sn=6315a1a2848aa8cb0694bf1f4accfb07#rd");
//	// // outMsg.addNews("秀色可餐", "JFinal Weixin 极速开发就是这么爽，有木有 ^_^",
//	// "http://mmbiz.qpic.cn/mmbiz/zz3Q6WSrzq2GJLC60ECD7rE7n1cvKWRNFvOyib4KGdic3N5APUWf4ia3LLPxJrtyIYRx93aPNkDtib3ADvdaBXmZJg/0",
//	// "http://mp.weixin.qq.com/s?__biz=MjM5ODAwOTU3Mg==&mid=200987822&idx=1&sn=7eb2918275fb0fa7b520768854fb7b80#rd");
//	//
//	// render(outMsg);
//	// }
//	// else if ("视频教程".equalsIgnoreCase(msgContent) ||
//	// "视频".equalsIgnoreCase(msgContent)) {
//	// renderOutTextMsg("\thttp://pan.baidu.com/s/1nt2zAT7 \t密码:824r");
//	// }
//	// // 其它文本消息直接返回原值 + 帮助提示
//	// else {
//	// renderOutTextMsg("\t文本消息已成功接收，内容为： " + inTextMsg.getContent() + "\n\n" +
//	// helpStr);
//	// }
//	// }
//	//
//	// /**
//	// * 实现父类抽方法，处理图片消息
//	// */
//	// protected void processInImageMsg(InImageMsg inImageMsg) {
//	// OutImageMsg outMsg = new OutImageMsg(inImageMsg);
//	// // 将刚发过来的图片再发回去
//	// outMsg.setMediaId(inImageMsg.getMediaId());
//	// render(outMsg);
//	// }
//	//
//	// /**
//	// * 实现父类抽方法，处理语音消息
//	// */
//	// protected void processInVoiceMsg(InVoiceMsg inVoiceMsg) {
//	// OutVoiceMsg outMsg = new OutVoiceMsg(inVoiceMsg);
//	// // 将刚发过来的语音再发回去
//	// outMsg.setMediaId(inVoiceMsg.getMediaId());
//	// render(outMsg);
//	// }
//	//
//	// /**
//	// * 实现父类抽方法，处理视频消息
//	// */
//	// protected void processInVideoMsg(InVideoMsg inVideoMsg) {
//	// /* 腾讯 api 有 bug，无法回复视频消息，暂时回复文本消息代码测试
//	// OutVideoMsg outMsg = new OutVideoMsg(inVideoMsg);
//	// outMsg.setTitle("OutVideoMsg 发送");
//	// outMsg.setDescription("刚刚发来的视频再发回去");
//	// // 将刚发过来的视频再发回去，经测试证明是腾讯官方的 api 有 bug，待 api bug 却除后再试
//	// outMsg.setMediaId(inVideoMsg.getMediaId());
//	// render(outMsg);
//	// */
//	// OutTextMsg outMsg = new OutTextMsg(inVideoMsg);
//	// outMsg.setContent("\t视频消息已成功接收，该视频的 mediaId 为: " +
//	// inVideoMsg.getMediaId());
//	// render(outMsg);
//	// }
//	//
//	// @Override
//	// protected void processInShortVideoMsg(InShortVideoMsg inShortVideoMsg)
//	// {
//	// OutTextMsg outMsg = new OutTextMsg(inShortVideoMsg);
//	// outMsg.setContent("\t视频消息已成功接收，该视频的 mediaId 为: " +
//	// inShortVideoMsg.getMediaId());
//	// render(outMsg);
//	// }
//	//
//	// /**
//	// * 实现父类抽方法，处理地址位置消息
//	// */
//	// protected void processInLocationMsg(InLocationMsg inLocationMsg) {
//	// OutTextMsg outMsg = new OutTextMsg(inLocationMsg);
//	// outMsg.setContent("已收到地理位置消息:" +
//	// "\nlocation_X = " + inLocationMsg.getLocation_X() +
//	// "\nlocation_Y = " + inLocationMsg.getLocation_Y() +
//	// "\nscale = " + inLocationMsg.getScale() +
//	// "\nlabel = " + inLocationMsg.getLabel());
//	// render(outMsg);
//	// }
//	//
//	// /**
//	// * 实现父类抽方法，处理链接消息
//	// * 特别注意：测试时需要发送我的收藏中的曾经收藏过的图文消息，直接发送链接地址会当做文本消息来发送
//	// */
//	// protected void processInLinkMsg(InLinkMsg inLinkMsg) {
//	// OutNewsMsg outMsg = new OutNewsMsg(inLinkMsg);
//	// outMsg.addNews("链接消息已成功接收",
//	// "链接使用图文消息的方式发回给你，还可以使用文本方式发回。点击图文消息可跳转到链接地址页面，是不是很好玩 :)" ,
//	// "http://mmbiz.qpic.cn/mmbiz/zz3Q6WSrzq1ibBkhSA1BibMuMxLuHIvUfiaGsK7CC4kIzeh178IYSHbYQ5eg9tVxgEcbegAu22Qhwgl5IhZFWWXUw/0",
//	// inLinkMsg.getUrl());
//	// render(outMsg);
//	// }
//	//
//	// @Override
//	// protected void processInCustomEvent(InCustomEvent inCustomEvent)
//	// {
//	// System.out.println("processInCustomEvent() 方法测试成功");
//	// }
//	//
//	// /**
//	// * 实现父类抽方法，处理关注/取消关注消息
//	// */
//	// protected void processInFollowEvent(InFollowEvent inFollowEvent) {
//	// OutTextMsg outMsg = new OutTextMsg(inFollowEvent);
//	// outMsg.setContent("感谢关注 JFinal Weixin 极速开发服务号，为您节约更多时间，去陪恋人、家人和朋友 :)
//	// \n\n\n " + helpStr);
//	// // 如果为取消关注事件，将无法接收到传回的信息
//	// render(outMsg);
//	// }
//	//
//	// /**
//	// * 实现父类抽方法，处理扫描带参数二维码事件
//	// */
//	// protected void processInQrCodeEvent(InQrCodeEvent inQrCodeEvent) {
//	// OutTextMsg outMsg = new OutTextMsg(inQrCodeEvent);
//	// outMsg.setContent("processInQrCodeEvent() 方法测试成功");
//	// render(outMsg);
//	// }
//	//
//	// /**
//	// * 实现父类抽方法，处理上报地理位置事件
//	// */
//	// protected void processInLocationEvent(InLocationEvent inLocationEvent) {
//	// OutTextMsg outMsg = new OutTextMsg(inLocationEvent);
//	// outMsg.setContent("processInLocationEvent() 方法测试成功");
//	// render(outMsg);
//	// }
//	//
//	// @Override
//	// protected void processInMassEvent(InMassEvent inMassEvent)
//	// {
//	// System.out.println("processInMassEvent() 方法测试成功");
//	// }
//	//
//	// /**
//	// * 实现父类抽方法，处理自定义菜单事件
//	// */
//	// protected void processInMenuEvent(InMenuEvent inMenuEvent) {
//	// renderOutTextMsg("processInMenuEvent() 方法测试成功");
//	// }
//	//
//	// /**
//	// * 实现父类抽方法，处理接收语音识别结果
//	// */
//	// protected void
//	// processInSpeechRecognitionResults(InSpeechRecognitionResults
//	// inSpeechRecognitionResults) {
//	// renderOutTextMsg("语音识别结果： " +
//	// inSpeechRecognitionResults.getRecognition());
//	// }
//	//
//	// // 处理接收到的模板消息是否送达成功通知事件
//	// protected void processInTemplateMsgEvent(InTemplateMsgEvent
//	// inTemplateMsgEvent) {
//	// String status = inTemplateMsgEvent.getStatus();
//	// renderOutTextMsg("模板消息是否接收成功：" + status);
//	// }
//	// @Override
//	// protected void
//	// processInShakearoundUserShakeEvent(InShakearoundUserShakeEvent
//	// inShakearoundUserShakeEvent) {
//	// logger.debug("摇一摇周边设备信息通知事件：" +
//	// inShakearoundUserShakeEvent.getFromUserName());
//	// OutTextMsg outMsg = new OutTextMsg(inShakearoundUserShakeEvent);
//	// outMsg.setContent("摇一摇周边设备信息通知事件UUID：" +
//	// inShakearoundUserShakeEvent.getUuid());
//	// render(outMsg);
//	// }
//	//
//	// @Override
//	// protected void processInVerifySuccessEvent(InVerifySuccessEvent
//	// inVerifySuccessEvent) {
//	// logger.debug("资质认证成功通知事件：" + inVerifySuccessEvent.getFromUserName());
//	// OutTextMsg outMsg = new OutTextMsg(inVerifySuccessEvent);
//	// outMsg.setContent("资质认证成功通知事件：" + inVerifySuccessEvent.getExpiredTime());
//	// render(outMsg);
//	// }
//	//
//	// @Override
//	// protected void processInVerifyFailEvent(InVerifyFailEvent
//	// inVerifyFailEvent){
//	// logger.debug("资质认证失败通知事件：" + inVerifyFailEvent.getFromUserName());
//	// OutTextMsg outMsg = new OutTextMsg(inVerifyFailEvent);
//	// outMsg.setContent("资质认证失败通知事件：" + inVerifyFailEvent.getFailReason());
//	// render(outMsg);
//	// }
//}
