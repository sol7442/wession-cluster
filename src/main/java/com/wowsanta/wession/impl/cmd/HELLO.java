package com.wowsanta.wession.impl.cmd;

import java.nio.ByteBuffer;

import com.wowsanta.util.Hex;
import com.wowsanta.wession.impl.server.RaonCommandProcessor;
import com.wowsanta.wession.impl.session.SessionServerCommand;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(exclude= {"readBuffer","writeBuffer"})
public class HELLO implements RaonCommandProcessor {
	byte[] command = SessionServerCommand.CMD_HELLO;
	byte[] byteorder = new byte[4];
	int productNum;
	int dataLen;
	String version;
	
	ByteBuffer writeBuffer;
	ByteBuffer readBuffer;
	
	int length;
	
	@Override
	public boolean process() {
		try {
			request();
			log.debug("request : {}",this);
			
			response();
//			this.writeBuffer.flip();
//			log.debug("response : {}",Hex.toHexString(this.writeBuffer.array()));
//			this.writeBuffer.flip();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void response() {
		this.writeBuffer.put(SessionServerCommand.CMD_HELLO); 	// 4
		this.writeBuffer.put(byteorder);						// 4
		this.writeBuffer.putInt(0);								// 4
		this.writeBuffer.putInt(version.length());				// 4
		this.writeBuffer.put(version.getBytes());				// 25
		
		length = 4 + 4 + 4 + 4 + version.length();}

	private void request() {
		this.readBuffer.get(byteorder);
		productNum = this.readBuffer.getInt();
		dataLen = this.readBuffer.getInt();
		byte[] data = new byte[dataLen];
		this.readBuffer.get(data, 0, dataLen);
		version = new String(data);
	}

	@Override
	public void setRequest(ByteBuffer readBuffer) {
		this.readBuffer  = readBuffer;
		this.writeBuffer = ByteBuffer.allocate(1024); 
	}

	@Override
	public byte[] getResponse() {
		byte[] buffer = new byte[length+1];
		this.writeBuffer.flip();
		this.writeBuffer.get(buffer,0,length);
		this.writeBuffer.clear();
		log.debug("response : {}",Hex.toHexString(buffer));
		return buffer;
	}

}
