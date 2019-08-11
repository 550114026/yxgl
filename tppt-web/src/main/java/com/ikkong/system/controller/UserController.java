package com.ikkong.system.controller;

import com.ikkong.common.vo.User;
import com.ikkong.core.aop.AopContext;
import com.ikkong.core.dao.Blade;
import com.ikkong.core.dao.Db;
import com.ikkong.core.interfaces.IQuery;
import com.ikkong.core.jfinal.ext.kit.JStrKit;
import com.ikkong.core.jfinal.ext.shiro.ClearShiro;
import com.ikkong.core.jfinal.ext.shiro.ShiroKit;
import com.ikkong.core.toolbox.Func;
import com.ikkong.core.toolbox.Record;
import com.ikkong.system.controller.base.AdminBaseController;
import com.ikkong.system.meta.intercept.PasswordValidator;
import com.ikkong.system.meta.intercept.UserIntercept;
import com.ikkong.system.meta.intercept.UserValidator;
import com.ikkong.system.model.RoleExt;
import com.jfinal.aop.Before;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ikkong.core.constant.ConstShiro.ADMIN;
import static com.ikkong.core.constant.ConstShiro.ADMINISTRATOR;

@RequiresRoles(value = { ADMINISTRATOR, ADMIN },logical = Logical.OR)
public class UserController extends AdminBaseController {

	private static String LIST_SOURCE = "User.list";
	private static String BASE_PATH = "/system/user/";
	private static String CODE = "user";
	private static String PERFIX = "TFW_USER";

	public void index() {
		setAttr("code", CODE);
		render(BASE_PATH + "user.html");
	}
	
	/**
	 * 分页aop
	 * 普通用法
	 */
	public void list() {
		Object gird = paginate(LIST_SOURCE, new UserIntercept());
		renderJson(gird);
	}
	
	public void add() {
		setAttr("code", CODE);
		render(BASE_PATH + "user_add.html");
	}

	@ClearShiro
	@RequiresUser
	public void edit() {
		User user = Blade.create(User.class).findById(getPara(0));
		Record maps = Record.parse(user);
		maps.set("roleName", Func.getRoleName(user.getRoleid()));
		setAttr("user", maps);
		setAttr("code", CODE);
		render(BASE_PATH + "user_edit.html");
	}

	@ClearShiro
	@RequiresUser
	public void editMySelf() {
		User user = Blade.create(User.class).findById(ShiroKit.getUser().getId());
		Record maps = Record.parse(user);
		maps.set("roleName", Func.getRoleName(user.getRoleid()));
		setAttr("user", maps);
		setAttr("code", CODE);
		setAttr("methodName", "editMySelf");
		render(BASE_PATH + "user_edit.html");
	}
	@ClearShiro
	@RequiresUser
	public void editPassword(){
		User user = Blade.create(User.class).findById(ShiroKit.getUser().getId());
		setAttr("user", user);
		setAttr("code", CODE);
		render(BASE_PATH + "user_edit_password.html");
	}

	@ClearShiro
	@RequiresUser
	@Before(PasswordValidator.class)
	public void updatePassword(){
		Blade blade = Blade.create(User.class);
		String userId = getPara("user.id");
		String password = getPara("user.newPassword");
		User user = blade.findById(userId);
		String salt = user.getSalt();
		user.setPassword(ShiroKit.md5(password, salt));
		user.setVersion(user.getVersion() + 1);
		boolean temp = blade.update(user);
		if (temp) {
			renderJson(success(UPDATE_SUCCESS_MSG));
		} else {
			renderJson(error(UPDATE_FAIL_MSG));
		}
	}

	public void view() {
		User user = Blade.create(User.class).findById(getPara(0));
		Record maps = Record.parse(user);
		maps.set("deptName", Func.getDeptName(user.getDeptid()))
			.set("roleName", Func.getRoleName(user.getRoleid()))
			.set("sexName", Func.getDictName(101, user.getSex()));
		setAttr("user", maps);
		setAttr("code", CODE);
		render(BASE_PATH + "user_view.html");
	}
	
	
	@Before(UserValidator.class)
	public void save() {
		User user = mapping(PERFIX, User.class);
		String pwd = user.getPassword();
		String salt = ShiroKit.getRandomSalt(5);
		String pwdMd5 = ShiroKit.md5(pwd, salt);
		user.setPassword(pwdMd5);
		user.setSalt(salt);
		user.setStatus(3);
		user.setCreatetime(new Date());
		boolean temp = Blade.create(User.class).save(user);
		if (temp) {
			CacheKit.removeAll(DEPT_CACHE);
			CacheKit.removeAll(DICT_CACHE);
			CacheKit.removeAll(USER_CACHE);
			renderJson(success(SAVE_SUCCESS_MSG));
		} else {
			renderJson(error(SAVE_FAIL_MSG));
		}
	}
	
	@Before(UserValidator.class)
	public void update() {
		User user = mapping(PERFIX, User.class);
		if(JStrKit.notBlank(PERFIX + "PASSWORD")){
			String pwd = user.getPassword();
			User oldUser = Blade.create(User.class).findById(user.getId());
			if(!pwd.equals(oldUser.getPassword())){
				String salt = oldUser.getSalt();
				String pwdMd5 = ShiroKit.md5(pwd, salt);
				user.setPassword(pwdMd5);
			}
		}
		user.setVersion(getParaToInt("VERSION", 0) + 1);
		boolean temp = Blade.create(User.class).update(user);
		if (temp) {
			CacheKit.removeAll(DEPT_CACHE);
			CacheKit.removeAll(DICT_CACHE);
			CacheKit.removeAll(USER_CACHE);
			renderJson(success(UPDATE_SUCCESS_MSG));
		} else {
			renderJson(error(UPDATE_FAIL_MSG));
		}
	}

	public void del() {
		boolean temp = Blade.create(User.class).updateBy("status = #{status}", "id in (#{join(ids)})", Record.create().set("status", 5).set("ids", getPara("ids").split(",")));
		if (temp) {
			renderJson(success(DEL_SUCCESS_MSG));
		} else {
			renderJson(error(DEL_FAIL_MSG));
		}
	}
	
	public void reset() {
		Blade blade = Blade.create(User.class);
		String [] idArr = getPara("ids").split(",");
		int cnt = 0;
		for(String id : idArr){
			User user = blade.findById(id);
			String pwd = "111111";
			String salt = user.getSalt();
			String pwdMd5 = ShiroKit.md5(pwd, salt);
			user.setVersion(((user.getVersion() == null) ? 0 : user.getVersion()) + 1);
			user.setPassword(pwdMd5);
			boolean temp = blade.update(user);
			if(temp){
				cnt++;
			}
		}
		if (cnt == idArr.length) {
			renderJson(success("重置密码成功"));
		} else {
			renderJson(error("重置密码失败"));
		}
	}

	public void auditOk() {
		Blade blade = Blade.create(User.class);
		Record countMap = Record.create().set("ids", getPara("ids").split(","));
		int cnt = blade.count("id in (#{join(ids)}) and (roleId='' or roleId is null)", countMap);
		if (cnt > 0) {
			renderJson(warn("存在没有分配角色的账号!"));
		}
		Record updateMap = Record.create().set("status", 1).set("ids", getPara("ids").split(","));
		boolean temp = blade.updateBy("status = #{status}", "id in (#{join(ids)})", updateMap);
		if (temp) {
			renderJson(success("审核成功!"));
		} else {
			renderJson(error("审核失败!"));
		}
	}

	public void auditRefuse() {
		Record updateMap = Record.create().set("status", 4).set("ids", getPara("ids").split(","));
		boolean temp = Blade.create(User.class).updateBy("status = #{status}", "id in (#{join(ids)})", updateMap);
		if (temp) {
			renderJson(success("审核拒绝成功!"));
		} else {
			renderJson(error("审核拒绝失败!"));
		}
	}

	public void ban() {
		Record updateMap = Record.create().set("ids", getPara("ids").split(","));
		boolean temp = Blade.create(User.class).updateBy("status = (CASE WHEN STATUS=2 THEN 3 ELSE 2 END)", "id in (#{join(ids)})", updateMap);
		if (temp) {
			renderJson(success("操作成功!"));
		} else {
			renderJson(error("操作失败!"));
		}
	}
	
	public void restore() {
		Record updateMap = Record.create().set("status", 3).set("ids", getPara("ids").split(","));
		boolean temp = Blade.create(User.class).updateBy("status = #{status}", "id in (#{join(ids)})", updateMap);
		if (temp) {
			renderJson(success("还原成功!"));
		} else {
			renderJson(error("还原失败!"));
		}
	}
	
	public void remove() {
		boolean temp = Blade.create(User.class).deleteByIds(getPara("ids")) > 0;
		if (temp) {
			CacheKit.removeAll(USER_CACHE);
			renderJson(success("删除成功!"));
		} else {
			renderJson(error("删除失败!"));
		}
	}
	
//	@RequestMapping("/extrole/{id}/{roleName}")
	public void extrole() {
		User user = Blade.create(User.class).findById(getPara(0));
		String roleId = user.getRoleid();
		setAttr("userId", getPara(0));
		setAttr("roleId", roleId);
		setAttr("roleName", Func.decodeUrl(getPara(1)));
		render(BASE_PATH + "user_extrole.html");
	}
	
	public void menuTreeIn() {
		String userId = getPara("userId");
		Db dao = Db.init();
		Map<String, Object> roleIn = dao.selectOne("select ROLEIN from tfw_role_ext where userId = #{userId}", Record.create().set("userId",userId));
		
		String in = "0";
		if (!Func.isEmpty(roleIn)) {
			in = Func.format(roleIn.get("ROLEIN"));
		}
		
		StringBuilder sb = Func.builder(
				"select m.id \"id\",(select id from tfw_menu  where code=m.pCode) \"pId\",name \"name\",(case when m.levels=1 then 'true' else 'false' end) \"open\",(case when r.id is not null then 'true' else 'false' end) \"checked\"",
				" from tfw_menu m",
				" left join (select id from tfw_menu where id in (" + in + ")) r",
				" on m.id=r.id",
				" where m.status=1 order by m.levels,m.num asc"
				);
		
		List<Record> menu = dao.selectList(sb.toString());
		renderJson(json(menu));
	}
	
	public void menuTreeOut() {
		String userId = getPara("userId");
		Db dao = Db.init();
		Map<String, Object> roleOut = dao.selectOne("select ROLEOUT from tfw_role_ext where userId = #{userId}", Record.create().set("userId",userId));
		
		String out = "0";
		if (!Func.isEmpty(roleOut)) {
			out = Func.format(roleOut.get("ROLEOUT"));
		}
		
		StringBuilder sb = Func.builder(
				"select m.id \"id\",(select id from tfw_menu  where code=m.pCode) \"pId\",name \"name\",(case when m.levels=1 then 'true' else 'false' end) \"open\",(case when r.id is not null then 'true' else 'false' end) \"checked\"",
				" from tfw_menu m",
				" left join (select id from tfw_menu where id in (" + out + ")) r",
				" on m.id=r.id",
				" where m.status=1 order by m.levels,m.num asc"
				);
		
		List<Record> menu = dao.selectList(sb.toString());
		renderJson(json(menu));
	}
	
	public void saveRoleExt() {
		Blade blade = Blade.create(RoleExt.class);
		String userId = getPara("userId");
		String roleIn = getPara("idsIn", "0");
		String roleOut = getPara("idsOut", "0");
		RoleExt roleExt = blade.findFirstBy("userId = #{userId}", Record.create().set("userId", userId));	
		boolean flag = false;
		if (Func.isEmpty(roleExt)) {
			roleExt = new RoleExt();
			flag = true;
		}
		roleExt.setUserid(userId);  
		roleExt.setRolein(roleIn); 
		roleExt.setRoleout(roleOut);
		if (flag) {
			blade.save(roleExt);
		} else {
			blade.update(roleExt);
		}
		CacheKit.removeAll(MENU_CACHE);
		renderJson(success("配置成功!")); 
	}
	
//	@RequestMapping("/roleAssign/{id}/{name}/{roleId}")
	public void roleAssign() {
		setAttr("id", getPara(0));
		setAttr("roleId", getPara(2));
		setAttr("name", Func.decodeUrl(getPara(1)));
		render(BASE_PATH + "user_roleassign.html");
	}
	
	public void saveRole() {
		String id = getPara("id");
		String roleIds = getPara("roleIds");
		Record maps = Record.create();
		maps.set("roleIds", roleIds).set("id", id.split(","));
		boolean temp = Blade.create(User.class).updateBy("ROLEID = #{roleIds}", "id in (#{join(id)})", maps);
		if (temp) {
			CacheKit.removeAll(ROLE_CACHE);
			CacheKit.removeAll(MENU_CACHE);
			renderJson(success("配置成功!"));
		} else {
			renderJson(error("配置失败!"));
		}
	}
	
	public void userTreeList() {
		List<Map<String, Object>> dept = CacheKit.get(DEPT_CACHE, "user_tree_all",
				new IDataLoader() {
					public Object load() {
						return Db.init().selectList("select id \"id\",pId \"pId\",simpleName as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_DEPT order by pId,num asc", Record.create(), new AopContext(), new IQuery() {
							
							@Override
							public void queryBefore(AopContext ac) {

							}
							
							@SuppressWarnings("unchecked")
							@Override
							public void queryAfter(AopContext ac) {
								List<Map<String, Object>> list = (List<Map<String, Object>>) ac.getObject();
								List<Map<String, Object>> _list = new ArrayList<>(); 
								for (Map<String, Object> map : list) {
									String [] deptIds = map.get("id").toString().split(",");
									List<User> users = Blade.create(User.class).findBy("DEPTID in (#{join(deptId)})", Record.create().set("deptId", deptIds));
									for (User user : users) {
										for (String deptId : deptIds) {
											Map<String, Object> userMap = Record.createHashMap();
											userMap.put("id", user.getId() + 9999);
											userMap.put("pId", deptId);
											userMap.put("name", user.getName());
											userMap.put("open", "false");
											userMap.put("iconSkin", "iconPerson");
											_list.add(userMap);
										}
									}
								}
								list.addAll(_list);
							}

							@Override
							public void beforeSQLInit(AopContext ac) {
								// TODO Auto-generated method stub
								
							}
						});
					}
				});

		renderJson(json(dept));
	}
	
}
