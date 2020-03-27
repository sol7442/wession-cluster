package test.util.config;

import org.junit.Test;

import com.wowsanta.util.config.JsonConfiguration;

public class ConfgiurationTest {

	String fileName="./data/configuration.json";
	@Test
	public void save() {
		TestConfiguration2 config2 = new TestConfiguration2();
		
		TestConfiguration1 config1 = new TestConfiguration1();
		TestConfiguration1 config11 = new TestConfiguration1();
		config1.setName("test1");
		config1.setValue("value1");
		config11.setName("test11");
		config11.setValue("value11");
		
		config2.setName("test2");
		config2.setConf1(config1);
		
		config2.getConfigMap().put(config1.getName(), config1);
		config2.getConfigMap().put(config11.getName(), config11);
		
		config2.getConfigSet().add(config1);
		config2.getConfigSet().add(config11);
		
		System.out.println(config2.toString(true));
		config2.save(fileName);
		
		
	}
	
	@Test
	public void load() {
		//TestConfiguration2 config =  JsonConfiguration.load(fileName, TestConfiguration2.class);
		TestConfiguration2 config =  JsonConfiguration.load(fileName, TestConfiguration2.class);
//		System.out.println(config.toString(true));
		System.out.println("----------------------------");
		System.out.println(config.getConf1());
		System.out.println("----------------------------");
		
		
		
	}
}
