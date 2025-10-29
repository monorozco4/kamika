package cat.uvic.teknos.dam.kamika.server.router;

import cat.uvic.teknos.dam.kamika.server.controllers.Controller;
import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.body.StringBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Routes HTTP requests to the appropriate controller based on the resource path.
 * This version uses a dynamic map to register controllers and returns full RawHttpResponse objects.
 * @author Montse
 * @version 2.0.0
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
     * Routes the given request to the correct controller.
     * @param request The incoming HTTP request.
     * @return The HTTP response from the controller, or a 404 response if no route is found.
     * @throws Exception if the controller throws an error.
     */
    public RawHttpResponse<?> route(RawHttpRequest request) throws Exception {
        String path = request.getUri().getPath();

        // Simple routing: get the first part of the path (e.g., /developers/1 -> developers)
        String[] pathParts = path.split("/");
        if (pathParts.length < 2 || pathParts[1].isEmpty()) {
            return createNotFoundResponse();
        }

        String resourceName = pathParts[1].toLowerCase();
        Controller controller = controllers.get(resourceName);

        if (controller != null) {
            // We found a controller, let it handle the request
            return controller.handle(request);
        } else {
            // No controller registered for this resource
            return createNotFoundResponse();
        }
    }

    private RawHttpResponse<?> createNotFoundResponse() {
        String body = "{\"error\": \"Not Found\", \"message\": \"The requested resource was not found.\"}";
        return http.parseResponse(
                "HTTP/1.1 404 Not Found\r\n" +
                        "Content-Type: application/json\r\n" +
                        "Content-Length: " + body.getBytes().length
        ).withBody(new StringBody(body));
    }
}