package com.wowsanta.raon.impl.session;

public class SessionServerCommand
{
	
	public static final byte[]	CMD_UNKNOWN			 = {0x0,0x0,0x0,0x0};
	public static final byte[]	CMD_HELLO			 = {0x0,0x0,0x0,0x1};
	public static final byte[]	CMD_ERROR			 = {0x0,0x0,0x0,0x2};
	public static final byte[]	CMD_INFOREQ			 = {0x0,0x0,0x0,0x3};
	public static final byte[]	CMD_INFORES			 = {0x0,0x0,0x0,0x4};
	
	public static final byte[]	CMD_PS_ADDUSERDATA	 = {0, 0x1, 0x1, 0x0};
	public static final byte[]	CMD_PS_UPDUSERDATA	 = {0, 0x1, 0x1, 0x1};
	public static final byte[]	CMD_PS_DELUSERDATA	 = {0, 0x1, 0x1, 0x2};
	public static final byte[]	CMD_PS_GETUSERDATA	 = {0, 0x1, 0x1, 0x3};
	public static final byte[]	CMD_PS_DELACCOUNT	 = {0, 0x1, 0x2, 0x0};
	public static final byte[]	CMD_PS_DELSESSION	 = {0, 0x1, 0x3, 0x0};
	public static final byte[]	CMD_PS_ACCOUNTINFO	 = {0, 0x1, 0x4, 0x0};
	public static final byte[]	CMD_PS_SESSIONINFO	 = {0, 0x1, 0x5, 0x0};
	public static final byte[]	CMD_PS_REGISTER		 = {0, 0x1, 0x6, 0x0};
	public static final byte[]	CMD_PS_UPDATE		 = {0, 0x1, 0x7, 0x0};
	public static final byte[]	CMD_PS_SESSIONDATA	 = {0, 0x1, 0x8, 0x0};
	public static final byte[]	CMD_PS_CHECKRECOVERY = {0, 0x1, 0x9, 0x0};
	public static final byte[]	CMD_PS_SESSIONVALID	 = {0, 0x1, 0xa, 0x0};
	public static final byte[]	CMD_PS_GETTOKENOTP	 = {0, 0x1, 0xa, 0x1};
	
}
