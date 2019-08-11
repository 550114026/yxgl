package com.ikkong.system.meta.intercept;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ikkong.core.aop.AopContext;
import com.ikkong.core.meta.PageIntercept;
import com.ikkong.core.toolbox.Func;
import com.ikkong.core.toolbox.support.BladePage;

public class CommonDicIntercept extends PageIntercept{
	
	/**
	 * 字段，字典code键值对
	 */
	Map<String, String> dictGroup=null;
	
	/**
	 * 
	 * @param map 字段，字典code键值对
	 */
	public CommonDicIntercept(Map<String, String> map){
		dictGroup=map;
	}
	
	/**
	 * 查询后操作 字典项、部门不通过数据库查询,通过缓存附加,减轻数据库压力,提高分页效率
	 * 
	 * @param ac
	 */
	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		if(dictGroup!=null){
			BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
			List<Map<String, Object>> list = page.getRows();
			Iterator<String> iter =null;
			String key=null;
			for (Map<String, Object> map : list) {
			iter = dictGroup.keySet().iterator();
				while (iter.hasNext()) {
				    key = iter.next();
					map.put(key+"Name", Func.getDictName(dictGroup.get(key), map.get(key)));
				}
			}
		}
	}
}
