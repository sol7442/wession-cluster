package com.wowsanta.wession.impl.cmd;

import java.nio.ByteBuffer;
import java.util.Date;

import com.wowsanta.util.Hex;
import com.wowsanta.wession.impl.cmd.PS_REGISTER.Request;
import com.wowsanta.wession.impl.cmd.PS_REGISTER.Response;
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
public class PS_SESSIONVALID extends RaonCommandProcessor{
	private static final CMD  command= new CMD(RaonCommand.CMD_PS_SESSIONVALID.getValue());
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Request extends AbstratRequest{
		STR userId;
		STR random;
		BYTE4 sessionIdx;
		RSTRS data;
		

		Request(ByteBuffer buffer){
			userId = STR.read(buffer);
			random = STR.read(buffer);
			sessionIdx = BYTE4.read(buffer);
			data = RSTRS.read(buffer);
		}
	}
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Response extends AbstratResponse{
		INT lot;
		INT lat;
		RSTRS data = new RSTRS();
		
		@Override
		byte[] reponse() {
			int data_len = command.getLength() + lot.getLength() + lot.getLength() + data.getLength();
			ByteBuffer buffer = ByteBuffer.allocate(data_len);

			command.write(buffer);
			lot.write(buffer);
			lat.write(buffer);
			data.write(buffer);
			
			return buffer.array();
		}
	}
	
	Request  request;
	Response response;
	
	
	@Override
	public void request(ByteBuffer buffer) {
		request  = new Request(buffer);
		response = new Response(); 
	}

	@Override
	public byte[] response() {
		return response.reponse();
	}

	@Override
	public boolean process() {
		log.debug("request : {}",request);
		
		response.lat = new INT((int)new Date().getTime()) ;
		response.lot = new INT((int)System.currentTimeMillis());
		response.data.add((byte)0x0,request.data.get(0x00));
		
		log.debug("response : {}",response);
		
		return false;
	}

}
