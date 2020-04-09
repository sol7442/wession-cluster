package com.wowsanta.wession.impl.cmd;

import java.nio.ByteBuffer;
import java.util.Random;

import com.wowsanta.util.Hex;
import com.wowsanta.wession.impl.data.BYTE4;
import com.wowsanta.wession.impl.data.CMD;
import com.wowsanta.wession.impl.data.INT;
import com.wowsanta.wession.impl.data.RSTRS;
import com.wowsanta.wession.impl.data.STR;
import com.wowsanta.wession.impl.server.RaonCommandProcessor;
import com.wowsanta.wession.impl.session.RaonCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper=true)
public class PS_REGISTER extends RaonCommandProcessor {
	private static final CMD  command= new CMD(RaonCommand.CMD_PS_REGISTER.getValue());

	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Request extends AbstratRequest{
		INT option;
		STR userId;
		
		Request(ByteBuffer buffer){
			option = INT.read(buffer);
			userId = STR.read(buffer);
		}
	}
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Response extends AbstratResponse{
		INT lot;
		BYTE4 session_idx;
		RSTRS data = new RSTRS();
		
		@Override
		byte[] reponse() {
			int data_len = command.getLength() + lot.getLength() + session_idx.getLength() + data.getLength();
			ByteBuffer buffer = ByteBuffer.allocate(data_len);

			command.write(buffer);
			lot.write(buffer);
			session_idx.write(buffer);
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
			
			response.lot = new INT((int)System.currentTimeMillis());
			byte[] session = {0x0,0x0,0x0,0x0};
			response.session_idx = new BYTE4(session);
			response.session_idx.set(1,(byte) 'A');
			response.session_idx.set(3,(byte)'"');
			
			byte[] token_key = new byte[8];
			byte[] random_key = new byte[8];
			new Random().nextBytes(token_key);
			new Random().nextBytes(random_key);
			
			response.data.add((byte) 0x00,Hex.toHexString(random_key));
			response.data.add((byte) 0x01,Hex.toHexString(token_key));
			
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
