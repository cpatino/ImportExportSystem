package co.com.carp.ies.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.carp.ies.file.Watcher;

/**
 * Project entry point.
 * 
 * @since 10/04/2014
 * @version 1.0
 * @author Carlos Rodriguez
 */
public class Main {
    
    /**
     * Systems logger
     */
    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");
    
    /**
     * Exceptions logger
     */
    private static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger("exception");
    
    /**
     * Project starting point
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            Watcher watcher = new Watcher();
            watcher.watch();
        } catch (Exception ex) {
            SYSTEM_LOGGER.error("Ups!, an exception was throwed: {}", ex.toString());
            EXCEPTION_LOGGER.error(ex.toString());
        }        
    }
}
