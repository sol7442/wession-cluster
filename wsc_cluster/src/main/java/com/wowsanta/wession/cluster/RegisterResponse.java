package com.wowsanta.wession.cluster;


import java.io.IOException;

import com.wowsanta.server.Connection;
import com.wowsanta.server.Response;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.nio.NioConnection;
import com.wowsanta.util.ObjectBuffer;
import com.wowsanta.wession.message.RegisterMessage;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class RegisterResponse extends RegisterMessage implements Response {
	private static final long serialVersionUID = 6803056689196263678L;
	
	private Connection connection;
	
	@Override
	public void write() throws  ServerException {
		try {
			NioConnection con = (NioConnection) connection;
			
			RegisterMessage message = new RegisterMessage();
			message.setSize(this.getSize());
			
			con.getWriteBuffer().put(ObjectBuffer.toByteArray(message));
			
			this.connection.write0();
		} catch (IOException e) {
			throw new ServerException(e.getMessage(),e);
		}
	}
}
