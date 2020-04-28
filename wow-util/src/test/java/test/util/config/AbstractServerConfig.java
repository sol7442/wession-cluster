package test.util.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.wowsanta.util.config.JsonConfiguration;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class AbstractServerConfig extends JsonConfiguration{
	static {
		JsonConfiguration.addTypeAdapter(AbstractServerConfig.class);
	}
	String serverName;
	List<AbstractServiceConfig> services = new ArrayList<AbstractServiceConfig>();
	abstract public void build() ;
}
