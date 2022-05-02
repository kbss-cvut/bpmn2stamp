package cz.cvut.kbss.bpmn2stamp.console;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ConfigurationTest {

	@Test
	public void getBpmnSuffix() throws IOException {
		Configuration instance = Configuration.getInstance();
		Assertions.assertThat(instance.getBpmnSuffix()).isNotNull();
	}

	@Test
	public void getOrgSuffix() throws IOException {
		Configuration instance = Configuration.getInstance();
		Assertions.assertThat(instance.getOrgSuffix()).isNotNull();
	}

	@Test
	public void getStampSuffix() throws IOException {
		Configuration instance = Configuration.getInstance();
		Assertions.assertThat(instance.getStampSuffix()).isNotNull();
	}

	@Test
	public void getInstance() throws IOException {
		Configuration instance = Configuration.getInstance();
		Assertions.assertThat(instance).isNotNull();
	}
}