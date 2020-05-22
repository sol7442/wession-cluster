package com.wowsanta.raon.impl.proc;

import java.util.Date;
import java.util.List;
import java.util.Random;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.INDEX;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RSTRS;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.message.RegisterRequestMessage;
import com.wowsanta.raon.impl.message.RegisterResonseMessage;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.raon.impl.session.SessionKeyGenerator;
import com.wowsanta.server.ServerException;
import com.wowsanta.util.Hex;
import com.wowsanta.wession.WessionCluster;
import com.wowsanta.wession.message.SearchRequestMessage;
import com.wowsanta.wession.message.SearchResponseMessage;
import com.wowsanta.wession.repository.RespositoryException;
import com.wowsanta.wession.session.Wession;

public class RegisterProcess extends AbstractSessionProcess {
	public RegisterProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new RegisterResonseMessage(), getRequest().getSession()));
	}

	@Override
	public void porcess() throws ServerException {
		try {
			RegisterRequestMessage request_message = (RegisterRequestMessage) getRequest().getMessage();
			LOG.process().info("request  : {} ", request_message);
			

			byte[] token_key = new byte[8];
			byte[] random_key = new byte[8];
			new Random().nextBytes(token_key);
			new Random().nextBytes(random_key);

			String user_id = request_message.getUserId().getValue();
			String random_value = Hex.toHexString(random_key);
			String token_value = Hex.toHexString(token_key);
			Date   now_time    = new Date();

			
			SearchRequestMessage  request  = new SearchRequestMessage();
			request.setFilter("userId="+user_id);
			SearchResponseMessage response = WessionCluster.getInstance().search(request);
			
			List<Wession> session_list = response.getResources();
			//------------------
			LOG.process().info("policy : {} / {}", user_id, session_list.size());
			//------------------
			
			
			char c1 = (char) (new Random().nextInt(26) + 'a');
			char c2 = (char) (new Random().nextInt(26) + 'a');

			INDEX index = new INDEX();
			index.set(0, c1);
			index.set(1, c2);
			
			register(user_id,index, random_value, token_value, now_time);
			RSTRS data = new RSTRS();
			data.add((byte) 0x00,token_value);
			data.add((byte) 0x01,random_value);
			
			RegisterResonseMessage response_message = (RegisterResonseMessage) getResponse().getMessage();
			response_message.setSessionIndex(index);
			response_message.setLot(new INT((int) System.currentTimeMillis()));
			response_message.setData(data);;
			
			LOG.process().info("response : {} ", response_message);
		} catch (Exception e) {
			LOG.process().error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

	private INDEX  register(String user_id,INDEX index, String random_value, String token_value, Date now_time) throws RespositoryException {
		
//		index.set(2, (byte)0x00);
//		index.set(3, (byte)0x00);		
		try {
			RaonSession session = new RaonSession();
			session.setKey(SessionKeyGenerator.generate(user_id.getBytes(), index.toBytes()));
			session.setUserId(user_id);
			session.setRandom(random_value);
			session.setToken(token_value);
			session.setCreateTime(now_time);
			session.setModifyTime(now_time);
			
			WessionCluster.getInstance().create(session);

		}catch (Exception e) {
			throw new RespositoryException(e.getMessage(), e);
		}
		return index;
	}

}
