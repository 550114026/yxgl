package com.yqglpt.utils;
//package com.bookhouse.utils;
//
//import javax.crypto.Cipher;
//import javax.crypto.spec.IvParameterSpec;
//import javax.crypto.spec.SecretKeySpec;
//
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
//
///**
// * AES 是一种可逆加密算法，对用户的敏感信息加密处理 对原始数据进行AES加密后，在进行Base64编码转化； 加密用的Key 可以用26个字母和数字组成
// * 此处使用AES-128-CBC加密模式，key需要为16位。
// */
//public enum AESOperator {
//
//	INSTANCE;
//
//	/**
//	 * 加密
//	 * 
//	 * @param sSrc
//	 * @return
//	 */
//	public String encrypt(String sSrc) {
//		try {
//			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//			byte[] raw = Constants.SKEY.getBytes();
//			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//			IvParameterSpec iv = new IvParameterSpec(Constants.IVPARAMETER.getBytes());// 使用CBC模式，需要一个向量iv，可增加加密算法的强度
//			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
//			byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
//			return new BASE64Encoder().encode(encrypted);// 此处使用BASE64做转码。
//		} catch (Exception e) {
//			return null;
//		}
//
//	}
//
//	/**
//	 * 解密
//	 * 
//	 * @param sSrc
//	 * @return
//	 */
//	public String decrypt(String sSrc) {
//		try {
//			byte[] raw = Constants.SKEY.getBytes("ASCII");
//			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
//			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//			IvParameterSpec iv = new IvParameterSpec(Constants.IVPARAMETER.getBytes());
//			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
//			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(sSrc);// 先用base64解密
//			byte[] original = cipher.doFinal(encrypted1);
//			String originalString = new String(original, "utf-8");
//			return originalString;
//		} catch (Exception ex) {
//			return null;
//		}
//	}
//
//	public static void main(String[] args) {
//		// 需要加密的字串
//		String cSrc = "我是Realfighter";
//		System.out.println(cSrc);
//		// 加密
//		long lStart = System.currentTimeMillis();
//		String enString = AESOperator.INSTANCE.encrypt(cSrc);
//		System.out.println("加密后的字串是：" + enString);
//
//		long lUseTime = System.currentTimeMillis() - lStart;
//		System.out.println("加密耗时：" + lUseTime + "毫秒");
//		// 解密
//		lStart = System.currentTimeMillis();
//		String DeString = AESOperator.INSTANCE.decrypt(enString);
//		System.out.println("解密后的字串是：" + DeString);
//		lUseTime = System.currentTimeMillis() - lStart;
//		System.out.println("解密耗时：" + lUseTime + "毫秒");
//	}
//}