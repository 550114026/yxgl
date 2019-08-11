/**
 * Copyright (c) 2015-2016, Chill Zhuang 庄骞 (smallchill@163.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ikkong.system.meta.intercept;

import com.ikkong.common.vo.User;
import com.ikkong.core.base.BaseValidator;
import com.ikkong.core.dao.Blade;
import com.ikkong.core.toolbox.Record;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

public class UserValidator extends BaseValidator {

	protected void validateAccount(String field, String errorMessage) {
		String account = getController().getPara(field);
		if (StrKit.isBlank(account)) {
			addError("请输入账号!");
		}
		if (Blade.create(User.class).isExist("SELECT * FROM tfw_user WHERE account = #{account} and status=1", Record.create().set("account", account))) {
			addError(errorMessage);
		}
	}

	@Override
	protected void validate(Controller c) {
		if (getActionKey().toString().indexOf("update") == -1) {
			validateAccount("TFW_USER.ACCOUNT", "账号已存在");
			validateStringExt("TFW_USER.ACCOUNT",  "含有非法字符,请检查");
			validateRequired("TFW_USER.ACCOUNT",  "请输入账号");
			validateString("TFW_USER.ACCOUNT", 5, 15,  "请输入5~15位的账号");
		}
		validateStringExt("TFW_USER.NAME",  "含有非法字符,请检查");
		validateRequired("TFW_USER.NAME",  "请输入姓名");
		validateRequired("TFW_USER.PASSWORD",  "请输入密码");
		validateRequired("password",  "请输入确认密码");
		validateTwoEqual("TFW_USER.PASSWORD", "password",  "两次密码不相同");
	}

	@Override
	protected void handleError(Controller c) {
		c.renderJson(result);
	}

}
