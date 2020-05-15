package test.raon.cmd;

import java.nio.ByteBuffer;

import org.junit.Test;

import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.raon.impl.session.SessionServerCommand;

public class RaonCommandTest {

	@Test
	public void command_test() {
		System.out.println("CMD_HELLO : " + ByteBuffer.wrap(SessionServerCommand.CMD_HELLO).getInt()); 
		System.out.println("CMD_ERROR : " + ByteBuffer.wrap(SessionServerCommand.CMD_ERROR).getInt());
		System.out.println("CMD_INFOREQ : " + ByteBuffer.wrap(SessionServerCommand.CMD_INFOREQ).getInt());
		System.out.println("CMD_INFORES : " + ByteBuffer.wrap(SessionServerCommand.CMD_INFORES).getInt());
		
		
		System.out.println("CMD_PS_ADDUSERDATA : " + ByteBuffer.wrap(SessionServerCommand.CMD_PS_ADDUSERDATA).getInt()); 
		System.out.println("CMD_PS_UPDUSERDATA : " + ByteBuffer.wrap(SessionServerCommand.CMD_PS_UPDUSERDATA).getInt());
		System.out.println("CMD_PS_DELUSERDATA : " + ByteBuffer.wrap(SessionServerCommand.CMD_PS_DELUSERDATA).getInt());
		System.out.println("CMD_PS_GETUSERDATA : " + ByteBuffer.wrap(SessionServerCommand.CMD_PS_GETUSERDATA).getInt());
		
		System.out.println("CMD_PS_DELACCOUNT : " + ByteBuffer.wrap(SessionServerCommand.CMD_PS_DELACCOUNT).getInt()); 
		System.out.println("CMD_PS_DELSESSION : " + ByteBuffer.wrap(SessionServerCommand.CMD_PS_DELSESSION).getInt());
		System.out.println("CMD_PS_ACCOUNTINFO : " + ByteBuffer.wrap(SessionServerCommand.CMD_PS_ACCOUNTINFO).getInt());
		System.out.println("CMD_PS_SESSIONINFO : " + ByteBuffer.wrap(SessionServerCommand.CMD_PS_SESSIONINFO).getInt());
		
		System.out.println("CMD_PS_REGISTER : " + ByteBuffer.wrap(SessionServerCommand.CMD_PS_REGISTER).getInt()); 
		System.out.println("CMD_PS_UPDATE : " + ByteBuffer.wrap(SessionServerCommand.CMD_PS_UPDATE).getInt());
		System.out.println("CMD_PS_SESSIONDATA : " + ByteBuffer.wrap(SessionServerCommand.CMD_PS_SESSIONDATA).getInt());
		System.out.println("CMD_PS_CHECKRECOVERY : " + ByteBuffer.wrap(SessionServerCommand.CMD_PS_CHECKRECOVERY).getInt());
		
		System.out.println("CMD_PS_SESSIONVALID : " + ByteBuffer.wrap(SessionServerCommand.CMD_PS_SESSIONVALID).getInt());
		System.out.println("CMD_PS_GETTOKENOTP : " + ByteBuffer.wrap(SessionServerCommand.CMD_PS_GETTOKENOTP).getInt());
	}
	
	@Test
	public void command_switch_test() {
		RaonCommand command = RaonCommand.valueOf("CMD_HELLO");// command = ByteBuffer.wrap(SessionServerCommand.CMD_HELLO).getInt();
		
		switch (command) {
		case CMD_HELLO:
			
			break;

		default:
			break;
		}
	}
}
