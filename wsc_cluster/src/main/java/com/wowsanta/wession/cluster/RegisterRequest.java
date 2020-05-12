package com.wowsanta.wession.cluster;

import java.io.IOException;

import com.wowsanta.server.Connection;
import com.wowsanta.server.Request;
import com.wowsanta.wession.message.RegisterMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class RegisterRequest extends RegisterMessage implements Request{
	private static final long serialVersionUID = 7895824136265992156L;
	private Connection connection;
	
	@Override
	public void read() throws IOException {
		
	}
}
