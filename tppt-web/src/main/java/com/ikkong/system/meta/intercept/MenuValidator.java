package com.ikkong.system.meta.intercept;

import com.ikkong.core.base.BaseValidator;
import com.ikkong.core.jfinal.ext.kit.JStrKit;
import com.ikkong.system.service.MenuService;
import com.ikkong.system.service.impl.MenuServiceImpl;
import com.jfinal.core.Controller;

public class MenuValidator extends BaseValidator {
	
	MenuService service = new MenuServiceImpl();

	@Override
	protected void validate(Controller c) {
		if (getActionKey().toString().indexOf("update") == -1) {
			validateRequired("tfw_menu.pcode", "请输入菜单父编号");
			validateCode("tfw_menu.code", "菜单编号已存在!");
		}
//		validateSql("tfw_menu.source", "含有非法字符,请仔细检查!");
	}

	@Override
	protected void handleError(Controller c) {
		c.renderJson(result);
	}
	
	protected void validateCode(String field, String errorMessage) {
		String code = getController().getPara(field);
		if (JStrKit.isBlank(code)) {
			addError("请输入菜单编号!");
		}
		if (service.isExistCode(code)) {
			addError(errorMessage);
		}
	}

}
