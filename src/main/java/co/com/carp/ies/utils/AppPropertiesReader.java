package co.com.carp.ies.utils;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is attempt to be a reader for properties defined in application.properties file. It is designed using singleton pattern. 
 * 
 * @since 10/03/2014
 * @version 1.0
 * @author Carlos Rodriguez
 */
public class AppPropertiesReader implements Serializable {

    /**
     * Auto generated serial version
     */
    private static final long serialVersionUID = 1808179889823499899L;

    /**
     * Systems logger
     */
    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");
    
    /**
     * {@link AppPropertiesReader} instance.
     */
    private static AppPropertiesReader instance;
    
    /**
     * Property file's name.
     */
    private static final String PROPERTY_FILE = "/application.properties";
    
    /**
     * {@link Properties} instance that will be used to get property values. 
     */
    private final Properties appProperties;

    /**
     * Folder that contains new import files.
     */
    private static final String WATCHER_FOLDER = "watcher.folder";
    
    /**
     * Folder that contains imported files that were processed. 
     */
    private static final String PROCESSED_FOLDER = "processed.folder";

    /**
     * Constructor
     */
    private AppPropertiesReader() {
        super();
        appProperties = new Properties();
        try {
            appProperties.load(AppPropertiesReader.class.getResourceAsStream(PROPERTY_FILE));
        } catch (IOException e) {
            SYSTEM_LOGGER.error("Error: {}", e.toString());
        }
    }

    /**
     * Gets the {@link AppPropertiesReader} instance. When it is required,
     * it is initialized using private constructor.
     * 
     * @return {@link AppPropertiesReader} instance.
     */
    public static AppPropertiesReader getInstance() {
        if (instance == null) {
            instance = new AppPropertiesReader();
        }
        return instance;
    }

    /**
     * Gets property value for key defined in WATCHER_FOLDER.
     * 
     * @return {@link String} value for WATCHER_FOLDER defined in application.property file.
     */
    public String getWatcherFolder() {
        if (!appProperties.containsKey(WATCHER_FOLDER)) {
            SYSTEM_LOGGER.error("Please provide a value for parameter {} in application.properties file", WATCHER_FOLDER);
            throw new InvalidParameterException("Please provide a value for parameter " + WATCHER_FOLDER + " in application.properties file.");
        }
        return appProperties.getProperty(WATCHER_FOLDER);
    }
    
    /**
     * Gets property value for key defined in PROCESSED_FOLDER.
     * 
     * @return {@link String} value for PROCESSED_FOLDER defined in application.property file.
     */
    public String getProcessedFolder() {
        if (!appProperties.containsKey(PROCESSED_FOLDER)) {
            SYSTEM_LOGGER.error("Please provide a value for parameter {} in application.properties file", PROCESSED_FOLDER);
            throw new InvalidParameterException("Please provide a value for parameter " + PROCESSED_FOLDER + " in application.properties file.");
        }
        return appProperties.getProperty(PROCESSED_FOLDER);
    }
}
