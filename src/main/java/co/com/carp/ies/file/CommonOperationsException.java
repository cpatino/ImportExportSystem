package co.com.carp.ies.file;

/**
 * This class is attempt to be an exception that will be thrown when a {@link CommonOperations} method reach an exception.
 * 
 * @since 10/04/2014
 * @version 1.0
 * @author Carlos Rodriguez
 */
public class CommonOperationsException extends Exception {
    
    /**
     * Auto generated serial version.
     */
    private static final long serialVersionUID = 2906034788342523543L;

    /**
     * Constructor
     * 
     * @param message Message that will be displayed in the exception.
     */
    public CommonOperationsException(String message) {
        super(message);
    }
}
