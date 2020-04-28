package com.wowsanta.server;

import com.wowsanta.util.config.JsonConfiguration;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class Server extends JsonConfiguration{
	static {
		addTypeAdapter(Server.class);
	}
	String name;
	public abstract boolean initialize();
	public abstract void start();
	public abstract void stop();
}
