package com.wowsanta.wession.impl.server;

import java.io.IOException;

import com.wowsanta.server.ServerException;
import com.wowsanta.server.nio.NioProcessHandler;
import com.wowsanta.util.Hex;
import com.wowsanta.wession.impl.cmd.HELLO;
import com.wowsanta.wession.impl.cmd.PS_ADDUSERDATA;
import com.wowsanta.wession.impl.cmd.PS_DELSESSION;
import com.wowsanta.wession.impl.cmd.PS_DELUSERDATA;
import com.wowsanta.wession.impl.cmd.PS_GETTOKENOTP;
import com.wowsanta.wession.impl.cmd.PS_GETUSERDATA;
import com.wowsanta.wession.impl.cmd.PS_REGISTER;
import com.wowsanta.wession.impl.cmd.PS_SESSIONVALID;
import com.wowsanta.wession.impl.data.INT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RaonSessionHandler extends NioProcessHandler{
	private RaonCommandProcessor command_process ;
	
	@Override
	public int read() throws IOException {
		int length = -1;
		try {
			length = this.connection.remaining();
			
			log.debug("read lenght : {}",length) ;
			if(length > 0) {
				byte[] bytes = new byte[length];
				this.connection.read(bytes);
				
				command_process = create_command_process(bytes);
				command_process.setData(bytes);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return length;
	}

	@Override
	public void run() throws ServerException {
		command_process.run();
	}

	@Override
	public int write() throws IOException {
		byte[] data  = command_process.getData();
		this.connection.write(data);
		return data.length;
	}


	@Override
	public void error(Throwable e) throws IOException {
	}
	
	
	private RaonCommandProcessor create_command_process(byte[] bytes) {
		RaonCommandProcessor cmd_proc = null;
		
		int command = new INT(bytes,0).getValue();
		log.debug("Session Command : {}", Hex.toHexString(command));
		
		
		switch (command) {
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
		case 65792:
			cmd_proc = new PS_ADDUSERDATA();
			break;
		case 65795:
			cmd_proc = new PS_GETUSERDATA();
			break;
		case 65794:
			cmd_proc = new PS_DELUSERDATA();
			break;
		case 68097:
			cmd_proc = new PS_GETTOKENOTP();
			break;
		default:
			break;
		}
		return cmd_proc;
	}








}
