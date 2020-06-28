package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.server.ServerException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class AccountDelRequestMessage extends RaonSessionMessage {
	private static final long serialVersionUID = RaonCommand.CMD_WRM_DELACCOUNTS.getValue();
	
	RaonCommand command = RaonCommand.CMD_WRM_DELACCOUNTS;//new CMD(RaonCommand.CMD_WRM_DELACCOUNTS.getValue());
	INT count;
	STR[] accounts;
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public int parse(ByteBuffer buffer) throws ServerException{
		int result = -1;
		try {
			if(count == null) {
				count = INT.parse(buffer);
			}
			
			if(accounts == null) {
				accounts = new STR[count.getValue()];
			}
			
			for(int i=0; i<count.getValue(); i++) {
				if(accounts[i] == null) {
					accounts[i] = STR.parse(buffer);// readStr(buffer); 
				}
			}
			
			result = buffer.remaining();
		}catch (BufferUnderflowException e) {
			LOG.application().warn("{}/{}",buffer,this);
			result = -1;
		}
		
		return result;
	}

	@Override
	public void flush() throws IOException{
	}

	@Override
	public boolean isComplate() {
		boolean result = false;
		if(count != null) {
			int last_index = count.getValue() -1;
			if(last_index >=0 && accounts[last_index] == null) {
				result = true;
			}
		}
		return result;
	}
}
