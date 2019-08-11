package com.ikkong.system.controller;


import java.util.Date;

import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.ikkong.common.vo.LoginLog;
import com.ikkong.core.dao.Blade;
import com.ikkong.core.jfinal.ext.autoroute.ControllerBind;
import com.ikkong.core.jfinal.ext.kit.JLogKit;
import com.ikkong.core.jfinal.ext.shiro.ShiroKit;
import com.ikkong.core.modules.beetl.BeetlRenderFactory;
import com.ikkong.core.toolbox.Func;
import com.ikkong.core.toolbox.log.LogManager;
import com.ikkong.system.controller.base.AdminBaseController;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.ext.interceptor.GET;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.LogKit;

@Clear
@ControllerBind(controllerKey = "/")
public class IndexController extends AdminBaseController {

	@Before(GET.class)
	public void login(){
		if(ShiroKit.isAuthenticated()){
			index();
			return;
		}
		render(loginRealPath);
	}
	public void index(){
		String browserType=getCookie("browserType");
		if(browserType!=null&&"isPad".equals(browserType)){
			setSessionAttr("isPad", "1");
			BeetlRenderFactory.groupTemplate.getSharedVars().put("isPad", "1");
		}else{
			setSessionAttr("isPad", "0");
			BeetlRenderFactory.groupTemplate.getSharedVars().put("isPad", "0");
		}
		render(indexRealPath);
	}
	
	@Before(POST.class)
	public void doLogin(){
		String account = getPara("account");
		String password = getPara("password");
		if (!validateCaptcha("imgCode")) {
			renderJson(error("验证码错误"));
			return;
		}
		Subject currentUser = ShiroKit.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(account, password.toCharArray());
		token.setRememberMe(true);
		try {
			currentUser.login(token);
			Session session = ShiroKit.getSession();
			JLogKit.println("sessionID	: {} ", session.getId());
			JLogKit.println("sessionHost	: {}", session.getHost());
			JLogKit.println("sessionTimeOut	: {}", session.getTimeout());
		} catch (UnknownAccountException e) {
			LogKit.error("账号不存在：{}", e);
			renderJson(error("账号不存在"));
			return;
		} catch (DisabledAccountException e) {
			LogKit.error("账号未启用：{}", e);
			renderJson(error("账号未启用"));
			return;
		} catch (IncorrectCredentialsException e) {
			LogKit.error("密码错误：{}", e);
			renderJson(error("密码错误"));
			return;
		} catch (RuntimeException e) {
			LogKit.error("未知错误,请联系管理员：{}", e);
			renderJson(error("未知错误,请联系管理员"));
			return;
		}
		doLog(ShiroKit.getSession(), "登录");
		renderJson(success("登录成功"));
		return;
	}
	
	public void captcha(){
		renderCaptcha();
	}
	
	public void logout() {
		doLog(ShiroKit.getSession(), "登出");
		Subject currentUser = ShiroKit.getSubject();
		currentUser.logout();
		redirect("/login");
	}

	public void unauth() {
		if (ShiroKit.notAuthenticated()) {
			redirect("/login");
		}
		render(noPermissionPath);
	}
	
	public void doLog(Session session, String type){
		if(!LogManager.isDoLog()){
			return;
		}
		try{
			LoginLog log = new LoginLog();
			String msg = Func.format("[sessionID]: {} [sessionHost]: {} [sessionHost]: {}", session.getId(), session.getHost(), session.getTimeout());
			log.setLogname(type);
			log.setMethod(msg);
			log.setCreatetime(new Date());
			log.setSucceed("1");
			log.setUserid(Func.format(ShiroKit.getUser().getId()));
			Blade.create(LoginLog.class).save(log);
		}catch(Exception ex){
			LogKit.logNothing(ex);
		}
	}
}
