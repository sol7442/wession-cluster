package com.wowsanta.server;

import java.io.Serializable;

public interface Response extends Serializable {
	public byte[] toBytes();
}
