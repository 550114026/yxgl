package com.ikkong.core.jfinal.ext.kit;

import com.jfinal.json.FastJsonFactory;

/**
 * JsonKit.
 */
public class JsonKit {
	
	public static String toJson(Object object) {
		return FastJsonFactory.me().getJson().setDatePattern("yyyy-MM-dd HH:mm:ss").toJson(object);
	}
	
	public static <T> T parse(String jsonString, Class<T> type) {
		return FastJsonFactory.me().getJson().parse(jsonString, type);
	}
	
}

