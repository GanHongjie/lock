package com.yiyun.lockcontroller.utils.rc4;

import java.util.Random;

public class StringUtil {

	public static boolean isBlank(final CharSequence cs) {
		int strLen;
		if (cs == null || (strLen = cs.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (Character.isWhitespace(cs.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取16进制随机数
	 * @param len
	 * @return
	 */
	public static String randomHexString(int len)  {
		try {
			StringBuilder result = new StringBuilder();
			for(int i=0;i<len;i++) {
				result.append(Integer.toHexString(new Random().nextInt(16)));
			}
			return result.toString().toLowerCase();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

		}
		return null;

	}

	public static String reverse(final String str) {
		if (str == null) {
			return null;
		}
		return new StringBuilder(str).reverse().toString();
	}


	/**
	 * 将string转化为变成16进制的assic的string
	 *
	 * @param str
	 * @return
	 */
	public static String str2ASSStr16(String str) {
		String transStr = "";
		char[] chars = str.toCharArray();
		for (int i = 0; i < str.length(); i++) {
			String strHex = Integer.toHexString((int) chars[i]);
			transStr += strHex;
		}

		return transStr;
	}
}
