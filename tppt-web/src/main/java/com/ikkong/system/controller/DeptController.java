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

import java.util.HashMap;
import java.util.Map;

import com.ikkong.core.dao.Blade;
import com.ikkong.core.jfinal.ext.kit.JsonKit;
import com.ikkong.core.toolbox.Record;
import com.ikkong.system.controller.base.UrlPermissController;
import com.ikkong.system.meta.intercept.CommonDicIntercept;
import com.ikkong.system.meta.intercept.DeptIntercept;
import com.ikkong.system.model.Dept;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.CacheKit;

public class DeptController extends UrlPermissController{
	private static String LIST_SOURCE = "Dept.list";
	private static String BASE_PATH = "/system/dept/";
	private static String CODE = "dept";
	private static String PERFIX = "tfw_dept";
	
	public void index() {
		setAttr("code", CODE);
		render(BASE_PATH + "dept.html");
	}
	
	
	public void list() {
		Map<String,String> dicGroup=new HashMap<String,String>();
		dicGroup.put("isarea", "10005");
		Object grid = paginate(LIST_SOURCE,new CommonDicIntercept(dicGroup));
		renderJson(grid);
	}
	
	public void add() {
		String id = getPara(0);
		if (StrKit.notBlank(id)) {
			setAttr("pId", id);
			setAttr("num", findLastNum(id));
		}
		setAttr("code", CODE);
		render(BASE_PATH + "dept_add.html");
	}
	
	public void edit() {
		String id = getPara(0);
		Dept dept = Blade.create(Dept.class).findById(id);
		setAttr("model", JsonKit.toJson(dept));
		setAttr("row", dept);
		setAttr("code", CODE);
		render(BASE_PATH + "dept_edit.html");
	}

	public void view() {
		String id = getPara(0);
		Blade blade = Blade.create(Dept.class);
		Dept Dept = blade.findById(id);
		Dept parent = blade.findById(Dept.getPid());
		String pName = (null == parent) ? "" : parent.getSimplename();
		Record maps = Record.parse(Dept);
		maps.set("pName", pName);
		setAttr("model", JsonKit.toJson(maps));
		setAttr("code", CODE);
		render(BASE_PATH + "dept_view.html");
	}
	
	public void save() {
		Dept dept = mapping(PERFIX, Dept.class);
		boolean temp = Blade.create(Dept.class).save(dept);
		if (temp) {
			CacheKit.removeAll(DEPT_CACHE);
			renderJson(success("新增成功"));
		} else {
			renderJson(error("新增失败"));
		}
	}

	public void update() {
		Dept dept = mapping(PERFIX, Dept.class);
		dept.setVersion(dept.getVersion() + 1);
		boolean temp =  Blade.create(Dept.class).update(dept);
		if (temp) {
			CacheKit.removeAll(DEPT_CACHE);
			renderJson(success("修改成功"));
		} else {
			renderJson(error("修改失败"));
		}
	}

	public void remove() {
		String ids = getPara("ids");
		int cnt = Blade.create(Dept.class).deleteByIds(ids);
		if (cnt > 0) {
			CacheKit.removeAll(DEPT_CACHE);
			renderJson(success("删除成功!"));
		} else {
			renderJson(error("删除失败!"));
		}
	}
	
	private int findLastNum(String id){
		try{
			Blade blade = Blade.create(Dept.class);
			Dept dept = blade.findFirstBy("pId = #{pId} order by num desc", Record.create().set("pId", id));
			return dept.getNum() + 1;
		}
		catch(Exception ex){
			return 1;
		}
	}
	
	
}
