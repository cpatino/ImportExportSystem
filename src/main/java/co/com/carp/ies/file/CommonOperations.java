package co.com.carp.ies.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is attempt to have all common file operations.
 * 
 * @since 10/04/2014
 * @version 1.0
 * @author Carlos Rodriguez
 */
public class CommonOperations implements Serializable {

    /**
     * Auto generated serial version
     */
    private static final long serialVersionUID = -1133091351201465391L;

    /**
     * Systems logger
     */
    private static final Logger SYSTEM_LOGGER = LoggerFactory.getLogger("system");
    
    /**
     * Exceptions logger
     */
    private static final Logger EXCEPTION_LOGGER = LoggerFactory.getLogger("exception");

    /**
     * Reads the file that was passed as parameter and return a {@link List} with all lines from it.
     * 
     * @param fileName Complete directory + file name from file that will be read.
     * @return {@link List} with all lines from file, or null if {@link IOException} is thrown.
     * @throws CommonOperationsException Exception thrown trying to read file.
     */
    public List<String> read(final String fileName) throws CommonOperationsException {
        List<String> lnsFromFile = new ArrayList<>(); 
        
        SYSTEM_LOGGER.debug("File that will be processed is {}", fileName);
        SYSTEM_LOGGER.info("Reading {} file", fileName);

        Charset charset = Charset.forName("UTF-8");
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName), charset)) {
            String line = null;
            
            while ((line = reader.readLine()) != null) {
                SYSTEM_LOGGER.info("{} => line from document", line);
                lnsFromFile.add(line);
            }
            
            SYSTEM_LOGGER.info("End of {} file was reached", fileName);
            
        } catch (IOException ex) {
            SYSTEM_LOGGER.error("Error: {}", ex.toString());
            EXCEPTION_LOGGER.error(ex.toString());
            throw new CommonOperationsException("Exception trying to read file " + fileName);
        }
        
        return lnsFromFile;
    }

    /**
     * Deletes the file that is informed.
     * 
     * @param fileName Complete directory + file name from file that will be deleted.
     * @throws CommonOperationsException Exception thrown trying to delete file.
     */
    public void delete(final String fileName) throws CommonOperationsException {
        SYSTEM_LOGGER.debug("File that will be processed is {}", fileName);
        SYSTEM_LOGGER.info("Deleting {} file", fileName);
        try {
            Files.delete(Paths.get(fileName));
        } catch (IOException ex) {
            SYSTEM_LOGGER.error("Error: {}", ex.toString());
            EXCEPTION_LOGGER.error(ex.toString());
            throw new CommonOperationsException("Exception trying to delete file " + fileName);
        }
    }

    /**
     * Moves the file from source folder (Import file directory + file name) to processed folder. 
     * 
     * @param source Complete directory + file name from file that will be moved.
     * @param destinyFolder Directory where file is going to be moved.
     * @throws CommonOperationsException Exception thrown trying to move file.
     */
    public void move(final String source, final String destinyFolder) throws CommonOperationsException {
        SYSTEM_LOGGER.debug("File that will be processed is {}", source);
        SYSTEM_LOGGER.info("Moving {} file", source);
        
        Path targetFolder = Paths.get(destinyFolder);
        
        try {
            if (Files.notExists(targetFolder)) {
                Files.createDirectory(targetFolder);
            }
            
            DateTime date = new DateTime(DateTimeZone.getDefault());
            
            String fileName = source.substring(source.lastIndexOf(File.separator), source.lastIndexOf("."));
            fileName = fileName.concat(date.toString("dd-MM-yyyy_HH-mm-ss", Locale.US)).concat(".txt");
            
            Files.move(Paths.get(source), Paths.get(destinyFolder + File.separator + fileName), StandardCopyOption.REPLACE_EXISTING);
            SYSTEM_LOGGER.info("{} file moved to {} directory", source, targetFolder.toAbsolutePath() + File.separator + fileName);
        } catch (IOException ex) {
            SYSTEM_LOGGER.error("Error: {}", ex.toString());
            EXCEPTION_LOGGER.error(ex.toString());
            throw new CommonOperationsException("Exception trying to move file " + source);
        }
    }
}
