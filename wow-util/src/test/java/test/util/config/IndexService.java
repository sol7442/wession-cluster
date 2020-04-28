package test.util.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class IndexService extends AbstractServiceConfig{
	int length;
	public void run() {
		System.out.println("runt....");
	}
}
