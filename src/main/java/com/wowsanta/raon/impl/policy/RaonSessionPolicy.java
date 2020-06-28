package com.wowsanta.raon.impl.policy;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.session.RaonError;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.wession.policy.Policy;
import com.wowsanta.wession.policy.PolicyException;
import com.wowsanta.wession.repository.RespositoryException;

public class RaonSessionPolicy implements Policy<RaonSession> {
	//int maxAccount = 1000;
	int maxSession = 5;
	int maxInactiveInterval=60;// TimeUnit.MINUTES;
	int tokenOtpTimeout = 100;
	
	transient long maxAlivedInterval;

//	public static final int RESULT_ERROR 		 = 0;
//	public static final int RESULT_CREATE 		 = 1;
//	public static final int RESULT_REMOVE_CREATE = 2;
//	public static final int RESULT_UPDATE 	     = 3;
//	public static final int RESULT_REMOVE 	     = 4;
//	public static final int RESULT_SUCCESS	     = 99;
	
	@Override
	public boolean initialize() {
		maxAlivedInterval = maxInactiveInterval * 1000 * 60;
		tokenOtpTimeout   = tokenOtpTimeout * 1000;
		return true;
	}
	
	public PolicyResult create(RaonSession session, int account_size, int session_size, byte[] reg_opt) throws PolicyException {
		PolicyResult result = PolicyResult.RESULT_CREATE;
		
		switch(reg_opt[3]) {
		case 0x00: 
		case 0x01:
		case 0x02:
			if (session_size >= maxSession){
				throw new PolicyException("Max Session Limtied : ",RaonError.ERRSESSIONFULL.getCode());
			}
			break;
		case 0x03:
			if (session_size >= maxSession){
				result = PolicyResult.RESULT_APPEND_SESSION;
			}
			break;
		}
		
		LOG.process().info("{} - {}/{}/{} : {}",session.getUserId(),reg_opt[3], session_size,account_size, result);
		return result;
	}
	
	public PolicyResult vaildte(RaonSession old_session, RaonSession vaildate_session) throws PolicyException {
		PolicyResult result = PolicyResult.RESULT_SUCCESS;
		
		long session_time = System.currentTimeMillis() - old_session.getModifyTime().getTime(); 
		try {
			if(session_time > maxAlivedInterval ) {
				result = PolicyResult.RESULT_REMOVE;
				throw new PolicyException(RaonError.ERRSESSIONTIMEOUT.getMessage(),RaonError.ERRSESSIONTIMEOUT.getCode())  ;
			}
			
			if(!check_session_value(old_session.getRandom(), vaildate_session.getRandom())) {
				result = PolicyResult.RESULT_ERROR;
				throw new PolicyException(RaonError.ERRWRONGRANDOM.getMessage(),RaonError.ERRWRONGRANDOM.getCode())  ;
			}
			
			if(vaildate_session.getToken() != null) {
				if(!check_session_value(old_session.getToken(), vaildate_session.getToken())) {
					result = PolicyResult.RESULT_ERROR;
					throw new PolicyException(RaonError.ERRNEQTOKENID.getMessage(),RaonError.ERRNEQTOKENID.getCode())  ;
				}
				result = PolicyResult.RESULT_UPDATE;
			}
			
			if(vaildate_session.getTokenOtp() != null) {
				if(!check_session_value(old_session.getTokenOtp(), vaildate_session.getTokenOtp())) {
					result = PolicyResult.RESULT_ERROR;
					throw new PolicyException(RaonError.ERRNEQTOKENOTP.getMessage(),RaonError.ERRNEQTOKENOTP.getCode())  ;
				}
				
				long otp_time_out = System.currentTimeMillis() - old_session.getModifyTime().getTime();
				if(otp_time_out > tokenOtpTimeout) {
					result = PolicyResult.RESULT_ERROR;
					throw new PolicyException(RaonError.ERRTIMEOUTTOKENOTP.getMessage(),RaonError.ERRTIMEOUTTOKENOTP.getCode())  ;
				}
				
				result = PolicyResult.RESULT_UPDATE;
			}
			
		}finally {
			LOG.process().info("{}:{}",vaildate_session.getUserId(),result);
		}
		
		return PolicyResult.RESULT_UPDATE;
	}
	

	
	

	
	private boolean check_session_value(String or, String vr) {
		return or.equals(vr);
	}

	@Override
	public Policy<RaonSession> load(String file_name) {
		
		File file = new File(file_name);
		if(file.exists() && file.isFile()) {
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			try (JsonReader reader = new JsonReader(new FileReader(file))) {
				return gson.fromJson(reader, RaonSessionPolicy.class);
			}catch (Exception e) {
				LOG.system().error(e.getMessage(),e);
			}
		}else {
			LOG.system().error("file not found : {}", file_name);
		}
		
		return null;
	}

	@Override
	public String toString(boolean pretty) {
		try {
			GsonBuilder builder = new GsonBuilder();
			if(pretty) {
				builder.setPrettyPrinting();
			}
			Gson gson = builder.create();
			return gson.toJson(this);
		} catch (Exception e) {
			LOG.system().error(e.getMessage(),e);
		}
		return null;
	}
	
	@Override
	public void save(String file_name) {
		try (FileWriter writer = new FileWriter(file_name)) {
			writer.write(toString(true));
		} catch (Exception e) {
			LOG.system().error(e.getMessage(),e);
		}
	}
}
