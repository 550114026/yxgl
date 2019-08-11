package com.ikkong.core.intercept;

import javax.naming.NoPermissionException;

import com.ikkong.common.vo.ShiroUser;
import com.ikkong.core.base.BaseController;
import com.ikkong.core.constant.Const;
import com.ikkong.core.constant.ConstShiro;
import com.ikkong.core.jfinal.ext.shiro.ShiroKit;
import com.ikkong.core.toolbox.log.LogManager;
import com.jfinal.aop.Invocation;
import com.jfinal.core.ActionException;
import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;

/**
 * 全局异常处理
 * @author ikkong
 *
 */
public class ExceptionHandlerInterceptor extends BladeInterceptor {

	@Override
	public void intercept(Invocation inv) {
		try{
			inv.invoke();
		}catch(Exception ex){
			BaseController controller = (BaseController) inv.getController();
			String url = Const.error500Path;
			String msg = ex.getMessage();
			if (ex.getClass() == NoPermissionException.class) {
				url = Const.noPermissionPath;// 无权限抛出的异常
				msg = ConstShiro.NO_PERMISSION;
			}else if(ex.getClass() == ActionException.class){
				url = ((ActionException)ex).getErrorRender().getView();
			}
			try {
				if(StrKit.notBlank(msg)){
					ShiroUser user = ShiroKit.getUser();
					LogManager.doLog(user, msg, "异常日志", controller.getRequest(), false);
				}
			} catch (Exception logex) {
				LogKit.error("异常日志",logex);
			}
			if (controller.isAjax()) {
				result.addFail(msg == null ? ex.getClass().getName():msg);
				controller.renderJson(result);
			} else {
				controller.setAttr("error", msg);
				controller.setAttr("class", ex.getClass());
				controller.setAttr("method", controller.getRequest().getRequestURI());
				controller.render(url);
			}
		}
	}

}
