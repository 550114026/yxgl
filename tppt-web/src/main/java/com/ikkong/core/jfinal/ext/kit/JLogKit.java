package com.ikkong.core.jfinal.ext.kit;

import java.util.Map;

import com.ikkong.core.toolbox.Func;
import com.ikkong.core.toolbox.kit.DateKit;
import com.jfinal.kit.LogKit;

public class JLogKit extends LogKit {

	public static void println(String message) {
		System.out.println(message);
	}
	
	public static void println(String message, Object ... values) {
		System.out.println(Func.format(message, values));
	}
	
	public static void println(String message, Map<?, ?> map) {
		System.out.println(Func.format(message, map));
	}

	public static void report(String message) {
		System.out.println("/nBlade report ------------- "+ DateKit.getTime() + " -----------------------------");
		System.out.println("msg	: " + message);
		System.out.println("---------------------------------------------------------------------------------");
	}
}
