package co.com.carp.ies.distribution.rules;

import java.util.List;

public class TableRule {
    
    /**
     * List of all allowed tables for database selected.
     */
    private List<String> allowed;
    
    /**
     * Database that will be used to validated allowed tables
     */
    private final String databaseName;
    
    public TableRule(String databaseName) {
        super();
        this.databaseName = databaseName;
        loadAllowedTables();
    }
    
    /**
     * Load the name of allowed tables that must be used to validate file's name that wants to be imported.   
     */
    private void loadAllowedTables() {
       /**
        * TODO: Connect to database and load a string using the database's name provided in the variable databaseName. 
        */
    }

    /**
     * @return the tables allowed
     */
    public List<String> getAllowed() {
        return allowed;
    }

    /**
     * @param allowed the tables allowed to set
     */
    public void setAllowed(List<String> allowed) {
        this.allowed = allowed;
    }
    
    
}
