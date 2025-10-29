package cat.uvic.teknos.dam.kamika.server;

import cat.uvic.teknos.dam.kamika.server.router.RequestRouter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The main server class that listens for incoming TCP connections.
 * It manages the server socket lifecycle and delegates the handling of each
 * client connection to a new {@link ClientHandler} thread to support concurrency.
 * @author Montse
 * @version 2.0.0
 */
public class Server {
    private final int port;
    private final RequestRouter router;
    private ServerSocket serverSocket;
    private volatile boolean running; // volatile ensures thread visibility of changes

    /**
     * Constructs a new Server instance.
     *
     * @param port The TCP port number on which the server will listen.
     * @param router The request router responsible for directing incoming requests.
     */
    public Server(int port, RequestRouter router) {
        this.port = port;
        this.router = router;
        this.running = false;
    }

    /**
     * Starts the server's listening loop.
     * This method binds to the specified port and enters an infinite loop to accept
     * client connections. Each accepted connection is processed in a separate thread.
     */
    public void start() {
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("Server started on http://localhost:" + port);

            while (running) {
                try {
                    Socket client = serverSocket.accept();
                    // Create and start a new thread for each client to handle requests concurrently.
                    var clientHandler = new ClientHandler(client, router);
                    new Thread(clientHandler).start();
                } catch (IOException e) {
                    if (!running) {
                        System.out.println("Server is shutting down.");
                        break;
                    }
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not start server on port " + port + ": " + e.getMessage());
        } finally {
            stop();
        }
    }

    /**
     * Stops the server and closes the server socket.
     * This method provides a way to gracefully shut down the server.
     */
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
    }
}