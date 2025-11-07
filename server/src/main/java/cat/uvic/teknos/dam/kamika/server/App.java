package cat.uvic.teknos.dam.kamika.server;

import cat.uvic.teknos.dam.kamika.repositories.DeveloperRepository;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.JdbcDeveloperRepository;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.DataSource;
import cat.uvic.teknos.dam.kamika.repositories.jdbc.datasources.SingleConnectionDataSource;
import cat.uvic.teknos.dam.kamika.server.controllers.Controller;
import cat.uvic.teknos.dam.kamika.server.controllers.DeveloperController;
import cat.uvic.teknos.dam.kamika.server.router.RequestRouter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The main entry point for the Kamika Server application.
 * This class now also initializes and injects a thread pool for concurrent client handling.
 * @author Your Name
 * @version 2.0
 */
public class App {

    /**
     * The main method that bootstraps the server.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        System.out.println("Starting Kamika Server...");

        DataSource dataSource = configureDataSource();
        RequestRouter router = configureRouter(dataSource);

        ExecutorService threadPool = Executors.newCachedThreadPool();

        startServer(router, threadPool);
    }

    /**
     * Configures and returns the data source for database connections.
     * @return A configured {@link DataSource} instance.
     */
    private static DataSource configureDataSource() {
        return new SingleConnectionDataSource(
                "mysql",
                "localhost:3306",
                "kamika",
                "root",
                "teknos"
        );
    }

    /**
     * Configures the request router with all the application's controllers.
     * @param dataSource The data source needed by repositories.
     * @return A configured {@link RequestRouter} instance.
     */
    private static RequestRouter configureRouter(DataSource dataSource) {
        DeveloperRepository developerRepository = new JdbcDeveloperRepository(dataSource);
        ObjectMapper objectMapper = new ObjectMapper();
        Controller developerController = new DeveloperController(developerRepository, objectMapper);

        var router = new RequestRouter();
        router.addController("developers", developerController);

        return router;
    }

    /**
     * Creates and starts a new Server instance.
     *
     * @param router The fully configured router to be used by the server.
     * @param threadPool The thread pool to manage concurrent client connections.
     */
    private static void startServer(RequestRouter router, ExecutorService threadPool) {
        var server = new Server(8081, router, threadPool); // Assegura't que el port és el correcte
        server.start();
    }
}