package cat.uvic.teknos.dam.kamika.server.exceptions;

/**
 * Exception thrown when a client uses an HTTP method that is not supported
 * for the requested resource URI.
 * This corresponds to HTTP 405 status code.
 * @author Montse
 * @version 2.0.0
 */
public class MethodNotAllowedException extends RuntimeException {

    /**
     * Constructs a new MethodNotAllowedException with a default message.
     * @param method The HTTP method that was not allowed.
     */
    public MethodNotAllowedException(String method) {
        super("HTTP method " + method + " is not supported for this URI.");
    }
}