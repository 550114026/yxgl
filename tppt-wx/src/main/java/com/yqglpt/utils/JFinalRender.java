package com.yqglpt.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jfinal.plugin.activerecord.CPI;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * JFinal源码处理集合的方法，此处用于处理封装的分页
 * 
 * @Package com.xx566.blog.utils
 * @ClassName: JFinalRender
 * @author Realfighter
 * @date 2014-7-10 下午01:13:40
 */
@SuppressWarnings("unchecked")
public class JFinalRender {

	public static final int DEPTH = 8;

	public static Object handleObject(Object value, int depth) {
		if (value == null || (depth--) <= 0)
			return value;

		if (value instanceof List)
			return handleList((List) value, depth);
		else if (value instanceof Model)
			return handleMap(CPI.getAttrs((Model) value), depth);
		else if (value instanceof Record)
			return handleMap(((Record) value).getColumns(), depth);
		else if (value instanceof Map)
			return handleMap((Map) value, depth);
		else if (value instanceof Object[])
			return handleArray((Object[]) value, depth);
		else
			return value;
	}

	private static Map handleMap(Map map, int depth) {
		if (map == null || map.size() == 0)
			return map;

		Map<Object, Object> result = map;
		for (Map.Entry<Object, Object> e : result.entrySet()) {
			Object key = e.getKey();
			Object value = e.getValue();
			value = handleObject(value, depth);
			result.put(key, value);
		}
		return result;
	}

	private static List handleList(List list, int depth) {
		if (list == null || list.size() == 0)
			return list;

		List result = new ArrayList(list.size());
		for (Object value : list)
			result.add(handleObject(value, depth));
		return result;
	}

	private static List handleArray(Object[] array, int depth) {
		if (array == null || array.length == 0)
			return new ArrayList(0);

		List result = new ArrayList(array.length);
		for (int i = 0; i < array.length; i++)
			result.add(handleObject(array[i], depth));
		return result;
	}
}
