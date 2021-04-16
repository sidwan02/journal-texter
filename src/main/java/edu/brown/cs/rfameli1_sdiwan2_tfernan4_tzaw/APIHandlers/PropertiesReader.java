package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// https://itnext.io/how-to-store-passwords-and-api-keys-in-project-code-1eaf5cb235c9
/**
 * Class that reads a properties file containing API keys.
 */
public final class PropertiesReader {

  private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesReader.class);

  private static final Properties PROPERTIES;

  /**
   * File location of the config file.
   */
  private static final String PROP_FILE = "config.properties";

  private PropertiesReader() {
  }

  // https://mkyong.com/java/java-properties-file-examples/
  static {
    PROPERTIES = new Properties();
    try (InputStream input = new FileInputStream(PROP_FILE)) {

      // load a properties file
      PROPERTIES.load(input);

    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Get the configuration key for a given keyword.
   * @param name String name file.
   * @return Return property.
   */
  public static String getProperty(final String name) {

    return PROPERTIES.getProperty(name);
  }
}
