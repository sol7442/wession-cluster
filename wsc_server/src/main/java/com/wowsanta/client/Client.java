package com.wowsanta.client;

import java.io.IOException;

import com.wowsanta.server.Request;
import com.wowsanta.server.Response;
import com.wowsanta.util.config.JsonConfiguration;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class Client extends JsonConfiguration{
	public abstract boolean connect() throws IOException;
	public abstract void close() throws IOException;
	public abstract void write(Request reqeust)throws IOException;
	public abstract Response send(Request reqeust)throws IOException, ClassNotFoundException;

}