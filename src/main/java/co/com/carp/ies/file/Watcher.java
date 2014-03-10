package co.com.carp.ies.file;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.carp.ies.utils.AppPropertiesReader;

/**
 * This class is attempt to be the responsible to keep watching for new import files that will be added. It will decide if the new files are correctly
 * and inform to other objects in order to process.  
 * 
 * @since 10/04/2014
 * @version 1.0
 * @author Carlos Rodriguez
 */
public class Watcher extends Thread {

    /**
     * Systems logger
     */
    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");
    
    /**
     * Exceptions logger
     */
    private static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger("exception");

    @Override
    public void run() {
        this.watch();
    }
    
    /**
     * Keep watching for import files and notify another objects when a new file appears. 
     */
    private void watch() {
        CommonOperations fileOperations = new CommonOperations();
        Path directory = Paths.get(AppPropertiesReader.getInstance().getWatcherFolder());
        String processedDir = AppPropertiesReader.getInstance().getProcessedFolder();
        SYSTEM_LOGGER.info("Watching the directory {}", directory.toAbsolutePath());

        try {
            WatchService watcher = directory.getFileSystem().newWatchService();
            directory.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);

            WatchKey watckKey = watcher.take();
            
            while (true) {
                List<WatchEvent<?>> events = watckKey.pollEvents();
                
                for (WatchEvent<?> event : events) {
                    try {
                        
                        SYSTEM_LOGGER.info("Someone just created the file {}", event.context().toString());
                        Thread.sleep(1000 * 60);
                        
                        String fileName = directory.toAbsolutePath() + File.separator + event.context().toString();
                        List<String> lnsFromFile = fileOperations.read(fileName);
                        
                        if (lnsFromFile != null) {
                            fileOperations.move(fileName, processedDir);
                        }                        
                        
                    } catch (Exception e) {
                        SYSTEM_LOGGER.error("Error: {}", e.toString());
                    }
                }
            }

        } catch (Exception ex) {
            SYSTEM_LOGGER.error("Error: {}", ex.toString());
            EXCEPTION_LOGGER.error(ex.toString());
        }
    }
}