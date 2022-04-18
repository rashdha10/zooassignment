package utils;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author 91809
 *
 */
public class ConfigReader {

	private static Properties properties = null;

	public static String getBaseUrl() {
		return ConfigReader.loadProperties().getProperty("baseUrl");
	}

	/**
	 * This method is used to load the properties from config.properties file
	 * 
	 * @return {@link Properties}
	 */
	public static synchronized Properties loadProperties() {
		if (properties == null) {
			properties = new Properties();
			try {
				// Load common properties
				InputStream inputStream = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties");
				properties.load(inputStream);
			} catch (Exception e) {
				throw new IllegalArgumentException("Unable to load the config properties file");
			}
		}
		return properties;
	}
}
