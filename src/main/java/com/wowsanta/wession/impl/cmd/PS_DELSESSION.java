package com.wowsanta.wession.impl.cmd;

import java.nio.ByteBuffer;
import java.util.Random;

import com.wowsanta.util.Hex;
import com.wowsanta.wession.impl.data.BYTE4;
import com.wowsanta.wession.impl.data.CMD;
import com.wowsanta.wession.impl.data.STR;
import com.wowsanta.wession.impl.server.RaonCommandProcessor;
import com.wowsanta.wession.impl.session.RaonCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper=true)
public class PS_DELSESSION extends RaonCommandProcessor {
	private static final CMD  command= new CMD(RaonCommand.CMD_PS_DELSESSION.getValue());
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Request extends AbstratRequest{
		BYTE4 session_idx;
		STR userId;
		
		
		Request(ByteBuffer buffer){
			session_idx = BYTE4.read(buffer);
			userId = STR.read(buffer);
		}
	}
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Response extends AbstratResponse{
		
		@Override
		byte[] reponse() {
			int data_len = command.getLength() ;
			ByteBuffer buffer = ByteBuffer.allocate(data_len);
			
			command.write(buffer);
			
			return buffer.array();
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