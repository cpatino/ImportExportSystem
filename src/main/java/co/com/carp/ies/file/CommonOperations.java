package co.com.carp.ies.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.com.carp.ies.utils.AppPropertiesReader;

public class CommonOperations {

    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");

    public void read(final String fileName) throws IOException {
        SYSTEM_LOGGER.debug("File that will be processed is {}", fileName);
        SYSTEM_LOGGER.info("Reading {} file", fileName);

        Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName), charset)) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                SYSTEM_LOGGER.info("{} => line from document", line);
            }
        } catch (IOException ex) {
            SYSTEM_LOGGER.error("Error: {}", ex);
        }
    }

    public void delete(final String fileName) {
        SYSTEM_LOGGER.debug("File that will be processed is {}", fileName);
        SYSTEM_LOGGER.info("Deleting {} file", fileName);
        try {
            Files.delete(Paths.get(fileName));
        } catch (NoSuchFileException ex) {
            SYSTEM_LOGGER.error("No such file or directory {}", fileName);
        } catch (DirectoryNotEmptyException ex) {
            SYSTEM_LOGGER.error("Not empty {}", fileName);
        } catch (IOException ex) {
            SYSTEM_LOGGER.error("Error: {}", ex.toString());
        }
    }

    public void move(final String source) {
        String target = AppPropertiesReader.getInstance().getProcessedFolder();
        Path targetFolder = Paths.get(target);
        try {
            if (Files.notExists(targetFolder)) {
                Files.createDirectory(targetFolder);
            }
            
            String fileName = source.substring(source.lastIndexOf(File.separator));
            Files.move(Paths.get(source), Paths.get(target + File.separator + fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            SYSTEM_LOGGER.error("Error: {}", ex.toString());
        }
    }
}
