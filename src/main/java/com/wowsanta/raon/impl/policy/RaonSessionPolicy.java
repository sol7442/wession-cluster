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
	int maxAccount = 1000;
	int maxSession = 5;
	int maxInactiveInterval=60;// TimeUnit.MINUTES;
	
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
		return true;
	}
	
	public PolicyResult create(RaonSession session, int account_size, int session_size) throws PolicyException {
		PolicyResult result ;
		
		result = check_account_size(account_size);
		result = check_reg_option(session,session_size);
		
		LOG.process().info("{}:{}",session.getUserId(),result);
		return result;
	}
	
	public PolicyResult vaildte(RaonSession old_session, RaonSession vaildate_session) throws PolicyException {
		PolicyResult result = null;
		long session_time = System.currentTimeMillis() - old_session.getModifyTime().getTime(); 
		try {
			if(session_time > maxAlivedInterval ) {
				result = PolicyResult.RESULT_REMOVE;
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
		}finally {
			LOG.process().info("{}:{}",vaildate_session.getUserId(),result);
		}
		
		return PolicyResult.RESULT_UPDATE;
	}
	

	/**
	 * 허용된 세션 서버 용량 (user 수)
	 * 
	 * @return
	 * @throws PolicyException
	 */
	private PolicyResult check_account_size(int account_size) throws PolicyException {
		if(maxAccount < account_size) {
			throw new PolicyException("Max Account Limtied : ", RaonError.ERRACCOUNTFULL.getCode())  ;
		}
		
		LOG.process().debug("{}/{}",account_size,maxAccount);
		return PolicyResult.RESULT_CREATE;
	}


	/**
	 * session_size == 0 또는
	 * 멀티 로그인이 가능하며, maxSession을 초과하지 않은 경우.
	 * 멀티 로그인이 가능하며, maxSession을 초과했지만, 덮어쓰기가 가능한 경우
	 * 
	 * @param session
	 * @return
	 * @throws RespositoryException
	 */
	private PolicyResult check_reg_option(RaonSession session,int session_size ) throws PolicyException {
		PolicyResult result = PolicyResult.RESULT_ERROR;
		
		int reg_option   = session.getOption();
		
		switch (reg_option) {
		case 0: // 00000000 : multilogin false, overwrite false
			if(session_size != 0) {
				throw new PolicyException("Max Session Limtied : ",RaonError.ERRSESSIONFULL.getCode())  ;
			}else {
				result = PolicyResult.RESULT_CREATE;
			}
			break;
		case 1: // 00000001 : multilogin false, overwrite true
			if(session_size == 0) {
				result = PolicyResult.RESULT_CREATE;
			}else {
				result = PolicyResult.RESULT_REMOVE_CREATE;
			}
			
			break;
		case 2: // 00000010 : multilogin true, overwrite false
			if(maxSession == session_size) {
				throw new PolicyException("Max Session Limtied : ",RaonError.ERRSESSIONFULL.getCode())  ;
			}else {
				result = PolicyResult.RESULT_CREATE;
			}
			break;
		case 3: // 00000011 : multilogin true, overwrite true
			if(maxSession != session_size) {
				result = PolicyResult.RESULT_CREATE;
			}else {
				result = PolicyResult.RESULT_REMOVE_CREATE;
			}
			break;
		}
		
		LOG.process().debug("{}({}) : {}/{}",session.getUserId(),reg_option,session_size,maxSession);
		return result;
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
