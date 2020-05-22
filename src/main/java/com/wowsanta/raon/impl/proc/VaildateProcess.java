package com.wowsanta.raon.impl.proc;

import java.util.Random;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.INDEX;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RSTR;
import com.wowsanta.raon.impl.data.RSTRS;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.message.VaildateRequestMessage;
import com.wowsanta.raon.impl.message.VaildateResponseMessage;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.raon.impl.session.SessionKeyGenerator;
import com.wowsanta.server.ServerException;
import com.wowsanta.util.Hex;
import com.wowsanta.wession.WessionCluster;

public class VaildateProcess extends AbstractSessionProcess {
	public VaildateProcess(RaonSessionMessage message) {
		setRequest(new SessionRequest(message));
		setResponse(new SessionResponse(new VaildateResponseMessage(),getRequest().getSession()));
	}

	@Override
	public void porcess() throws ServerException {
		try {

			VaildateRequestMessage  request_message  = (VaildateRequestMessage) getRequest().getMessage();
			LOG.process().info("request  : {} ", request_message);
			
			
			String user_id      = request_message.getUserId().getValue();
			INDEX index         = request_message.getSessionIndex();
			String ramdom_value = request_message.getRandom().getValue();
			String token_val  = request_message.getData().get(0).getValue();
			String token_opt = null;
			if(request_message.getData().get(1) != null) {
				token_opt = request_message.getData().get(1).getValue();
			}
		
			
			String session_key = SessionKeyGenerator.generate(user_id.getBytes(),index.toBytes());
			RaonSession session = (RaonSession) WessionCluster.getInstance().read(session_key);
			
			if(session != null) {
				if(ramdom_value.equals(session.getRandom()) && token_val.equals(session.getToken())){
					String otp_val = (String) session.getAttribute("opt.val");
					LOG.process().debug("token_opt : {}-{}", token_opt, otp_val);
					
					RSTRS data = new RSTRS();
					byte[] token_otp_key = new byte[8];
					new Random().nextBytes(token_otp_key);
					String token_opt_value = Hex.toHexString(token_otp_key);
					data.add((byte)0x0, new RSTR((byte)0x0, token_opt_value));
					
					VaildateResponseMessage response_message = (VaildateResponseMessage) getResponse().getMessage();
					response_message.setLot(new INT((int) session.getCreateTime().getTime()));
					response_message.setLat(new INT((int) session.getModifyTime().getTime())) ;
					response_message.setData(data);
					
				}else {
					
					ErrorResonseMessage error_message = new ErrorResonseMessage();
					error_message.setRequest(request_message.getCommand());
					error_message.setCode(new INT(5000));
					error_message.setMessage(new STR("Token Missmatched : ["+session.getRandom()+"]/["+session.getToken()+"] "));
					
					setResponse(new SessionResponse(error_message,getRequest().getSession()));
				}
				
			}else {
				
				ErrorResonseMessage error_message = new ErrorResonseMessage();
				error_message.setRequest(request_message.getCommand());
				error_message.setCode(new INT(5000));
				error_message.setMessage(new STR("Session Not Found"));
				
				setResponse(new SessionResponse(error_message,getRequest().getSession()));
			}
			
			LOG.process().info("response : {} ", getResponse().getMessage());
		} catch (Exception e) {
			LOG.process().error(e.getMessage(), e);
			throw new ServerException(e.getMessage(),e);
		}
	}

}
