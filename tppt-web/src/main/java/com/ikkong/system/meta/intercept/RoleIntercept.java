package com.ikkong.system.meta.intercept;

import com.ikkong.core.aop.AopContext;
import com.ikkong.core.constant.ConstShiro;
import com.ikkong.core.jfinal.ext.kit.JStrKit;
import com.ikkong.core.jfinal.ext.shiro.ShiroKit;
import com.ikkong.core.meta.PageIntercept;

public class RoleIntercept extends PageIntercept {

	public void queryBefore(AopContext ac) {
		if (ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)) {
			String roles = ShiroKit.getUser().getRoles() + "," + ShiroKit.getUser().getSubRoles();
			String condition = "and id in (" + JStrKit.removeSuffix(roles, ",") + ")";
			ac.setCondition(condition);
		}
	}

}
