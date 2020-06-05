package com.wowsanta.raon.impl.policy;

public enum PolicyResult {
	RESULT_ERROR 		  (0),
	RESULT_CREATE 		  (1),
	RESULT_REMOVE_CREATE  (2),
	RESULT_UPDATE 	      (3),
	RESULT_REMOVE 	      (4),
	RESULT_SUCCESS	      (9),
	;
	
	int value = 0;
	private PolicyResult(int value){
		
	}
	public int getValue() {
		return value;
	}
}
