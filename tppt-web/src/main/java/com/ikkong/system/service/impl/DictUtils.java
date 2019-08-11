package com.ikkong.system.service.impl;

import java.util.List;
import com.ikkong.core.constant.ConstCache;
import com.ikkong.core.dao.Db;
import com.ikkong.core.toolbox.Record;
import com.jfinal.plugin.ehcache.CacheKit;
import com.jfinal.plugin.ehcache.IDataLoader;

public class DictUtils implements ConstCache {

	public static List<Record> getDictByCode(final String code){
		List<Record> list = CacheKit.get(DICT_CACHE, "dict_code_" + code,
				new IDataLoader() {
					public Object load() {
						return Db.init().selectList("select num,pid,name from TFW_DICT where code=#{code}",Record.create().set("code", code));
					}
				});
		return list;
	}
}
