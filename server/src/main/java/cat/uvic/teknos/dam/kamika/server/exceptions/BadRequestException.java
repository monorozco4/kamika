package cat.uvic.teknos.dam.kamika.server.exceptions;

/**
 * Exception thrown when a client request is malformed or invalid.
 * This typically corresponds to HTTP 400 status code.
 *
 * @author Montse
 * @version 2.0.0
 */
public class BadRequestException extends RuntimeException {

    /**
     * Constructs a new BadRequestException with the specified detail message.
     *
     * @param message the detail message explaining why the request was invalid
     */
    public BadRequestException(String message) {
        super(message);
    }
}