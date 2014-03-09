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

public class Watcher extends Thread {

    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");

    @Override
    public void run() {
        CommonOperations fileOperations = new CommonOperations();
        Path directory = Paths.get(AppPropertiesReader.getInstance().getWatcherFolder());
        SYSTEM_LOGGER.info("Now watching the directory {}", directory.toAbsolutePath());

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
                        fileOperations.read(fileName);
                        fileOperations.move(fileName);
                        
                        
                    } catch (Exception e) {
                        SYSTEM_LOGGER.error("Error: {}", e.toString());
                    }
                }
            }

        } catch (Exception e) {
            SYSTEM_LOGGER.error("Error: {}", e.toString());
        }
    }
}