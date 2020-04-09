package com.wowsanta.wession.impl.cmd;

import java.nio.ByteBuffer;
import java.util.Random;

import com.wowsanta.util.Hex;
import com.wowsanta.wession.impl.data.BYTE4;
import com.wowsanta.wession.impl.data.CMD;
import com.wowsanta.wession.impl.data.INT;
import com.wowsanta.wession.impl.data.RSTR;
import com.wowsanta.wession.impl.data.RSTRS;
import com.wowsanta.wession.impl.data.STR;
import com.wowsanta.wession.impl.server.RaonCommandProcessor;
import com.wowsanta.wession.impl.session.RaonCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class PS_GETTOKENOTP extends RaonCommandProcessor {
	private static final CMD  command= new CMD(RaonCommand.CMD_PS_GETTOKENOTP.getValue());
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Request extends AbstratRequest{
		STR user_id;
		STR random;
		BYTE4 session_idx;
		RSTRS  data;
		
		Request(ByteBuffer buffer){
			this.user_id     = STR.read(buffer); 
			this.random      = STR.read(buffer);
			this.session_idx = BYTE4.read(buffer);
			this.data        = RSTRS.read(buffer);
		}
	}
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Response extends AbstratResponse{
		RSTRS  data = new RSTRS();
		@Override
		byte[] reponse() {
			int data_len = command.getLength() + data.getLength();
			
			ByteBuffer buffer = ByteBuffer.allocate(data_len);
			command.write(buffer);
			data.write(buffer);
			
			return buffer.array();
		}
	}
	
	
	Request  request;
	Response response;
	
	@Override
	public boolean process() {
		try {
			log.debug("request : {}",request);
			
			response.data.add((byte)0x1,"value1"); 
			response.data.add((byte)0x2,"value2");
			
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
