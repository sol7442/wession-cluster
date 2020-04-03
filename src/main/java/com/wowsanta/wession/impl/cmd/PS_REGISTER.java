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
public class PS_REGISTER extends RaonCommandProcessor {
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Request extends AbstratRequest{
		RaonCommand command = RaonCommand.CMD_PS_REGISTER;
		int option;
		String userId;
		
		Request(ByteBuffer buffer){
			option = buffer.getInt();
			userId = readStr(buffer);
		}
	}
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Response extends AbstratResponse{
		RaonCommand command = RaonCommand.CMD_PS_REGISTER;
		int lot;
		byte[] prefix = new byte[4];//{0x00,0x65,0x00,0x34};//
		byte[] option = new byte[4];
		byte[] token_key = new byte[8];
		byte[] random_key = new byte[8];
		
		@Override
		byte[] reponse() {
			RSTR random_str = new RSTR((byte) 0x00,Hex.toHexString(random_key));
			RSTR token_str = new RSTR((byte) 0x01,Hex.toHexString(token_key));
			
			int data_len = 16 + token_str.data_len + random_str.data_len;
			log.debug("random_key : {}, {} ", Hex.toHexString(random_key),Hex.toHexString(random_key).length() );
			log.debug("data_len : {} m {} m {} p {}", data_len, token_str.data_len, token_str.length, token_str.padding);
			
			ByteBuffer buffer = ByteBuffer.allocate(data_len);
			buffer.putInt(command.getValue());
			buffer.putInt(lot);
			buffer.put(prefix);
			buffer.put(option);
			random_str.write(buffer);
			token_str.write(buffer);
			
			byte[] data = buffer.array();
			
			log.debug("register.response : {} ", Hex.toHexString(data));
			return data;
		}
	}
	
	
	Request  request;
	Response response;
	
	@Override
	public boolean process() {
		try {
			log.debug("request : {}",request);
			
			response.lot = (int)System.currentTimeMillis();
			response.prefix[1] = 65;
			response.prefix[3] = 34;
			
			response.option[1] = 0x03;
			new Random().nextBytes(response.token_key);
			new Random().nextBytes(response.random_key);
			
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
