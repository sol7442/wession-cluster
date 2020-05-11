package com.wowsanta.server;

import java.io.Serializable;

public interface Request extends Serializable {
	public Connection getConnection();
	public void setConnection(Connection conn);
	
	public void parse();
}
