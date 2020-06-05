package com.wowsanta.raon.impl.proc;

import java.util.Date;
import java.util.List;
import java.util.Random;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INDEX;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RSTRS;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.message.RegisterRequestMessage;
import com.wowsanta.raon.impl.message.RegisterResonseMessage;
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
import com.wowsanta.wession.manager.IndexManager;
import com.wowsanta.wession.manager.PolicyManager;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.policy.PolicyException;
import com.wowsanta.wession.repository.RespositoryException;

public class RegisterProcess extends AbstractSessionProcess {
	public RegisterProcess() {
		setRequest(new SessionRequest(new RegisterRequestMessage()));
	}

	@Override
	public void porcess() throws ServerException {

		RegisterRequestMessage request_message  = (RegisterRequestMessage) getRequest().getMessage();
		RaonSessionMessage response_message = null;
		
		String user_id = null;
		int reg_option = -1;
		try {
			
			LOG.application().info("request  : {} ", request_message);
			
			user_id = request_message.getUserId().getValue();
			reg_option = request_message.getOption().getValue();
			

			INDEX index = generateIndex();
			RaonSession session = generateSession(user_id, index, reg_option);
			
			int account_size = IndexManager.getInstance().size("userId");
			int session_size = IndexManager.getInstance().size("userId", session.getUserId());
			
			System.out.println(session.getUserId() + ": " + session_size  + "/"+ account_size );
			
			RaonSessionPolicy policy = (RaonSessionPolicy) PolicyManager.getInstance().getPolicy();
			PolicyResult result = policy.create(session,account_size,session_size);
			if(result != PolicyResult.RESULT_ERROR) {
				if(result == PolicyResult.RESULT_REMOVE_CREATE) {
					
					SearchRequestMessage request = new SearchRequestMessage();
					request.setFilter("userId eq "+session.getUserId());
					request.setOrderKey("createTime"); //<-- 만들기 귀찮다.
					
					RaonSession old_session = session;
					
					@SuppressWarnings("unchecked")
					SearchResponseMessage<RaonSession> response = WessionCluster.getInstance().search(request);
					List<RaonSession> search_list = response.getResources();
					for (RaonSession wession : search_list) {
						if(old_session.getModifyTime().getTime() > wession.getModifyTime().getTime()) {
							old_session = wession;
						}
					}
					WessionCluster.getInstance().delete(old_session);
				}

				WessionCluster.getInstance().create(session);
				
				response_message = generateRegisterMessage(index, session);
			}
			
		} catch (PolicyException e) {
			LOG.process().warn("{}({}) : " + e.getMessage() + " : {}",user_id,reg_option,e.getCode());
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_REGISTER.getValue()));
			erro_messge.setCode(new INT(e.getCode()));
			erro_messge.setMessage(new STR(e.getMessage()));
			
			response_message = erro_messge;
		} catch (RespositoryException e) {
			LOG.application().error(e.getMessage(), e);
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_REGISTER.getValue()));
			erro_messge.setCode(new INT(RaonError.ERRINTERNAL.getCode()));
			erro_messge.setMessage(new STR(RaonError.ERRINTERNAL.getMessage()));
			
			response_message = erro_messge;
		} finally {
			LOG.application().info("response : {} ", response_message);
			
			setResponse(new SessionResponse(response_message, getRequest().getSession()));
			
		}
	}

	private RegisterResonseMessage generateRegisterMessage(INDEX index, RaonSession session) {
		RegisterResonseMessage register_message= new RegisterResonseMessage();
		
		register_message.setCreateTime(new INT((int) session.getCreateTime().getTime()));
		register_message.setSessionIndex(index);
		
		RSTRS data = new RSTRS();
		data.add((byte) 0x00,session.getToken());
		data.add((byte) 0x01,session.getRandom());
		register_message.setData(data);;
		return register_message;
	}

	private RaonSession generateSession(String user_id,	INDEX index, int reg_opt) {
		byte[] token_key = new byte[8];
		byte[] random_key = new byte[8];
		new Random().nextBytes(token_key);
		new Random().nextBytes(random_key);
		
		String random_value = Hex.toHexString(random_key);
		String token_value = Hex.toHexString(token_key);
		Date   now_time    = new Date();
		
		RaonSession session = new RaonSession();
		session.setKey(SessionKeyGenerator.generate(user_id.getBytes(), index.toBytes()));
		session.setUserId(user_id);
		session.setRandom(random_value);
		session.setToken(token_value);
		session.setCreateTime(now_time);
		session.setModifyTime(now_time);
		session.setOption(reg_opt);
		
		return session;
	}

	/*****************************************************************
	 * 1,3번째 바이트는 0x00으로 고정, IDX1, IDX2는 아스키 코드 값으로 표현 됨 
	 *****************************************************************/
	private INDEX generateIndex() {
		char c1 = (char) (new Random().nextInt(26) + 'a');
		char c2 = (char) (new Random().nextInt(26) + 'a');

		INDEX index = new INDEX();
		index.set(0, c1);
		index.set(1, c2);
		
		return index;
	}
}
