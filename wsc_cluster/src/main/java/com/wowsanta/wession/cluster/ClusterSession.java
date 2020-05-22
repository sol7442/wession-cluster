package com.wowsanta.wession.cluster;

import java.util.Random;

import com.wowsanta.server.Session;
import com.wowsanta.util.Hex;


public class ClusterSession implements Session {
	private final String id;
	
	private ClusterSession(String id) {
		this.id = id;
	}
	
	public static ClusterSession generate() {
		return new ClusterSession(Hex.toHexString(new Random().nextLong()));
	}

	@Override
	public String getId() {
		return this.id;
	}
}
