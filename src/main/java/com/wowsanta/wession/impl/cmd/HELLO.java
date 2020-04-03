package com.wowsanta.wession.impl.cmd;

import java.nio.ByteBuffer;

import com.wowsanta.util.Hex;
import com.wowsanta.wession.impl.server.RaonCommandProcessor;
import com.wowsanta.wession.impl.session.RaonCommand;
import com.wowsanta.wession.impl.session.SessionServerCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class HELLO extends RaonCommandProcessor {
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Request extends AbstratRequest{
		RaonCommand command = RaonCommand.CMD_HELLO;
		int byteOrder;
		int productNum;
		String version;
		
		Request(ByteBuffer buffer){
			byteOrder  = buffer.getInt();
			productNum = buffer.getInt();
			int str_len = buffer.getInt();
			byte[] str_data = new byte[str_len];
			buffer.get(str_data);
			version = new String(str_data);
		}
	}
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Response extends AbstratResponse{
		RaonCommand command = RaonCommand.CMD_HELLO;
		int byteOrder = 1;
		int code;
		String version = "WOWSANTA WESSION - WITH WISEACCESS : b2020.04";
		
		@Override
		byte[] reponse() {
			STR str = new STR(version);
			int data_len = 12 + str.length + 1;
			
			ByteBuffer buffer = ByteBuffer.allocate(data_len);
			buffer.putInt(command.getValue());
			buffer.putInt(byteOrder);
			buffer.putInt(code);
			str.write(buffer);
			
			byte[] data = buffer.array();
			
			log.debug("hellow.response : {} ", Hex.toHexString(data));
			return data;
		}
	}
	 
	Request  request;
	Response response;
	
	@Override
	public boolean process() {
		try {
			log.debug("request : {}", request);
			log.info("byte odre {} ", request.byteOrder == 1 ? "BIG_ENDIAN" : "LITTLE_ENDIAN") ;
			response.code = 0;
			log.debug("response : {}", response);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void request(ByteBuffer readBuffer) {
		request  = new Request(readBuffer);
		response = new Response(); 
	}

	@Override
	public byte[] response() {
		return response.reponse();
	}
}
