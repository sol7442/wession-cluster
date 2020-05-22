package com.wowsanta.raon.impl.proc;

import java.util.Random;

import com.wowsanta.server.Session;
import com.wowsanta.util.Hex;

public class SessionImp implements Session {
private final String id;
	
	private SessionImp(String id) {
		this.id = id;
	}
	
	public static SessionImp generate() {
		return new SessionImp(Hex.toHexString(new Random().nextLong()));
	}

	@Override
	public String getId() {
		return this.id;
	}
}
