package com.wowsanta.wession.impl.cmd;

import java.nio.ByteBuffer;

import com.wowsanta.wession.impl.data.BYTE4;
import com.wowsanta.wession.impl.data.CMD;
import com.wowsanta.wession.impl.data.INT;
import com.wowsanta.wession.impl.data.STR;
import com.wowsanta.wession.impl.server.RaonCommandProcessor;
import com.wowsanta.wession.impl.session.RaonCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class PS_DELUSERDATA extends RaonCommandProcessor {
	private static final CMD  command= new CMD(RaonCommand.CMD_PS_DELUSERDATA.getValue());
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Request extends AbstratRequest{
		STR user_id;
		BYTE4 session_idx;

		Request(ByteBuffer buffer){
			this.user_id     = STR.read(buffer); 
			this.session_idx = BYTE4.read(buffer);
		}
	}
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Response extends AbstratResponse{
		INT lot;
		@Override
		byte[] reponse() {
			int data_len = command.getLength() + lot.getLength() ;
			
			ByteBuffer buffer = ByteBuffer.allocate(data_len);
			command.write(buffer);
			lot.write(buffer);
			
			return buffer.array();
		}
	}
	
	
	Request  request;
	Response response;
	
	@Override
	public boolean process() {
		try {
			log.debug("request : {}",request);
			
			response.lot = new INT((int)System.currentTimeMillis());
			
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
