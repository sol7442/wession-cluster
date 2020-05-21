package com.wowsanta.wession.impl.server;


import java.io.IOException;

import com.wowsanta.logger.LOG;
import com.wowsanta.raon.impl.data.INT;
import com.wowsanta.raon.impl.data.RaonSessionMessage;
import com.wowsanta.raon.impl.data.STR;
import com.wowsanta.raon.impl.message.ErrorResonseMessage;
import com.wowsanta.raon.impl.proc.AbstractSessionProcess;
import com.wowsanta.raon.impl.proc.SessionResponse;
import com.wowsanta.server.Message;
import com.wowsanta.server.ServerException;
import com.wowsanta.server.ServiceDispatcher;
import com.wowsanta.server.ServiceProcess;
import com.wowsanta.server.nio.NioConnection;


public class RaonSessionServiceDispatcher extends ServiceDispatcher{

	long start_time;
	long end_time;
	
	@Override
	public void before(ServiceProcess<?, ?> process) {
		try {
			start_time = System.currentTimeMillis();
		}catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
		}finally {
			LOG.application().debug("before : {} / {}", process.getClass().getName(), start_time);
		}
	}

	@Override
	public void dispatcher(ServiceProcess<?,?> process) {
		try {
			process.porcess();
		}catch (ServerException e) {
			errer(process,e);
		}finally {
			try {
				process.getResponse().getMessage().flush();
			} catch (IOException e) {
				LOG.application().error(e.getMessage(), e);
			}
		}
	}

	private void errer(ServiceProcess<?, ?> process, Exception error) {
		try {

			AbstractSessionProcess session_process = (AbstractSessionProcess) process;
			RaonSessionMessage request_message = (RaonSessionMessage) session_process.getRequest().getMessage();
			
			ErrorResonseMessage error_message = new ErrorResonseMessage();
			error_message.setRequest(request_message.getCommand());
			error_message.setCode(new INT(5000));
			error_message.setMessage(new STR("SERVER ERROR : " + error.getMessage()));
			
			session_process.setResponse(new SessionResponse(error_message));

			LOG.process().info("error message : {}", error_message );
			
		} catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
		}
	}

	@Override
	public void after(ServiceProcess<?,?> process) {
		try {
			Message message = process.getResponse().getMessage();
			NioConnection connection = (NioConnection) process.getConnection();
			connection.write(message.toBytes());
			connection.write0();
			end_time = System.currentTimeMillis();
		}catch (Exception e) {
			LOG.application().error(e.getMessage(), e);
		}finally {
			LOG.application().debug("after : {} / {} ", process.getClass().getName(), end_time - start_time);
		}
	}
}
