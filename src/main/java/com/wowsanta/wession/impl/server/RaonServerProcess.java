package com.wowsanta.wession.impl.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

import com.wowsanta.server.nio.NioProcess;
import com.wowsanta.util.Hex;
import com.wowsanta.wession.impl.cmd.HELLO;
import com.wowsanta.wession.impl.cmd.PS_DELSESSION;
import com.wowsanta.wession.impl.cmd.PS_REGISTER;
import com.wowsanta.wession.impl.cmd.PS_SESSIONVALID;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RaonServerProcess extends NioProcess{
	private RaonCommandProcessor cmd_proc ;
	public void read() {
		log.debug("read : {} ", Hex.toHexString(this.connection.readBuffer.array()));
		this.connection.readBuffer.flip();
		
		byte[] command = new byte[4];
		this.connection.readBuffer.get(command);

		cmd_proc = create_command_process(ByteBuffer.wrap(command).getInt());
		cmd_proc.request(this.connection.readBuffer);
	}
	public void write() {
		try {
			this.connection.write(cmd_proc.response());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void run() {
		cmd_proc.process();
	}

	private RaonCommandProcessor create_command_process(int cmd) {
		RaonCommandProcessor cmd_proc = null;
		log.debug("command : {} ", cmd);
		
		switch (cmd) {
		case 1:
			cmd_proc = new HELLO();
			break;
		case 66304:
			cmd_proc = new PS_DELSESSION();
			break;
		case 67072:
			cmd_proc = new PS_REGISTER();
			break;
		case 68096:
			cmd_proc = new PS_SESSIONVALID();
			break;
			
		default:
			break;
		}
		
		
		return cmd_proc;
	}
}
