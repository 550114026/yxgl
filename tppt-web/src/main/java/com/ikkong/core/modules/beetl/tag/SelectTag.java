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
package com.ikkong.core.modules.beetl.tag;

import com.ikkong.core.aop.AopContext;
import com.ikkong.core.constant.ConstCache;
import com.ikkong.core.constant.Cst;
import com.ikkong.core.dao.Blade;
import com.ikkong.core.dao.Db;
import com.ikkong.core.interfaces.IQuery;
import com.ikkong.core.jfinal.ext.shiro.ShiroKit;
import com.ikkong.core.toolbox.Func;
import com.ikkong.core.toolbox.Record;
import com.ikkong.core.toolbox.kit.ClassKit;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;
import org.beetl.core.Tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SelectTag extends Tag {

	@Override
	@SuppressWarnings("unchecked")
	public void render() {
		try {
			Map<String, String> param = (Map<String, String>) args[1];

			final String code = param.get("code");
			String name = param.get("name");
			int value = Func.toInt(param.get("value"));
			String token ="";// (value > 0) ? "" : "token_";
			String type = param.get("type");
			String where = param.get("where");
			String required = param.get("required");
			String tail = param.get("tail");
			String inter = param.get("intercept");
			String disabled = param.get("disabled");
			
			int showAll = Func.toInt(param.get("showAll"));
			String sql = "";
			
			Map<String, Object> modelOrMap = Record.createHashMap();
			
			IQuery intercept = Cst.me().getDefaultQueryFactory();
			
			String CACHE_NAME = ConstCache.DICT_CACHE;
			
			if (type.equals("dict")) {
				sql = "select num as ID,pId as PID,name as TEXT from  TFW_DICT where code=" + code + " and num>0";
				intercept = Cst.me().getDefaultSelectFactory().dictIntercept();
			} else if (type.equals("user")) {
				CACHE_NAME = ConstCache.USER_CACHE;
				sql = "select ID,name as TEXT from  TFW_USER where status=1";
				intercept = Cst.me().getDefaultSelectFactory().userIntercept();
			} else if (type.equals("dept")) {
				CACHE_NAME = ConstCache.DEPT_CACHE;
				sql = "select ID,PID,SIMPLENAME as TEXT from  TFW_DEPT";
				intercept = Cst.me().getDefaultSelectFactory().deptIntercept();
			} else if (type.equals("area")) {
				CACHE_NAME = ConstCache.AREA_CACHE;
				sql = "select ID,PID,SIMPLENAME as TEXT from  TFW_DEPT where isarea=2";
				intercept = Cst.me().getDefaultSelectFactory().deptIntercept();
			} else if (type.equals("role")) {
				CACHE_NAME = ConstCache.ROLE_CACHE;
				sql = "select ID,name as TEXT from  TFW_ROLE";
				intercept = Cst.me().getDefaultSelectFactory().roleIntercept();
			} else if (type.equals("diy")) {
				CACHE_NAME = ConstCache.DIY_CACHE;
				type = type + "_" + param.get("source");
				if(StrKit.notBlank(where)){
					modelOrMap = JsonKit.parse(where, Map.class);
				}
				sql = Blade.dao().getScript(param.get("source")).getSql();
			}

			if(StrKit.notBlank(inter)) {
				intercept = ClassKit.newInstance(inter);
			}
			
			final String _sql = sql;
			final Map<String, Object> _modelOrMap = modelOrMap;
			final IQuery _intercept = intercept;
			
			List<Map<String, Object>> dict = CacheKit.get(CACHE_NAME, "dict_" + type + "_" + code + "_" + ShiroKit.getUser().getId(), new IDataLoader() {
				@Override
				public Object load() {
					return Db.init().selectList(_sql, _modelOrMap, new AopContext(), _intercept);
				}
			}); 

			StringBuilder sb = new StringBuilder();
			String sid = "_" + name.split("\\.")[1];
			sb.append("<select onchange=\"" +sid + "_selectChanged('" + sid + "')\" "+disabled+" " + required + " class=\"form-control\" id=\"" + sid + "\"  name=\"" + token + name + "\">");
			if(showAll!=0){
				if(showAll==1)
					sb.append("<option value>全部</option>");
				else
					sb.append("<option value>请选择</option>");
			}
			
			for (Map<String, Object> dic : dict) {
				int id =  Func.toInt(dic.get("ID"));
				String selected = "";
				if (id == value) {
					selected = "selected";
				}
				sb.append("<option " + selected + " value=\"" + id + "\">" + dic.get("TEXT") + "</option>");
			}
			sb.append("</select>");
			
			
			sb.append("<script type=\"text/javascript\">");
			sb.append("		function " +sid + "_selectChanged(sid) {");
			sb.append("			$('#form_token').val(1);");
			sb.append("			$('#' + sid).attr('name','"+name+"');");
			if(StrKit.notBlank(tail)) {
				sb.append("			var options=$('#' + sid + ' option:selected');");
				sb.append("			$('#' + sid + '_ext').val(options.text());");
			}
			sb.append("		};");
			sb.append("</script>");
			if(StrKit.notBlank(tail)) {
				sb.append("<input type=\"hidden\" id=\"" + sid + "_ext\" name=\"" + name.split("\\.")[0] + "." + tail + "\">");
			}

			ctx.byteWriter.writeString(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
