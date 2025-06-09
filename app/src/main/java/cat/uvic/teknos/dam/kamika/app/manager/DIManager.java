package cat.uvic.teknos.dam.kamika.app.manager;

import cat.uvic.teknos.dam.kamika.app.exceptions.DIException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * Dependency Injection Manager for the KAMIKA system.
 * Handles the creation and management of dependencies based on configuration in di.properties.
 *
 * @author Montse Orozco
 * @version 1.0
 */
public class DIManager {
    private final Properties properties;

    /**
     * Loads dependency configuration from the di.properties file.
     * Throws DIException if the file cannot be loaded.
     */
    public DIManager() {
        properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("/di.properties"));
        } catch (IOException e) {
            throw new DIException(e);
        }
    }

    /**
     * Returns an instance of the class mapped to the given key in di.properties.
     * Uses the public no-argument constructor for instantiation.
     *
     * @param className    the key in di.properties for the desired class
     * @param expectedType the expected type of the returned object
     * @param <T>          the generic type of the expected object
     * @return an instance of the requested class
     * @throws DIException if instantiation fails or the type is incompatible
     */
    public <T> T get(String className, Class<T> expectedType) {
        try {
            var clazz = Class.forName(properties.getProperty(className));
            Object instance = clazz.getConstructor().newInstance();
            if (!expectedType.isInstance(instance)) {
                throw new DIException("Class " + clazz.getName() + " is not compatible with " + expectedType.getName());
            }
            return expectedType.cast(instance);
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new DIException(e);
        }
    }
}