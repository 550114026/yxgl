package com.ikkong.system.controller;

import com.ikkong.core.aop.AopContext;
import com.ikkong.core.base.BaseController;
import com.ikkong.core.constant.Cst;
import com.ikkong.core.dao.Blade;
import com.ikkong.core.dao.Db;
import com.ikkong.core.interfaces.IQuery;
import com.ikkong.core.jfinal.ext.kit.JStrKit;
import com.ikkong.core.jfinal.ext.kit.JsonKit;
import com.ikkong.core.toolbox.Func;
import com.ikkong.core.toolbox.Record;
import com.ikkong.core.toolbox.kit.ClassKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

import java.util.List;
import java.util.Map;

public class ZtreeController extends BaseController {
	
//	@RequestMapping("/open/{type0}/{index1}/{name2}/{source3}/{check4}/{where5}/{intercept6}/{ext7}/{val8}")
	public void open(){
		setAttr("type", getTypeName(getPara(0), getPara(3)));
		setAttr("index", getPara(1));
		setAttr("name", getPara(2));
		setAttr("source", getPara(3));
		setAttr("check", getPara(4));
		setAttr("where", getPara(5));
		setAttr("intercept", getPara(6));
		setAttr("ext", getPara(7));
		setAttr("val", getPara(8));
		render("/common/_function/_ztree.html");
	}
	
	public void getTreeList() {	
		String type = getPara("type");
		String source = getPara("source");
		String where = getPara("where");
		String val = getPara("val");
		String intercept = getPara("intercept");
		String ext = getPara("ext");
		final String sqlSource = getSql(type, source);
		
		Map<String, Object> params = Record.createHashMap();
		if(!where.equals("0")){
			params = JsonKit.parse(where, Map.class);
		}
		
		final Map<String, Object> modelOrMap = params;
		
		IQuery _intercept = getIntercept(type);
		if(StrKit.notBlank(intercept) && !Func.equals(intercept, "0")){
			_intercept = ClassKit.newInstance(intercept);
		}
		
		AopContext ac = new AopContext();
		ac.setObject(ext);
		ac.setTips("ztree");
		
		List<Record> list = Db.init().selectList(sqlSource, modelOrMap, ac, _intercept);

		String key = "id";
		if(type.indexOf("dict") >= 0){
			key = "num";
		}
		String [] arr = val.split(",");
		for(Map<String, Object> map : list){
			for(String v : arr){
				if(Func.format(map.get(key)).equals(v) && !v.equals("0")){
					map.put("checked", "true");
				}
			}
		}
		
		renderJson(json(list));
	}
	
	public void getTreeListName(){
		 String type = getPara("type");
		 String source = getPara("source");
		 String where = getPara("where");
		 String val = getPara("val");
		type = getTypeName(type, source);
		final String sqlSource = getSql(type, source);
		Map<String, Object> params = Record.createHashMap();
		if(StrKit.notBlank(where)){
			params = JsonKit.parse(where, Map.class);
		}
		final Map<String, Object> modelOrMap = params;
		List<Map<String, Object>> list = CacheKit.get(DICT_CACHE, "ztree_list_" + type,
				new IDataLoader() {
					public Object load() {
						return Db.init().selectList(sqlSource, modelOrMap);
					}
				});
		
		String name = "";
		String key = "id";
		if(type.indexOf("dict") >= 0){
			key = "num";
		}
		String [] arr = val.split(",");
		for(Map<String, Object> map : list){
			for(String v : arr){
				if(Func.format(map.get(key)).equals(v)){
					name += Func.format(map.get("name")) + ",";
				}
			}
		}
		name = JStrKit.removeSuffix(name, ",");
		renderJson(json(name));
	}
	
	private String getTypeName(String type, String source){
		if(type.indexOf("opentreeUserWG") >= 0){
			type = "userWG";
		} else if(type.indexOf("opentreeUser") >= 0){
			type = "user";
		} else if(type.indexOf("opentreeDept") >= 0){
			type = "dept";
		}else if(type.indexOf("opentreeLedger") >= 0){
			type = "ledger";
		} else if(type.indexOf("opentreeRole") >= 0){
			type = "role";
		} else if(type.indexOf("opentree_") >= 0 || type.indexOf("opentreeDict") >= 0){
			type = "dict_" + type.replace("opentree_", "").replace("opentreeDict", "");
		} else {
			type = "diy_" + source;
		}
		return type;
	}
	
	private String getSql(String type, String source){
		String sql = "";
		if (type.indexOf("dict") >= 0) {
			String code = type.replace("dict_", "");
			sql = "select NUM as \"num\",ID as \"id\",PID as \"pId\",NAME as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_DICT where code=" + code;
		} else if (type.equals("user")) {
			sql="select CONCAT('d',ID) as id,CONCAT('d',PID) as pId,SIMPLENAME as name,'false' as \"open\",'true' as nocheck   from  TFW_DEPT"
				+ " UNION SELECT	ID AS id,	CONCAT('d',DEPTID) AS pId,	NAME AS name,	'false' as \"open\",'false' as nocheck FROM 	TFW_USER WHERE	STATUS = 1 ";
			//sql = "select ID as \"id\",0 as \"pId\",NAME as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_USER where status=1";
		} else if (type.equals("userWG")) {
			sql="select CONCAT('d',ID) as id,CONCAT('d',PID) as pId,SIMPLENAME as name,'true' as \"open\",'true' as nocheck   from  TFW_DEPT"
					+ " UNION SELECT	ID AS id,	CONCAT('d',DEPTID) AS pId,	NAME AS name,	'false' as \"open\",'false' as nocheck FROM 	TFW_USER WHERE	STATUS = 1 and exists(select 1 from tfw_role r where r.type=1 and FIND_IN_SET(r.id,ROLEID) )";
				//sql = "select ID as \"id\",0 as \"pId\",NAME as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_USER where status=1";
		} else if (type.equals("dept")) {
			sql = "select ID as \"id\",PID as \"pId\",SIMPLENAME as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_DEPT";
		} else if (type.equals("role")) {
			sql = "select ID as \"id\",0 as \"pId\",NAME as \"name\",(case when (pId=0 or pId is null) then 'true' else 'false' end) \"open\" from  TFW_ROLE";
		} else if (type.equals("ledger")) {
			sql = "SELECT id, parentid pId, `name`, (select CASE WHEN count(0)>0 then 'true' else 'false' end  from safety_ledger a where a.parentid=safety_ledger.id) `open`	from safety_ledger  order by `order`,id";
		}else {
			sql = Blade.dao().getScript(source).getSql();
		}
		return sql;
	}
	
	private IQuery getIntercept(String type) {
		IQuery intercept = Cst.me().getDefaultQueryFactory();
		if (type.indexOf("dict") >= 0) {
			intercept = Cst.me().getDefaultSelectFactory().dictIntercept();
		} else if (type.equals("user")) {
			intercept = Cst.me().getDefaultSelectFactory().userIntercept();
		} else if (type.equals("dept")) {
			intercept = Cst.me().getDefaultSelectFactory().deptIntercept();
		} else if (type.equals("role")) {
			intercept = Cst.me().getDefaultSelectFactory().roleIntercept();
		}  
		return intercept;
	}
}
