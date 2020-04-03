package com.wowsanta.wession.impl.cmd;

import java.nio.ByteBuffer;
import java.util.Random;

import com.wowsanta.util.Hex;
import com.wowsanta.wession.impl.server.RaonCommandProcessor;
import com.wowsanta.wession.impl.session.RaonCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class PS_DELSESSION extends RaonCommandProcessor {
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Request extends AbstratRequest{
		RaonCommand command = RaonCommand.CMD_PS_DELSESSION;
		byte[] sessionIdx = new byte[4];
		String userId;
		
		Request(ByteBuffer buffer){
			buffer.get(sessionIdx);
			userId = readStr(buffer);
		}
	}
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Response extends AbstratResponse{
		RaonCommand command = RaonCommand.CMD_PS_DELSESSION;
		
		@Override
		byte[] reponse() {
			int data_len = 4;
			ByteBuffer buffer = ByteBuffer.allocate(data_len);
			buffer.putInt(command.getValue());
			
			byte[] data = buffer.array();
			log.debug("delete.response : {} ", Hex.toHexString(data));
			return data;
		}
	}
	
	
	Request  request;
	Response response;
	
	@Override
	public boolean process() {
		try {
			log.debug("request : {}",request);
			
			log.debug("response : {}",response);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void request(ByteBuffer buffer) {
		request = new Request(buffer);
		response = new Response(); 
	}

	@Override
	public byte[] response() {
		return response.reponse();
	}
}
