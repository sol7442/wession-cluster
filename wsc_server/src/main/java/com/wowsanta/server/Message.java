package com.wowsanta.server;

import java.io.IOException;
import java.io.Serializable;

public interface Message extends Serializable {
	public byte[] toBytes() throws IOException;
	public void flush() throws IOException;
}
