package cat.uvic.teknos.dam.kamika.client;

import cat.uvic.teknos.dam.kamika.client.api.DeveloperApiClient;
import cat.uvic.teknos.dam.kamika.client.manager.DIManager;
import cat.uvic.teknos.dam.kamika.model.Developer;
import cat.uvic.teknos.dam.kamika.model.ModelFactory;
import cat.uvic.teknos.dam.kamika.model.impl.DeveloperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import rawhttp.core.RawHttp;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The main entry point for the console client application.
 * It manually creates and injects all dependencies into the main {@link Client} class
 * following the Dependency Injection pattern. Uses proper logging for errors.
 * @author Your Name
 * @version 1.3 // Added Logger
 */
public class App {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    /**
     * The main method that bootstraps the console client.
     * Creates all necessary instances and injects them via constructors. Handles initialization errors.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        logger.log(Level.INFO, "Starting Kamika Client...");

        Scanner scanner = new Scanner(System.in);
        RawHttp rawHttp = new RawHttp();

        ModelFactory modelFactory;
        try {
            Class.forName(DIManager.class.getName());
            modelFactory = DIManager.get("model_factory");
            logger.log(Level.CONFIG, "DIManager initialized and ModelFactory obtained.");
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "FATAL ERROR: DIManager class not found. Cannot start application.", e);
            return;
        } catch (ExceptionInInitializerError | RuntimeException e) {
            logger.log(Level.SEVERE, "FATAL ERROR: Failed to initialize dependencies via DIManager. Check di.properties or implementation classes.", e);
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addAbstractTypeMapping(Developer.class, DeveloperImpl.class);
        objectMapper.registerModule(module);
        logger.log(Level.CONFIG, "ObjectMapper configured.");

        DeveloperApiClient developerApiClient = new DeveloperApiClient(rawHttp, objectMapper);

        Client clientApp = new Client(scanner, modelFactory, developerApiClient);

        logger.log(Level.INFO, "Client instance created. Starting UI loop...");
        clientApp.run();
    }
}