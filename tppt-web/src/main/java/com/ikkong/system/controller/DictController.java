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
package com.ikkong.system.controller;

import com.ikkong.core.dao.Blade;
import com.ikkong.core.jfinal.ext.kit.JsonKit;
import com.ikkong.core.toolbox.Record;
import com.ikkong.system.controller.base.AdminBaseController;
import com.ikkong.system.model.Dict;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.CacheKit;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;

import static com.ikkong.core.constant.ConstShiro.ADMIN;
import static com.ikkong.core.constant.ConstShiro.ADMINISTRATOR;

@RequiresRoles(value = { ADMINISTRATOR, ADMIN },logical = Logical.OR)
public class DictController extends AdminBaseController{
	private static String LIST_SOURCE = "Dict.list";
	private static String BASE_PATH = "/system/dict/";
	private static String CODE = "dict";
	private static String PERFIX = "tfw_dict";
	
	public void index() {
		setAttr("code", CODE);
		render(BASE_PATH + "dict.html");
	}
	
	public void list() {
		Object gird = paginate(LIST_SOURCE);
		renderJson(gird);
	}
	
	public void add() {
		String id = getPara(0);
		if (StrKit.notBlank(id)) {
			Dict dict = Blade.create(Dict.class).findById(id);
			setAttr("dictcode", dict.getCode());
			setAttr("pId", id);
			setAttr("num", findLastNum(dict.getCode()));
		}
		setAttr("code", CODE);
		render(BASE_PATH + "dict_add.html");
	}
	
	public void edit() {
		String id = getPara(0);
		Dict dict = Blade.create(Dict.class).findById(id);
		setAttr("model", JsonKit.toJson(dict));
		setAttr("code", CODE);
		render(BASE_PATH + "dict_edit.html");
	}

	public void view() {
		String id = getPara(0);
		Blade blade = Blade.create(Dict.class);
		Dict dict = blade.findById(id);
		Dict parent = blade.findById(dict.getPid());
		String pName = (null == parent) ? "" : parent.getName();
		Record maps = Record.parse(dict);
		maps.set("pName", pName);
		setAttr("model", JsonKit.toJson(maps));
		setAttr("code", CODE);
		render(BASE_PATH + "dict_view.html");
	}
	
	public void save() {
		Dict dict = mapping(PERFIX, Dict.class);
		boolean temp = Blade.create(Dict.class).save(dict);
		if (temp) {
			CacheKit.removeAll(DICT_CACHE);
			CacheKit.removeAll(DIY_CACHE);
			renderJson(success(SAVE_SUCCESS_MSG));
		} else {
			renderJson(error(SAVE_FAIL_MSG));
		}
	}

	public void update() {
		Dict dict = mapping(PERFIX, Dict.class);
		dict.setVersion(dict.getVersion() + 1);
		boolean temp =  Blade.create(Dict.class).update(dict);
		if (temp) {
			CacheKit.removeAll(DICT_CACHE);
			CacheKit.removeAll(DIY_CACHE);
			renderJson(success(UPDATE_SUCCESS_MSG));
		} else {
			renderJson(error(UPDATE_FAIL_MSG));
		}
	}

	public void remove() {
		String ids = getPara("ids");
		int cnt = Blade.create(Dict.class).deleteByIds(ids);
		if (cnt > 0) {
			CacheKit.removeAll(DICT_CACHE);
			CacheKit.removeAll(DIY_CACHE);
			renderJson(success(DEL_SUCCESS_MSG));
		} else {
			renderJson(error(DEL_FAIL_MSG));
		}
	}

	
	private int findLastNum(String code){
		try{
			Blade blade = Blade.create(Dict.class);
			Dict dict = blade.findFirstBy("code = #{code} order by num desc", Record.create().set("code", code));
			return dict.getNum() + 1;
		}
		catch(Exception ex){
			return 1;
		}
	}
	
	
}
