package com.wowsanta.raon.impl.proc;


import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.CMD;
import com.wowsanta.raon.impl.data.INDEX;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.message.UserDataDelRequestMessage;
import com.wowsanta.raon.impl.message.UserDataDelResponseMessage;
import com.wowsanta.raon.impl.session.RaonCommand;
import com.wowsanta.raon.impl.session.RaonError;
import com.wowsanta.raon.impl.session.RaonSession;
import com.wowsanta.raon.impl.session.SessionKeyGenerator;
import com.wowsanta.raon.impl.session.SessionRequest;
import com.wowsanta.raon.impl.session.SessionResponse;
import com.wowsanta.server.ServerException;
import com.wowsanta.wession.WessionCluster;
import com.wowsanta.wession.repository.RespositoryException;


public class UserDataDelProcess extends AbstractSessionProcess {
	public UserDataDelProcess() {
		setRequest(new SessionRequest(new UserDataDelRequestMessage()));
	}

	@Override
	public void porcess() throws ServerException {
		UserDataDelRequestMessage  request_message  = (UserDataDelRequestMessage) getRequest().getMessage();
		RaonSessionMessage response_message = null;
		try {
			LOG.application().info("request  : {} ", request_message);
			
			String user_id      = request_message.getUserId().getValue();
			INDEX index         = request_message.getSessionIndex();
			
			String session_key = SessionKeyGenerator.generate(user_id.getBytes(),index.toBytes());
			RaonSession session = (RaonSession) WessionCluster.getInstance().read(session_key);
			if(session != null) {
				String user_data = (String) session.getAttribute("user.data");
				LOG.application().debug("user.data : {} ", user_data);
				session.removeAttribute("user.data");
				WessionCluster.getInstance().update(session);
				
				UserDataDelResponseMessage data_del_message = new UserDataDelResponseMessage();
				data_del_message.setLot(new INT(session.getCreateTime().getTime()));
				
				response_message = data_del_message;;
			}else {
				ErrorResonseMessage erro_messge = new ErrorResonseMessage();
				erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_DELUSERDATA.getValue()));
				erro_messge.setCode(new INT(RaonError.ERRSESSIONISNOTEXIST.getCode()));
				erro_messge.setMessage(new STR(RaonError.ERRSESSIONISNOTEXIST.getMessage()));
				
				response_message = erro_messge;;
			}
		}  catch (RespositoryException e) {
			LOG.application().error(e.getMessage(), e);
			
			ErrorResonseMessage erro_messge = new ErrorResonseMessage();
			erro_messge.setRequest(new CMD(RaonCommand.CMD_PS_DELUSERDATA.getValue()));
			erro_messge.setCode(new INT(RaonError.ERRINTERNAL.getCode()));
			erro_messge.setMessage(new STR(RaonError.ERRINTERNAL.getMessage()));
			
			response_message = erro_messge;
		}finally {
			LOG.application().info("response : {} ", response_message);
			
			setResponse(new SessionResponse(response_message, getRequest().getSession()));
		}
	}

}
