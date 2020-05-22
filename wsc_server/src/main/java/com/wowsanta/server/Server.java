package com.wowsanta.server;

import com.wowsanta.util.config.JsonConfiguration;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class Server extends JsonConfiguration{
	public abstract boolean initialize() throws ServerException;
	public abstract void start() throws ServerException;
	public abstract void stop();
}
