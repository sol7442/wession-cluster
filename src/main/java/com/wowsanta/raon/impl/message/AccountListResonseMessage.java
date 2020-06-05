package com.wowsanta.raon.impl.message;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RES_ACT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.session.RaonCommand;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class AccountListResonseMessage extends RaonSessionMessage {
	private static final long serialVersionUID = -RaonCommand.CMD_WRM_ACCOUNTS.getValue();
	
	CMD command   = new CMD(RaonCommand.CMD_WRM_ACCOUNTS.getValue());
	INT totalCount;
	INT count1;
	INT count2;
	List<RES_ACT> resources = new ArrayList<RES_ACT>();
	
	@Override
	public byte[] toBytes() throws IOException {
		return this.bytes;
	}

	@Override
	public void parse(ByteBuffer buffer) throws IOException {
		//
	}
	
	public void addResourceAcount(RES_ACT res_act) {
		this.resources.add(res_act);
	}
	@Override
	public void flush() throws IOException{
		int total_size = command.getSize() + INT.LENGTH * 3;  ;
		int resource_size =0;
		for (RES_ACT res : resources) {
			resource_size += res.getSize();
		}
		total_size += resource_size;		
				
				//createTime.getSize() + sessionIndex.getSize() + data.getSize();
		ByteBuffer buffer = ByteBuffer.allocate(total_size);
		
		buffer.put(command.toBytes());
		buffer.put(totalCount.toBytes());
		buffer.put(count1.toBytes());
		buffer.put(count2.toBytes());
		for (RES_ACT res : resources) {
			buffer.put(res.toBytes());
		}
		
		this.bytes = buffer.array();
	}
}
