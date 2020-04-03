package com.wowsanta.wession.impl.cmd;

import java.nio.ByteBuffer;
import java.util.Date;

import com.wowsanta.util.Hex;
import com.wowsanta.wession.impl.cmd.PS_REGISTER.Request;
import com.wowsanta.wession.impl.cmd.PS_REGISTER.Response;
import com.wowsanta.wession.impl.server.RaonCommandProcessor;
import com.wowsanta.wession.impl.session.RaonCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PS_SESSIONVALID extends RaonCommandProcessor{

	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Request extends AbstratRequest{
		RaonCommand command = RaonCommand.CMD_PS_SESSIONVALID;
		String userId;
		String random;
		byte[] sessionIdx = new byte[4];
		
		int option;
		String tokenId;
		String tokenOtp;

		Request(ByteBuffer buffer){
			userId = readStr(buffer);
			random = readStr(buffer);
			buffer.get(sessionIdx);
			option = buffer.getInt();
			switch(option) {
			case 65536 : 	//00010000
				tokenId = readRsrt(buffer);
				break;
			case 131072:	//00020000
				tokenOtp = readRsrt(buffer);
				break;
			case 196608:	//00030000
				tokenId  = readRsrt(buffer);
				tokenOtp = readRsrt(buffer);
				break;
			}
			//log.debug("option >> :  {} ", option);
			//log.debug("option >> :  {} ", Hex.toHexString(ByteBuffer.allocate(4).putInt(option).array()));
		}
	}
	
	@Data
	@EqualsAndHashCode(callSuper=false)
	public class Response extends AbstratResponse{
		RaonCommand command = RaonCommand.CMD_PS_SESSIONVALID;
		int lot;
		int lat;
		byte[] option = new byte[4];
		String tokenId;
		
		@Override
		byte[] reponse() {
			RSTR token_str = new RSTR((byte) 0x00,tokenId);
			
			int data_len = 16 + token_str.data_len;
			
			ByteBuffer buffer = ByteBuffer.allocate(data_len);
			buffer.putInt(command.getValue());
			buffer.putInt(lot);
			buffer.putInt(lat);
			buffer.put(option);
			token_str.write(buffer);
			
			byte[] data = buffer.array();
			log.debug("register.response : {} ", Hex.toHexString(data));
			return data;
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
		response.lat = (int)new Date().getTime();
		response.lot = (int)System.currentTimeMillis();
		
		response.tokenId = request.tokenId;
		response.option[1] = (byte)0x01;
		
		log.debug("response : {}",response);
		return false;
	}

}
