package com.ikkong.core.intercept;

import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ikkong.common.vo.ShiroUser;
import com.ikkong.core.annotation.DoLog;
import com.ikkong.core.jfinal.ext.kit.JStrKit;
import com.ikkong.core.jfinal.ext.shiro.ShiroKit;
import com.ikkong.core.toolbox.Func;
import com.ikkong.core.toolbox.log.LogManager;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.LogKit;

/**
 * 记录日志
 * 
 * @author ikkong
 *
 */
public class DoLogInterceptor extends BladeInterceptor {

	@Override
	public void intercept(Invocation inv) {
		if (!LogManager.isDoLog()) {
			inv.invoke();
		} else {
			Method method = inv.getMethod();
			String methodName = method.getName();
			DoLog doLog = method.getAnnotation(DoLog.class);
			if (!isWriteLog(methodName) && null == doLog) {
				inv.invoke();
			} else {
				ShiroUser user = ShiroKit.getUser();
				if (null == user) {
					inv.invoke();
				}
				String className = inv.getTarget().getClass().getName();
				Map<String, String[]> params = inv.getController().getParaMap();
				StringBuilder sb = new StringBuilder();
				Enumeration<String> paraNames = null;
				HttpServletRequest request = inv.getController().getRequest();
				if (params != null && params.size() > 0) {
					paraNames = request.getParameterNames();
					String key;
					String value;
					while (paraNames.hasMoreElements()) {
						key = paraNames.nextElement();
						value = request.getParameter(key);
						Func.builder(sb, key, "=", value, "&");
					}
					if (Func.isEmpty(sb.toString())) {
						Func.builder(sb, request.getQueryString());
					}
				}
				try {
					String msg = Func.format("[类名]:{}  [方法]:{}  [参数]:{}",
							className, methodName,
							JStrKit.removeSuffix(sb.toString(), "&"));
					LogManager.doLog(user, msg, getLogName(methodName),
							request, true);
				} catch (Exception e) {
					e.printStackTrace();
					LogKit.error("异常日志",e);
				}
				inv.invoke();
			}
		}
	}

	private boolean isWriteLog(String method) {
		String[] pattern = LogManager.logPatten();
		for (String s : pattern) {
			if (method.indexOf(s) > -1) {
				return true;
			}
		}
		return false;
	}

	private String getLogName(String method) {
		String[] pattern = LogManager.logPatten();
		for (String s : pattern) {
			if (method.indexOf(s) > -1) {
				return LogManager.logMaps().getStr(s);
			}
		}
		return "";
	}

}
