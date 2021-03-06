package cz.cvut.kbss.bpmn2stamp.console;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

	private static final String CONFIG_PROPERTIES_FILE = "/config.properties";

	private Properties properties;

	private Configuration() {
	}

	private Configuration init() throws IOException {
		try (InputStream input = Configuration.class.getResourceAsStream(CONFIG_PROPERTIES_FILE)) {
			properties = new Properties();
			properties.load(input);
			return this;
		} catch (IOException io) {
			// TODO handle this case, where initialization failed
			io.printStackTrace();
			throw io;
		}
	}

	public String getBpmnSuffix() {
		return properties.getProperty("cli.iri.suffix.bpmn");
	}

	public String getOrgSuffix() {
		return properties.getProperty("cli.iri.suffix.org");
	}

	public String getStampSuffix() {
		return properties.getProperty("cli.iri.suffix.stamp");
	}

	// singleton logic
	private static Configuration instance;

	public static Configuration getInstance() throws IOException {
		if (instance == null) {
			instance = new Configuration().init();
			return instance;
		}
		return instance;
	}

}
