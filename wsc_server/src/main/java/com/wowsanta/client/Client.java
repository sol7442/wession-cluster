package com.wowsanta.client;

import java.io.IOException;

import com.wowsanta.server.Message;
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
	public abstract void write(Message reqeust)throws IOException;
	public abstract Message send(Message reqeust)throws IOException, ClassNotFoundException;

}