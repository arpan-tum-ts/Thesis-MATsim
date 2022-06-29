package abm.properties;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Created by Nico on 19.07.2017.
 */
public class AbitResources {


    //TODO: provide defaults.

    public static AbitResources instance;

    private final Properties properties;

    private final Path baseDirectory;


    private AbitResources(Properties properties, String baseDirectory) {
        this.properties = properties;
        this.baseDirectory = Paths.get(baseDirectory).getParent();
    }

    public static void initializeResources(String fileName) {
        try (FileInputStream in = new FileInputStream(fileName)) {
            Properties properties = new Properties();
            properties.load(in);
            instance = new AbitResources(properties, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

    public synchronized int getInt(String key, int defaultValue) {
        if (properties.containsKey(key)) {
            return Integer.parseInt(properties.getProperty(key));
        } else {
            return defaultValue;
        }
    }

    public synchronized String getString(String key) {
        return properties.getProperty(key);
    }

    public synchronized String[] getArrayOfStrings(String key) {
        return properties.getProperty(key).split(",");
    }

    public synchronized String[] getArrayOfStrings(String key, String[] defaultValue) {
        if (properties.containsKey(key)) {
            return properties.getProperty(key).split(",");
        } else {
            return defaultValue;
        }
    }

    public synchronized boolean getBoolean(String key) {
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public synchronized boolean getBoolean(String key, boolean defaultValue) {
        if (properties.containsKey(key)) {
            return Boolean.parseBoolean(properties.getProperty(key));
        } else {
            return defaultValue;
        }
    }

    public synchronized double getDouble(String key, double defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Double.parseDouble(value) : defaultValue;
    }


}
