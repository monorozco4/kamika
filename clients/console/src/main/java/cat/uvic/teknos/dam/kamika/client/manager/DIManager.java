package cat.uvic.teknos.dam.kamika.client.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * A simple Dependency Injection (DI) manager.
 * It loads class implementations from a properties file and provides singleton
 * instances of them on demand.
 * Author: Montse
 * Version: 2.0.0
 */
public final class DIManager {
    private static final String DI_PROPERTIES_FILE = "/di.properties";
    private static final Properties properties = new Properties();
    private static final Map<String, Object> instances = new ConcurrentHashMap<>();

    static {
        try (InputStream input = DIManager.class.getResourceAsStream(DI_PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find " + DI_PROPERTIES_FILE);
            }
            properties.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Error loading DI properties file", ex);
        }
    }

    /**
     * Gets a singleton instance of the class associated with the given key.
     * @param <T> The type of the object to retrieve.
     * @param key The key specified in the di.properties file.
     * @return The singleton instance of the requested object.
     * @throws RuntimeException if the class cannot be found or instantiated.
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        return (T) instances.computeIfAbsent(key, k -> {
            String className = properties.getProperty(k);
            if (className == null) {
                throw new RuntimeException("DI key '" + k + "' not found in " + DI_PROPERTIES_FILE);
            }
            try {
                return Class.forName(className).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate class for key '" + k + "'", e);
            }
        });
    }

    private DIManager() {}
}