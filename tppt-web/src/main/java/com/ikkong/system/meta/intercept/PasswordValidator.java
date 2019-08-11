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
import com.ikkong.core.jfinal.ext.shiro.ShiroKit;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;

public class PasswordValidator extends BaseValidator {

	protected void validateOldPwd(String field1, String field2, String errorMessage) {
		String userId = getController().getPara(field1);
		String password = getController().getPara(field2);
		if (StrKit.isBlank(password)) {
			addError("请输入原密码!");
		}
		Blade blade = Blade.create(User.class);
		User user = blade.findById(userId);
		if(null == user){
			addError("未找到该用户!");
		}
		String pwd = user.getPassword();
		String salt = user.getSalt();
		boolean temp = (ShiroKit.md5(password, salt).equals(pwd));
		if (!temp) {
			addError(errorMessage);
		}
	}

	@Override
	protected void validate(Controller c) {
		validateOldPwd("user.id", "user.oldPassword", "原密码错误!");
		validateRequired("user.newPassword", "新密码不能为空");
		validateRequired("user.newPasswordr", "确认密码不能为空");
		validateTwoNotEqual("user.oldPassword", "user.newPassword", "新密码不能与原密码相同!");
		validateTwoEqual("user.newPassword", "user.newPasswordr", "确认密码与新密码不相同!");
	}

	@Override
	protected void handleError(Controller c) {
		c.renderJson(result);
	}

}
