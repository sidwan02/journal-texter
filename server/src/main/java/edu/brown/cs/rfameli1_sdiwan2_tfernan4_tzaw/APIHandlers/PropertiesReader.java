package edu.brown.cs.rfameli1_sdiwan2_tfernan4_tzaw.APIHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * The class Properties reader.
 */
public final class PropertiesReader {

  /**
   * Constant LOGGER.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesReader.class);

  /**
   * Constant PROPERTIES.
   */
  private static final Properties PROPERTIES;

  /**
   * Constant PROP_FILE.
   */
  private static final String PROP_FILE = "config.properties";

  /**
   * Default private constructor PropertiesReader.
   */
  private PropertiesReader() {
  }

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
   * Method getProperty.
   *
   * @param name String name file.
   * @return Return property
   */
  public static String getProperty(final String name) {

    return PROPERTIES.getProperty(name);
  }
}
