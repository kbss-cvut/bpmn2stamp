package console;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConfigurationTest {

	@Test
	public void getBpmnSuffix() {
		Configuration instance = Configuration.getInstance();
		Assertions.assertThat(instance.getBpmnSuffix()).isNotNull();
	}

	@Test
	public void getOrgSuffix() {
		Configuration instance = Configuration.getInstance();
		Assertions.assertThat(instance.getOrgSuffix()).isNotNull();
	}

	@Test
	public void getStampSuffix() {
		Configuration instance = Configuration.getInstance();
		Assertions.assertThat(instance.getStampSuffix()).isNotNull();
	}

	@Test
	public void getInstance() {
		Configuration instance = Configuration.getInstance();
		Assertions.assertThat(instance).isNotNull();
	}
}