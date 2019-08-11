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

import com.ikkong.core.constant.ConstCache;
import com.ikkong.core.dao.Db;
import com.ikkong.core.jfinal.ext.kit.JsonKit;
import com.ikkong.core.toolbox.Func;
import com.ikkong.core.toolbox.Record;
import com.ikkong.system.controller.base.AdminBaseController;
import com.ikkong.system.meta.intercept.MenuValidator;
import com.ikkong.system.model.Menu;
import com.ikkong.system.service.MenuService;
import com.ikkong.system.service.impl.MenuServiceImpl;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;

import java.util.List;
import java.util.Map;

import static com.ikkong.core.constant.ConstShiro.ADMIN;
import static com.ikkong.core.constant.ConstShiro.ADMINISTRATOR;

@RequiresRoles(value = { ADMINISTRATOR, ADMIN },logical = Logical.OR)
public class MenuController extends AdminBaseController{
	private static String LIST_SOURCE = "Menu.list";
	private static String BASE_PATH = "/system/menu/";
	private static String CODE = "menu";
	private static String PERFIX = "TFW_MENU";

	MenuService service = new MenuServiceImpl();

	@RequiresRoles(ADMINISTRATOR)
	public void index() {
		setAttr("code", CODE);
		render(BASE_PATH + "menu.html");
	}
	
	@RequiresRoles(ADMINISTRATOR)
	public void add() {
		String id = getPara(0);
		if (StrKit.notBlank(id)) {
			Menu menu = service.findById(id);
			setAttr("PCODE", menu.getCode());
			setAttr("LEVELS", menu.getLevels() + 1);
			setAttr("NUM", service.findLastNum(menu.getCode()));
		}
		setAttr("code", CODE);
		render(BASE_PATH + "menu_add.html");
	}

	@RequiresRoles(ADMINISTRATOR)
	public void edit() {
		String id = getPara(0);
		Menu menu = service.findById(id);
		setAttr("model", JsonKit.toJson(menu));
		setAttr("code", CODE);
		render(BASE_PATH + "menu_edit.html");
	}

	@RequiresRoles(ADMINISTRATOR)
	public void view() {
		String id = getPara(0);
		Menu menu = service.findById(id);
		setAttr("model", JsonKit.toJson(menu));
		setAttr("code", CODE);
		render(BASE_PATH + "menu_view.html");
	}

	@RequiresRoles(ADMINISTRATOR)
	public void list() {
		Object gird = paginate(LIST_SOURCE);
		renderJson(gird);
	}

	@Before(MenuValidator.class)
	@RequiresRoles(ADMINISTRATOR)
	public void save() {
		Menu menu = mapping(PERFIX, Menu.class);
		menu.setStatus(1);
		boolean temp = service.save(menu);
		if (temp) {
			CacheKit.removeAll(MENU_CACHE);
			renderJson(success(SAVE_SUCCESS_MSG));
		} else {
			renderJson(error(SAVE_FAIL_MSG));
		}
	}

	@Before(MenuValidator.class)
	@RequiresRoles(ADMINISTRATOR)
	public void update() {
		Menu menu = mapping(PERFIX, Menu.class);
		menu.setVersion(menu.getVersion() + 1);
		boolean temp = service.update(menu);
		if (temp) {
			CacheKit.removeAll(MENU_CACHE);
			renderJson(success(UPDATE_SUCCESS_MSG));
		} else {
			renderJson(error(UPDATE_FAIL_MSG));
		}
	}

	@RequiresRoles(ADMINISTRATOR)
	public void del() {
		String ids = getPara("ids");
		boolean temp = service.updateStatus(ids, 2);
		if (temp) {
			CacheKit.removeAll(MENU_CACHE);
			renderJson(success(DEL_SUCCESS_MSG));
		} else {
			renderJson(error(DEL_FAIL_MSG));
		}
	}

	@RequiresRoles(ADMINISTRATOR)
	public void restore() {
		String ids = getPara("ids");
		boolean temp = service.updateStatus(ids, 1);
		if (temp) {
			CacheKit.removeAll(MENU_CACHE);
			renderJson(success(RESTORE_SUCCESS_MSG));
		} else {
			renderJson(error(RESTORE_FAIL_MSG));
		}

	}

	@RequiresRoles(ADMINISTRATOR)
	public void remove() {
		String ids = getPara("ids");
		int cnt = service.deleteByIds(ids);
		if (cnt > 0) {
			CacheKit.removeAll(MENU_CACHE);
			renderJson(success(DEL_SUCCESS_MSG));
		} else {
			renderJson(error(DEL_FAIL_MSG));
		}
	}
	
	
	
	public void getMenu(){
		String MENU_CACHE = ConstCache.MENU_CACHE;
		final Object userId = getPara("userId");
		final Object roleId = getPara("roleId");

		Map<String, Object> userRole = CacheKit.get(MENU_CACHE, "role_ext_" + userId, new IDataLoader() {
			@Override
			public Object load() {
				return Db.init().selectOne("select * from TFW_ROLE_EXT where USERID=#{userId}", Record.create().set("userId", userId));
			}
		}); 

		String roleIn = "0";
		String roleOut = "0";
		if (!Func.isEmpty(userRole)) {
			Record map = Record.parse(userRole);
			roleIn = map.getStr("ROLEIN");
			roleOut = map.getStr("ROLEOUT");
		}
		final StringBuilder sql = new StringBuilder();
		sql.append("select * from TFW_MENU  ");
		sql.append(" where ( ");
		sql.append("	 (status=1)");
		sql.append("	 and (icon is not null and icon not LIKE '%btn%' and icon not LIKE '%icon%' ) ");
		sql.append("	 and (id in (select menuId from TFW_RELATION where roleId in (" + roleId + ")) or id in (" + roleIn + "))");
		sql.append("	 and id not in(" + roleOut + ")");
		sql.append("	)");
		sql.append(" order by levels,pCode,num");

		List<Map<String, Object>> sideBar = CacheKit.get(MENU_CACHE, "sideBar_" + userId, new IDataLoader() {
			@Override
			public Object load() {
				return Db.init().selectList(sql.toString());
			}
		}); 
		renderJson(sideBar);
	}

}
