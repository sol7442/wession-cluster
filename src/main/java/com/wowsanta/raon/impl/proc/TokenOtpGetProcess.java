package com.wowsanta.raon.impl.proc;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.BYTE4;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RSTR;
import com.wowsanta.raon.impl.data.RSTRS;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.message.TokenOtpGetRequestMessage;
import com.wowsanta.raon.impl.message.TokenOtpGetResponseMessage;
import com.wowsanta.raon.impl.message.VaildateResponseMessage;
import com.wowsanta.raon.impl.policy.PolicyResult;
import com.wowsanta.raon.impl.policy.RaonSessionPolicy;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.raon.impl.session.RaonError;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.raon.impl.session.SessionKeyGenerator;
import com.wowsanta.raon.impl.session.SessionRequest;
import com.wowsanta.raon.impl.session.SessionResponse;
import com.wowsanta.server.ServerException;
import com.wowsanta.util.Hex;
import com.wowsanta.wession.WessionCluster;
import com.wowsanta.wession.manager.PolicyManager;
import com.wowsanta.wession.policy.PolicyException;
import com.wowsanta.wession.repository.RespositoryException;


public class TokenOtpGetProcess extends AbstractSessionProcess {
	public TokenOtpGetProcess() {
		setRequest(new SessionRequest(new TokenOtpGetRequestMessage()));
		setResponse(new SessionResponse(new TokenOtpGetResponseMessage(), getRequest().getSession()));
	}

	@Override
	public void porcess() throws ServerException {
		TokenOtpGetRequestMessage  request_message  = (TokenOtpGetRequestMessage) getRequest().getMessage();
		RaonSessionMessage response_message = null;
		
		
		String user_id = null;
		try {
			LOG.application().info("request  : {} ", request_message);
			
			user_id = request_message.getUserId().getValue();
			BYTE4 index         = request_message.getSessionIndex();
			String random_value = request_message.getRandom().getValue();
			String token_value  = getOptionalTokenValue(request_message, 0);
			//String token_otp    = getOptionalTokenValue(request_message, 1);
			
			String session_key = SessionKeyGenerator.generate(user_id.getBytes(),index.toBytes());
			RaonSession old_session = (RaonSession) WessionCluster.getInstance().read(session_key);
			
			if(old_session != null) {
				RaonSession val_session = generateVaildateSession(old_session,random_value, token_value);
				RaonSessionPolicy policy = (RaonSessionPolicy) PolicyManager.getInstance().getPolicy();
				
				PolicyResult result = policy.vaildte(old_session,val_session);	
				if(result == PolicyResult.RESULT_REMOVE) {
					WessionCluster.getInstance().delete(old_session);
					throw new PolicyException(RaonError.ERRSESSIONTIMEOUT.getMessage(),RaonError.ERRSESSIONTIMEOUT.getCode())  ;
				}

				String token_otp = Hex.toHexString(new Random().nextLong());

				old_session.setTokenOtp(token_otp);				
				WessionCluster.getInstance().update(old_session);
				
				SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
				TokenOtpGetResponseMessage token_message = new TokenOtpGetResponseMessage();
				
				RSTRS data = new RSTRS();
				data.add((byte)0x1,token_otp);
				data.add((byte)0x2,date_format.format(new Date()));
				
				token_message.setData(data);
				
				response_message = token_message;
				
			}else {
				ErrorResonseMessage erro_messge = new ErrorResonseMessage();
				erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_SESSIONVALID.getValue()));
				erro_messge.setCode(new INT(RaonError.ERRACCOUNTISNOTEXIST.getCode()));
				erro_messge.setMessage(new STR(RaonError.ERRACCOUNTISNOTEXIST.getMessage()));
				
				response_message = erro_messge;
			}
			
		} 
		
		catch (RespositoryException e) {
			LOG.application().error(e.getMessage(), e);
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_REGISTER.getValue()));
			erro_messge.setCode(new INT(RaonError.ERRINTERNAL.getCode()));
			erro_messge.setMessage(new STR(RaonError.ERRINTERNAL.getMessage()));
			
			response_message = erro_messge;
		} catch (PolicyException e) {
			LOG.process().warn("{} : " + e.getMessage() + " : {}",user_id, e.getCode());
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_SESSIONVALID.getValue()));
			erro_messge.setCode(new INT(e.getCode()));
			erro_messge.setMessage(new STR(e.getMessage()));
			
			response_message = erro_messge;
		}
		catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_REGISTER.getValue()));
			erro_messge.setCode(new INT(RaonError.ERRINTERNAL.getCode()));
			erro_messge.setMessage(new STR(RaonError.ERRINTERNAL.getMessage()));
			
			response_message = erro_messge;
		} 
		
		finally {
			LOG.application().info("response : {} ", response_message);
			
			setResponse(new SessionResponse(response_message, getRequest().getSession()));
			
		}
	}
	private RaonSession generateVaildateSession(RaonSession old_session, String ramdom_value, String token_value) {
		RaonSession vaildate_session = new RaonSession();
		vaildate_session.setKey(old_session.getKey());
		vaildate_session.setUserId(old_session.getUserId());
		vaildate_session.setIndex(old_session.getIndex());
		
		vaildate_session.setRandom(ramdom_value);
		vaildate_session.setToken(token_value);
		
		vaildate_session.setCreateTime(old_session.getCreateTime());
		vaildate_session.setModifyTime(new Date());
		
		return vaildate_session;
	}
	private String getOptionalTokenValue(TokenOtpGetRequestMessage request_message, int index) {
		String token_value  = null;
		RSTRS optional_data = request_message.getData();
		if(optional_data != null) {
			RSTR token = optional_data.get(index);
			if(token != null) {
				token_value = token.getValue();
			}
		}
		return token_value;
	}

}
