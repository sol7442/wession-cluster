package com.wowsanta.raon.impl.session;

public enum RaonError {
	SUCCESS( 				0    ,"Success"),
	
	ERRNET_SOCKET( 			-9101,"NETWORK Error - SOCKET ERROR"),
	ERRNET_CLOSE( 			-9102,"NETWORK Error - SOCKET Closed"),
	ERRNET_EXCEED_SOCK( 	-9103,"NETWORK Error - SOCKET IO ERROR"),
	ERRNET_PROTOCOL( 		-9104,"NETWORK Error - PROTOCAL ERROR"),
	ERRNET_TIMEOUT( 		-9105,"NETWORK Error - READ TIME OUT"),
	
	ERRNOTINIT( 			-9201,"Internal error - Unavailable or not initialized"),
	ERRINTERNAL( 			-9203,"Internal error - "),
	ERRNENOUGHSIZE( 		-9204,"Internal error - ERRNENOUGHSIZE"),
	ERRTHREADMINSIZE( 		-9205,"Internal error - ERRTHREADMINSIZE"),
	ERRNOTIMPLEMENT( 		-9206,"Internal error - ERRNOTIMPLEMENT"),
	ERREXCEEDINDEX( 		-9207,"Internal error - ERREXCEEDINDEX"),
	ERRCONFIGVALUE( 		-9208,"Internal error - ERRCONFIGVALUE"),
	ERRRANGEVALUE( 			-9209,"Internal error - ERRRANGEVALUE"),
	ERRFAILREGSTARTUID( 	-9211,"Internal error - ERRFAILREGSTARTUID"),
	ERRINDEXOUTOFRANGE( 	-9212,"Internal error - ERRINDEXOUTOFRANGE"),
	ERRINVALIDARGUMENTS( 	-9213,"Internal error - ERRINVALIDARGUMENTS"),
	ERRCONFIG( 				-9214,"Internal error - ERRCONFIG"),
	ERRLOG( 				-9215,"Internal error - ERRLOG"),
	ERRDUPSYNCIDX( 			-9216,"Internal error - Syncidx duplication"),
	ERRBUSYGC( 				-9217,"Internal error - ERRBUSYGC"),
	ERRBUSYBOOTUPSYNC( 		-9218,"Internal error - ERRBUSYBOOTUPSYNC"),
	
	ERRCMDINVALID( 			-9301,"Invalid command"),
	
	ERRACCOUNTFULL( 		-9401,"Account full"),
	ERRSESSIONFULL(			-9402,"Session full"),
	ERRACCOUNTISNOTEXIST( 	-9403,"Account is not exist"),
	ERRSESSIONISNOTEXIST( 	-9404,"Session is not exist"),
	ERRSESSIONTIMEOUT( 		-9405,"Session timeout"),
	ERRDATAISNULL( 			-9406,"Data is null"),
	
	ERRWRONGRANDOM( 		-9501,"Vaildate failed - random value missmatch"),
	ERRNEQTOKENID( 			-9502,"Vaildate failed - token value missmatch"),
	ERRNEQTOKENOTP( 		-9503,"Vaildate failed - token otp value missmatch"),
	ERRTIMEOUTTOKENOTP( 	-9504,"Vaildate failed - token otp value timeout"),
	
	EOF( 					-1	 ,""),
	;
	
	private int code;
	private String message;
	private RaonError(int code, String msg) {
		this.code = code;
		this.message = msg;
	}
	public int getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
}
