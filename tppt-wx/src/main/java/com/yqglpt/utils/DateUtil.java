package com.yqglpt.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static int getYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.YEAR);
	}

	public static int getMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.MONTH) + 1;
	}

	public static int getDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.get(Calendar.DATE);
	}

	public static int getLastDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1);
		calendar.roll(Calendar.DATE, -1);
		return calendar.get(Calendar.DATE);
	}

	public static String getFull() {
		Calendar calendar = Calendar.getInstance();
		return String.format("%1$tY-%<tm-%<te", calendar);
	}

	public static String getWeekMonth() {
		return String.format("%1$tY年%<tm月", Calendar.getInstance());
	}

	public static void main(String[] args) {
		System.out.println(getLastDay());
	}

}
