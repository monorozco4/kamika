package cat.uvic.teknos.dam.kamika.app.manager;

import cat.uvic.teknos.dam.kamika.app.exceptions.DIException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * Dependency Injection Manager for the KAMIKA system.
 * <p>
 * This class handles the creation and management of dependencies based on configuration
 * specified in the {@code di.properties} file. It provides a simple dependency injection
 * mechanism that instantiates classes using their no-argument constructors.
 * </p>
 * <p>
 * The properties file should contain key-value pairs where the key represents a logical
 * name for the dependency and the value is the fully qualified class name.
 * </p>
 *
 * @author Montse Orozco
 * @version 1.0
 * @see DIException
 */
public class DIManager {
    private final Properties properties;

    /**
     * Constructs a new DIManager and loads the dependency configuration from
     * the {@code di.properties} file located in the classpath.
     *
     * @throws DIException if the properties file cannot be loaded or is not found
     */
    public DIManager() {
        properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("/di.properties"));
        } catch (IOException e) {
            throw new DIException("Failed to load di.properties configuration file", e);
        }
    }

    /**
     * Creates and returns an instance of the class mapped to the specified key
     * in the {@code di.properties} file.
     * <p>
     * The method performs the following operations:
     * <ol>
     *   <li>Looks up the class name in the properties file using the provided key</li>
     *   <li>Loads the class using reflection</li>
     *   <li>Creates a new instance using the class's no-argument constructor</li>
     *   <li>Verifies the instance is compatible with the expected type</li>
     *   <li>Returns the properly typed instance</li>
     * </ol>
     * </p>
     *
     * @param className    the property key for the desired class in di.properties
     * @param expectedType the expected interface or superclass of the returned object
     * @param <T>          the generic type parameter representing the expected type
     * @return a newly created instance of the requested class
     * @throws DIException if any of the following occurs:
     *                     <ul>
     *                       <li>The class name is not found in the properties file</li>
     *                       <li>The class cannot be loaded</li>
     *                       <li>Instantiation fails (no public no-arg constructor)</li>
     *                       <li>The created instance is not compatible with the expected type</li>
     *                     </ul>
     * @see Class#forName(String)
     * @see Class#getConstructor(Class...)
     * @see Class#isInstance(Object)
     */
    public <T> T get(String className, Class<T> expectedType) {
        try {
            String propertyValue = properties.getProperty(className);
            if (propertyValue == null) {
                throw new DIException("No configuration found for key: " + className);
            }

            var clazz = Class.forName(propertyValue);
            Object instance = clazz.getConstructor().newInstance();

            if (!expectedType.isInstance(instance)) {
                throw new DIException("Class " + clazz.getName() +
                        " does not implement/extend " + expectedType.getName());
            }

            return expectedType.cast(instance);
        } catch (ClassNotFoundException e) {
            throw new DIException("Class not found for key: " + className, e);
        } catch (NoSuchMethodException e) {
            throw new DIException("Missing public no-argument constructor for class mapped to: " + className, e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new DIException("Failed to instantiate class for key: " + className, e);
        }
    }
}