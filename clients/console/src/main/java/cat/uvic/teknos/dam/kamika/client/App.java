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

/**
 * The main entry point for the console client application.
 * It manually creates and injects all dependencies into the main {@link Client} class
 * following the Dependency Injection pattern.
 * @author Your Name
 * @version 2.0.1
 */
public class App {

    /**
     * The main method that bootstraps the console client.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RawHttp rawHttp = new RawHttp();

        ModelFactory modelFactory;
        try {
            Class.forName(DIManager.class.getName());
            modelFactory = DIManager.get("model_factory");
        } catch (Exception e) {
            System.err.println("FATAL ERROR: Could not initialize dependencies via DIManager.");
            System.err.println("Check di.properties and class implementations.");
            e.printStackTrace(System.err);
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addAbstractTypeMapping(Developer.class, DeveloperImpl.class);
        objectMapper.registerModule(module);

        DeveloperApiClient developerApiClient = new DeveloperApiClient(rawHttp, objectMapper);

        Client clientApp = new Client(scanner, modelFactory, developerApiClient);

        clientApp.run();
    }
}