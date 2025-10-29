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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles communication for a single client. It includes a centralized
 * exception handling mechanism to convert custom exceptions thrown by controllers
 * into appropriate HTTP error responses.
 * @author Montse
 * @version 2.0.0
 */
public class ClientHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

    private final Socket clientSocket;
    private final RequestRouter router;
    private final RawHttp http;

    /**
     * Constructs a new ClientHandler.
     *
     * @param socket The client socket representing the connection.
     * @param router The router that will delegate the request to a controller.
     */
    public ClientHandler(Socket socket, RequestRouter router) {
        this.clientSocket = socket;
        this.router = router;
        this.http = new RawHttp();
    }

    /**
     * Executes the request handling logic for the client thread. It parses the request,
     * routes it, and centrally manages exceptions to generate final HTTP responses.
     */
    @Override
    public void run() {
        try (clientSocket) {
            RawHttpRequest request = http.parseRequest(clientSocket.getInputStream());
            logger.log(Level.INFO, "Request received: {0} {1}",
                    new Object[]{request.getStartLine().getMethod(), request.getStartLine().getUri()});

            RawHttpResponse<?> response;
            try {
                response = router.route(request);
            }
            catch (NotFoundException e) {
                logger.log(Level.INFO, "Resource not found: {0}", e.getMessage());
                response = createErrorResponse(404, "Not Found", e.getMessage());
            } catch (BadRequestException e) {
                logger.log(Level.WARNING, "Bad request: {0}", e.getMessage());
                response = createErrorResponse(400, "Bad Request", e.getMessage());
            } catch (MethodNotAllowedException e) {
                logger.log(Level.WARNING, "Method not allowed: {0}", e.getMessage());
                response = createErrorResponse(405, "Method Not Allowed", e.getMessage());
            }
            catch (Exception e) { // Catches any other unexpected exception
                logger.log(Level.SEVERE, "An internal server error occurred.", e);
                response = createErrorResponse(500, "Internal Server Error", "An unexpected error occurred on the server.");
            }

            response.writeTo(clientSocket.getOutputStream());

        } catch (IOException e) {
            logger.log(Level.WARNING, "A communication error occurred with the client.", e);
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