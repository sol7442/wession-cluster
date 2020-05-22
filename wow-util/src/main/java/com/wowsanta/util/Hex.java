package com.wowsanta.util;

import java.nio.ByteBuffer;

public class Hex {
	public static byte[] toByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02X", b & 0xff));
		}
		return sb.toString();
	}
	public static String toHexString(int value) {
		return toHexString(ByteBuffer.allocate(4).putInt(value).array());
	}

	public static String toHexString(long value) {
		return toHexString(ByteBuffer.allocate(8).putLong(value).array());
	}
}
