package com.ikkong.core.jfinal.ext.shiro;

import com.ikkong.core.jfinal.ext.kit.JStrKit;
import com.jfinal.aop.Invocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.subject.Subject;

import java.lang.annotation.Annotation;

/**
 * 基于权限的访问控制处理器，非单例模式运行。
 * @author ikkong
 *
 */
class UrlPermissionAuthzHandler extends AbstractAuthzHandler {
	private final Annotation annotation;

	public UrlPermissionAuthzHandler(Annotation annotation) {
		this.annotation = annotation;
	}

	public void assertAuthorized(Invocation ai) throws AuthorizationException {
		if (!(annotation instanceof RequiresUrlPermission))
			return;

		RequiresUrlPermission rpAnnotation = (RequiresUrlPermission) annotation;
		String[] perms = rpAnnotation.value();
		Subject subject = getSubject();
		if(ai.getMethodName().startsWith("select"))//选择类的开放
			return ;
		if (perms.length == 1) {
			if(JStrKit.isBlank(perms[0])){
				//默认设置为当前访问路径
				perms[0] = ai.getControllerKey()+"/"
						+ai.getMethodName().replace("index","").replace("list","") //默认拥有index和list权限
							.replace("save","add")	//save权限跟随add
							.replace("update","edit") //update跟随edit
							.replace("uploading","upload") //uploading跟随upload
							.replace("search",""); //search跟随index
			}
			subject.checkPermission(perms[0]);
			return;
		}
		if (Logical.AND.equals(rpAnnotation.logical())) {
			subject.checkPermissions(perms);
			return;
		}
		if (Logical.OR.equals(rpAnnotation.logical())) {
			// Avoid processing exceptions unnecessarily - "delay" throwing the
			// exception by calling hasRole first
			boolean hasAtLeastOnePermission = false;
			for (String permission : perms)
				if (subject.isPermitted(permission))
					hasAtLeastOnePermission = true;
			// Cause the exception if none of the role match, note that the
			// exception message will be a bit misleading
			if (!hasAtLeastOnePermission)
				subject.checkPermission(perms[0]);

		}

	}

}