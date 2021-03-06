package com.wowsanta.raon.impl.session;

import java.util.Date;
import java.util.Properties;

import com.wowsanta.wession.session.Wession;

import lombok.Data;

@Data
public class RaonSession implements Wession {
	private static final long serialVersionUID = -6949212685743717047L;
	String key;
	String userId;
	byte[] index;   
	String random;
	String token;
	String tokenOtp;
	
	byte[] option;
	Date   createTime;
	Date   modifyTime;
	Properties attributes = new Properties();
	
	public void setAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}
	public Object getAttribute(String key) {
		return this.attributes.get(key);
	}
	public void removeAttribute(String key) {
		this.attributes.remove(key);
	}
}
