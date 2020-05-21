package com.wowsanta.raon.impl.session;

import com.wowsanta.util.Hex;

public class SessionKeyGenerator {
	public static String generate(String seed1, int seed2) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(seed1.hashCode()).append(seed2);
		return Hex.toHexString(buffer.toString().getBytes());
	}
	
	public static String generate(byte[] seed1,byte[] seed2) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(Hex.toHexString(seed1));
		buffer.append("-");
		buffer.append(Hex.toHexString(seed2));
		return buffer.toString();
	}
}
