package de.fuh.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utilities {

    /**
     * Load config.properties file from main/resources folder
     *
     * @return
     * @throws IOException
     */
    public Properties getProperties() throws IOException {
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/main/resources/config.properties"));
        return prop;
    }
}
