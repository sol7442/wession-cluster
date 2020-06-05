package com.wowsanta.raon.impl.proc;

import java.util.Date;
import java.util.Random;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INDEX;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RSTR;
import com.wowsanta.raon.impl.data.RSTRS;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.message.VaildateRequestMessage;
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

public class VaildateProcess extends AbstractSessionProcess {
	public VaildateProcess() {
		setRequest(new SessionRequest(new VaildateRequestMessage()));
		//setResponse(new SessionResponse(new VaildateResponseMessage(),getRequest().getSession()));
	}

	@Override
	public void porcess() throws ServerException {
		RaonSessionMessage response_message = null;
		String user_id = null;
		try {

			VaildateRequestMessage  request_message  = (VaildateRequestMessage) getRequest().getMessage();
			LOG.application().info("request  : {} ", request_message);
			
			
			user_id      		= request_message.getUserId().getValue();
			INDEX index         = request_message.getSessionIndex();
			String ramdom_value = request_message.getRandom().getValue();
			String token_value  = getOptionalTokenId(request_message);
			
			String session_key = SessionKeyGenerator.generate(user_id.getBytes(),index.toBytes());
			RaonSession old_session = (RaonSession) WessionCluster.getInstance().read(session_key);
			
			if(old_session != null) {
				
				RaonSession val_session = generateVaildateSession(old_session,ramdom_value, token_value);
				RaonSessionPolicy policy = (RaonSessionPolicy) PolicyManager.getInstance().getPolicy();
				
				VaildateResponseMessage validate_message = new VaildateResponseMessage();
				
				PolicyResult result = policy.vaildte(old_session,val_session);	
				if(result == PolicyResult.RESULT_REMOVE) {
					WessionCluster.getInstance().delete(old_session);
					throw new PolicyException(RaonError.ERRSESSIONTIMEOUT.getMessage(),RaonError.ERRSESSIONTIMEOUT.getCode())  ;
					
				}else if(result == PolicyResult.RESULT_UPDATE) {
					
					String new_token_value = generateNewToken();
					
					RSTRS data = new RSTRS();
					data.add((byte)0x0, new RSTR((byte)0x0, new_token_value));
					
					val_session.setToken(new_token_value);
					WessionCluster.getInstance().update(val_session);
										
					validate_message.setData(data);
				}else {
					// not
				}
				validate_message.setLot(new INT((int) old_session.getCreateTime().getTime()));
				validate_message.setLat(new INT((int) val_session.getModifyTime().getTime())) ;
				
				response_message = validate_message;
				
			}else {
				ErrorResonseMessage erro_messge = new ErrorResonseMessage();
				erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_SESSIONVALID.getValue()));
				erro_messge.setCode(new INT(RaonError.ERRACCOUNTISNOTEXIST.getCode()));
				erro_messge.setMessage(new STR(RaonError.ERRACCOUNTISNOTEXIST.getMessage()));
				
				response_message = erro_messge;
			}
		
		} catch (RespositoryException e) {
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
		}catch (Exception e) {
			LOG.process().warn("{} : {} ",e.getMessage(),user_id);
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_SESSIONVALID.getValue()));
			erro_messge.setCode(new INT(RaonError.ERRINTERNAL.getCode()));
			erro_messge.setMessage(new STR(RaonError.ERRINTERNAL.getMessage()));
			
			response_message = erro_messge;
		}
		
		
		finally {
			LOG.application().info("response : {} ", response_message);
			setResponse(new SessionResponse(response_message, getRequest().getSession()));
		}
	}

	private String generateNewToken() {
		String new_token_value = null;
		
		byte[] new_token_otp = new byte[8];
		new Random().nextBytes(new_token_otp);
		new_token_value = Hex.toHexString(new_token_otp);
		
		return new_token_value;
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

	private String getOptionalTokenId(VaildateRequestMessage request_message) {
		String token_value  = null;
		RSTRS optional_data = request_message.getData();
		if(optional_data != null) {
			RSTR token_id = optional_data.get(0);
			if(token_id != null) {
				token_value = token_id.getValue();
			}
		}
		return token_value;
	}

}
