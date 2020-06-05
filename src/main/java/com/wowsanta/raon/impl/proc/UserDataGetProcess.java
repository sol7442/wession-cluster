package com.wowsanta.raon.impl.proc;


import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INDEX;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.message.UserDataGetRequestMessage;
import com.wowsanta.raon.impl.message.UserDataGetResponseMessage;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.raon.impl.session.RaonError;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.raon.impl.session.SessionKeyGenerator;
import com.wowsanta.raon.impl.session.SessionRequest;
import com.wowsanta.raon.impl.session.SessionResponse;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.WessionCluster;
import com.wowsanta.wession.repository.RespositoryException;

public class UserDataGetProcess extends AbstractSessionProcess {
	public UserDataGetProcess() {
		setRequest(new SessionRequest(new UserDataGetRequestMessage()));
	}

	@Override
	public void porcess() throws ServerException {
		UserDataGetRequestMessage  request_message  = (UserDataGetRequestMessage) getRequest().getMessage();
		RaonSessionMessage response_message = null;
		
		try {
			LOG.application().info("request  : {} ", request_message);
			
			String user_id      = request_message.getUserId().getValue();
			INDEX index         = request_message.getSessionIndex();
			
			String session_key = SessionKeyGenerator.generate(user_id.getBytes(),index.toBytes());
			RaonSession session = (RaonSession) WessionCluster.getInstance().read(session_key);
			if(session != null) {
				String user_data = (String)session.getAttribute("user.data");
				if(user_data != null) {
					UserDataGetResponseMessage data_get_message = new UserDataGetResponseMessage();
					data_get_message.setLot(new INT(session.getCreateTime().getTime()));
					data_get_message.setData(new STR(user_data));
					
					response_message = data_get_message;
				}else {
					ErrorResonseMessage erro_messge = new ErrorResonseMessage();
					erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_GETUSERDATA.getValue()));
					erro_messge.setCode(new INT(RaonError.ERRDATAISNULL.getCode()));
					erro_messge.setMessage(new STR(RaonError.ERRDATAISNULL.getMessage()));
					
					response_message = erro_messge;;
				}
			}else {
				ErrorResonseMessage erro_messge = new ErrorResonseMessage();
				erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_GETUSERDATA.getValue()));
				erro_messge.setCode(new INT(RaonError.ERRSESSIONISNOTEXIST.getCode()));
				erro_messge.setMessage(new STR(RaonError.ERRSESSIONISNOTEXIST.getMessage()));
				
				response_message = erro_messge;;
			}
			
		} catch (RespositoryException e) {
			LOG.application().error(e.getMessage(), e);
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_GETUSERDATA.getValue()));
			erro_messge.setCode(new INT(RaonError.ERRINTERNAL.getCode()));
			erro_messge.setMessage(new STR(RaonError.ERRINTERNAL.getMessage()));
			
			response_message = erro_messge;
		}finally {
			LOG.application().info("response : {} ", response_message);
			
			setResponse(new SessionResponse(response_message, getRequest().getSession()));
		}
	}

}
