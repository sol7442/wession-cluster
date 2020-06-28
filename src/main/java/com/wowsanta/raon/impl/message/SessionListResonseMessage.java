package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RES_SES;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.server.ServerException;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class SessionListResonseMessage extends RaonSessionMessage {
	private static final long serialVersionUID = -RaonCommand.CMD_WRM_SESSION.getValue();
	
	RaonCommand command = RaonCommand.CMD_WRM_ACCOUNTS;
	
	INT count;
	List<RES_SES> resources = new ArrayList<RES_SES>();
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public int parse(ByteBuffer buffer) throws ServerException {
		return 0;
	}
	
	public void addResourceAcount(RES_SES res_ses) {
		this.resources.add(res_ses);
	}
	@Override
	public void flush() throws IOException{
		int total_size = command.toCommand().getSize() + INT.LENGTH;
		int resource_size =0;
		for (RES_SES res : resources) {
			resource_size += res.getSize();
		}
		total_size += resource_size;		
				
		ByteBuffer buffer = ByteBuffer.allocate(total_size);
		
		buffer.put(command.toCommand().toBytes());
		buffer.putInt(resources.size());
		for (RES_SES res : resources) {
			buffer.put(res.toBytes());
		}
		
		this.bytes = buffer.array();
	}

	@Override
	public boolean isComplate() {
		return false;
	}
}
