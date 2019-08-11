package com.yqglpt.interceptor;
//package com.bookhouse.interceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import com.ai.ctos.cas.interceptor.Constants;
//
//
///**
// * 对于配置的URL（微信的菜单），获取微信用户信息到session中
// * Title: AIDP_SMARTOS <br>
// * Description: <br>
// * Date: 2016年10月23日 <br>
// * Copyright (c) 2016 AIDP <br>
// * 
// * @author wj
// */
//public class WeixinMenuInterceptor  extends HandlerInterceptorAdapter{
//    private static final Logger logger = LoggerFactory.getLogger(WeixinMenuInterceptor.class);
//    
//    
//    @Autowired
//    protected WxMpConfigStorage configStorage;
//    @Autowired
//    protected WxMpService wxMpService;
//    @Autowired
//    protected CoreService coreService;
//    
//    @Autowired
//    protected OAuth2AccessTokenCacheService oAuth2AccessTokenCacheSvc;
//    
//    @Override    
//    public boolean preHandle(HttpServletRequest request,    
//            HttpServletResponse response, Object handler) throws Exception {    
//        
//        //var isWechat = u.indexOf('wechat') > -1 || u.indexOf('MicroMessenger') > -1; 
//    	String refererUrl=request.getHeader(HmbConsts.ASIAINFO_REFERER);
//    	String newLogin=request.getHeader("newLogin");
//    	//System.out.println(refererUrl);
//    	request.getSession().setAttribute("contextPath", request.getContextPath());
//    	request.getSession().setAttribute("tokenParam", Constants.APP_WECHART_USER_TOKEN_PARAM);
//    	
//		if(StringUtils.isNotEmpty(refererUrl)&&!"true".equalsIgnoreCase(newLogin)){
//		   request.getSession().setAttribute(HmbConsts.SESSION_REFERER_URL, refererUrl);
//		}
//		
//         String userAgent = request.getHeader("User-Agent");
//        logger.debug("用户使用的浏览器为： {}",userAgent);
//        if(StringKit.notBlank(userAgent) &&
//              (  userAgent.contains("wechat")  || userAgent.contains("MicroMessenger") )  ){
//            logger.debug("微信客户端访问，需要获取微信用户信息");
//        }else{
//            return true; // 不是微信客户端，这里就直接略过了
//        }
//        
//        //logger.info("==============执行顺序: 1、preHandle================");    
//        String requestUri = request.getRequestURI();  
//        String contextPath = request.getContextPath();  
//        String url = requestUri.substring(contextPath.length());  
//        
//        logger.info("requestUri:"+requestUri);    
//        logger.info("contextPath:"+contextPath);    
//        logger.info("url:"+url);    
//        
//        String wxCode = request.getParameter("code");
//        
//        
//        WxMpUser wxUser =  (WxMpUser)request.getSession().getAttribute(HmbConsts.SESSION_WX_USER);
//        if(wxUser!=null){
//            logger.debug("session中已有微信用户信息");
//            return true;
//        }
//        
//        if(StringKit.isBlank(wxCode)){
//            logger.debug("构造要重定向的URL向微信请求code");
//            String currUrl = HmbConsts.DOMAIN + request.getContextPath() + request.getServletPath();
//            //String accessToken = wxMpService.getAccessToken();
//            String callBackUrl = wxMpService.oauth2buildAuthorizationUrl(currUrl, "snsapi_userinfo", "123"); //重定向到自己
//            logger.info("Interceptor: 重定向的URL为： {}",callBackUrl);
//            //request.getRequestDispatcher(callBackUrl).forward(request, response);
//            response.sendRedirect(callBackUrl);
//            
//            return false;
//        }
//        logger.debug("从微信获取的code为{}",wxCode);
//        
//        try {
//            WxMpOAuth2AccessToken oauth2AT = wxMpService.oauth2getAccessToken(wxCode); //TODO:搞错，缓存的不是这个
//            //WxMpOAuth2AccessToken oauth2AT = oAuth2AccessTokenCacheSvc.getOAuth2AccessToken(wxCode);
//            logger.debug("获取到OAuth2AccessToken: {}",oauth2AT.toString());
//            wxUser = wxMpService.oauth2getUserInfo(oauth2AT, "zh_CN");
//            logger.debug("获取到微信用户 {}",wxUser.toString());
//            request.getSession().setAttribute(HmbConsts.SESSION_WX_USER, wxUser);//将用户设置到session里
//            
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//            return false;
//        }
//        
//        return true;
//        
//        
//    }    
//    
//    /** 
//     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作    
//     * 可在modelAndView中加入数据，比如当前时间 
//     */  
////    @Override    
////    public void postHandle(HttpServletRequest request,    
////            HttpServletResponse response, Object handler,    
////            ModelAndView modelAndView) throws Exception {     
////        //logger.info("==============执行顺序: 2、postHandle================");    
////        if(modelAndView != null){  //
////            //modelAndView.addObject("var", "测试postHandle");    
////        }    
////    }    
//    
//    /**  
//     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等   
//     *   
//     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()  
//     */    
////    @Override    
////    public void afterCompletion(HttpServletRequest request,    
////            HttpServletResponse response, Object handler, Exception ex)    
////            throws Exception {    
////        //logger.info("==============执行顺序: 3、afterCompletion================");    
////    }  
//    
//}
