package test.util.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.wowsanta.util.config.JsonConfiguration;

import lombok.Data;

@Data
public class TestConfiguration2 extends JsonConfiguration{
	String name;
	JsonConfiguration conf1;
	Map<String,JsonConfiguration> configMap = new HashMap<>();
	Set<JsonConfiguration> configSet = new HashSet<>();
}
