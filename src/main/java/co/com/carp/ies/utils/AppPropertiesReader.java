package co.com.carp.ies.utils;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AppPropertiesReader {

    private static final Logger logger = LoggerFactory.getLogger(AppPropertiesReader.class);

    private static final String WATCHER_FOLDER = "watcher.folder";
    
    private static final String PROCESSED_FOLDER = "processed.folder";

    private static final String PROPERTY_FILE = "/application.properties";

    private static AppPropertiesReader instance;

    private final Properties appProperties;

    private AppPropertiesReader() {
        super();
        appProperties = new Properties();
        try {
            appProperties.load(AppPropertiesReader.class.getResourceAsStream(PROPERTY_FILE));
        } catch (IOException e) {
            logger.error("Error: {}", e.toString());
        }
    }

    public static AppPropertiesReader getInstance() {
        if (instance == null) {
            instance = new AppPropertiesReader();
        }
        return instance;
    }

    public String getWatcherFolder() {
        if (!appProperties.containsKey(WATCHER_FOLDER)) {
            logger.error("Please provide a value for parameter {} in application.properties file", WATCHER_FOLDER);
            throw new InvalidParameterException("Please provide a value for parameter " + WATCHER_FOLDER + " in application.properties file.");
        }
        return appProperties.getProperty(WATCHER_FOLDER);
    }
    
    public String getProcessedFolder() {
        if (!appProperties.containsKey(PROCESSED_FOLDER)) {
            logger.error("Please provide a value for parameter {} in application.properties file", PROCESSED_FOLDER);
            throw new InvalidParameterException("Please provide a value for parameter " + PROCESSED_FOLDER + " in application.properties file.");
        }
        return appProperties.getProperty(PROCESSED_FOLDER);
    }
}
