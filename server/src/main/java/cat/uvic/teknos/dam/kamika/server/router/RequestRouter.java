package cat.uvic.teknos.dam.kamika.server.router;

import cat.uvic.teknos.dam.kamika.server.controllers.Controller;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Routes HTTP requests to the appropriate controller or handles
 * special protocol messages like DISCONNECT.
 * @author Your Name
 * @version 2.0.1
 */
public class RequestRouter {
    private final Map<String, Controller> controllers = new HashMap<>();
    private final RawHttp http = new RawHttp();

    /**
     * Registers a controller for a specific resource name (e.g., "developers").
     * @param resourceName The name of the resource.
     * @param controller The controller that will handle this resource.
     */
    public void addController(String resourceName, Controller controller) {
        controllers.put(resourceName.toLowerCase(), controller);
    }

    /**
     * Routes the given request to the correct handler.
     * It checks for special protocol messages before attempting to route to a controller.
     * @param request The incoming HTTP request.
     * @return The HTTP response to send to the client.
     * @throws Exception if an error occurs during response generation.
     */
    public RawHttpResponse<?> route(RawHttpRequest request) throws Exception {
        String method = request.getMethod();
        String path = request.getUri().getPath();

        if ("POST".equalsIgnoreCase(method) && "/disconnect".equals(path)) {
            return handleDisconnect();
        }

        String[] pathParts = path.split("/");
        if (pathParts.length < 2 || pathParts[1].isEmpty()) {
            return createNotFoundResponse();
        }

        String resourceName = pathParts[1].toLowerCase();
        Controller controller = controllers.get(resourceName);

        if (controller != null) {
            return controller.handle(request);
        } else {
            return createNotFoundResponse();
        }
    }

    /**
     * Handles the graceful disconnect request from an inactive client.
     * Sends an "ACK" acknowledgement, waits 1 second (as required),
     * and then returns the response, which allows the ClientHandler to close.
     * @return A 200 OK response with "ACK" in the body.
     */
    private RawHttpResponse<?> handleDisconnect() {
        String ackBody = "ACK";

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return http.parseResponse(
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: " + ackBody.length()
        ).withBody(new StringBody(ackBody));
    }

    /**
     * Creates a standard HTTP 404 Not Found response.
     * @return A {@link RawHttpResponse} with a 404 status and a JSON error body.
     */
    private RawHttpResponse<?> createNotFoundResponse() {
        String body = "{\"error\": \"Not Found\", \"message\": \"The requested resource was not found.\"}";
        return http.parseResponse(
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Content-Length: " + body.getBytes().length
        ).withBody(new StringBody(body));
    }
}