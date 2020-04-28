package test.util.config;

import com.wowsanta.util.config.JsonConfiguration;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class AbstractServiceConfig extends JsonConfiguration{
	static {
		JsonConfiguration.addTypeAdapter(AbstractServiceConfig.class);
	}
	String serviceClass;
	
	abstract public void run() ;
}
