package com.wowsanta.wession.impl.cmd;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Random;

import com.wowsanta.util.Hex;
import com.wowsanta.wession.impl.server.RaonCommandProcessor;
import com.wowsanta.wession.impl.session.SessionServerCommand;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@ToString(exclude= {"readBuffer","writeBuffer"})
public class PS_REGISTER implements RaonCommandProcessor {
	byte[] command = SessionServerCommand.CMD_PS_REGISTER;
	byte[] optData = new byte[4];
	int dataLen;
	String userId;
	int length;
	
	transient ByteBuffer writeBuffer;
	transient ByteBuffer readBuffer;
	
	@Override
	public void setRequest(ByteBuffer readBuffer) {
		this.readBuffer = readBuffer;
		this.writeBuffer = ByteBuffer.allocate(1024);
	}
	@Override
	public byte[] getResponse() {
		byte[] buffer = new byte[length + 1];
		
		//this.writeBuffer.rewind();
		
		this.writeBuffer.flip();
		this.writeBuffer.get(buffer,0,length);
		this.writeBuffer.clear();
		
		log.debug("response : {}",Hex.toHexString(buffer));
		return buffer;
	}

	@Override
	public boolean process() {
		try {
			request();
			log.debug("request : {}",this);
			
			response(new Date().getTime());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void response(long lot) {
		this.writeBuffer.put(SessionServerCommand.CMD_PS_REGISTER);
		this.writeBuffer.putInt((int) lot);
		this.writeBuffer.put((byte) 0x00);//-- prefix
		this.writeBuffer.put((byte) 0x65);//
		this.writeBuffer.put((byte) 0x00);//
		this.writeBuffer.put((byte) 0x34);//-- prefix
		this.writeBuffer.put((byte) 0x00);//-- data
		this.writeBuffer.put((byte) 0x03);//-- data -- 10,01 
		this.writeBuffer.put((byte) 0x00);//-- data
		this.writeBuffer.put((byte) 0x00);//-- data
		
		byte[] random_key = new byte[16];
		this.writeBuffer.putInt(random_key.length +  1);//-- length
		this.writeBuffer.put((byte) 0x00);//-- data index = 0
		new Random().nextBytes(random_key);
		this.writeBuffer.put(random_key);//
		this.writeBuffer.put((byte) 0x00); //--padding
		this.writeBuffer.put((byte) 0x00); //--padding
		this.writeBuffer.put((byte) 0x00); //--padding
		
		byte[] token_key = new byte[16];
		this.writeBuffer.putInt(token_key.length +  1);//-- length
		this.writeBuffer.put((byte) 0x01);//-- data index = 1
		new Random().nextBytes(token_key);
		this.writeBuffer.put(token_key);
		this.writeBuffer.put((byte) 0x00); //--padding
		this.writeBuffer.put((byte) 0x00); //--padding
		this.writeBuffer.put((byte) 0x00); //--padding
		
		length = 4 + 4 + 4 + 4 + 4 +random_key.length + 4 + 4 + token_key.length;
	}

	private void request() {
		this.readBuffer.get(optData);
		dataLen = this.readBuffer.getInt();
		byte[] data = new byte[dataLen];
		this.readBuffer.get(data, 0, dataLen);
		userId = new String(data);
	}

	

}
