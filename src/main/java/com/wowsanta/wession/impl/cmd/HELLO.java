package com.wowsanta.wession.impl.cmd;

import java.nio.ByteBuffer;

import com.wowsanta.util.Hex;
import com.wowsanta.wession.impl.data.CMD;
import com.wowsanta.wession.impl.data.INT;
import com.wowsanta.wession.impl.data.STR;
import com.wowsanta.wession.impl.server.RaonCommandProcessor;
import com.wowsanta.wession.impl.session.RaonCommand;
import com.wowsanta.wession.impl.session.SessionServerCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper=true)
public class HELLO extends RaonCommandProcessor {
	private static final CMD  command= new CMD(RaonCommand.CMD_HELLO.getValue());

	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Request extends AbstratRequest{
		INT byteOrder;
		INT productNum;
		STR version;
		
		Request(ByteBuffer buffer){
			byteOrder  = INT.read(buffer);
			productNum = INT.read(buffer);
			version    = STR.read(buffer);
		}
	}
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Response extends AbstratResponse{
		INT byteOrder = new INT(1);
		INT code;
		STR version = new STR("WOWSANTA WESSION - WITH WISEACCESS : b2020.04");
		
		@Override
		byte[] reponse() {
			int data_len = command.getLength() + byteOrder.getLength() + code.getLength() + version.getLength();
			ByteBuffer buffer = ByteBuffer.allocate(data_len);

			command.write(buffer);
			byteOrder.write(buffer);
			code.write(buffer);
			version.write(buffer);
			
			return buffer.array();
		}
	}
	 
	Request  request;
	Response response;
	
	@Override
	public boolean process() {
		try {
			log.debug("request : {}", request);
			response.code = new INT(0);
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
