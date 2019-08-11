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
package com.ikkong.core.toolbox.grid;

import java.util.HashMap;
import java.util.Map;

import com.ikkong.core.aop.AopContext;
import com.ikkong.core.base.BaseController;
import com.ikkong.core.constant.Const;
import com.ikkong.core.dao.Blade;
import com.ikkong.core.dao.Db;
import com.ikkong.core.interfaces.IGrid;
import com.ikkong.core.interfaces.IQuery;
import com.ikkong.core.jfinal.ext.kit.JsonKit;
import com.ikkong.core.toolbox.Func;
import com.ikkong.core.toolbox.support.SqlKeyword;
import com.jfinal.json.FastJsonFactory;
import com.jfinal.kit.StrKit;
import com.qiniu.util.StringUtils;

/**
 * @title grid工厂基类,封装通用分页方法
 * @author zhuangqian
 * @email smallchill@163.com
 * @date 2016-1-23上午9:41:48
 * @copyright 2016
 */
public abstract class BaseGridFactory implements IGrid{

	/**
	 * 封装grid返回数据类型
	 * 
	 * @param slaveName
	 *            数据库别名
	 * @param page
	 *            当前页号
	 * @param rows
	 *            每页的数量
	 * @param sql
	 *            数据源
	 * @param para
	 *            额外条件 {"id":1,"title":"test"}
	 * @param sort
	 *            排序列名 (id)
	 * @param order
	 *            排序方式 (desc)
	 * @param intercept
	 *            业务拦截器
	 * @param ctrl
	 *            控制器
	 * @return Object
	 */
	protected Object basePaginate(Integer page, Integer rows, String source, String para, String sort, String order, IQuery intercept, BaseController ctrl) {
		if (source.toLowerCase().indexOf("select") == -1) {
			return paginateById(page, rows, source, para, sort, order, intercept, ctrl);
		} else {
			return paginateBySql(page, rows, source, para, sort, order, intercept, ctrl);
		}
	}
	
	private Object paginateById(Integer page, Integer rows, String sqlId, String para, String sort, String order, IQuery intercept, BaseController ctrl) {	
		String sqlTemplate = Blade.dao().getScript(sqlId).getSql();
		return paginateBySql(page, rows, sqlTemplate, para, sort, order, intercept, ctrl);
	}

	private Object paginateBySql(Integer page, Integer rows, String sqlTemplate, String para, String sort, String order, IQuery intercept, BaseController ctrl) {
		
		Map<String, Object> paraMap=FastJsonFactory.me().getJson().parse(Func.isEmpty(Func.decodeUrl(para)) ? null : Func.decodeUrl(para), HashMap.class);
		
		// 查询前拦截
		AopContext ac = null;
		if (null != intercept) {
			ac = new AopContext(ctrl);
			ac.setParam(paraMap);
			intercept.beforeSQLInit(ac);
			if (ac.getParam()!=null) 
				para= JsonKit.toJson(ac.getParam());
			
		}		
		
		Map<String, Object> map = getSqlMap(para, sort, order);	
		String sqlex ="";
		
		if(null!=ac&&!StringUtils.isNullOrEmpty(ac.getWhere()))
			sqlex=ac.getWhere();
		sqlex=sqlex+SqlKeyword.getWhere(para);
		String statement = "select * from (" + sqlTemplate + ") blade_statement";
		
		if (null != intercept) {
			ac = new AopContext(ctrl);
			ac.setSql(statement + sqlex);
			ac.setCondition("");
			ac.setParam(map);
			intercept.queryBefore(ac);
			statement = statement + (StrKit.isBlank(ac.getWhere()) ? (sqlex + ac.getCondition()) : ac.getWhere());
		} else {
			statement = statement + sqlex;
		}

		Object list = null;
		String orderBy = (Func.isEmpty(map.get(Const.ORDER_BY_STR))) ? " " : (" order by " + Func.format(map.get(Const.ORDER_BY_STR)));
		list = Db.init().paginate(statement + orderBy, Map.class, map, page, rows);

		// 查询后拦截
		if (null != intercept) {
			ac.setObject(list);
			intercept.queryAfter(ac);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	private static Map<String, Object> getSqlMap(String para, String sort, String order){
		Map<String, Object> map = FastJsonFactory.me().getJson().parse(Func.isEmpty(Func.decodeUrl(para)) ? null : Func.decodeUrl(para), HashMap.class);
		if (Func.isEmpty(map)) {
			map = new HashMap<>();
		}
		map.put(Const.ORDER_BY_STR, Func.isAllEmpty(sort, order) ? "" : (sort + " " + order));
		return map;
	}
}
