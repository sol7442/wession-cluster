package test.util.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ClusterService extends AbstractServiceConfig{
	
	int size;
	public void run() {
		System.out.println("runt....");
	}
}
