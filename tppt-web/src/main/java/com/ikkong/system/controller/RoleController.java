package com.ikkong.system.controller;

import com.ikkong.core.jfinal.ext.kit.JStrKit;
import com.ikkong.core.jfinal.ext.kit.JsonKit;
import com.ikkong.core.jfinal.ext.shiro.ShiroKit;
import com.ikkong.core.toolbox.Func;
import com.ikkong.core.toolbox.Record;
import com.ikkong.system.controller.base.AdminBaseController;
import com.ikkong.system.meta.intercept.CommonDicIntercept;
import com.ikkong.system.meta.intercept.RoleIntercept;
import com.ikkong.system.meta.intercept.RoleValidator;
import com.ikkong.system.model.Role;
import com.ikkong.system.service.RoleService;
import com.ikkong.system.service.impl.RoleServiceImpl;
import com.jfinal.aop.Before;
import com.jfinal.plugin.ehcache.CacheKit;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;

import static com.ikkong.core.constant.ConstShiro.ADMIN;
import static com.ikkong.core.constant.ConstShiro.ADMINISTRATOR;

import java.util.HashMap;
import java.util.Map;

@RequiresRoles(value = { ADMINISTRATOR, ADMIN },logical = Logical.OR)
public class RoleController extends AdminBaseController {

	private static String LIST_SOURCE = "Role.list";
	private static String BASE_PATH = "/system/role/";
	private static String CODE = "role";
	private static String PERFIX = "tfw_role";
	
	RoleService service = new RoleServiceImpl();

	public void index() {
		setAttr("code", CODE);
		render(BASE_PATH + "role.html");
	}
	
	public void list() {

		Map<String,String> dicGroup=new HashMap<String,String>();
		dicGroup.put("type", "10004");
		Object grid = paginate(LIST_SOURCE,new CommonDicIntercept(dicGroup));
		renderJson(grid);
	}
	
	public void add() {
		if (JStrKit.notBlank(getPara(0))) {
			setAttr("pId", getPara(0));
			setAttr("num", service.findLastNum(getPara(0)));
		}
		setAttr("code", CODE);
		render(BASE_PATH + "role_add.html");
	}
	
	public void edit() {
		Role role = service.findById(getPara(0));
		setAttr("model", JsonKit.toJson(role));
		setAttr("row", role);
		setAttr("code", CODE);
		render(BASE_PATH + "role_edit.html");
	}

	public void view() {
		Role role = service.findById(getPara(0));
		Role parent = service.findById(role.getPid());
		String pName = (null == parent) ? "" : parent.getName();
		Record maps = Record.parse(role);
		maps.set("deptName", Func.getDeptName(role.getDeptid()))
			.set("pName", pName);
		setAttr("model", JsonKit.toJson(maps));
		setAttr("code", CODE);
		render(BASE_PATH + "role_view.html");
	}
	
//	 /authority/{roleId}/{roleName}
	public void authority() {
		String roleId = getPara(0);
		String roleName = getPara(1);
		if(!ShiroKit.hasAnyRoles(ADMINISTRATOR + "," + ADMIN)){
			renderText("redirect:/unauth");
			return;
		}
		setAttr("roleId", roleId);
		setAttr("roleName", Func.decodeUrl(roleName));
		render(BASE_PATH + "role_authority.html");
	}
	
	public void saveAuthority() {
		String ids = getPara("ids");
		String roleId = getPara("roleId");
		String[] id = ids.split(",");
		if (id.length <= 1) {
			CacheKit.removeAll(ROLE_CACHE);
			CacheKit.removeAll(MENU_CACHE);
			renderJson(success("设置成功"));
			return;
		}
		boolean temp = service.saveAuthority(ids, roleId);
		if (temp) {
			CacheKit.removeAll(ROLE_CACHE);
			CacheKit.removeAll(MENU_CACHE);
			renderJson(success("设置成功"));
		} else {
			renderJson(error("设置失败"));
		}
	}

	@Before(RoleValidator.class)
	public void save() {
		Role role = mapping(PERFIX, Role.class);
		boolean temp = service.save(role);
		if (temp) {
			CacheKit.removeAll(ROLE_CACHE);
			CacheKit.removeAll(MENU_CACHE);
			renderJson(success(SAVE_SUCCESS_MSG));
		} else {
			renderJson(error(SAVE_FAIL_MSG));
		}
	}

	@Before(RoleValidator.class)
	public void update() {
		Role role = mapping(PERFIX, Role.class);
		role.setVersion(role.getVersion() + 1);
		boolean temp = service.update(role);
		if (temp) {
			CacheKit.removeAll(ROLE_CACHE);
			CacheKit.removeAll(MENU_CACHE);
			renderJson(success(UPDATE_SUCCESS_MSG));
		} else {
			renderJson(error(UPDATE_FAIL_MSG));
		}
	}
	
	public void remove() {
		String ids = getPara("ids");
		// 删除角色之前要判断 是否被用户引用
		if(service.getRoleUsers(ids) > 0){
			renderJson(error("删除失败！请先处理此角色下的用户！"));
			return;
		}
		int cnt = service.deleteByIds(ids);
		if (cnt > 0) {
			CacheKit.removeAll(ROLE_CACHE);
			CacheKit.removeAll(MENU_CACHE);
			renderJson(success(DEL_SUCCESS_MSG));
		} else {
			renderJson(error(DEL_FAIL_MSG));
		}
	}

	public void getPowerById() {
		int cnt = service.getParentCnt(getPara("id"));
		if (cnt > 0) {
			renderJson(success("success"));
		} else {
			renderJson(error("error"));
		}
	}
	
}
