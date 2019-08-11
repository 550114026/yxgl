package com.ikkong.system.meta.intercept;

import com.ikkong.core.aop.AopContext;
import com.ikkong.core.constant.ConstShiro;
import com.ikkong.core.jfinal.ext.kit.JStrKit;
import com.ikkong.core.jfinal.ext.shiro.ShiroKit;
import com.ikkong.core.meta.PageIntercept;

public class DeptIntercept extends PageIntercept {

	public void queryBefore(AopContext ac) {
		if (ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)) {
			String depts = ShiroKit.getUser().getDeptId() + "," + ShiroKit.getUser().getSubDepts();
			String condition = "and id in (" + JStrKit.removeSuffix(depts, ",") + ")";
			ac.setCondition(condition);
		}
	}

}
