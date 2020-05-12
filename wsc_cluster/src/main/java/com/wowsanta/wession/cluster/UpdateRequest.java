package com.wowsanta.wession.cluster;

import java.io.IOException;

import com.wowsanta.server.Connection;
import com.wowsanta.server.Request;
import com.wowsanta.server.Response;
import com.wowsanta.wession.message.CreateMessage;
import com.wowsanta.wession.session.Wession;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class UpdateRequest extends CreateMessage implements Request{
	private static final long serialVersionUID = 6670989908562946675L;
	
	private Connection connection;
	public UpdateRequest(Wession s) {
		super(s);
	}
	@Override
	public Response getResponse() {
		return null;
	}
	@Override
	public void read() throws IOException {
		
	}
}
