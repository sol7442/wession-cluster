package test.util.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class DaemonT extends AbstractDaeomConfig{

	AbstractServerConfig server;
	@Override
	public void initialize() {
		System.out.println( "initialize ----");
		server.build();
	}

}
