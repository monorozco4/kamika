package cat.uvic.teknos.dam.kamika.server;

import cat.uvic.teknos.dam.kamika.server.exceptions.BadRequestException;
import cat.uvic.teknos.dam.kamika.server.exceptions.MethodNotAllowedException;
import cat.uvic.teknos.dam.kamika.server.exceptions.NotFoundException;
import cat.uvic.teknos.dam.kamika.server.router.RequestRouter;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles communication for a single client connection on a dedicated thread.
 * It reads the request, routes it to the appropriate controller, and centrally
 * handles exceptions to generate the final HTTP response. It also updates a
 * shared client counter and logs the thread handling the request.
 *
 * @author Your Name
 * @version 2.2 // Added thread logging and client counting
 */
public class ClientHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    private final Socket clientSocket;
    private final RequestRouter router;
    private final RawHttp http;
    private final AtomicInteger activeClients;

    /**
     * Constructs a new ClientHandler.
     *
     * @param socket The client socket representing the connection.
     * @param router The router that will delegate the request to a controller.
     * @param activeClients The shared atomic counter for tracking active clients.
     */
    public ClientHandler(Socket socket, RequestRouter router, AtomicInteger activeClients) {
        this.clientSocket = socket;
        this.router = router;
        this.http = new RawHttp();
        this.activeClients = activeClients;
    }

    /**
     * Executes the request handling logic for the client thread.
     * It increments the active client counter, processes the request,
     * and decrements the counter in a finally block to ensure accuracy.
     * It also logs the name of the thread from the pool handling this request.
     */
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();

        int currentClients = activeClients.incrementAndGet();
        logger.log(Level.INFO, "[" + threadName + "] Client connected. Total active clients: " + currentClients);

        try (clientSocket) {
            RawHttpRequest request = http.parseRequest(clientSocket.getInputStream());
            logger.log(Level.INFO, "[" + threadName + "] Request received: {0} {1}",
                    new Object[]{request.getStartLine().getMethod(), request.getStartLine().getUri()});

            RawHttpResponse<?> response;
            try {
                response = router.route(request);
            }
            catch (NotFoundException e) {
                logger.log(Level.INFO, "[" + threadName + "] Resource not found: {0}", e.getMessage());
                response = createErrorResponse(404, "Not Found", e.getMessage());
            } catch (BadRequestException e) {
                logger.log(Level.WARNING, "[" + threadName + "] Bad request: {0}", e.getMessage());
                response = createErrorResponse(400, "Bad Request", e.getMessage());
            } catch (MethodNotAllowedException e) {
                logger.log(Level.WARNING, "[" + threadName + "] Method not allowed: {0}", e.getMessage());
                response = createErrorResponse(405, "Method Not Allowed", e.getMessage());
            }
            // Handle any other unexpected error as a 500
            catch (Exception e) {
                logger.log(Level.SEVERE, "[" + threadName + "] An internal server error occurred.", e);
                response = createErrorResponse(500, "Internal Server Error", "An unexpected error occurred on the server.");
            }

            response.writeTo(clientSocket.getOutputStream());

        } catch (IOException e) {
            logger.log(Level.WARNING, "[" + threadName + "] A communication error occurred with the client.", e);
        } finally {
            int clientsLeft = activeClients.decrementAndGet();
            logger.log(Level.INFO, "[" + threadName + "] Client disconnected. Clients remaining: " + clientsLeft);
        }
    }

    /**
     * Creates a generic JSON error response.
     *
     * @param code The HTTP status code.
     * @param status The HTTP status message.
     * @param message The detailed error message for the JSON body.
     * @return A fully constructed {@link RawHttpResponse} for an error.
     */
    private RawHttpResponse<?> createErrorResponse(int code, String status, String message) {
        String body = "{\"error\": \"" + status + "\", \"message\": \"" + message + "\"}";
        return http.parseResponse(
                "HTTP/1.1 " + code + " " + status + "\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Content-Length: " + body.getBytes().length
        ).withBody(new StringBody(body));
    }
}