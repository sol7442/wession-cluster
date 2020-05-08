package com.wowsanta.server;

import java.io.Serializable;

public interface Request extends Serializable {
	public void parse();
}
