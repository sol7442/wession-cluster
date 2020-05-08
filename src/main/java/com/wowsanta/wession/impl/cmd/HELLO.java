package com.wowsanta.wession.impl.cmd;

import java.io.IOException;

import com.wowsanta.server.ServerException;
import com.wowsanta.wession.impl.data.CMD;
import com.wowsanta.wession.impl.data.INT;
import com.wowsanta.wession.impl.data.RaonSessionRequest;
import com.wowsanta.wession.impl.data.RaonSessionResponse;
import com.wowsanta.wession.impl.data.STR;
import com.wowsanta.wession.impl.server.RaonCommandProcessor;
import com.wowsanta.wession.impl.session.RaonCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HELLO extends RaonCommandProcessor {
	HELLO_Request  hello_request  ;
	HELLO_Response hello_response = new HELLO_Response();
	
	@Override
	public void setData(byte[] data) {
		hello_request = new HELLO_Request(data);
		hello_request.parse();
	}
	@Override
	public byte[] getData() {
		return hello_response.toBytes();
	}
	
	public class HELLO_Request extends RaonSessionRequest{
		private static final long serialVersionUID = 2076520011329680994L;
		CMD  command = new CMD(RaonCommand.CMD_HELLO.getValue());
		INT byteOrder;
		INT productNum;
		STR version;
		
		public HELLO_Request(byte[] data) {
			super(data);
		}

		@Override
		public void parse() {
			byteOrder 	= readInt(4);
			productNum 	= readInt(8);
			version    	= readStr(12);
		}
	}
	
	public class HELLO_Response extends RaonSessionResponse{
		private static final long serialVersionUID = -4222670362927770022L;
		CMD command   = new CMD(RaonCommand.CMD_HELLO.getValue());
		INT byteOrder = new INT(1);
		INT code;
		STR version = new STR("WOWSANTA WESSION - WITH WISEACCESS : b2020.04");
		

		@Override
		public byte[] toBytes() {
			allocate(CMD.LENGTH + INT.LENGTH + INT.LENGTH + version.getLength());
			
			writeCommand(hello_response.command.getValue());
			writeInt(hello_response.byteOrder);
			writeInt(hello_response.code);
			writeStr(hello_response.version);
			
			return buffer.array();
		}
	}
	
	@Override
	public void run() throws ServerException {
		hello_response.code = new INT(0);
	}
}
