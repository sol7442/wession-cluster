package com.wowsanta.raon.impl.session;

import java.nio.ByteBuffer;

import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.AccountDelRequestMessage;
import com.wowsanta.raon.impl.message.AccountListRequestMessage;
import com.wowsanta.raon.impl.message.HelloRequestMessage;
import com.wowsanta.raon.impl.message.RegisterRequestMessage;
import com.wowsanta.raon.impl.message.SessionDelRequestMessage;
import com.wowsanta.raon.impl.message.SessionListRequestMessage;
import com.wowsanta.raon.impl.message.TokenOtpGetRequestMessage;
import com.wowsanta.raon.impl.message.UnkownRequestMessage;
import com.wowsanta.raon.impl.message.UnregisterRequestMessage;
import com.wowsanta.raon.impl.message.UserDataAddRequestMessage;
import com.wowsanta.raon.impl.message.UserDataDelRequestMessage;
import com.wowsanta.raon.impl.message.UserDataGetRequestMessage;
import com.wowsanta.raon.impl.message.UserDataModRequestMessage;
import com.wowsanta.raon.impl.message.VaildateRequestMessage;
import com.wowsanta.util.Hex;

public enum RaonCommand {
	CMD_UNKNOWN	(0) {
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new UnkownRequestMessage();
		}
	},//{0x0,0x0,0x0,0x0};
	CMD_HELLO	(1){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new HelloRequestMessage();
		}
	},//{0x0,0x0,0x0,0x1};
	CMD_ERROR	(2){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return null;
		}
	},//{0x0,0x0,0x0,0x2};
	CMD_INFOREQ	(3){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return null;
		}
	},//{0x0,0x0,0x0,0x3};
	CMD_INFORES	(4){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return null;
		}
	},//{0x0,0x0,0x0,0x4};
	
	CMD_PS_ADDUSERDATA(65792){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new UserDataAddRequestMessage();
		}
	},//{0x0, 0x1, 0x1, 0x0};
	CMD_PS_UPDUSERDATA(65793){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new UserDataModRequestMessage();
		}
	},//{0x0, 0x1, 0x1, 0x1};
	CMD_PS_DELUSERDATA(65794){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new UserDataDelRequestMessage();
		}
	},//{0x0, 0x1, 0x1, 0x2};
	CMD_PS_GETUSERDATA(65795){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new UserDataGetRequestMessage();
		}
	},//{0x0, 0x1, 0x1, 0x3};
	
	CMD_PS_DELACCOUNT	(66048){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new AccountDelRequestMessage();
		}
	}, // {0x0, 0x1, 0x2, 0x0};
	CMD_PS_DELSESSION	(66304){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new UnregisterRequestMessage();
		}
	}, // {0x0, 0x1, 0x3, 0x0};
	CMD_PS_ACCOUNTINFO	(66560){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return null;
		}
	}, // {0x0, 0x1, 0x4, 0x0};
	CMD_PS_SESSIONINFO	(66816){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return null;
		}
	}, // {0x0, 0x1, 0x5, 0x0};
	CMD_PS_REGISTER		(67072){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new RegisterRequestMessage();
		}
	}, // {0x0, 0x1, 0x6, 0x0};
	CMD_PS_UPDATE		(67328){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return null;
		}
	}, // {0x0, 0x1, 0x7, 0x0};
	CMD_PS_SESSIONDATA	(67584){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return null;
		}
	}, // {0x0, 0x1, 0x8, 0x0};
	CMD_PS_CHECKRECOVERY(67840){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return null;
		}
	}, // {0x0, 0x1, 0x9, 0x0};
	CMD_PS_SESSIONVALID	(68096){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new VaildateRequestMessage();
		}
	}, // {0x0, 0x1, 0xa, 0x0};
	CMD_PS_GETTOKENOTP	(68097){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new TokenOtpGetRequestMessage();
		}
	}, // {0x0, 0x1, 0xa, 0x1};
	
	CMD_WRM_ACCOUNTS    (512){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new AccountListRequestMessage();
		}
	},//{0x0, 0x0, 0x2, 0x0} ;
	CMD_WRM_DELACCOUNTS (528){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new AccountDelRequestMessage();
		}
	},//{0x0, 0x0, 0x2, 0x10};
	CMD_WRM_SESSION     (768){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new SessionListRequestMessage();
		}
	},//{0x0, 0x0, 0x3, 0x0} ;
	CMD_WRM_DELSESSION  (784){
		@Override
		public RaonSessionMessage genRequestMessage() {
			return new SessionDelRequestMessage();
		}
	},//{0x0, 0x0, 0x3, 0x10};
	
	;
	
	private int value;
	RaonCommand(int value){
		this.value = value;
	}
	RaonCommand(byte[] value){
		this.value = ByteBuffer.wrap(value).getInt();
	}
	public static RaonCommand valueOf(int command) {
		switch(command) {
		case 0 :
			return CMD_UNKNOWN;
	    case 1 :
	    	return CMD_HELLO;
	    case 2 :
	    	return CMD_ERROR;
	    case 3 :
	    	return CMD_INFOREQ;
	    case 4 :
	    	return CMD_INFORES;
	    
	    case 65792 :
	    	return CMD_PS_ADDUSERDATA;
	    case 65793 :
	    	return CMD_PS_UPDUSERDATA;
	    case 65794 :
	    	return CMD_PS_DELUSERDATA;
	    case 65795 :
	    	return CMD_PS_GETUSERDATA;

	    case 66048 :  
	    	return CMD_PS_DELACCOUNT;
	    case 66304 :    
	    	return CMD_PS_DELSESSION;
	    case 66560 :
	    	return CMD_PS_ACCOUNTINFO;
	    case 66816 :
	    	return CMD_PS_SESSIONINFO;
	    case 67072 :    
	    	return CMD_PS_REGISTER;
	    case 67328 :
	    	return CMD_PS_UPDATE;
	    case 67584 :
	    	return CMD_PS_SESSIONDATA;
	    case 67840 :
	    	return CMD_PS_CHECKRECOVERY;
	    case 68096 :    
	    	return CMD_PS_SESSIONVALID;
	    case 68097 :
	    	return CMD_PS_GETTOKENOTP;
	 
	    case 512 :
	    	return CMD_WRM_ACCOUNTS;
	    case 528 :
	    	return CMD_WRM_DELACCOUNTS;
	    case 768 :
	    	return CMD_WRM_SESSION;
	    case 784 :  
	    	return CMD_WRM_DELSESSION;
    	default :
    		RaonCommand cmd = CMD_UNKNOWN;//
    		cmd.setValue(command);
    		return cmd;
		}
	}
	public byte[] getByte() {
		return ByteBuffer.allocate(4).putInt(value).array();
	}
	public String getHexStr() {
		return Hex.toHexString(getByte());
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getValue() {
		return this.value;
	}
	public CMD toCommand() {
		return new CMD(this.value);
	}
	public static RaonCommand getByValue(byte[] value) {
		for (RaonCommand command : values()) {
			int int_value = ByteBuffer.wrap(value).getInt();
			if(command.value == int_value) return command;
		}
		return CMD_UNKNOWN;
	}
	public abstract RaonSessionMessage genRequestMessage();
}
