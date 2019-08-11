package com.ikkong.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class StrUtils {

	/**
	 * 生成唯一UUID
	 * 
	 * @Title: uuid
	 * @author Realfighter
	 * @return String
	 * @throws
	 */
	public static String uuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	
	/* 
     * 将时间戳转换为时间
     */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }
    
    /* 
     * 将时间转换为时间戳
     */    
    public static String dateToStamp(String s) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }
	
	public static String decode(String str){
		return str;
//		try{
//			return new String(str.getBytes("ISO-8859-1"),"UTF-8");
//		}
//		catch(UnsupportedEncodingException ex){
//			return "";
//		}
	}

}
