package com.yqglpt.utils;

public class Constants {
	public static final String SKEY = "abcdef0123456789";// 密钥
	public static final String IVPARAMETER = "0123456789abcdef";// 向量
	
	
	public static String getNewId(String key){
		return StrUtils.uuid();		
	}
}
