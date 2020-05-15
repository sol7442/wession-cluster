package com.wowsanta.raon.impl.data;

import com.wowsanta.server.Request;

public abstract class RaonSessionRequest implements Request {
	protected final byte[] bytes;
	public RaonSessionRequest(byte[] data) {
		bytes = data;
	}
	public int readCommand() {
		return new INT(bytes,0).value;
	}
	public INT readInt(int idx) {
		return new INT(bytes, idx);
	}
	public STR readStr(int idx) {
		return new STR(bytes,idx);
	}
	
}
