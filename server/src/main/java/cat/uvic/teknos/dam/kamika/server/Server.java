package cat.uvic.teknos.dam.kamika.server;

import cat.uvic.teknos.dam.kamika.server.router.RequestRouter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages the server socket and uses an ExecutorService (thread pool)
 * to handle multiple client connections concurrently.
 * It also maintains a count of active clients and reports it via a daemon thread.
 * @author Your Name
 * @version 2.0.1
 */
public class Server {
    private final int port;
    private final RequestRouter router;
    private ServerSocket serverSocket;
    private volatile boolean running;
    private final ExecutorService threadPool;

    private final AtomicInteger activeClients = new AtomicInteger(0);

    /**
     * Constructs a new Server instance.
     *
     * @param port The TCP port number on which the server will listen.
     * @param router The request router responsible for directing incoming requests.
     * @param threadPool The thread pool to manage concurrent client connections.
     */
    public Server(int port, RequestRouter router, ExecutorService threadPool) {
        this.port = port;
        this.router = router;
        this.threadPool = threadPool;
        this.running = false;
    }

    /**
     * Starts the server's listening loop.
     * It also starts the client monitoring daemon thread.
     */
    public void start() {
        startClientMonitor();

        try {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("✅ Server started on http://localhost:" + port + " (using Thread Pool)");

            while (running) {
                try {
                    Socket client = serverSocket.accept();

                    var clientHandler = new ClientHandler(client, router, activeClients);

                    threadPool.submit(clientHandler);

                } catch (IOException e) {
                    if (!running) {
                        System.out.println("Server is shutting down.");
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
     * Starts a new daemon thread that monitors and reports the active client count every minute.
     */
    private void startClientMonitor() {
        Runnable monitorTask = () -> {
            try {
                while (true) {
                    Thread.sleep(60000);

                    System.out.println("[SERVER MONITOR] Currently connected clients: " + activeClients.get());
                }
            } catch (InterruptedException e) {
                System.out.println("[SERVER MONITOR] Monitor thread interrupted and stopped.");
            }
        };

        Thread monitorThread = new Thread(monitorTask);
        monitorThread.setDaemon(true);
        monitorThread.setName("ClientMonitorThread");
        monitorThread.start();

        System.out.println("[SERVER MONITOR] Client monitoring thread has started.");
    }

    /**
     * Stops the server, closes the server socket, and shuts down the thread pool.
     */
    public void stop() {
        running = false;

        if (threadPool != null && !threadPool.isShutdown()) {
            threadPool.shutdown();
        }

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage()); // English
        }
    }
}