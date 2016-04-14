package com.xhc.util;

import java.util.UUID;

/**
 * UUID生成器
 * 
 */
public class UUIDGen {

	/**
	 * 生成32位UUID
	 */
	public static String generate() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}

}
