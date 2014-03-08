package co.com.carp.ies.main;

import co.com.carp.ies.file.Watcher;

public class Main {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        Thread thread = new Watcher();
        thread.setName(Watcher.class.getName());
        thread.start();
    }
}
