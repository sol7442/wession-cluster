package test.util.config;

import com.wowsanta.util.config.JsonConfiguration;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class AbstractDaeomConfig extends JsonConfiguration{
	static {
		JsonConfiguration.addTypeAdapter(AbstractDaeomConfig.class);
	}
	
	String instanceName;
	abstract public void initialize();
}
