package test.util.config;

import com.wowsanta.util.config.JsonConfiguration;

import lombok.Data;

@Data
public class TestConfiguration1 extends JsonConfiguration {
	String name;
	String value;
}
