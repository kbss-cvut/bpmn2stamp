package console;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

	private static final String CONFIG_PROPERTIES_FILE = "/config.properties";

	private Properties properties;

	private Configuration() {
	}

	private Configuration init() {
		try (InputStream input = Configuration.class.getResourceAsStream(CONFIG_PROPERTIES_FILE)) {
			properties = new Properties();
			properties.load(input);
			return this;
		} catch (IOException io) {
			io.printStackTrace();
		}
		// TODO handle this case, where initialization failed
		return null;
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

	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration().init();
			return instance;
		}
		return instance;
	}

}
