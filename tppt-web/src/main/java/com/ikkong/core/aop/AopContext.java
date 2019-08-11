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
package com.ikkong.core.aop;

import java.util.Map;

import com.ikkong.common.vo.User;
import com.ikkong.core.constant.Const;
import com.jfinal.core.Controller;

/**
 * 业务拦截器上下文
 * 拦截器beforeSQLInit方法可使用属性param
 * 
 */
public class AopContext {

	/**
	 * 当前控制器
	 */
	private Controller ctrl;

	/**
	 * 当前用户对象
	 */
	private User user;

	/**
	 * 当前对象id
	 */
	private Object id;
	
	/**
	 * 当前操作对象(model、list)
	 */
	private Object object;

	/**
	 * 当前定义SQL
	 */
	private String sql;

	/**
	 * 追加SQL条件(程序自动生成) 格式: where xxx = xxx
	 */
	private String condition;

	/**
	 * 自定义SQL覆盖默认查询条件 格式: where xxx = xxx
	 */
	private String where;
	/**
	 * 自定义SQL参数(map形式)[]
	 */
	private Map<String, Object> param;

	/**
	 * 用于判断当前使用场景
	 */
	private String tips = "";

	public AopContext() {

	}

	public AopContext(String tips) {
		this.tips = tips;
	}

	public AopContext(Controller ctrl) {
		this.ctrl = ctrl;
		this.user = (User) ctrl.getSessionAttr(Const.USER_SESSION_KEY);
	}

	public AopContext(Controller ctrl, Object object) {
		this(ctrl);
		this.object = object;
	}

	public Controller getCtrl() {
		return ctrl;
	}

	public void setCtrl(Controller ctrl) {
		this.ctrl = ctrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public Map<String, Object> getParam() {
		return param;
	}

	public void setParam(Map<String, Object> param) {
		this.param = param;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

}
