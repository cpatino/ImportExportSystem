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

import co.com.carp.ies.distribution.rules.TableRule;
import co.com.carp.ies.utils.AppPropertiesReader;

/**
 * This class is attempt to be the responsible to keep watching for new import files that will be added. It will decide if the new files are correctly
 * and inform to other objects in order to process.
 * @since 10/04/2014
 * @version 1.0
 * @author Carlos Rodriguez
 */
public class Watcher {

    /**
     * Systems logger
     */
    protected static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");

    /**
     * Exceptions logger
     */
    protected static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger("exception");

    /**
     * Keep watching for import files and notify another objects when a new file appears.
     */
    public void watch() {
        Path directory = Paths.get(AppPropertiesReader.getInstance().getWatcherFolder());
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
                        WatcherThread wThread = new WatcherThread(fileName);
                        wThread.start();

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

/**
 * This class is attempt to be a thread that will be created all times when a new file is created. It will help to process imported files.
 * @since 11/03/2014
 * @version 1.0
 * @author Carlos Rodriguez
 */
class WatcherThread extends Thread {

    private String filePath;

    public WatcherThread(final String filePath) {
        super();
        this.filePath = filePath;
    }

    @Override
    public void run() {
        CommonOperations fileOperations = new CommonOperations();
        String processedDir = AppPropertiesReader.getInstance().getProcessedFolder();
        String notProcessedDir = AppPropertiesReader.getInstance().getNotProcessedFolder();
        String tableName = filePath.substring(filePath.lastIndexOf(File.separator) + 1, filePath.lastIndexOf("."));

        try {
            TableRule tableRule = new TableRule(AppPropertiesReader.getInstance().getSelectedDatabaseName());
            
            List<String> lnsFromFile = fileOperations.read(filePath);

            if (lnsFromFile != null) {
                fileOperations.move(filePath, processedDir);
            }
        } catch (CommonOperationsException ex) {

            Watcher.SYSTEM_LOGGER.error("An error ocurred when the file {} was trying to be processed", filePath);
            Watcher.EXCEPTION_LOGGER.error(ex.toString());

        } catch (IllegalArgumentException ex) {

            Watcher.SYSTEM_LOGGER.error("Table {} is not valid, please fix the imported file name", tableName);
            Watcher.EXCEPTION_LOGGER.error(ex.toString());

            try {
                fileOperations.move(filePath, notProcessedDir);
            } catch (CommonOperationsException e) {
                Watcher.SYSTEM_LOGGER.error("An error ocurred when the file {} was trying to be processed", filePath);
                Watcher.EXCEPTION_LOGGER.error(ex.toString());
            }

        }
    }

}