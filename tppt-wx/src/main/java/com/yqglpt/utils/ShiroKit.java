/**
 * Copyright (c) 2011-2013, dafei 李飞 (myaniu AT gmail DOT com)
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
package com.yqglpt.utils;


import java.util.Random;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;


/**
 * 将所有Shiro指令封装成HTTL的函数。
 *
 * @author dafei
 */
public class ShiroKit {



	
	/**
	 * 加盐参数
	 */
	public final static String hashAlgorithmName = "MD5";

	/**
	 * 循环次数
	 */
	public final static int hashIterations = 1024;

	/**
	 * 禁止初始化
	 */
	private ShiroKit() {}


	
	
	
	/**
	 * shiro密码加密工具类
	 * 
	 * @param credentials 密码
	 * @param saltSource 密码盐
	 * @return
	 */
	public static String md5(String credentials, String saltSource) {
		ByteSource salt = new Md5Hash(saltSource);
		return new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations).toString();
	}

	/**
	 * 获取随机盐值
	 * @param length
	 * @return
	 */
	public static String getRandomSalt(int length) { 
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }  
}