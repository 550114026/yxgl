package com.ikkong.system.controller;

import com.google.common.collect.Lists;
import com.ikkong.core.base.BaseController;
import com.ikkong.core.constant.ConstCache;
import com.ikkong.core.dao.Blade;
import com.ikkong.core.dao.Db;
import com.ikkong.core.jfinal.ext.kit.JStrKit;
import com.ikkong.core.jfinal.ext.kit.JsonKit;
import com.ikkong.core.jfinal.ext.render.excel.PoiRender;
import com.ikkong.core.toolbox.Func;
import com.ikkong.core.toolbox.kit.DateKit;
import com.ikkong.core.toolbox.support.SqlKeyword;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelController extends BaseController {

	private static String cacheName = ConstCache.FILE_CACHE;
	
	@SuppressWarnings("unchecked")
	public void preExport() {
		String postdata = getPara("postdata");
		String colnames = getPara("colnames");  
		String colmodel = getPara("colmodel");
		String source = getPara("source");  
		String code = getPara("code");  
		Map<String, Object> _postdata = JsonKit.parse(postdata, HashMap.class);
		String[] _colname = colnames.replace("[", "").replace("]", "").split(",");
		List<Map<String, String>> _colmodel = JsonKit.parse(colmodel, ArrayList.class);
	
		String xml_source = Blade.dao().getScript(source).getSql();
		String menu_source = getInfoByCode(code, "SOURCE");

		String _source = (JStrKit.notBlank(menu_source)) ? menu_source : xml_source;

		if (JStrKit.isBlank(_source)) {
			 renderJson(error("未找到与该模块匹配的数据源！"));
			 return;
		}
		
		Object where = _postdata.get("where");
		Object sidx = _postdata.get("sidx");
		Object sord = _postdata.get("sord");
		Object sort = _postdata.get("sort");
		Object order = _postdata.get("order");
		if (!Func.isEmpty(sidx)) {
			sort = sidx + " " + sord + (Func.isEmpty(sort) ? ("," + sort) : "");
		}
		String orderby = (Func.isOneEmpty(sort, order)) ? (" order by " + sort + " " + order) : "";
		String sql = "select {} from (" + _source + ") a " + SqlKeyword.getWhere((String) where) + orderby;

		CacheKit.remove(cacheName, "excel_sql_" + code);
		CacheKit.remove(cacheName, "excel_colname_" + code);
		CacheKit.remove(cacheName, "excel_colmodel_" + code);
		CacheKit.put(cacheName, "excel_sql_" + code, sql);
		CacheKit.put(cacheName, "excel_colname_" + code, _colname);
		CacheKit.put(cacheName, "excel_colmodel_" + code, _colmodel);
		
		renderJson(json(code));
	}
	
	/**
	 * excel视图方式
	 */
	@SuppressWarnings("rawtypes")
	public void export() {
		String code = getPara("code");
		String sql = CacheKit.get(cacheName, "excel_sql_" + code);
		String [] _colname = CacheKit.get(cacheName, "excel_colname_" + code);
		List<Map<String, String>> _colmodel = CacheKit.get(cacheName, "excel_colmodel_" + code);
		
		List<String> entityList = Lists.newArrayList();
		StringBuilder sb = new StringBuilder();
		int cnt = 0;
		for (Map<String, String> m : _colmodel) {
			if (cnt > 1) {
				if(Func.format(m.get("hidden")).equals("true")){
					cnt++;
					continue;					
				}
				String name = m.get("name");
				entityList.add(_colname[cnt].replaceAll("\"", ""));
				sb.append(name).append(",");
			}
			cnt++;
		}
		
		String menu_name = getInfoByCode(code, "NAME");
		List dataResult = Db.init().queryListMap(Func.format(sql, JStrKit.removeSuffix(sb.toString(), ",")));
		String[] header = entityList.toArray(new String[entityList.size()]);
		render(PoiRender.me(dataResult).fileName(menu_name + DateKit.getAllTime()+".xls")
				.headers(header).sheetName(code).cellWidth(5000).headerRow(2));
	}
	
	private String getInfoByCode(String code, String col) {
		List<Map<String, Object>> menu = CacheKit.get("menuCache", "menu_table_all",
				new IDataLoader() {
					public Object load() {
						return Db.init().selectList("select CODE,PCODE,NAME,URL,SOURCE,PATH,TIPS,ISOPEN from TFW_MENU order by levels asc,num asc");
					}
				});
		for (Map<String, Object> _menu : menu) {
			if (code.equals(Func.format(_menu.get("CODE")))) {
				String _info = Func.format(_menu.get(col));
				return _info;
			}
		}
		return "";
	}
	
	
}
