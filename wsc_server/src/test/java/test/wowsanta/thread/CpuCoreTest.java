package test.wowsanta.thread;

import org.junit.Test;

public class CpuCoreTest {

	@Test
	public void cpu_core_test() {
		System.out.println(Runtime.getRuntime().availableProcessors());
	}
}
